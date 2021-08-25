import React, {useState} from "react";
import CardHeader from "@material-ui/core/CardHeader";
import IconButton from "@material-ui/core/IconButton";
import {Card} from "@material-ui/core";
import './EventListItem.css'
import CardContent from "@material-ui/core/CardContent";
import {GET_EVENT_WEATHER, GET_LIST_EVENTS, ListEventRecord} from "../Data/Query";
import {useMutation, useQuery} from "@apollo/client";
import UpsertEventCard from "./UpsertEventModal";
import MoreVertIcon from '@material-ui/icons/MoreVert';
import AssignmentTurnedInIcon from '@material-ui/icons/AssignmentTurnedIn';
import {UPSERT_LIST_EVENT} from "../Data/Mutation";
import {dateToDateString, toFormattedString, toISOString} from "../Util/DateUtil";

interface EventListItemProps {
    event: ListEventRecord;
    listViewEndDate: Date;
}

const WeatherInstrumentedEventListItem = (props: EventListItemProps) => {
    const {data, loading, error} = useQuery(GET_EVENT_WEATHER, {
        fetchPolicy: 'cache-and-network',
        returnPartialData: true,
        variables: {
            eventId: props.event.id
        }
    });
    const [upsertEvent] = useMutation(UPSERT_LIST_EVENT);
    const markCompleted = () => {
        upsertEvent({
            variables: {
                eventId: props.event.id,
                event: {
                    completed: true,
                    description: props.event.description,
                    dueDate: props.event.dueDate,
                    zipCode: props.event.zipCode
                }
            },
            refetchQueries: [
                {
                    query: GET_LIST_EVENTS,
                    variables: {
                        endDate: toFormattedString(props.listViewEndDate)
                    }
                }
            ]
        });
    };
    const hasWeatherData: boolean = (!loading && !error && data && data.getEventWeather && true) || false;
    const [showModifyCard, setShowModifyCard] = useState<boolean>(false);
    return (
        <>
            <Card className={"list-item-card"} style={{marginTop: '1rem'}}>
                <CardHeader
                    action={
                        <>
                            <IconButton aria-label="modify" onClick={() => markCompleted()}>
                                <AssignmentTurnedInIcon />
                            </IconButton>
                            <IconButton aria-label="modify" onClick={() => setShowModifyCard(true)}>
                                <MoreVertIcon />
                            </IconButton>
                        </>
                    }
                    title={props.event.description}
                    subheader={dateToDateString(new Date(props.event.dueDate))}
                />
                {hasWeatherData && <CardContent>
                    <div className={"col-6"}>
                        Location: {props.event.zipCode}
                    </div>
                    <div className={"col-6"}>
                        <b>{data.getEventWeather.conditions}</b>: {data.getEventWeather.temperature}F / {data.getEventWeather.windSpeed}MPH
                    </div>
                </CardContent>}
                {showModifyCard && <UpsertEventCard isEmbedded={true}
                                                    listViewEndDate={props.listViewEndDate}
                                                    onClose={() => setShowModifyCard(false)}
                                                    existingEvent={props.event}
                />}
            </Card>
        </>
    );
};

const EventListItem = (props: EventListItemProps) => {
    const [upsertEvent] = useMutation(UPSERT_LIST_EVENT);
    if (props.event.zipCode) {
        return <WeatherInstrumentedEventListItem listViewEndDate={props.listViewEndDate} event={props.event} />;
    }
    const markCompleted = () => {
        upsertEvent({
            variables: {
                eventId: props.event.id,
                event: {
                    completed: true,
                    description: props.event.description,
                    dueDate: props.event.dueDate,
                    zipCode: props.event.zipCode
                }
            },
            refetchQueries: [
                {
                    query: GET_LIST_EVENTS,
                    variables: {
                        endDate: toFormattedString(props.listViewEndDate)
                    }
                }
            ]
        });
    };
    const [showModifyCard, setShowModifyCard] = useState<boolean>(false);
    return (
      <>
          <Card className={"list-item-card"} style={{marginTop: '1rem'}}>
              <CardHeader
                  action={
                      <>
                          <IconButton aria-label="modify" onClick={() => markCompleted()}>
                              <AssignmentTurnedInIcon />
                          </IconButton>
                          <IconButton aria-label="modify" onClick={() => setShowModifyCard(true)}>
                              <MoreVertIcon />
                          </IconButton>
                      </>
                  }
                  title={props.event.description}
                  subheader={dateToDateString(new Date(props.event.dueDate))}
              />
              {props.event.zipCode && <CardContent>
                  <div className={"col-6"}>
                      Location: {props.event.zipCode}
                  </div>
                  <div className={"col-6"}>
                      78F / 22NE
                  </div>
              </CardContent>}
              {showModifyCard && <UpsertEventCard isEmbedded={true}
                                                  listViewEndDate={props.listViewEndDate}
                                                  onClose={() => setShowModifyCard(false)}
                                                  existingEvent={props.event}
              />}
          </Card>
      </>
    );
};

export default EventListItem;