package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PsqlStoreTest {
    private Store init(String fileName)
            throws SQLException, NoSuchFieldException, IllegalAccessException {
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:dreamjob;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
        Store store = PsqlStore.instanceOf();
        Field field = store.getClass().getDeclaredField("pool");
        field.setAccessible(true);
        field.set(store, pool);
        return store;
    }

    @Test
    public void whenTestMethodForPost()
            throws NoSuchFieldException, IllegalAccessException, SQLException {
        Store store = this.init("./db/update_002.sql");

        store.save(new Post(0, "name1", "desc1", new Timestamp(System.currentTimeMillis())));
        store.save(new Post(1, "name2", "desc2", new Timestamp(System.currentTimeMillis())));

        Collection<Post> allPosts = store.findAllPosts();
        Post postById = store.findPostById(1);

        assertThat(allPosts.size(), is(1));
        assertThat(postById.getDescription(), is("desc2"));
    }

    @Test
    public void whenTestMethodForCandidate()
            throws NoSuchFieldException, IllegalAccessException, SQLException {
        Store store = this.init("./db/update_001.sql");

        store.save(new Candidate(0, "name1", 1, 1));
        store.save(new Candidate(1, "name10", 1));

        Candidate candidate = store.findCandidateById(1);

        assertThat(candidate.getName(), is("name10"));
    }

    @Test
    public void whenTestMethodForPhoto()
            throws NoSuchFieldException, IllegalAccessException, SQLException {
        Store store = this.init("./db/update_003.sql");

        store.createPhoto(new Photo("name1"));
        store.createPhoto(new Photo("name100"));

        List<String> photo = store.findAllNamePhoto();
        Photo byId = store.findPhotoById(2);

        assertThat(photo.size(), is(2));
        assertThat(byId.getTitle(), is("name100"));
    }

    @Test
    public void whenTestMethodForUser()
            throws NoSuchFieldException, IllegalAccessException, SQLException {
        Store store = this.init("./db/update_004.sql");

        store.createUser(new User(0, "root", "root@local", "root"));
        store.createUser(new User(0, "root100", "root100@local", "root"));

        User byEmail = store.findByEmail("root100@local");
        User byEmail1 = store.findByEmail("root@local");

        assertThat(byEmail.getName(), is("root100"));
        assertThat(byEmail1.getPassword(), is("root"));
    }
}
