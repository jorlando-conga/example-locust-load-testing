package example.todolist.context;

import example.todolist.data.cache.model.Session;

public interface SessionContext {
    Session getSession();
    String getUserAccountId();
    void initialize(Session session);
}
