package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class AuthServletTest {

    @Test
    public void whenDoPostRedirectViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.mockStatic(PsqlStore.class);
        HttpSession session = mock(HttpSession.class);

        Store store = MemStore.instanceOf();

        when(PsqlStore.instanceOf()).thenReturn(store);
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
        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();

        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getParameter("email")).thenReturn("root@local");
        when(req.getParameter("password")).thenReturn("");
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new AuthServlet().doPost(req, resp);

        verify(req).getRequestDispatcher("login.jsp");
    }
}