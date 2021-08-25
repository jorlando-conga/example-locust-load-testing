package example.todolist.filter;

import example.todolist.context.SessionContext;
import example.todolist.data.cache.model.Session;
import example.todolist.data.cache.repository.SessionRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SessionFilter implements Filter {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SessionFilter.class);
    public static final String SESSION_COOKIE_NAME = "TODOLIST_SID";

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionContext sessionContext;

    private static final String extractSessionToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equalsIgnoreCase(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String sessionToken = extractSessionToken((HttpServletRequest) request);
        if (sessionToken != null) {
            Session session = sessionRepository.findById(sessionToken).orElse(null);
            if (session != null) {
                final boolean extendSessionDuration = true;
                sessionContext.initialize(sessionRepository.persist(session, extendSessionDuration));
                LOG.info("Initialized session for UserAccount ID: {}", session.getUserAccountId());
            } else {
                LOG.warn("Expired session cookie found for request");
            }
        } else {
            LOG.warn("No session cookie found for request");
        }
        chain.doFilter(request, response);
    }
}
