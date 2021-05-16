import React from "react";
import {connect} from "react-redux";
import {Input, Tag, Tooltip} from "antd";
import axios from "axios";
import {SERVER_URL} from "../config";

class LexerComponent extends React.Component {

    handleTokenClose = (removeTag: any, index: any) => {
        axios({
            method: 'put',
            url: SERVER_URL + "/api/token",
            withCredentials: true,
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({
                symbol: removeTag.symbol,
                value: removeTag.value,
                index: index
            })
        }).then((response: any) => {
            let responseData = response.data
            console.log(responseData)

        })
    }

    render() {

        return (<>
            {
                // @ts-ignore
                this.props.tokens.map((token, index) => {
                    return (
                        <Tooltip placement={"top"} title={token.symbol + "  " + token.value}>
                            <Tag
                                closable
                                onClose={e => {
                                    e.preventDefault()
                                    this.handleTokenClose(token, index)
                                }}
                            >
                                {token.symbol}
                            </Tag>
                        </Tooltip>
                    )
                })
            }
        </>);
    }
}

function mapDispatchToProps(dispatch: any) {
    return {}
}

function mapStateToProps(state: any) {
    return {
        tokens: state.tokenReducer.tokens
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(LexerComponent)
