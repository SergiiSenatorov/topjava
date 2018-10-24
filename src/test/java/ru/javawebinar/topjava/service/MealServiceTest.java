package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal newMeal = service.get(meal2.getId(), USER_ID);
        assertMatch(newMeal, meal2);
    }

    @Test(expected = NotFoundException.class)
    public void faultGet() {
        service.get(meal2.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(meal2.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), meal7, meal6, meal5, meal4, meal3);
    }

    @Test(expected = NotFoundException.class)
    public void faultDelete () {
        service.delete(meal2.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(LocalDate.of(2018,10,24),
                                            LocalDate.of(2018,10,24),
                                            USER_ID), meal7, meal6, meal5);
    }

    @Test
    public void getBetweenDatesWithEmptyResult() {
        assertMatch(service.getBetweenDates(LocalDate.of(2018,10,20),
                LocalDate.of(2018,10,22),
                ADMIN_ID), new ArrayList<>());
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2018,10,24,12,0),
                LocalDateTime.of(2018,10,24,20,0),
                USER_ID), meal7, meal6);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), meal7, meal6, meal5, meal4, meal3, meal2);
    }

    @Test
    public void getAllWithEmptyResult() {
        assertMatch(service.getAll(100), new ArrayList<>());
    }

    @Test
    public void update() {
        Meal newMeal = new Meal(meal2.getId(), LocalDateTime.of(2020,1,1,12,0),"Полдник", 1000);
        service.update(newMeal, USER_ID);
        assertMatch(service.get(meal2.getId(), USER_ID), newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void faultUpdate() {
        Meal newMeal = new Meal(meal2.getId(), LocalDateTime.of(2020,1,1,12,0),"Полдник", 1000);
        service.update(newMeal, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(2017,10,23,8,0),"Завтрак",800);
        Meal created = service.create(newMeal,USER_ID);
        assertMatch(service.getAll(USER_ID), meal7, meal6, meal5, meal4, meal3, meal2, newMeal);
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateCreate() {
        Meal newMeal = new Meal(LocalDateTime.of(2018,10,23,8,0),"Завтрак",800);
        Meal created = service.create(newMeal,USER_ID);
    }
}