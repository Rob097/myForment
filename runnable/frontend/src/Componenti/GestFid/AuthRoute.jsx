import React, { Component, useState } from "react";
import ReactDOM from 'react-dom';
import createReactClass from "create-react-class";
import AuthenticationService from "./Services/authService.js";
import { Redirect, Route } from "react-router-dom"
import jwt from "jsonwebtoken"
import axios from 'axios'
import AlertsExpired from "./Login/AlertsExpired"
import * as constants from "./constants";

//Inizializzo variabili globali di questo file
let token = null;
let user = null;
let exp = null;
let now = null;

//Questa classe viene utilizzata nel ROUTING alle diverse pagine dell'app. Viene quindi chiamata quando si vuole aprire tutte le pagine che sono indirizzate tramite questa classe.
//Visualizzi il suo utilizzo in GestFid.jsx
export default class AuthRoute extends Component {

	//Il costruttore viene eseguito al primo utilizzo della classe e avvia l'intercettore.
	constructor() {
		super();
		this.setupAxiosInterceptors();
	}

	//Metodo eseguito ogni volta alla fine del caricamento della classe.
	//Imposto correttamente le variabili globali e poi avvio il metodo loopIfExpired.
	componentDidMount = () => {
		try {
			this.setVariablesByCookie();
			this.loopIfExpired();
		} catch (e) {
			console.log(e);
		}
	}

	setVariablesByCookie = () => {
		try {
			token = AuthenticationService.getTokenFromCookie();
			if (token !== null) {
				user = AuthenticationService.getUserInfo();
				exp = new Date(jwt.decode(token).exp * 1000).getTime(); //.getTime mi fornisce il valore timestamp unix di una data.
				now = new Date().getTime();
			}
		} catch (e) {
			console.log(e);
		}
	}

	//Questo metodo serve per controllare automaticamente ogni periodo di tempo uguale alla costante globale time se il token è prossimo alla scadenza.
	//Se lo è, visualizzo una finestra di alert (AlertsExpired.jsx) con la possibilità di aggiornare il token o di tornare al login con anche il countdown.
	loopIfExpired = () => {

		var timer = setInterval(() => {
			console.log("loopIfExpired: " + constants.TIME/1000 + " secondi");
			//Aggiorno le varibili globali essendo dentro ad una funzione ricorsiva.
			this.setVariablesByCookie();

			//Controllo se il token è prossimo alla scadenza
			if(this.checkIfExpired() !== null){
				if (this.checkIfExpired()) {

					//Aggiungo al body la finestra di alert
					var Hello = createReactClass({
						render: function () {
							return <AlertsExpired obj={this} show={true} diff={(exp - now)} time={constants.TIME} />;
						}
					});
					var temp = document.createElement("div");
					// render
					ReactDOM.render(<Hello />, temp);
					var container = document.querySelector("body");
					if (container.querySelector("#alertExp") === null || container.querySelector("#alertExp") === undefined) {
						container.appendChild(temp, document.querySelector("body"));
					}
					//Fine di aggiunta al body della finestra di alert

				}
				//In ogni caso richiamo la funzione che aspetterà time tempo prima di eseguirsi nuovamente.
				//this.loopIfExpired();
			}else{
				clearInterval(timer);
			}
		}, constants.TIME);
	}

	//Metodo per controlalre se il token è prossimo alla scadenza secondo la costante globale time
	checkIfExpired = () => {
		if (token !== null) {

			//Se la data exp è nel futuro rispetto ad adesso e se exp meno now è minore o uguale al periodo di allerta (time = 15 minuti) ritorno true
			if (now < exp && (exp - now) <= constants.TIME + 10000) {
				return true;
			} else if (new Date() > exp) {//Se il token è già scaduto ritorno null
				return null;
			} else { //Se il token non è ne già scaduto ne è prossimo alla scadenza ritorno false
				return false;
			}
		}
		return null; //Se il token non viene trovato ritorno null
	}

	//Questo è un Intercettore (Interceptors).
	//Servono per svolgere un operazione prima che si invia una richiesta alle webapi
	setupAxiosInterceptors() {
		let tokenB = "";
		//Questo è il metodo per usare l'intercettore.
		axios.interceptors.request.use(
			//Creo una nuova configurazione
			(config) => {

				//Aggiorno le variabili globali
				this.setVariablesByCookie();

				//Devo aggiornare la variabile token anche qui altrimenti ritorna valori non aggiornati se non si aggiorna la pagina.
				tokenB = 'Bearer ' + token;
				if (AuthenticationService.isLogged()) {
					config.headers.authorization = tokenB
				}
				return config;
			},
			(err) => {
				return Promise.reject(err);
			}
		)

	}

	render() {

		this.setVariablesByCookie();

		//Mi ricavo i ruoli dell'utente loggato
		let ruoli = "";
		if (token !== null && token !== undefined) {
			ruoli = jwt.decode(token).roles;
		}
		//Controllo che l'utente sia loggato e che iltoken non sia già scaduto. Altrimenti reindirizzo alla pagina di login
		if (AuthenticationService.isLogged() && this.checkIfExpired() !== null) {

			//myRole è in realtà il ruolo che è necessario avere per poter accedere ad una determinata pagina e viene passato nelle props in GestFid.jsx
			let myRole = this.props.role;

			//Variabile di controllo per verificare che l'utnete abbia il ruolo necessario
			let check = false;

			//Verifico che l'utente abbia il ruolo necessario
			ruoli.forEach(e => {
				if (e.authority !== null && e.authority === myRole) {
					check = true;
				}
			})


			//Se l'utente non ha il ruolo necessario ad accedere alla pagina lo rimando alla pagina forbidden
			if (check) {

				//Se il token sta per scadere visualizzo la finestra di alert e renderizzo la pagina richiesta.
				if (this.checkIfExpired()) {
					return (
						<>
							<AlertsExpired obj={this} show={true} diff={(exp - now)} time={constants.TIME} />
							<Route {...this.props}></Route>
						</>
					)
				} else { //Altrimenti renderizzo solamente la pagina richiesta					
					return (
						<Route {...this.props}></Route>
					)
				}
			} else {
				return <Redirect to="/forbidden" />
			}

		} else {
			return <Redirect to="/login" />
		}

	}

}