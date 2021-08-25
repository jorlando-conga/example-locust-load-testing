package example.todolist.controller.v1;

import example.todolist.context.SessionContext;
import example.todolist.controller.v1.dto.ListEventDto;
import example.todolist.controller.v1.dto.WeatherResultDto;
import example.todolist.data.persistence.model.ListEvent;
import example.todolist.data.persistence.model.WeatherResult;
import example.todolist.data.persistence.repository.ListEventRepository;
import example.todolist.data.persistence.repository.WeatherResultRepository;
import example.todolist.external.WeatherApiAdapter;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GraphQLQuery implements GraphQLQueryResolver {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(GraphQLQuery.class);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/uuuu");

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ListEventRepository listEventRepository;

    @Autowired
    private WeatherResultRepository weatherResultRepository;

    @Autowired
    private WeatherApiAdapter weatherApiAdapter;

    public List<ListEventDto> fetchListEvents(String endDate) {
        if (sessionContext == null || sessionContext.getUserAccountId() == null) {
            throw new RuntimeException("Invalid Session");
        }
        LOG.debug("Retrieving events for UserAccount ID: {}", sessionContext.getUserAccountId());

        List<ListEventDto> listResult = new ArrayList<>();
        LocalDateTime endDateObj = null;
        if (!StringUtils.isEmpty(endDate)) {
            endDateObj = LocalDate.parse(endDate, DATE_FORMAT).atTime(0, 0);
        }
        List<ListEvent> listEvents = null;
        if (endDate != null) {
            listEvents = listEventRepository.findAllByUserAccountIdAndCompletedIsFalseAndDueDateBeforeOrderByDueDate(
                    sessionContext.getUserAccountId(), endDateObj);
        } else {
            listEvents = listEventRepository.findAllByUserAccountIdAndCompletedIsFalseOrderByDueDate(
                    sessionContext.getUserAccountId());
        }
        for (ListEvent listEvent : listEvents) {
            ListEventDto listEventDto = new ListEventDto();
            listEventDto.setId(listEvent.getId());
            listEventDto.setDescription(listEvent.getDescription());
            listEventDto.setDueDate(listEvent.getDueDate() == null ? null : listEvent.getDueDate().format(DATE_FORMAT));
            listEventDto.setZipCode(listEvent.getLocationZipCode());
            listEventDto.setCompleted(listEvent.isCompleted());
            listResult.add(listEventDto);
        }
        LOG.info("Retrieved {} events for UserAccount ID: {}", listEvents.size(), sessionContext.getUserAccountId());
        return listResult;
    }

    public WeatherResultDto getEventWeather(String eventId) {
        if (sessionContext == null || sessionContext.getUserAccountId() == null) {
            throw new RuntimeException("Invalid Session");
        }
        LOG.info("Retrieving weather data for eventId: {}, UserAccount ID: {}",
                eventId, sessionContext.getUserAccountId());

        ListEvent listEvent = listEventRepository.findByUserAccountIdAndId(sessionContext.getUserAccountId(), eventId);
        if (listEvent == null) {
            throw new RuntimeException("Invalid Event ID");
        }

        LocalDateTime eventDate = listEvent.getDueDate();
        WeatherResult result = weatherResultRepository.findWeatherResult(listEvent.getLocationZipCode(), eventDate);
        if (result == null) {
            WeatherApiAdapter.WeatherApiResult apiResult = weatherApiAdapter.getWeatherData(
                    listEvent.getLocationZipCode(), listEvent.getDueDate());
            if (apiResult == null) {
                return null;
            }
            result = new WeatherResult();
            result.setConditions(apiResult.getConditions());
            result.setTemperature(apiResult.getTemperature());
            result.setWindSpeed(apiResult.getWindSpeed());
            result.setTemperature(apiResult.getTemperature());
            result.setZipCode(listEvent.getLocationZipCode());
            result.setEventDate(eventDate);
            result = weatherResultRepository.persist(result);
        }

        if (result.getConditions() == null || result.getTemperature() == null) {
            return null;
        }

        WeatherResultDto resultDto = new WeatherResultDto();
        resultDto.setId(listEvent.getId());
        resultDto.setZipCode(listEvent.getLocationZipCode());
        resultDto.setConditions(result.getConditions());
        resultDto.setTemperature(result.getTemperature());
        resultDto.setWindSpeed(result.getWindSpeed());
        resultDto.setLastUpdatedAt(LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_DATE_TIME));
        return resultDto;
    }
}
