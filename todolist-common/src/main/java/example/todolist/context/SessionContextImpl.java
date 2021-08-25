package example.todolist.context;

import example.todolist.data.cache.model.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class SessionContextImpl implements SessionContext {

    private Session session;

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getUserAccountId() {
        if (session == null) {
            return null;
        }
        return session.getUserAccountId();
    }

    @Override
    public void initialize(Session session) {
        this.session = session;
    }
}
