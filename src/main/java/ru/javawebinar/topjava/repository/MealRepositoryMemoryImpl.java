package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MealRepositoryMemoryImpl implements MealRepository {

    private Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong(0);


    @Override
    public Meal save(Meal meal) {
        if(meal.getId() == null) meal.setId(counter.incrementAndGet());
        return meals.put(meal.getId(),meal);
    }

    @Override
    public void delete(long id) {
        meals.remove(id);
    }

    @Override
    public Meal getById(long id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values())
                .stream()
                .sorted(Comparator.comparing(Meal::getDate))
                .collect(Collectors.toList());
    }
}
