package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @RequestMapping(method = RequestMethod.POST)
    public String updateOrCreate(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal userMeal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        if (userMeal.isNew()) {
            super.create(userMeal);
        } else {
            super.update(userMeal, userMeal.getId());
        }
        return "redirect:/meals";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        model.addAttribute("meal",super.get(Integer.valueOf(request.getParameter("id"))));
        return "mealForm";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request) {
        super.delete(Integer.valueOf(request.getParameter("id")));
        return "redirect:/meals";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),"", SecurityUtil.authUserCaloriesPerDay());
        model.addAttribute("meal",meal);
        return "mealForm";
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = LocalDate.parse(resetParam("startDate", request));
        LocalDate endDate = LocalDate.parse(resetParam("endDate", request));
        LocalTime startTime = LocalTime.parse(resetParam("startTime", request));
        LocalTime endTime = LocalTime.parse(resetParam("endTime", request));
        model.addAttribute("meals",super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
