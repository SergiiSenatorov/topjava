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

        switch (req.getParameter(action)) {
            default:
                List<MealWithExceed> list = MealsUtil.getFilteredWithExceeded(repository.getAll(),LocalTime.MIN,LocalTime.MAX,2000);
                req.setAttribute("mealList",list);
                req.getRequestDispatcher("/meals.jsp").forward(req,resp);
        }
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
}
