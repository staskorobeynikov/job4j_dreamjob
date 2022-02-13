package ru.job4j.dream.store;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.Timestamp;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PsqlStoreTest {

    @Before
    public void setUp() {
        PsqlStore.instanceOf().deleteAllUsers();
        PsqlStore.instanceOf().deleteAllCandidates();
        PsqlStore.instanceOf().deleteAllPosts();
    }

    @Test
    public void whenFindByIdUser() {
        User user = PsqlStore.instanceOf().createUser(new User(
                0, "Admin152444", "root121212@local", "root1789"
        ));

        User userById = PsqlStore.instanceOf().findUserById(user.getId());

        assertThat(userById.getEmail(), is("root121212@local"));
    }

    @Test
    public void whenUpdatePhotoForCandidate() {
        Candidate candidate = new Candidate(0, "First_Name_Its_Mine", 0, 1);
        PsqlStore.instanceOf().save(candidate);

        PsqlStore.instanceOf().addPhoto(candidate.getId());

        Candidate candidateById = PsqlStore.instanceOf().findCandidateById(candidate.getId());

        assertThat(candidateById.getPhotoId(), is(candidate.getId()));
        assertThat(candidateById.getName(), is("First_Name_Its_Mine"));
    }

    @Test
    public void whenDeletePhotoForCandidate() {
        Candidate candidate = new Candidate(0, "First_Name_Its_Yours", 10, 1);
        PsqlStore.instanceOf().save(candidate);

        PsqlStore.instanceOf().deletePhoto(candidate.getId());

        Candidate candidateById = PsqlStore.instanceOf().findCandidateById(candidate.getId());

        assertThat(candidateById.getPhotoId(), is(0));
        assertThat(candidateById.getName(), is("First_Name_Its_Yours"));
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate candidate = new Candidate(0, "Your_name_is_correct", 10, 1);
        PsqlStore.instanceOf().save(candidate);

        Candidate update = new Candidate(candidate.getId(), "Your_name_edited", 10, 1);

        PsqlStore.instanceOf().save(update);

        Candidate candidateById = PsqlStore.instanceOf().findCandidateById(candidate.getId());

        assertThat(candidateById.getName(), is("Your_name_edited"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(
                0, "Your_name_is_funny",
                "The description is good", new Timestamp(System.currentTimeMillis())
        );
        PsqlStore.instanceOf().save(post);

        Post update = new Post(
                post.getId(), "Your_name_edited",
                "Description_after_edit", new Timestamp(System.currentTimeMillis())
        );

        PsqlStore.instanceOf().save(update);

        Post postById = PsqlStore.instanceOf().findPostById(post.getId());

        assertThat(postById.getName(), is("Your_name_edited"));
        assertThat(postById.getDescription(), is("Description_after_edit"));
    }

//    @Test
//    public void whenFindCandidatesLastDay() {
//        Candidate first = new Candidate(0, "Name_is_Boris", 10, 1);
//        Candidate second = new Candidate(0, "Name_is_John", 50, 1);
//        Candidate third = new Candidate(0, "Name_is_Peter", 100, 1);
//        PsqlStore.instanceOf().save(first);
//        PsqlStore.instanceOf().save(second);
//        PsqlStore.instanceOf().save(third);
//
//        Collection<Candidate> candidates = PsqlStore.instanceOf().findCandidatesLastDay();
//
//        assertThat(candidates.size(), is(3));
//    }

    @Test
    public void whenFindPostsLastDay() {
        Post first = new Post(
                0, "Post_name_is_first",
                "This_description_is_first", new Timestamp(System.currentTimeMillis())
        );
        Post second = new Post(
                0, "Post_name_is_second",
                "This_description_is_second", new Timestamp(System.currentTimeMillis())
        );
        PsqlStore.instanceOf().save(first);
        PsqlStore.instanceOf().save(second);

        Collection<Post> posts = PsqlStore.instanceOf().findPostsLastDay();

        assertThat(posts.size(), is(2));
    }
}
