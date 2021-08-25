import React from "react";
import {UserAccount} from "../Types/UserAccount";

export interface SessionContextDefinition {
    sessionContext: SessionContextState;
    setSessionContext: (value: SessionContextState) => void;
}

export interface SessionContextState {
    userAccount: UserAccount | undefined | null;
}

const INITIAL_SESSION_CONTEXT: SessionContextState = {
    userAccount: undefined
};

const SessionContext = React.createContext<SessionContextDefinition | undefined>(undefined);
export const useSessionContext = () => React.useContext(SessionContext)
    || {sessionContext: INITIAL_SESSION_CONTEXT, setSessionContext: (value: SessionContextState) => null};
export function SessionContextProvider(props: any) {
    const [sessionContext, setSessionContext] = React.useState(INITIAL_SESSION_CONTEXT);
    return <SessionContext.Provider value={{sessionContext, setSessionContext}} {...props} />;
}