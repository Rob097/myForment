import React, { Component, useState } from "react";
import { Alert, Button } from 'react-bootstrap';
import AuthenticationService from "../Services/authService.js";
import { CountdownCircleTimer } from 'react-countdown-circle-timer'
import "./LoginComponent.css";


/*
* File chiamato da AuthRoute, utilizzato per visualizzare una finestra di alert con l'avviso che il token è prossimo alla scdenza con la possibilità
* di rinnovare il token o di ritornare al login. Vengono visualizzati anche i coundown dei minuti e dei secondi del tempo rimanente. Di default 15 minuti.
*/

//Costanti utilizzate nelle diverse funzionalità.
const minuteSeconds = 60;
const hourSeconds = 3600;
const timerProps = {
	isPlaying: true,
	size: 160,
	strokeWidth: 6
};
const secondsDim = "Secondi";
const minutesDim = "Minuti";
const getTimeSeconds = time => (minuteSeconds - time) | 0;
const getTimeMinutes = time => ((time % hourSeconds) / minuteSeconds) | 0;

//Variabili utilizzate per capire se il tempo è finito o no.
let min = true;
let sec = true;



//Funzionalità principale di questo file. Visualizzo la tabella di alert con i countdown di minuti e secondi alla scadenza.
const AlertsExpired = (props) => {

	//Devo inizializzare queste variabili qui altrimenti senza aggiornare la pagina il valore non verrebbe resettato.
	min = true;
	sec = true;
	
	//Variabile show e metodo setShow per capire se visualizzare o meno la finestra di alert
	const [show, setShow] = useState(props.show);
	
	//Variabile diff è il tempo in timestamp unix che manca alla scadenza del token
	let diff = 0;
	if(props.diff !== undefined || props.diff < 0){
		diff = props.diff;
	}

	//Varibile time viene inizializzata come diff per sicurezza ma se la proprieta esiste gli viene assegnata la sua proprietà che indica il tempo iniziale di scandenza del token
	let time = diff;
	if(props.time !== undefined || props.time < 0){
		time = props.time;
	}
    
    if (show) {
        return (
			<>
			<div id="alertExp" style={{position: "fixed",zIndex: "1",width: "100%",top: "4rem",textAlign: "center"}}>

			<Alert variant="danger" onClose={click} dismissible>
				<Alert.Heading>ATTENZIONE!</Alert.Heading>  
					<p id="alertText">Il tuo token sta per scaddere, lo vuoi aggiornare?</p>                  
					<div className="timer-wrapper">						
						<CountdownCircleTimer
							{...timerProps}
							colors={[["#EF798A"]]}
							duration={hourSeconds}
							initialRemainingTime={(diff/1000) % hourSeconds}
							onComplete={totalElapsedTime => [
							(diff/1000) - totalElapsedTime > minuteSeconds
							]}
						>
							{({ elapsedTime }) =>					
								renderTime(minutesDim,	getTimeMinutes(hourSeconds - elapsedTime))
							}
						</CountdownCircleTimer>
						<CountdownCircleTimer
							{...timerProps}
							colors={[["#218380"]]}
							duration={minuteSeconds}
							initialRemainingTime={(diff/1000) % minuteSeconds}
							onComplete={totalElapsedTime => [(diff/1000) - totalElapsedTime > 0]}
						>
							{({ elapsedTime }) =>
								renderTime(secondsDim, getTimeSeconds(elapsedTime))
							}
						</CountdownCircleTimer>
					</div>
				<hr />
				<Button id="RefreshToken" onClick={refresh} className="btn btn-warning rounded">Refresh Token</Button>
				<a href="/login" type="button" className="btn btn-info rounded">LOGIN</a>
			</Alert>
			</div>
		</>
        )
    }else{
		return null;
    }

	//Funzione per chiudere la tabella di alert
    function click(){
        setShow(false);
    }

	//Funzione per aggiornare il token. Unica Copia quindi importante non perderla.
    function refresh() {
        click();
        return(
		AuthenticationService.refreshToken()
            .catch(error => console.log(error))
		);
		        
	}  	
}

//Funzione per eliminare tutti gli elementi del dom con una specifica classe
function removeElementsByClass(className){
	var elements = document.getElementsByClassName(className);
	while(elements.length > 0){
		elements[0].parentNode.removeChild(elements[0]);
	}
}

//Funzionalità per renderizzare il tempo mancante nei due countdown.
const renderTime = ( dimension, remainingTime ) => {

	//Imposto le variabili sec e min a false se il corrispondente tempo si è esaurito
	if(dimension === secondsDim){
		if(remainingTime === 0){
			sec = false;
		}
	}else if(dimension === minutesDim){
		if(remainingTime === 0){
			min = false;
		}
	}

	//Se il tempo è finito aggiorno i valori della finestra di alert.
	if (!min && !sec) {
		if(document.getElementById("RefreshToken") !== null)
			document.getElementById("RefreshToken").remove();
		if(document.getElementById("alertText") !== null)
			document.getElementById("alertText").innerHTML = "Il tuo token è scaduto rifai il login per continuare.";
		removeElementsByClass("timer-wrapper");
	  return <div className="timer">Troppo tardi...</div>;
	}
  
	//Se il tempo non è finito, ritorno il valore aggiornato del tempo rimanente.
	return (
	  <div className="timer">
		<div className="text">Rimangono</div>
		<div className="value">{remainingTime}</div>
		<div className="text">{dimension}</div>
	  </div>
	);
  };

export default AlertsExpired;