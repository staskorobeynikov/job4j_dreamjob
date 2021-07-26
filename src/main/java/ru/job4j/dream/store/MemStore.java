package ru.job4j.dream.store;

import ru.job4j.dream.model.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final MemStore INSTANCE = new MemStore();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Map<Integer, User> users = new ConcurrentHashMap<>();

    private static final AtomicInteger POST_ID = new AtomicInteger(4);

    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);

    private static final AtomicInteger USER_ID = new AtomicInteger(4);

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Требуется Junior Java Developer", new Timestamp(1593601200000L)));
        posts.put(2, new Post(2, "Middle Java Job", "Требуется Middle Java Developer", new Timestamp(1593619200000L)));
        posts.put(3, new Post(3, "Senior Java Job", "Требуется Senior Java Developer", new Timestamp(1593684000000L)));
        candidates.put(1, new Candidate(1, "Junior Java", 0));
        candidates.put(2, new Candidate(2, "Middle Java", 0));
        candidates.put(3, new Candidate(3, "Senior Java", 0));
        users.put(1, new User(1, "Admin", "root@local", "root"));
    }

    public static MemStore instanceOf() {
        return INSTANCE;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public Collection<Post> findPostsLastDay() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public Post findPostById(int id) {
        return posts.get(id);
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public Photo findPhotoById(int id) {
        return null;
    }

    @Override
    public Photo createPhoto(Photo photo) {
        return null;
    }

    @Override
    public List<String> findAllNamePhoto() {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        User result = null;
        List<User> list = new ArrayList<>(users.values());
        for (User user : list) {
            if (user.getEmail().equals(email)) {
                result = user;
                break;
            }
        }
        return result;
    }

    @Override
    public User createUser(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUserById(int id) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public Collection<City> findAllCity() {
        return List.of(new City(1, "Minsk"), new City(2, "Moscow"));
    }
}
