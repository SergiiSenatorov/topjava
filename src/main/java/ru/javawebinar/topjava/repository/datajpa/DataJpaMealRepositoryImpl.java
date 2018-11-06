package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if(!meal.isNew() && get(meal.getId(),userId) == null)
            return null;
        meal.setUser(userRepository.get(userId));
        return mealRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        //return mealRepository.delete(id, userId);
        return (mealRepository.deleteByIdAndUser(id, userRepository.get(userId)) != null);
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUserOrderByDateTimeDesc(userRepository.get(userId));
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return mealRepository.findAllByUserAndDateTimeBetweenOrderByDateTimeDesc(userRepository.get(userId), startDate, endDate);
    }
}
