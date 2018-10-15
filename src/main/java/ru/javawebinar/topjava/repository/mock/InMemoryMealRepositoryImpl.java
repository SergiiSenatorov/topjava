package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted((o1, o2) -> {
                    int res = o2.getDate().compareTo(o1.getDate());
                    return res == 0 ? o2.getTime().compareTo(o1.getTime()) : res;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getWithFilter(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getWithFilter userId={} startDate={} endDate={}",userId,startDate,endDate);
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId &&
                        DateTimeUtil.isBetween(m.getDate(),startDate,endDate))
                .sorted((o1, o2) -> {
                    int res = o2.getDate().compareTo(o1.getDate());
                    return res == 0 ? o2.getTime().compareTo(o1.getTime()) : res;
                })
                .collect(Collectors.toList());
    }
}

