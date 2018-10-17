package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService mealService;

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        return mealService.save(meal);
    }

    public void delete(int id) {
        log.info("delete id = {}", id);
        mealService.delete(id);
    }

    public void delete(int id, int userId) {
        log.info("delete id = {}, userId = {}", id, userId);
        mealService.delete(id, userId);
    }

    public Meal get(int id) {
        log.info("get id = {}", id);
        return mealService.get(id);
    }

    public Meal get(int id, int userId) {
        log.info("get id = {}, userId = {}", id, userId);
        return mealService.get(id, userId);
    }

    public List<Meal> getAll(int userId) {
        log.info("getAll userId = {}", userId);
        return mealService.getAll(userId);
    }

    public List<MealWithExceed> getWithFilter(int userId, LocalDate fromDate, LocalDate toDate,
                                       LocalTime fromTime, LocalTime toTime) {
        log.info("getWithFilter for userId = {}", userId);
        return mealService.getWithFilter(userId, fromDate, toDate, fromTime, toTime);
    }
}