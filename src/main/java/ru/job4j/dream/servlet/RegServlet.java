package ru.job4j.dream.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(RegServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean rsl = false;
        String email = req.getParameter("email");
        try {
            PsqlStore.instanceOf().createUser(
                    new User(
                            0,
                            req.getParameter("name"),
                            email,
                            req.getParameter("password")
                    )
            );
            rsl = true;
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        if (!rsl) {
            req.setAttribute(
                    "error",
                    String.format(
                            "Пользователь с email: %s уже зарегистрирован.",
                            email
                    )
            );
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}
