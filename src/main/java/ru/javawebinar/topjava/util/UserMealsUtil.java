package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        //.toLocalDate();
        //.toLocalTime();

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)
                .forEach(System.out::println);
    }



    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field

        /*
        List<UserMealWithExceed> list = mealList.stream()
                .map(p -> new UserMealWithExceed(p.getDateTime(), p.getDescription(), p.getCalories(),
                        mealList.stream().filter(userMeal -> p.getDateTime().toLocalDate().equals(userMeal.getDateTime().toLocalDate()))
                                .map(UserMeal::getCalories)
                                .reduce((integer, integer2) -> integer + integer2)
                                .get() > caloriesPerDay
                ))
                .filter(userMealWithExceed -> (userMealWithExceed.getDateTime().toLocalTime().compareTo(startTime) >= 0 &&
                        userMealWithExceed.getDateTime().toLocalTime().compareTo(endTime) <= 0))
                .collect(Collectors.toList());
                */

        Map<LocalDate,Integer> caloriesDay = mealList.stream()
                .collect(Collectors.toMap(UserMeal::toLocalDate, UserMeal::getCalories,
                        Integer::sum,
                        HashMap::new));

        List<UserMealWithExceed> list =

        return list;
    }
}
