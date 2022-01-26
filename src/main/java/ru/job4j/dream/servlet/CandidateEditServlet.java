package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Candidate candidate = new Candidate(0, "", 0, 0, "Не выбран");
        String id = req.getParameter("id");
        if (id != null) {
            candidate = PsqlStore.instanceOf().findCandidateById(Integer.parseInt(id));
        }
        req.setAttribute("candidate", candidate);
        req.getRequestDispatcher("edit.jsp").forward(req, resp);
    }
}
