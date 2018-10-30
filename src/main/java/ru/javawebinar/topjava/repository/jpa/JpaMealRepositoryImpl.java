package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(em.getReference(User.class, userId));
            em.persist(meal);
            return meal;
        } else {
            /*
            em.merge не подходит, т.к. не учитывается принадлежность еды пользователю
            мы не можем обновлять чужую еду
             */
            int count = em.createNamedQuery(Meal.UPDATE)
                    .setParameter("datetime", meal.getDateTime())
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("id", meal.getId())
                    .setParameter("user_id", userId)
                    .executeUpdate();
            return count == 0 ? null : meal;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        if(get(id,userId) != null) {
            em.remove(em.getReference(Meal.class, id));
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        TypedQuery<Meal>  query = em.createNamedQuery(Meal.GET_BY_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId);
        return query.getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Meal>  query = em.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("user_id", userId);
        return query.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        TypedQuery<Meal>  query =  em.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        return query.getResultList();
    }
}