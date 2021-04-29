import React from "react";
import {connect} from "react-redux";

class LexerComponent extends React.Component {
    render() {
        return (<>

        </>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {

    }
}

function mapStateToProps(state: any) {
    return {
        tokens: state.tokenReducer.tokens
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(LexerComponent)
