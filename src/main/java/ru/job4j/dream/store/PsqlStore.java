package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private static final Logger LOG = LogManager.getLogger(PsqlStore.class);

    private BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties config = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            config.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(config.getProperty("jdbc.driver"));
        pool.setUrl(config.getProperty("jdbc.url"));
        pool.setUsername(config.getProperty("jdbc.username"));
        pool.setPassword(config.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INSTANCE = new PsqlStore();
    }

    public static Store instanceOf() {
        return Lazy.INSTANCE;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(
                            new Post(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("description"),
                                    it.getTimestamp(4)
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Collection<Post> findPostsLastDay() {
        List<Post> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post as p "
                             + "WHERE p.created between now() - interval '24 hours' and now()")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(
                            new Post(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("description"),
                                    it.getTimestamp(4)
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT candidate.id, name, photo_id, c.city, candidate.created FROM candidate "
                             + "JOIN cities c on candidate.city_id = c.id"
             )
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(
                            new Candidate(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getInt("photo_id"),
                                    it.getString("city"),
                                    it.getTimestamp(5)
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Collection<Candidate> findCandidatesLastDay() {
        List<Candidate> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT candidate.id, name, photo_id, c.city, candidate.created FROM candidate " +
                             "JOIN cities c on candidate.city_id = c.id " +
                             "WHERE candidate.created between now() - interval '24 hours' and now()"
             )
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(
                            new Candidate(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getInt("photo_id"),
                                    it.getString("city"),
                                    it.getTimestamp(5)
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO post(name, description, created) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    private void updatePost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = ?, description = ? WHERE id = ?"
             )
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post WHERE id = ?"
             )
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new Post(
                        rs.getInt(1),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp(4)
                );
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO candidate(name, photo_id, city_id) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidate SET name = ?, city_id = ? WHERE id = ?"
             )
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM candidate "
                             + "join cities c on c.id = candidate.city_id "
                             + "WHERE candidate.id = ?"
             )
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new Candidate(
                        rs.getInt(1),
                        rs.getString("name"),
                        rs.getInt("photo_id"),
                        rs.getInt("city_id"),
                        rs.getString("city")
                );
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Photo findPhotoById(int id) {
        Photo result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM photo WHERE id = ?"
             )
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new Photo(
                        rs.getInt(1),
                        rs.getString("title")
                );
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Photo createPhoto(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO photo(title) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, photo.getTitle());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    photo.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return photo;
    }

    @Override
    public List<String> findAllNamePhoto() {
        List<String> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT title FROM photo;")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(it.getString("title"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE email = ?"
             )
        ) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new User(
                        rs.getInt(1),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new IllegalArgumentException("Нарушение ограничения уникальности поля email.");
        }
        return user;
    }

    @Override
    public User findUserById(int id) {
        User result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "select * from users where id = ?"
             )
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new User(
                        rs.getInt(1),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void updateUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update users set name = ?, password = ? where id = ?"
             )
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Collection<City> findAllCity() {
        List<City> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM cities;")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    result.add(
                            new City(
                                    it.getInt("id"),
                                    it.getString("city")
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
