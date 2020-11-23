import React, { Component } from "react";
import { withCookies, Cookies } from 'react-cookie';
import "./LogoutComponent.css";
import * as Constants from "../constants";
import { instanceOf } from 'prop-types';

//Classe per la funzionalità di logout
export default class LogoutComponent extends Component {

    render() {

        document.cookie = Constants.TOKEN_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = Constants.REMEMBER_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        this.props.history.push(`/login`); //Reindirizzo nella pagina di login
        return null;
    }
}