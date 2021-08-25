import React, {useState} from 'react';
import LoginForm from "../Component/LoginForm";
import SignupForm from "../Component/SignupForm";
import {UserAccount} from "../Types/UserAccount";

export enum LoginViewStep {
    LOGIN_FORM="LOGIN_FORM",
    SIGNUP_FORM="SIGNUP_FORM"
}

interface LoginViewProps {
    onLogin: (userAccount: UserAccount) => void;
}

const LoginView = (props: LoginViewProps) => {
    const [viewStep, setViewStep] = useState<LoginViewStep>(LoginViewStep.LOGIN_FORM);
    if (viewStep === LoginViewStep.SIGNUP_FORM) {
        return <SignupForm onLogin={props.onLogin} onStepChange={(step: LoginViewStep) => setViewStep(step)} />;
    }
    return <LoginForm onLogin={props.onLogin} onStepChange={(step: LoginViewStep) => setViewStep(step)} />;
};

export default LoginView;