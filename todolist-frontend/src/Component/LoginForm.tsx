import React, {ChangeEvent, useState} from 'react';
import {UserAccount} from "../Types/UserAccount";
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import {Card} from "@material-ui/core";
import { red } from '@material-ui/core/colors';
import Typography from "@material-ui/core/Typography";
import CardContent from "@material-ui/core/CardContent";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import AppBar from "@material-ui/core/AppBar";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import PermIdentityIcon from '@material-ui/icons/PermIdentity';
import { Alert, AlertTitle } from '@material-ui/lab';
import {useForm} from "react-hook-form";
import {validateEmail} from "../Util/EmailUtil";
import {LoginViewStep} from "../View/LoginView";

const useStyles = makeStyles((theme: any) => ({
    root: {
        flexGrow: 1,
    },
    avatar: {
        backgroundColor: red[500],
    },
    textInput: {
        width: '100%',
        marginTop: '1rem'
    },
    appBarToolBar: {
        textAlign: 'center'
    },
    appBarMenu: {
        marginRight: theme.spacing(2),
    },
    appBarTitle: {
        flexGrow: 1,
    },
    bottomNav: {
        marginTop: '1rem'
    }
}));

interface LoginFormFields {
    emailAddress?: string;
    password?: string;
}

interface LoginResponse {
    success: boolean;
    message?: string;
    redirectUrl?: string;
}

interface LoginViewProps {
    onStepChange: (step: LoginViewStep) => void;
    onLogin: (userAccount: UserAccount) => void;
}

interface LoginViewState {
    isSubmitting: boolean;
    errorMessage: string | undefined;
}

const LoginForm = (props: LoginViewProps) => {
    const [state, setState] = useState<LoginViewState>({
        isSubmitting: false,
        errorMessage: undefined
    });
    const classes = useStyles();
    const {getValues, setValue, setError, clearErrors, formState} = useForm({
        reValidateMode: "onChange",
        mode: "onChange"
    });
    const values: LoginFormFields = getValues();
    const isReady: boolean = (validateEmail(values.emailAddress) && values.password && true) || false;
    return (
        <Grid container
              direction="row"
              justifyContent="center"
              alignItems="center"
              style={{height: '90vh'}}>
            <Grid item xs={12} sm={6} md={4} lg={3}>
                <Card className={classes.root}>
                    <CardContent>
                        <AppBar position="static">
                            <Toolbar className={classes.appBarToolBar}>
                                <Typography variant="h5" className={classes.appBarTitle}>
                                    ToDoList Login
                                </Typography>
                            </Toolbar>
                        </AppBar>
                        {state.errorMessage && <div className={"col-12"}>
                            <Alert severity="error" style={{margin: '1rem'}}>
                                <AlertTitle>Error</AlertTitle>
                                {state.errorMessage}
                            </Alert>
                        </div>}
                        <div className={"col-12"}>
                            <TextField label="E-Mail Address"
                                       variant="outlined"
                                       type={"emailAddress"}
                                       onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                           const value: string | undefined = event.target.value;
                                           setValue("emailAddress", value || undefined);
                                           clearErrors("emailAddress");
                                       }}
                                       className={classes.textInput}
                                       error={("emailAddress" in formState.errors && formState.errors["emailAddress"] && true) || false}
                                       helperText={("emailAddress" in formState.errors && formState.errors["emailAddress"].message) || undefined}
                                       required
                            />
                        </div>
                        <div className={"col-12"}>
                            <TextField label="Password"
                                       variant="outlined"
                                       type={"password"}
                                       onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                           const value: string | undefined = event.target.value;
                                           setValue("password", value || undefined);
                                           clearErrors("password");
                                       }}
                                       className={classes.textInput}
                                       error={("password" in formState.errors && formState.errors["password"] && true) || false}
                                       helperText={("password" in formState.errors && formState.errors["password"].message) || undefined}
                                       required
                            />
                        </div>
                        <BottomNavigation
                            value={undefined}
                            onChange={(event, newValue) => {
                                event.preventDefault();
                                const values: LoginFormFields = getValues();
                                if (newValue === LoginViewStep.LOGIN_FORM) {
                                    if (!validateEmail(values.emailAddress)) {
                                        setError("emailAddress", {message: "Invalid E-Mail Address"});
                                        return;
                                    }
                                    fetch('/api/rest/v1/login', {
                                        method: 'POST',
                                        headers: {
                                            'Accept': 'application/json',
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify(values)
                                    }).then((response: Response) => {
                                        if (response && response.ok) {
                                            return response.json();
                                        }
                                        throw response;
                                    }).then((response: LoginResponse) => {
                                        if (response.success && response.redirectUrl) {
                                            if (state.errorMessage) {
                                                setState({
                                                    ...state,
                                                    errorMessage: undefined
                                                });
                                            }
                                            window.location.href = response.redirectUrl || '/';
                                        } else if (response.message) {
                                            setState({
                                                ...state,
                                                errorMessage: response.message
                                            });
                                        } else {
                                            throw response;
                                        }

                                    }).catch((response: any) => {
                                        alert(JSON.stringify(response));
                                        setState({
                                            ...state,
                                            errorMessage: 'An unexpected error occurred'
                                        });
                                    });
                                } else {
                                    props.onStepChange(newValue);
                                }
                            }}
                            className={classes.bottomNav}
                            showLabels>
                            <BottomNavigationAction value={LoginViewStep.SIGNUP_FORM} label="Sign Up" icon={<PermIdentityIcon />} />
                            <BottomNavigationAction value={LoginViewStep.LOGIN_FORM} disabled={!isReady} style={isReady ? {color: 'blue'} : undefined} label="Login" icon={<ArrowForwardIosIcon />} />
                        </BottomNavigation>
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
};

export default LoginForm;