import React, {useState} from 'react';
import {Card, CircularProgress, Container, makeStyles} from "@material-ui/core";
import NavBar from "../Component/NavBar";
import Grid from "@material-ui/core/Grid";
import CardContent from "@material-ui/core/CardContent";
import CardHeader from "@material-ui/core/CardHeader";
import IconButton from "@material-ui/core/IconButton";
import AddIcon from '@material-ui/icons/Add';
import EventListItem from "../Component/EventListItem";
import {useQuery} from "@apollo/client";
import {GET_LIST_EVENTS, ListEventRecord} from "../Data/Query";
import {Alert, AlertTitle} from "@material-ui/lab";
import {dateToDateString, toFormattedString, toISOString} from "../Util/DateUtil";
import UpsertEventCard from "../Component/UpsertEventModal";

const useStyles = makeStyles((theme: any) => ({
    header: {
        flexGrow: 1,
        marginBottom: '1.5rem'
    },
}));

interface ListViewState {
    showCreateDialog: boolean;
    listEndDate: Date;
    title: string;
}

const ListView = () => {
    const classes: any = useStyles();
    const defaultEndDate: Date = new Date();
    defaultEndDate.setDate(defaultEndDate.getDate() + 1);
    const [state, setState] = useState<ListViewState>({
        showCreateDialog: false,
        listEndDate: defaultEndDate,
        title: "All Events"
    });
    const {data, loading, error} = useQuery(GET_LIST_EVENTS, {
        fetchPolicy: 'cache-and-network',
        returnPartialData: true,
        variables: {
            endDate: (state == null || state.listEndDate == null) ? null : toFormattedString(state.listEndDate)
        }
    });
    const hasError: boolean = (error && !loading && true) || false;
    const hasData: boolean = (!hasError && data && data.fetchListEvents && true) || false;
    let startDate: Date | null = null;
    let endDate: Date | null = null;
    if (hasData) {
        (data.fetchListEvents || []).forEach((event: ListEventRecord) => {
            const currentEventDate: Date = new Date(event.dueDate);
            if (startDate == null || currentEventDate < startDate) {
                startDate = currentEventDate;
            }
            if (endDate == null || currentEventDate > startDate) {
                endDate = currentEventDate;
            }
        });
    }
    const subTitle = (!startDate && !endDate)
        ? ""
        : (startDate === endDate
            ? dateToDateString(startDate)
            : (`${dateToDateString(startDate)} - ${dateToDateString(endDate)}`));
    return (
        <>
            <NavBar onListDateChange={(listEndDate: Date, title: string) => setState({
                ...state,
                title,
                listEndDate: listEndDate
            })}/>
            <Container maxWidth="md">
                <Grid container
                      direction="row"
                      justifyContent="center"
                      style={{height: '90vh', marginTop: '200px'}}>
                    <Grid item xs={12} sm={12} md={12} lg={12}>
                        <Card className={classes.header}>
                            <CardHeader
                                action={
                                    <IconButton aria-label="add" onClick={() => setState({
                                        ...state,
                                        showCreateDialog: true
                                    })}>
                                        <AddIcon />
                                    </IconButton>
                                }
                                title="Today's Events"
                                subheader={subTitle}
                            />
                        </Card>
                        {state.showCreateDialog && <UpsertEventCard listViewEndDate={state.listEndDate} onClose={() => setState({
                            ...state,
                            showCreateDialog: false
                        })}/>}
                        {hasError && <Card className={classes.header}>
                            <CardContent>
                                <Alert severity="error" style={{margin: '1rem'}}>
                                    <AlertTitle>Error</AlertTitle>
                                    An unexpected error occurred while loading items
                                </Alert>
                            </CardContent>
                        </Card>}
                        {!error && !loading && (!data || !data.fetchListEvents || (data.fetchListEvents.length <= 0)) && <Card className={classes.header}>
                            <CardContent>
                                <Alert severity="info" style={{margin: '1rem'}}>
                                    <AlertTitle>Info</AlertTitle>
                                    No upcoming events were found
                                </Alert>
                            </CardContent>
                        </Card>}
                        {hasData && <div children={(data.fetchListEvents || []).map((listEvent: ListEventRecord) => {
                            return (<EventListItem listViewEndDate={state.listEndDate} event={listEvent}/>);
                        })} />}
                        {loading && <Card className={classes.header}>
                            <CardContent>
                                <CircularProgress disableShrink />
                            </CardContent>
                        </Card>}
                    </Grid>
                </Grid>
            </Container>
        </>
    );
};

export default ListView;