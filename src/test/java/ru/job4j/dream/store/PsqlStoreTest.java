package ru.job4j.dream.store;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.User;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PsqlStoreTest {

    @Before
    public void setUp() {
        PsqlStore.instanceOf().deleteAllUsers();
        PsqlStore.instanceOf().deleteAllCandidates();
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
}
