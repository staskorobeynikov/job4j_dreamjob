package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {

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
        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();

        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getParameter("name")).thenReturn("Admin2");
        when(req.getParameter("email")).thenReturn("admin2@local");
        when(req.getParameter("password")).thenReturn("root");

        new RegServlet().doPost(req, resp);
        User result = store.findByEmail("admin2@local");

        verify(resp).sendRedirect(String.format("%s/login.jsp", req.getContextPath()));
        assertThat(result.getName(), is("Admin2"));
    }

    @Test
    public void whenDoPostNotUniqueEmailAndRedirectViewRegJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();
        store.createUser(new User(0, "", "root@local", ""));

        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getParameter("name")).thenReturn("Admin2");
        when(req.getParameter("email")).thenReturn("root@local");
        when(req.getParameter("password")).thenReturn("root");
        when(PsqlStore.instanceOf().createUser(
                new User(0, "", "root@local", "")
        )).thenThrow(
                IllegalArgumentException.class
        );
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new RegServlet().doPost(req, resp);

        verify(req).getRequestDispatcher("reg.jsp");
    }
}