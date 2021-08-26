package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Item;

import java.util.List;

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

    public Item save(Item item) {
        if (item.getId() == 0) {
            return add(item);
        } else {
            return update(item);
        }
    }

    private Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    private Item update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public List<Item> getAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session.createQuery("from ru.job4j.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Item findById(Integer id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item rsl = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return rsl;
    }
}
