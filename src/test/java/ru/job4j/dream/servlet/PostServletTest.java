package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServletTest {

    @Test
    public void whenDoGetReturnViewPostsJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new PostServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("post/posts.jsp");
    }

    @Test
    public void whenDoPostRedirectViewPostsJSPAndAddNewPost() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("id")).thenReturn(String.valueOf(0));
        when(req.getParameter("name")).thenReturn("Junior Java Developer");
        when(req.getParameter("description")).thenReturn("Требуется Junior Java Developer");

        new PostServlet().doPost(req, resp);
        Post post = PsqlStore.instanceOf()
                .findAllPosts()
                .stream()
                .filter(p -> p.getName().equals("Junior Java Developer"))
                .findFirst()
                .get();

        verify(resp).sendRedirect(String.format("%s/posts.do", req.getContextPath()));
        assertThat(post.getDescription(), is("Требуется Junior Java Developer"));
    }
}