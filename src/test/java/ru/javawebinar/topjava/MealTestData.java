package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Meal meal2  = new Meal(START_SEQ + 2,  LocalDateTime.of(2018,10,23,8,0),"Завтрак",800);
    public static final Meal meal3  = new Meal(START_SEQ + 3,  LocalDateTime.of(2018,10,23,13,0),"Обед",1000);
    public static final Meal meal4  = new Meal(START_SEQ + 4,  LocalDateTime.of(2018,10,23,18,0),"Ужин",800);
    public static final Meal meal5  = new Meal(START_SEQ + 5,  LocalDateTime.of (2018,10,24,8,0),"Завтрак",400);
    public static final Meal meal6  = new Meal(START_SEQ + 6,  LocalDateTime.of (2018,10,24,13,0),"Обед",800);
    public static final Meal meal7  = new Meal(START_SEQ + 7,  LocalDateTime.of (2018,10,24,18,0),"Ужин",600);
    public static final Meal meal8  = new Meal(START_SEQ + 8,  LocalDateTime.of (2018,10,23,9,0),"Завтрак",800);
    public static final Meal meal9  = new Meal(START_SEQ + 9,  LocalDateTime.of (2018,10,24,12,0),"Обед",1000);
    public static final Meal meal10 = new Meal(START_SEQ + 10, LocalDateTime.of (2018,10,24,20,0),"Ужин",800);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        //assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
