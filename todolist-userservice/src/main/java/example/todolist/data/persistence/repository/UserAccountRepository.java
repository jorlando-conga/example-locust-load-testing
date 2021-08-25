package example.todolist.data.persistence.repository;

import example.todolist.data.persistence.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findByEmailAddressIgnoreCase(String emailAddress);
    default UserAccount persist(UserAccount userAccount) {
        LocalDateTime now = LocalDateTime.now();
        if (userAccount.getCreatedOn() == null) {
            userAccount.setCreatedOn(now);
        }
        userAccount.setLastModifiedOn(now);
        return save(userAccount);
    }
}
