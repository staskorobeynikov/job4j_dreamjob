package ru.job4j.dream.servlet;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
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

public class CandidateServletTest {

    @Test
    public void whenDoGetReturnViewCandidatesJSP() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(any())).thenReturn(dispatcher);

        new CandidateServlet().doGet(req, resp);

        verify(req).getRequestDispatcher("candidate/candidates.jsp");
    }

    @Test
    public void whenDoPostRedirectViewCandidatesJSPAndAddNewCandidate() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("id")).thenReturn(String.valueOf(0));
        when(req.getParameter("name")).thenReturn("Junior Java Developer");
        when(req.getParameter("photoId")).thenReturn(String.valueOf(20));
        when(req.getParameter("city")).thenReturn(String.valueOf(1));

        new CandidateServlet().doPost(req, resp);
        Candidate candidate = PsqlStore.instanceOf().findAllCandidates()
                .stream()
                .filter(c -> c.getName().equals("Junior Java Developer"))
                .findFirst()
                .orElse(null);

        verify(resp).sendRedirect(String.format("%s/candidates.do", req.getContextPath()));
        assertThat(candidate.getName(), is("Junior Java Developer"));
    }
}