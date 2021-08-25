import React, {useEffect} from 'react';
import {UserAccount} from "../Types/UserAccount";
import ListView from "../View/ListView";
import FullScreenSpinner from "../Component/FullScreenSpinner";
import {useSessionContext} from "../Context/SessionContext";
import './ToDoListApp.css';

const ListApp = () => {
    const {sessionContext, setSessionContext} = useSessionContext();
    useEffect(() => {
        fetch('/api/rest/v1/user').then((response: Response) => {
            if (response && response.ok) {
                return response.json();
            }
            throw response;
        }).then((userAccount: UserAccount) => {
            setSessionContext({
                ...sessionContext,
                userAccount: (userAccount || null)
            });
        }).catch((response: any) => {
            setSessionContext({
                ...sessionContext,
                userAccount: null
            });
        });
    }, []);
    if (sessionContext.userAccount === undefined) {
        return (<FullScreenSpinner />);
    } else if (sessionContext.userAccount === null) {
        window.location.href = '/';
        return (<FullScreenSpinner />);
    }
    return (<ListView />);
};

export default ListApp;