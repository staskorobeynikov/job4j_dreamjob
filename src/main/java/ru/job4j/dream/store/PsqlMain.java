package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.sql.Timestamp;

public class PsqlMain {

    public static void main(String[] args) {
        Store store = PsqlStore.instanceOf();
        store.save(new Post(0, "Java Job", "Требуется Java Developer", new Timestamp(System.currentTimeMillis())));
        store.save(new Post(3, "Middle", "Middle Developer", null));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName() + " " + post.getDescription() + " " + post.getCreated());
        }
        System.out.println(store.findPostById(7).getDescription());

        System.out.println();
        store.save(new Candidate(0, "I am Middle Java Developer", 0));
        store.save(new Candidate(3, "Senior Java Developer", 0));
        for (Candidate candidate : store.findAllCandidates())  {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println();
        System.out.println(store.findCandidateById(3).getName());
    }
}
