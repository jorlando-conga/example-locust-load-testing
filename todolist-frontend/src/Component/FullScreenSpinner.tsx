import React from "react";
import {CircularProgress, Container} from "@material-ui/core";
import './FullScreenSpinner.css';

const FullScreenSpinner = () => {
    return (
        <Container maxWidth="md" style={{height: '100%'}}>
            <div className={"center-screen"}>
                <CircularProgress disableShrink />
            </div>
        </Container>
    );
};
export default FullScreenSpinner;