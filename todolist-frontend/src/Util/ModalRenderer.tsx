import React from 'react';
import ReactDOM from "react-dom";

interface ModalWrapperProps {
    children: any[];
}

class ModalWrapper extends React.Component<ModalWrapperProps, {}> {
    render() {
        return this.props.children;
    }
}

const modalRenderer = function(modalElement: any) {
    const modalContainer: Element | null = document.querySelector("#modal");
    if (modalContainer) {
        return ReactDOM.createPortal(<ModalWrapper children={[modalElement]}/>, modalContainer);
    }
    throw new Error("Modal container cannot be found");
};

export default modalRenderer;