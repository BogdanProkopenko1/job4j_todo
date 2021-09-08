package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HbmStore {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static class Lazy {
        private static final HbmStore INSTANCE = new HbmStore();
    }

    public static HbmStore instOf() {
        return Lazy.INSTANCE;
    }

    public void addItem(Item item) {
        tx(session -> session.save(item));
    }

    public void updateItem(Item item) {
        this.tx(session -> {
            Query query = session
                    .createQuery("UPDATE Item SET description = :des, created = :created, done = :done")
                    .setParameter("des", item.getDescription())
                    .setParameter("created", item.getCreated())
                    .setParameter("done", item.isDone());
            query.executeUpdate();
            return query;
        });
    }

    public Item findItemById(Integer id) {
        return tx(session -> session.get(Item.class, id));
    }

    public List getAllItems() {
        return tx(
                session -> session.createQuery("from ru.job4j.model.Item").list()
        );
    }

    public Optional<User> getUserOnEmail(String email) {
        return tx(session ->
                session.createQuery("from User where email =: email")
                        .setParameter("email", email).list().stream().findFirst());
    }

    public void save(User user) {
        tx(session -> session.save(user));
    }

    public List getAllCategories() {
        return tx(
                session -> session.createQuery("from Category").list()
        );
    }

    public void save(Category category) {
        tx(session -> session.save(category));
    }

    public Category findCategoryById(int id) {
        return tx(session -> session.find(Category.class, id));
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
