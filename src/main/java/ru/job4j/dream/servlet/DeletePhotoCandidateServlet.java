package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeletePhotoCandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("photoId");
        PsqlStore.instanceOf().deletePhoto(Integer.parseInt(id));
        Files.deleteIfExists(Paths.get("c:/bin/images" + File.separator + id + ".png"));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
