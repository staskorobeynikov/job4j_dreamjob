package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Post post = new Post(0, "", "", null);
        String id = req.getParameter("id");
        if (id != null) {
            post = PsqlStore.instanceOf().findPostById(Integer.parseInt(id));
        }
        req.setAttribute("post", post);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }
}
