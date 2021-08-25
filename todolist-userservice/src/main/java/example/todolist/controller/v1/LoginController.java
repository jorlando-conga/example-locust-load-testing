package example.todolist.controller.v1;

import example.todolist.context.SessionContext;
import example.todolist.controller.v1.dto.LoginRequest;
import example.todolist.controller.v1.dto.LoginResponse;
import example.todolist.data.cache.model.Session;
import example.todolist.data.cache.repository.SessionRepository;
import example.todolist.data.persistence.model.UserAccount;
import example.todolist.data.persistence.repository.UserAccountRepository;
import example.todolist.util.PasswordUtil;
import example.todolist.util.SessionCookieUtil;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rest/v1")
public class LoginController {
    private static final String LOGIN_ERROR_MESSAGE = "Invalid E-Mail Address or Password";
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public LoginResponse doLogin(@RequestBody LoginRequest loginRequest) {
        try {
            LOG.info("Attempting to perform Login request");
            validateLoginRequest(loginRequest);

            UserAccount userAccount = userAccountRepository.findByEmailAddressIgnoreCase(loginRequest.getEmailAddress());
            if (userAccount == null) {
                LOG.warn("Invalid E-Mail Address for login request");
                throw new IllegalArgumentException(LOGIN_ERROR_MESSAGE);
            }

            if (!PasswordUtil.matches(loginRequest.getPassword(), userAccount.getPassword())) {
                LOG.warn("Invalid Password for login request");
                throw new IllegalArgumentException(LOGIN_ERROR_MESSAGE);
            }

            Session session = new Session();
            session.setExpiresOn(LocalDateTime.now().plus(Session.DEFAULT_SESSION_DURATION));
            session.setUserAccountId(userAccount.getId());
            session.setToken(Session.generateSessionToken());
            sessionRepository.save(session);

            httpServletResponse.addCookie(SessionCookieUtil.buildSessionCookie(
                    session.getToken(), new Long(session.getTimeToLive()).intValue()));
            LoginResponse response = new LoginResponse();
            response.setSuccess(true);
            response.setRedirectUrl("/app");
            LOG.info("Handled login successfully for UserAccount ID: {}", userAccount.getId());
            return response;
        } catch (Exception e) {
            LOG.warn("Unable to complete login request due to exception", e);
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public boolean doLogout() {
        try {
            LOG.info("Attempting to perform logout request");
            Session session = sessionContext.getSession();
            if (session != null) {
                sessionRepository.deleteById(session.getToken());
                httpServletResponse.addCookie(SessionCookieUtil.buildLogoutCookie());
                LOG.info("Successfully removed session with token {}", session.getToken());
            }
        } catch (Exception e) {
            LOG.warn("Unable to complete logout request due to exception", e);
        }
        return true;
    }

    private static void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            LOG.warn("LoginRequest cannot be NULL");
            throw new IllegalArgumentException("Invalid Login Request");
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            LOG.warn("Invalid E-Mail Address for Login Request");
            throw new IllegalArgumentException(LOGIN_ERROR_MESSAGE);
        }
        if (StringUtils.isEmpty(loginRequest.getEmailAddress())) {
            LOG.warn("Invalid E-Mail Address for Login Request");
            throw new IllegalArgumentException(LOGIN_ERROR_MESSAGE);
        }
        if (!EmailValidator.getInstance().isValid(loginRequest.getEmailAddress())) {
            LOG.warn("Invalid E-Mail Address for Login Request");
            throw new IllegalArgumentException(LOGIN_ERROR_MESSAGE);
        }
    }
}
