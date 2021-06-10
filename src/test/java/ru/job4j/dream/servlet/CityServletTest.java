package ru.job4j.dream.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({LogManager.class, PsqlStore.class})
@PowerMockIgnore("javax.management.*")
public class CityServletTest {

    @Test
    public void whenJSONContainsContent() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("");
        PrintWriter writer = new PrintWriter(stringWriter);

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.mockStatic(LogManager.class);

        Store store = MemStore.instanceOf();
        Logger logger = mock(Logger.class);

        when(LogManager.getLogger(PsqlStore.class)).thenReturn(logger);
        when(PsqlStore.instanceOf()).thenReturn(store);
        when(resp.getWriter()).thenReturn(writer);

        new CityServlet().doGet(req, resp);

        String result = stringWriter.toString();
        writer.flush();

        assertThat(result, is("[{\"id\":1,\"name\":\"Minsk\"},{\"id\":2,\"name\":\"Moscow\"}]"));
    }
}