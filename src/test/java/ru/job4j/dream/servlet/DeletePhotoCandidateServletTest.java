package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class DeletePhotoCandidateServletTest {
    @Test
    public void whenDeleteCandidatePhoto() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        Candidate create = new Candidate(0, "Candidate_test_delete_photo", 20, 1);
        PsqlStore.instanceOf().save(create);

        when(req.getParameter("photoId")).thenReturn(String.valueOf(create.getPhotoId()));

        new DeletePhotoCandidateServlet().doGet(req, resp);

        verify(resp).sendRedirect(String.format("%s/candidates.do", req.getContextPath()));
    }
}