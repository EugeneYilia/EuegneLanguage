import React from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import HomeComponent from "./homeComponent/HomeComponent";
import LoginComponent from "./loginComponent/LoginComponent";


function App() {
    return (
        <BrowserRouter>
            <Route path="*" component={LoginComponent}/>
            <Route path='/home' component={HomeComponent}/>
            <Route path='/login' component={LoginComponent}/>
            <Route exact path='/' component={HomeComponent}/>
        </BrowserRouter>
    );
}

export default App;
