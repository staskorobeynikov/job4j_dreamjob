package ru.job4j.dream.servlet;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegServletTest {
    @Before
    public void setUp() {
        PsqlStore.instanceOf().deleteAllUsers();
    }

    @Test
    public void whenDoGetReturnViewReqJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new RegServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("reg.jsp");
    }

    @Test
    public void whenDoPostRedirectViewLoginJSPAndAddNewUser() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("name")).thenReturn("Admin2");
        when(req.getParameter("email")).thenReturn("admin2@local");
        when(req.getParameter("password")).thenReturn("root");

        new RegServlet().doPost(req, resp);

        User result = PsqlStore.instanceOf().findByEmail("admin2@local");

        verify(resp).sendRedirect(String.format("%s/login.jsp", req.getContextPath()));
        assertThat(result.getName(), is("Admin2"));
    }

    @Test
    public void whenDoPostNotUniqueEmailAndRedirectViewRegJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        PsqlStore.instanceOf().createUser(new User(0, "Admin152", "root@local", "root1000"));

        when(req.getParameter("name")).thenReturn("Admin2");
        when(req.getParameter("email")).thenReturn("root@local");
        when(req.getParameter("password")).thenReturn("root");

        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new RegServlet().doPost(req, resp);

        User result = PsqlStore.instanceOf().findByEmail("root@local");

        verify(req).getRequestDispatcher("reg.jsp");
        assertThat(result.getName(), is("Admin152"));
    }
}