package ru.job4j.dream.filter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CORSFilterTest {
    @Test
    public void whenHeaderContainsMethodTypeWithoutStatus() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);

        when(req.getMethod()).thenReturn("");
        doNothing().when(resp).addHeader(any(), message.capture());

        new CORSFilter().doFilter(req, resp, chain);

        assertThat(message.getValue(), is("GET, OPTIONS, HEAD, PUT, POST"));
    }

    @Test
    public void whenResponseWithStatus202() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        ArgumentCaptor<Integer> message = ArgumentCaptor.forClass(Integer.class);

        when(req.getMethod()).thenReturn("OPTIONS");
        doNothing().when(resp).setStatus(message.capture());

        new CORSFilter().doFilter(req, resp, chain);

        assertThat(message.getValue(), is(202));
    }
}