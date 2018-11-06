package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query ("DELETE FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    boolean delete(@Param("id") int id, @Param("userId") int userId);

    @Query ("SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :userId")
    Meal get(@Param("id") int id, @Param("userId") int userId);

    Meal deleteByIdAndUser(int id, User user);

    List<Meal> findAllByUserOrderByDateTimeDesc(User user);

    List<Meal> findAllByUserAndDateTimeBetweenOrderByDateTimeDesc(User user, LocalDateTime startDate, LocalDateTime endDate);
}
