package example.todolist.controller.v1;

import example.todolist.context.SessionContext;
import example.todolist.controller.v1.dto.UpsertListEvent;
import example.todolist.data.persistence.model.ListEvent;
import example.todolist.data.persistence.repository.ListEventRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class GraphQLMutation implements GraphQLMutationResolver {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(GraphQLMutation.class);
    private static final DateTimeFormatter EXPECTED_FORMAT = DateTimeFormatter.ofPattern("MM/dd/uuuu");

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ListEventRepository listEventRepository;

    public String upsertListEvent(String eventId, UpsertListEvent upsertListEvent) {
        LOG.info("Upserting event ID: {} for UserAccount ID: {}",
                eventId, sessionContext.getUserAccountId());
        ListEvent listEvent = listEventRepository.findByUserAccountIdAndId(sessionContext.getUserAccountId(), eventId);
        if (listEvent == null) {
            listEvent = new ListEvent();
            listEvent.setUserAccountId(sessionContext.getUserAccountId());
        }

        listEvent.setDueDate(LocalDate.parse(upsertListEvent.getDueDate(), EXPECTED_FORMAT).atTime(0, 0));
        listEvent.setDescription(upsertListEvent.getDescription());
        listEvent.setLocationZipCode(upsertListEvent.getZipCode());
        listEvent.setCompleted(Optional.ofNullable(upsertListEvent.getCompleted()).orElse(false));
        return listEventRepository.persist(listEvent).getId();
    }
}
