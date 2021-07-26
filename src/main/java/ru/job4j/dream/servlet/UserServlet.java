package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        User user = PsqlStore.instanceOf().findUserById(Integer.parseInt(id));
        req.setAttribute("user", user);
        req.getRequestDispatcher("users/edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(
                Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"),
                "",
                req.getParameter("password")
        );
        PsqlStore.instanceOf().updateUser(user);
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
