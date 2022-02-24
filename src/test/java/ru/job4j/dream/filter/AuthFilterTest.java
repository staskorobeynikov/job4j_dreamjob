package ru.job4j.dream.filter;

import org.junit.Test;
import ru.job4j.dream.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AuthFilterTest {
    @Test
    public void whenAnyURIAndUserNotNullThenRedirectLoginJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User(
                0, "Filter_test", "filter@test.com", "filter"
        );

        when(req.getRequestURI()).thenReturn("");
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(any())).thenReturn(user);

        new AuthFilter().doFilter(req, resp, chain);

        verify(chain).doFilter(req, resp);
    }

    @Test
    public void whenAnyURIAndUserIsNullThenRedirectLoginJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getRequestURI()).thenReturn("");
        when(req.getSession()).thenReturn(session);

        new AuthFilter().doFilter(req, resp, chain);

        verify(resp).sendRedirect(String.format("%s/login.jsp", req.getContextPath()));
    }

    @Test
    public void whenURIContainsAuthDo() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getRequestURI()).thenReturn("/auth.do");

        new AuthFilter().doFilter(req, resp, chain);

        verify(chain).doFilter(req, resp);
    }

    @Test
    public void whenURIContainsRegDo() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getRequestURI()).thenReturn("/reg.do");

        new AuthFilter().doFilter(req, resp, chain);

        verify(chain).doFilter(req, resp);
    }
}