package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INSTANCE = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Требуется Junior Java Developer", new Timestamp(1593601200000L)));
        posts.put(2, new Post(2, "Middle Java Job", "Требуется Middle Java Developer", new Timestamp(1593619200000L)));
        posts.put(3, new Post(3, "Senior Java Job", "Требуется Senior Java Developer", new Timestamp(1593684000000L)));
    }

    public static Store instanceOf() {
        return INSTANCE;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
