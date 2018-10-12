package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryMemoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository repository = new MealRepositoryMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("defaultLocale", Locale.getDefault().toString());
        req.setAttribute("isEditFormVisible","none");

        String action = req.getParameter("action");

        if(action == null) {
            getMeals(req,resp);
            return;
        }

        Long id = getId(req);

        switch (action) {
            case "delete" :
                delete(resp,id);
                break;
            case "create" :
                create(req,resp);
                break;
            case "edit" :
                edit(req,resp,id);
                break;
            default:
                getMeals(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            Long id = req.getParameter("id").isEmpty() ? null : Long.valueOf(req.getParameter("id"));
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            String description = req.getParameter("description");
            Integer calories = Integer.valueOf(req.getParameter("calories"));

            if (!description.isEmpty() && calories != 0) {
                Meal meal = new Meal(dateTime, description, calories);
                meal.setId(id);
                repository.save(meal);
                log.info(meal.getId() == null ? "Create new Meal {}" : "Update current meal {}", meal.toString());
            } else {
                log.warn("Couldn't save a meal with empty data!");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            log.warn("Couldn't parse meal data: {}", e.getMessage());
        }
        resp.sendRedirect("meals");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        IntStream.range(0,3).forEach(i -> populate());
    }

    private void populate() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        LocalDate date = TimeUtil.getRandomDate(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 10, 31));
        repository.save(new Meal(LocalDateTime.of(date, LocalTime.of(r.nextInt(8, 12), 0)), "Завтрак", r.nextInt(300, 700)));
        repository.save(new Meal(LocalDateTime.of(date, LocalTime.of(r.nextInt(12, 15), 0)), "Обед", r.nextInt(800, 1100)));
        repository.save(new Meal(LocalDateTime.of(date, LocalTime.of(r.nextInt(18, 21), 0)), "Ужин", r.nextInt(300, 700)));
    }

    private void getMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealWithExceed> mealList = MealsUtil.getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("mealList", mealList);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    private void delete(HttpServletResponse resp, Long id) throws IOException {
        if (id != null) {
            repository.delete(id);
        } else {
            log.warn("Couldn't delete meal with null or incorrect ID");
        }
        resp.sendRedirect("meals");
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Create new meal...");
        req.setAttribute("isEditFormVisible", "block");
        Meal curMeal = new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute())), "", 0);
        req.setAttribute("curMeal", curMeal);
        getMeals(req, resp);
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp, Long id) throws ServletException, IOException {
        if (id != null) {
            log.info("Edit meal with ID = {} ...", id);
            req.setAttribute("isEditFormVisible", "block");
            Meal curMeal = repository.getById(id);
            req.setAttribute("curMeal", curMeal);
        } else {
            log.warn("Couldn't edit meal with null or incorrect ID");
        }
        getMeals(req, resp);
    }

    private Long getId(HttpServletRequest req) {
        try {
            return Long.valueOf(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
