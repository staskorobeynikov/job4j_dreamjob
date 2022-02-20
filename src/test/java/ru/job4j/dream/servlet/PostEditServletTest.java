package ru.job4j.dream.servlet;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class PostEditServletTest {
    @Test
    public void whenPostIdIsNullThenRedirectEditWithEmptyObjectPost() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ArgumentCaptor<Post> argument = ArgumentCaptor.forClass(Post.class);

        when(req.getParameter("id")).thenReturn(null);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);
        doNothing().when(req).setAttribute(any(), argument.capture());

        new PostEditServlet().doGet(req, resp);

        Post post = argument.getValue();

        verify(req).getRequestDispatcher("edit.jsp");
        assertThat(post.getDescription().length(), is(0));
    }

    @Test
    public void whenPostIdIsNotNullThenRedirectEditWithObjectPost() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ArgumentCaptor<Post> argument = ArgumentCaptor.forClass(Post.class);

        Post first = new Post(
                0, "Post for Project manager",
                "Manager with experience 10 years", new Timestamp(System.currentTimeMillis())
        );
        PsqlStore.instanceOf().save(first);

        when(req.getParameter("id")).thenReturn(String.valueOf(first.getId()));
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);
        doNothing().when(req).setAttribute(any(), argument.capture());

        new PostEditServlet().doGet(req, resp);

        Post post = argument.getValue();

        verify(req).getRequestDispatcher("edit.jsp");
        assertThat(post.getDescription(), is("Manager with experience 10 years"));
        assertThat(post.getName(), is("Post for Project manager"));
    }
}