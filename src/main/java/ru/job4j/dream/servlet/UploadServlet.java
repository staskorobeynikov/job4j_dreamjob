package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String redict = "";
        if (req.getAttribute("photoId") == null) {
            List<String> images = new ArrayList<>();
            for (File name : new File("c:/bin/images").listFiles()) {
                images.add(name.getName());
            }
            req.setAttribute("images", images);
            redict = "/upload.jsp";
        } else {
            redict = "/candidate/edit.jsp";
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(redict);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Photo result = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("c:/bin/images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String name = this.getName(item.getName());
                    result = PsqlStore.instanceOf().createPhoto(new Photo(0, name));
                    File file = new File(folder + File.separator + name);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        req.setAttribute("photoId", String.valueOf(result.getId()));
        doGet(req, resp);
    }

    private boolean isValidName(String name) {
        boolean result = false;
        for (String str : PsqlStore.instanceOf().findAllNamePhoto()) {
            if (str.equals(name)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private String getName(String name) {
        while (this.isValidName(name)) {
            String[] splitName = name.split("\\.");
            name = String.format("%s_1.%s", splitName[0], splitName[1]);
        }
        return name;
    }
}
