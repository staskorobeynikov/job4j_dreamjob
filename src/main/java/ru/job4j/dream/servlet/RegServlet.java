package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        User byEmail = PsqlStore.instanceOf().findByEmail(email);
        if (byEmail != null) {
            req.setAttribute(
                    "error",
                    String.format(
                            "Пользователь с email: %s уже зарегистрирован.",
                            email
                    )
            );
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
            return;
        }
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        PsqlStore.instanceOf().createUser(
                new User(
                        0,
                        name,
                        email,
                        password
                )
        );
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
