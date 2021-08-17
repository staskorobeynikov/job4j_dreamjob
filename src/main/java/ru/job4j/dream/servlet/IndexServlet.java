package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", req.getSession().getAttribute("user"));
        Collection<Post> posts = PsqlStore.instanceOf().findPostsLastDay();
        Collection<Candidate> candidates = PsqlStore.instanceOf().findCandidatesLastDay();
        req.setAttribute("posts", posts.size());
        req.setAttribute("postLastDay", posts);
        req.setAttribute("candidates", candidates.size());
        req.setAttribute("candidateLastDay", candidates);
        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }
}
