import './polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import {
    BrowserRouter,
    Switch,
    Route
} from "react-router-dom";
import * as serviceWorker from './serviceWorker';
import {ApolloProvider} from "@apollo/client/react";
import {ApolloClient, InMemoryCache} from "@apollo/client/core";
import {persistCache, LocalStorageWrapper} from "apollo3-cache-persist";
import {SessionContextProvider} from "./Context/SessionContext";
import LoginApp from "./App/LoginApp";
import ListApp from "./App/ListApp";

const cache = new InMemoryCache();
persistCache({
    cache,
    storage: new LocalStorageWrapper(window.localStorage)
}).then(() => {
    const client = new ApolloClient({
        uri: '/api/data/v1',
        cache: cache,
        credentials: 'include'
    });

    ReactDOM.render(
        <React.StrictMode>
            <SessionContextProvider>
                <ApolloProvider client={client}>
                    <BrowserRouter>
                        <Switch>
                            <Route path={"/app"} exact={true}>
                                <ListApp />
                            </Route>
                            <Route path={"/"} exact={true}>
                                <LoginApp />
                            </Route>
                        </Switch>
                    </BrowserRouter>
                </ApolloProvider>
            </SessionContextProvider>
        </React.StrictMode>,
        document.getElementById('app')
    );
});

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();