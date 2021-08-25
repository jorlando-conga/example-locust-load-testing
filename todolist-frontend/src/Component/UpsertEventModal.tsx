import React, {ChangeEvent, useState} from "react";
import {Card, makeStyles} from "@material-ui/core";
import TextField from "@material-ui/core/TextField/TextField";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import SaveAltIcon from '@material-ui/icons/SaveAlt';
import {GET_LIST_EVENTS, ListEventRecord} from "../Data/Query";
import {UPSERT_LIST_EVENT} from "../Data/Mutation";
import {useMutation} from "@apollo/client";
import {toFormattedString, toISOString} from "../Util/DateUtil";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import CloseIcon from '@material-ui/icons/Close';
import './Modal.css';
import CardContent from "@material-ui/core/CardContent";

const useStyles = makeStyles((theme: any) => ({
    header: {
        flex: 1,
        marginBottom: '1.5rem'
    },
    textInput: {
        width: '100%',
        marginTop: '1rem'
    },
    bottomNav: {
        marginTop: '1rem'
    }
}));

interface UpsertEventModalProps {
    isEmbedded?: boolean | undefined;
    onClose: () => void;
    existingEvent?: ListEventRecord | undefined;
    listViewEndDate: Date;
}

interface UpsertEventModalState {
    isSubmitting: boolean;
    description?: string | undefined;
    dueDate?: string | undefined;
    zipCode?: string | undefined;
}

const UpsertEventCard = (props: UpsertEventModalProps) => {
    const classes = useStyles();
    const [upsertEvent] = useMutation(UPSERT_LIST_EVENT);
    const [state, setState] = useState<UpsertEventModalState>({
        isSubmitting: false,
        ...(!props.existingEvent ? {} : {
            description: props.existingEvent.description,
            dueDate: props.existingEvent.dueDate,
            zipCode: props.existingEvent.zipCode
        })
    });
    const isReady: boolean = (state.description && state.dueDate && !state.isSubmitting && true) || false;
    if (props.isEmbedded) {
        return (
            <div style={{padding: '16px', borderTop: '1px solid #b2bec3'}}>
                <TextField label="Description"
                           variant="outlined"
                           type={"text"}
                           className={state.zipCode ? `${classes.textInput} MuiFormLabel-filled` : classes.textInput}
                           value={state.description}
                           onChange={(event: ChangeEvent<HTMLInputElement>) => {
                               const value: string = event.target.value;
                               setState({
                                   ...state,
                                   description: value || undefined
                               });
                           }}
                           required
                />
                <TextField label="Event Date"
                           variant="outlined"
                           type={"text"}
                           className={state.zipCode ? `${classes.textInput} MuiFormLabel-filled` : classes.textInput}
                           value={state.dueDate}
                           onChange={(event: ChangeEvent<HTMLInputElement>) => {
                               const value: string = event.target.value;
                               setState({
                                   ...state,
                                   dueDate: value || undefined
                               });
                           }}
                           required
                />
                <TextField label="Postal Code"
                           variant="outlined"
                           type={"text"}
                           className={state.zipCode ? `${classes.textInput} MuiFormLabel-filled` : classes.textInput}
                           value={state.zipCode}
                           onChange={(event: ChangeEvent<HTMLInputElement>) => {
                               const value: string = event.target.value;
                               setState({
                                   ...state,
                                   zipCode: value || undefined
                               });
                           }}
                />
                <BottomNavigation
                    value={undefined}
                    onChange={(event, newValue) => {
                        if (newValue === 'cancel') {
                            props.onClose();
                        } else if (newValue === 'submit') {
                            setState({
                                ...state,
                                isSubmitting: true
                            });
                            upsertEvent({
                                variables: {
                                    eventId: (props.existingEvent && props.existingEvent.id) || undefined,
                                    event: {
                                        description: state.description,
                                        dueDate: state.dueDate,
                                        zipCode: state.zipCode
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
                            }).then(() => {
                                props.onClose();
                            });
                        }
                    }}
                    className={classes.bottomNav}
                    showLabels>
                    <BottomNavigationAction value={'cancel'} label="Cancel" icon={<CloseIcon />} />
                    <BottomNavigationAction value={'submit'} disabled={!isReady} style={isReady ? {color: 'blue'} : undefined} label="Save" icon={<SaveAltIcon />} />
                </BottomNavigation>
            </div>
        );
    }
    return (
        <Card className={classes.header}>
            <CardContent>
                <div className={"col-12"}>
                    <TextField label="Description"
                               variant="outlined"
                               type={"text"}
                               onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                   const value: string = event.target.value;
                                   setState({
                                       ...state,
                                       description: value || undefined
                                   });
                               }}
                               className={classes.textInput}
                               required
                    />
                </div>
                <div className={"col-12"}>
                    <TextField label="Event Date"
                               variant="outlined"
                               type={"text"}
                               onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                   const value: string = event.target.value;
                                   setState({
                                       ...state,
                                       dueDate: value || undefined
                                   });
                               }}
                               className={classes.textInput}
                               required
                    />
                </div>
                <div className={"col-12"}>
                    <TextField label="Postal Code"
                               variant="outlined"
                               type={"text"}
                               onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                   const value: string = event.target.value;
                                   setState({
                                       ...state,
                                       zipCode: value || undefined
                                   });
                               }}
                               className={classes.textInput}
                    />
                </div>
                <BottomNavigation
                    value={undefined}
                    onChange={(event, newValue) => {
                        if (newValue === 'cancel') {
                            props.onClose();
                        } else if (newValue === 'submit') {
                            setState({
                                ...state,
                                isSubmitting: true
                            });
                            upsertEvent({
                                variables: {
                                    eventId: (props.existingEvent && props.existingEvent.id) || undefined,
                                    event: {
                                        description: state.description,
                                        dueDate: state.dueDate,
                                        zipCode: state.zipCode
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
                            }).then(() => {
                                props.onClose();
                            });
                        }
                    }}
                    className={classes.bottomNav}
                    showLabels>
                    <BottomNavigationAction value={'cancel'} label="Cancel" icon={<CloseIcon />} />
                    <BottomNavigationAction value={'submit'} disabled={!isReady} style={isReady ? {color: 'blue'} : undefined} label="Save" icon={<SaveAltIcon />} />
                </BottomNavigation>
            </CardContent>
        </Card>
    );
};

export default UpsertEventCard;