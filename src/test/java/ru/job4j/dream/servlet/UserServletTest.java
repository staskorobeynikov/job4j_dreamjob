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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServletTest {

    @Before
    public void setUp() {
        PsqlStore.instanceOf().deleteAllUsers();
    }

    @Test
    public void whenDoGetReturnViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(req.getParameter("id")).thenReturn(String.valueOf(1));
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new UserServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("users/edit.jsp");
    }

    @Test
    public void whenDoPostReturnViewCandidatesJSPAndUpdateUser() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        User user = PsqlStore.instanceOf().createUser(new User(
                0, "Admin152", "root@local", "root1000"
        ));

        when(req.getParameter("id")).thenReturn(String.valueOf(user.getId()));
        when(req.getParameter("name")).thenReturn("Junior Java Developer");
        when(req.getParameter("password")).thenReturn("12345");
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new UserServlet().doPost(req, resp);

        User byEmail = PsqlStore.instanceOf().findByEmail(user.getEmail());

        verify(resp).sendRedirect(String.format("%s/candidates.do", req.getContextPath()));
        assertThat(byEmail.getName(), is("Junior Java Developer"));
    }
}