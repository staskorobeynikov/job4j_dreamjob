package ru.job4j.dream.servlet;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DownloadServletTest {
    @Test
    public void whenDownloadFileCheckHeaderAndContent() throws ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> message2 = ArgumentCaptor.forClass(String.class);

        when(req.getParameter("photoId")).thenReturn("25");
        doNothing().when(resp).setContentType(message.capture());
        doNothing().when(resp).setHeader(any(), message2.capture());

        try {
            new DownloadServlet().doGet(req, resp);
        } catch (IOException e) {
            System.out.println();
        }

        assertThat(message.getValue(), is("image/png"));
        assertThat(message2.getValue(), is("attachment; filename=\"25\""));
    }
}