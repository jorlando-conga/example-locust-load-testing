import React, {ChangeEvent} from "react";
import modalRenderer from "../Util/ModalRenderer";
import {makeStyles, Modal} from "@material-ui/core";
import TextField from "@material-ui/core/TextField/TextField";
import {useForm} from "react-hook-form";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import SaveAltIcon from '@material-ui/icons/SaveAlt';
import CloseIcon from '@material-ui/icons/Close';

const useStyles = makeStyles((theme: any) => ({
    textInput: {
        width: '100%',
        marginTop: '1rem'
    },
    bottomNav: {
        marginTop: '1rem'
    }
}));

interface CreateEventModalProps {
    onClose: () => void;
}

const CreateEventModal = (props: CreateEventModalProps) => {
    const classes = useStyles();
    const {getValues, setValue, setError, clearErrors, formState} = useForm({
        reValidateMode: "onChange",
        mode: "onChange"
    });
    const values: any = getValues();
    const isReady: boolean = values.description && values.dueDate;
    return (
        modalRenderer(
            <Modal open={true} onClose={props.onClose} aria-labelledby="create event" aria-describedby="create event dialog">
                <>
                    <div className={"col-12"}>
                        <TextField label="Description"
                                   variant="outlined"
                                   type={"description"}
                                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                       const value: string | undefined = event.target.value;
                                       setValue("description", value || undefined);
                                       clearErrors("description");
                                   }}
                                   className={classes.textInput}
                                   error={("description" in formState.errors && formState.errors["description"] && true) || false}
                                   helperText={("description" in formState.errors && formState.errors["description"].message) || undefined}
                                   required
                        />
                    </div>
                    <div className={"col-12"}>
                        <TextField label="Event Date"
                                   variant="outlined"
                                   type={"dueDate"}
                                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                       const value: string | undefined = event.target.value;
                                       setValue("dueDate", value || undefined);
                                       clearErrors("dueDate");
                                   }}
                                   className={classes.textInput}
                                   error={("dueDate" in formState.errors && formState.errors["dueDate"] && true) || false}
                                   helperText={("dueDate" in formState.errors && formState.errors["dueDate"].message) || undefined}
                                   required
                        />
                    </div>
                    <div className={"col-12"}>
                        <TextField label="Postal Code"
                                   variant="outlined"
                                   type={"zipCode"}
                                   onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                       const value: string | undefined = event.target.value;
                                       setValue("zipCode", value || undefined);
                                       clearErrors("zipCode");
                                   }}
                                   className={classes.textInput}
                                   error={("zipCode" in formState.errors && formState.errors["zipCode"] && true) || false}
                                   helperText={("zipCode" in formState.errors && formState.errors["zipCode"].message) || undefined}
                        />
                    </div>
                    <BottomNavigation
                        value={undefined}
                        onChange={(event, newValue) => {
                            if (newValue === 'cancel') {
                                props.onClose();
                            } else if (newValue === 'submit') {
                                alert('Submit!');
                            }
                        }}
                        className={classes.bottomNav}
                        showLabels>
                        <BottomNavigationAction value={'cancel'} label="Cancel" icon={<CloseIcon />} />
                        <BottomNavigationAction value={'submit'} disabled={!isReady} style={isReady ? {color: 'blue'} : undefined} label="Create" icon={<SaveAltIcon />} />
                    </BottomNavigation>
                </>
            </Modal>
        )
    );
}