package ru.job4j.dream.store;

import ru.job4j.dream.model.*;

import java.util.Collection;
import java.util.List;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Post> findPostsLastDay();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    Post findPostById(int id);

    void save(Candidate candidate);

    Candidate findCandidateById(int id);

    Photo findPhotoById(int id);

    Photo createPhoto(Photo photo);

    List<String> findAllNamePhoto();

    User findByEmail(String email);

    User createUser(User user);

    Collection<City> findAllCity();
}
