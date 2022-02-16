package ru.job4j.dream.servlet;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CandidateEditServletTest {

    @Test
    public void whenCandidateIdIsNullThenRedirectEditWithEmptyObjectCandidate() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ArgumentCaptor<Candidate> argument = ArgumentCaptor.forClass(Candidate.class);

        when(req.getParameter("id")).thenReturn(null);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);
        doNothing().when(req).setAttribute(any(), argument.capture());

        new CandidateEditServlet().doGet(req, resp);

        Candidate candidate = argument.getValue();

        verify(req).getRequestDispatcher("edit.jsp");
        assertThat(candidate.getCity(), is("Не выбран"));
    }

    @Test
    public void whenCandidateIdIsNotNullThenRedirectEditWithObjectCandidate() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        ArgumentCaptor<Candidate> argument = ArgumentCaptor.forClass(Candidate.class);

        Candidate create = new Candidate(0, "Candidate_for_mock_test", 10, 1);
        PsqlStore.instanceOf().save(create);

        when(req.getParameter("id")).thenReturn(String.valueOf(create.getId()));
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);
        doNothing().when(req).setAttribute(any(), argument.capture());

        new CandidateEditServlet().doGet(req, resp);

        Candidate candidate = argument.getValue();

        verify(req).getRequestDispatcher("edit.jsp");
        assertThat(candidate.getCity(), is("Minsk"));
        assertThat(candidate.getName(), is("Candidate_for_mock_test"));
    }
}