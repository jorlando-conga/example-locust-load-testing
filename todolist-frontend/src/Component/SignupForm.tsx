import React, {ChangeEvent, useState} from 'react';
import Grid from '@material-ui/core/Grid';
import {Card, makeStyles} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import CardContent from "@material-ui/core/CardContent";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import AppBar from "@material-ui/core/AppBar";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import PermIdentityIcon from '@material-ui/icons/PermIdentity';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { Alert, AlertTitle } from '@material-ui/lab';
import {useForm} from "react-hook-form";
import {validateEmail} from "../Util/EmailUtil";
import {LoginViewStep} from "../View/LoginView";
import {UserAccount} from "../Types/UserAccount";
import {red} from "@material-ui/core/colors";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";

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

interface SignupFormFields {
    firstName?: string;
    lastName?: string;
    emailAddress?: string;
    password?: string;
}

interface SignupFormResponse {
    success: boolean;
    message?: string;
    redirectUrl?: string;
}

const doSubmitSignup = (values: SignupFormFields, onSuccess: (redirectUrl: string) => void, onError: (message: string) => void) => {
    fetch('/api/rest/v1/signup', {
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
    }).then((response: SignupFormResponse) => {
        if (response.success && response.redirectUrl) {
            onSuccess(response.redirectUrl || '/');
        } else if (response.message) {
            onError(response.message);
        } else {
            throw response;
        }
    }).catch((response: any) => {
        alert(JSON.stringify(response));
        onError('An unexpected error occurred');
    });
};

interface SignupFormProps {
    onLogin: (userAccount: UserAccount) => void;
    onStepChange: (step: LoginViewStep) => void;
}

interface SignupFormState {
    errorMessage: string | undefined;
}

const SignupForm = (props: SignupFormProps) => {
    const [state, setState] = useState<SignupFormState>({
        errorMessage: undefined
    });
    const classes = useStyles();
    const {getValues, setValue, setError, clearErrors, formState} = useForm({
        reValidateMode: "onChange",
        mode: "onChange"
    });

    const values: any = getValues();
    const isReady: boolean = (values.firstName && values.lastName && validateEmail(values.emailAddress) && values.password && true) || false;
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
                                    ToDoList Signup
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
                            <TextField label="First Name"
                                       variant="outlined"
                                       type={"firstName"}
                                       onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                           const value: string | undefined = event.target.value;
                                           setValue("firstName", value || undefined);
                                           clearErrors("firstName");
                                       }}
                                       className={classes.textInput}
                                       error={("firstName" in formState.errors && formState.errors["firstName"] && true) || false}
                                       helperText={("firstName" in formState.errors && formState.errors["firstName"].message) || undefined}
                                       required
                            />
                        </div>
                        <div className={"col-12"}>
                            <TextField label="Last Name"
                                       variant="outlined"
                                       type={"lastName"}
                                       onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                           const value: string | undefined = event.target.value;
                                           setValue("lastName", value || undefined);
                                           clearErrors("lastName");
                                       }}
                                       className={classes.textInput}
                                       error={("lastName" in formState.errors && formState.errors["lastName"] && true) || false}
                                       helperText={("lastName" in formState.errors && formState.errors["lastName"].message) || undefined}
                                       required
                            />
                        </div>
                        <div className={"col-12"}>
                            <TextField label="E-Mail Address"
                                       variant="outlined"
                                       type={"emailAddress"}
                                       onChange={(event: ChangeEvent<HTMLInputElement>) => {
                                           const value: string | undefined = event.target.value;
                                           setValue("emailAddress", value || undefined);
                                           if (!validateEmail(value)) {
                                               setError("emailAddress", {message: "Invalid E-Mail Address"});
                                           } else {
                                               clearErrors("emailAddress");
                                           }
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
                                const values: SignupFormFields = getValues();
                                if (newValue === LoginViewStep.SIGNUP_FORM) {
                                    if (!validateEmail(values.emailAddress)) {
                                        setError("emailAddress", {message: "Invalid E-Mail Address"});
                                        return;
                                    }
                                    doSubmitSignup(values, (redirectUrl: string) => {
                                        if (state.errorMessage) {
                                            setState({
                                                ...state,
                                                errorMessage: undefined
                                            });
                                        }
                                        window.location.href = redirectUrl;
                                    }, (errorMessage: string) => {
                                        setState({
                                            ...state,
                                            errorMessage: errorMessage
                                        });
                                    })
                                } else {
                                    props.onStepChange(newValue);
                                }
                            }}
                            className={classes.bottomNav}
                            showLabels>
                            <BottomNavigationAction value={LoginViewStep.LOGIN_FORM}
                                                    label="Back to Login"
                                                    icon={<ArrowBackIcon />}
                            />
                            <BottomNavigationAction value={LoginViewStep.SIGNUP_FORM}
                                                    style={isReady ? {color: 'blue'} : undefined}
                                                    disabled={!isReady}
                                                    label="Sign Up"
                                                    icon={<PermIdentityIcon />}
                            />
                        </BottomNavigation>
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
};

export default SignupForm;