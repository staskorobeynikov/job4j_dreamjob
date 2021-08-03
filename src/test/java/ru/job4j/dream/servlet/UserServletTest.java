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
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class UserServletTest {

    @Test
    public void whenDoGetReturnViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();

        when(req.getParameter("id")).thenReturn(String.valueOf(1));
        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new UserServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("users/edit.jsp");
    }
}