package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void whenDoGetReturnViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();

        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new PostServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("post/posts.jsp");
    }

    @Test
    public void whenDoPostRedirectViewPostsJSPAndAddNewPost() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.mockStatic(PsqlStore.class);

        Store store = MemStore.instanceOf();

        when(PsqlStore.instanceOf()).thenReturn(store);
        when(req.getParameter("id")).thenReturn(String.valueOf(0));
        when(req.getParameter("name")).thenReturn("Junior Java Developer");
        when(req.getParameter("description")).thenReturn("Требуется Junior Java Developer");

        new PostServlet().doPost(req, resp);
        List<Post> list = new ArrayList<>(store.findAllPosts());

        verify(resp).sendRedirect(String.format("%s/posts.do", req.getContextPath()));
        assertThat(list.size(), is(4));
    }
}