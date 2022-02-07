package ru.job4j.dream.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CityServletTest {

    @Test
    public void whenJSONContainsContent() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("");
        PrintWriter writer = new PrintWriter(stringWriter);

        when(resp.getWriter()).thenReturn(writer);

        new CityServlet().doGet(req, resp);

        String result = stringWriter.toString();
        writer.flush();

        assertThat(result, is("[{\"id\":1,\"name\":\"Minsk\"}]"));
    }
}