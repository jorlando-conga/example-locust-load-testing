package example.todolist.data.persistence.repository;

import example.todolist.data.persistence.model.ListEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ListEventRepository extends JpaRepository<ListEvent, String> {
    List<ListEvent> findAllByUserAccountIdAndCompletedIsFalseOrderByDueDate(String userAccountId);
    List<ListEvent> findAllByUserAccountIdAndCompletedIsFalseAndDueDateBeforeOrderByDueDate(
            String userAccountId, LocalDateTime endDate);
    ListEvent findByUserAccountIdAndId(String userAccountId, String id);
    default ListEvent persist(ListEvent userAccount) {
        LocalDateTime now = LocalDateTime.now();
        if (userAccount.getCreatedOn() == null) {
            userAccount.setCreatedOn(now);
        }
        userAccount.setLastModifiedOn(now);
        return save(userAccount);
    }
}
