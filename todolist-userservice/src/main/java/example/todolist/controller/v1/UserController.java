package example.todolist.controller.v1;

import example.todolist.context.SessionContext;
import example.todolist.controller.v1.dto.UserAccountDto;
import example.todolist.data.persistence.model.UserAccount;
import example.todolist.data.persistence.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/v1")
public class UserController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public UserAccountDto getUserAccount() {
        LOG.info("Attempting to retrieve User Account");
        String userAccountId = sessionContext.getUserAccountId();
        if (userAccountId == null) {
            LOG.warn("Unable to retrieve User Account record for invalid session");
            throw new IllegalStateException("Invalid Session");
        }

        UserAccount userAccount = userAccountRepository.findById(userAccountId).orElse(null);
        if (userAccount == null) {
            LOG.warn("Unable to retrieve User Account record for Session UserAccountId: {}", userAccountId);
            throw new IllegalStateException("Invalid User Account");
        }

        LOG.info("Retrieving User Account information for User ID: {}", userAccount.getId());
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setFirstName(userAccount.getFirstName());
        userAccountDto.setLastName(userAccount.getLastName());
        userAccountDto.setEmailAddress(userAccount.getEmailAddress());
        return userAccountDto;
    }

}
