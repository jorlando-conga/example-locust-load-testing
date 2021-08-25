package example.todolist.controller.v1;

import example.todolist.controller.v1.dto.SignupRequest;
import example.todolist.controller.v1.dto.SignupResponse;
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
public class SignupController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public SignupResponse doSignup(@RequestBody SignupRequest signupRequest) {
        try {
            LOG.info("Attempting to perform Signup request");
            validateSignupRequest(signupRequest);

            if (userAccountRepository.findByEmailAddressIgnoreCase(signupRequest.getEmailAddress()) != null) {
                throw new IllegalArgumentException("E-Mail Address already exists");
            }

            UserAccount userAccount = new UserAccount();
            userAccount.setFirstName(signupRequest.getFirstName());
            userAccount.setLastName(signupRequest.getLastName());
            userAccount.setEmailAddress(signupRequest.getEmailAddress());
            userAccount.setPassword(PasswordUtil.hash(signupRequest.getPassword()));
            userAccount = userAccountRepository.persist(userAccount);

            Session session = new Session();
            session.setExpiresOn(LocalDateTime.now().plus(Session.DEFAULT_SESSION_DURATION));
            session.setUserAccountId(userAccount.getId());
            session.setToken(Session.generateSessionToken());
            sessionRepository.save(session);

            httpServletResponse.addCookie(SessionCookieUtil.buildSessionCookie(
                    session.getToken(), new Long(session.getTimeToLive()).intValue()));
            SignupResponse response = new SignupResponse();
            response.setSuccess(true);
            response.setRedirectUrl("/app");
            LOG.info("Handled signup successfully for new UserAccount ID: {}", userAccount.getId());
            return response;
        } catch (Exception e) {
            LOG.warn("Unable to complete signup request due to exception", e);
            SignupResponse response = new SignupResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    private static void validateSignupRequest(SignupRequest signupRequest) {
        if (signupRequest == null) {
            LOG.warn("SignupRequest cannot be NULL");
            throw new IllegalArgumentException("Invalid Signup Request");
        }
        if (StringUtils.isEmpty(signupRequest.getFirstName())) {
            LOG.warn("Invalid E-Mail Address for Signup Request");
            throw new IllegalArgumentException("Invalid E-Mail Address");
        }
        if (StringUtils.isEmpty(signupRequest.getLastName())) {
            LOG.warn("Invalid E-Mail Address for Signup Request");
            throw new IllegalArgumentException("Invalid E-Mail Address");
        }
        if (StringUtils.isEmpty(signupRequest.getPassword())) {
            LOG.warn("Invalid E-Mail Address for Signup Request");
            throw new IllegalArgumentException("Invalid E-Mail Address");
        }
        if (StringUtils.isEmpty(signupRequest.getEmailAddress())) {
            LOG.warn("Invalid E-Mail Address for Signup Request");
            throw new IllegalArgumentException("Invalid E-Mail Address");
        }
        if (!EmailValidator.getInstance().isValid(signupRequest.getEmailAddress())) {
            LOG.warn("Invalid E-Mail Address for Signup Request");
            throw new IllegalArgumentException("Invalid E-Mail Address");
        }
    }
}
