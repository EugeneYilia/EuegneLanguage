import React from 'react';
import {BrowserRouter, Route} from "react-router-dom";
import HomeComponent from "./homeComponent/HomeComponent";


function App() {
    return (
        <BrowserRouter>
            <Route path='/home' component={HomeComponent}/>
            <Route exact path='/' component={HomeComponent}/>
        </BrowserRouter>
    );
}

export default App;
