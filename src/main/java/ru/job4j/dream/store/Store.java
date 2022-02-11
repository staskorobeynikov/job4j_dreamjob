package ru.job4j.dream.store;

import ru.job4j.dream.model.*;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Post> findPostsLastDay();

    Collection<Candidate> findAllCandidates();

    Collection<Candidate> findCandidatesLastDay();

    void save(Post post);

    Post findPostById(int id);

    void save(Candidate candidate);

    Candidate findCandidateById(int id);

    User findByEmail(String email);

    User createUser(User user);

    User findUserById(int id);

    void updateUser(User user);

    Collection<City> findAllCity();

    void addPhoto(int id);

    void deletePhoto(int id);
}
