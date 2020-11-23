import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import 'font-awesome/css/font-awesome.css';
import $ from 'jquery';
import Popper from 'popper.js';
import 'mdbreact/dist/css/mdb.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import * as Constants from "./Componenti/GestFid/constants";
import AuthenticationService from "./Componenti/GestFid/Services/authService"


//Questa è la base assoluto della parte frontend del progetto.
//Viene renderizzato nell'elemento root (creato in index.html in public) il contenuto della funziona App.js
ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root'),

  //Controllo se l'utente è loggato e vuole essere ricordato, altrimenti elimino i cookie
  AuthenticationService.firstCheckIsLogged()
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
