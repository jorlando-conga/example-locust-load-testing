package example.todolist.data.cache.repository;

import example.todolist.data.cache.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface SessionRepository extends CrudRepository<Session, String> {
    default Session persist(Session session, boolean extendSessionDuration) {
        if (extendSessionDuration) {
            session.setExpiresOn(LocalDateTime.now().plus(Session.DEFAULT_SESSION_DURATION));
        }
        return save(session);
    }
}
