package mate.academy.dao.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public User save(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can not save user: " + user, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can not get user by id: " + id, e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where email = :email")
                    .setParameter("email", email)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Can not get user by email: " + email, e);
        }
    }
}
