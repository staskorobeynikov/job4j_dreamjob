package ru.job4j.dream.servlet;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AuthServletTest {

    @Before
    public void setUp() {
        PsqlStore.instanceOf().deleteAllUsers();
    }

    @Test
    public void whenDoPostRedirectViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        PsqlStore.instanceOf().createUser(new User(
                0, "Admin152", "root@local", "root"
        ));

        when(req.getParameter("email")).thenReturn("root@local");
        when(req.getParameter("password")).thenReturn("root");
        when(req.getSession()).thenReturn(session);

        new AuthServlet().doPost(req, resp);

        verify(resp).sendRedirect(String.format("%s/posts.do", req.getContextPath()));
    }

    @Test
    public void whenDoPostRedirectViewLoginJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getParameter("email")).thenReturn("root@local");
        when(req.getParameter("password")).thenReturn("");
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new AuthServlet().doPost(req, resp);

        verify(req).getRequestDispatcher("login.jsp");
    }
}