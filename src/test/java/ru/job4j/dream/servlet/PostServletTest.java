package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
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
        ArgumentCaptor<HttpServletRequest> value = ArgumentCaptor.forClass(HttpServletRequest.class);
        doNothing().when(req).setAttribute(any(String.class), value.capture());
        req.setAttribute("posts", List.of(new Post(1, "fdfd", "dsdsds", new Timestamp(System.currentTimeMillis()))));
//        doAnswer(i -> {
//            Object argument = i.getArgument(0);
//            Object argument1 = i.getArgument(1);
//            assertEquals("posts", argument);
//            assertEquals(argument1, List.of(new Post(1, "fdfd", "dsdsds", new Timestamp(System.currentTimeMillis()))));
//            return null;
//        }).when(req).setAttribute(any(String.class), any(List.class));
//        req.setAttribute("posts", List.of(new Post(1, "fdfd", "dsdsds", new Timestamp(System.currentTimeMillis()))));

        Object posts = value.getValue();

        new PostServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("posts.jsp");
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