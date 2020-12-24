import React, { Component } from "react";
import "./LoginComponent.css";
import { Redirect, Link } from "react-router-dom"
import AuthenticationService from '../Services/authService.js';
import HandleError from "../Errors/HandleError.js"

//Classe per la pagina di login
export default class LoginComponent extends Component {

    state = {
        userId: 'Roberto',
        password: '123Stella',
        rememberMe: false,
        isLogged: false,
        noLogged: false,
        username: null,
        token: null,
        ErrMsg: null
    }

    componentDidMount = () => {
        try {
            this.setState({
                token: AuthenticationService.getTokenFromCookie(),
			    username: AuthenticationService.getUserInfo()
            });			
		} catch (e) {
			console.log(e);
		}
    }

    //Form di login
    render() {

        if (this.state.token === null || this.state.username === null || this.state.token === '' || this.state.username === '') {
            
            return (

                <div>
                    <p>Accedi all'App GestFid</p>
                    <section id="logInSection">
                        <form className="form-signin" onSubmit={this.Login}>
                            <img className="mb-4" src="https://getbootstrap.com/docs/4.0/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72" />
                            <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>

                            {this.state.ErrMsg && <div className="alert alert-danger"><h5>{this.state.ErrMsg}</h5></div>}

                            <label htmlFor="userId" className="sr-only">Email address</label>
                            <input type="text" id="userId" name="userId" className="form-control" defaultValue={this.state.userId} onChange={this.GestMod} required autoFocus />

                            <label htmlFor="password" className="sr-only">Password</label>
                            <input type="password" id="password" name="password" className="form-control" defaultValue={this.state.password} onChange={this.GestMod} required />

                            <div className="checkbox mb-3">
                                <label>
                                    <input id="rememberMe" name="rememberMe" type="checkbox" onChange={this.GestMod} /> Remember me
                            </label>
                            </div>
                            <button className="btn btn-lg btn-primary btn-block" type="submit">Accedi</button>
                            <hr/>
                            <Link className="btn btn-md btn-warning btn-block" to="/signup" type="button">Signup</Link>
                            <p className="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
                        </form>
                    </section>

                </div>

            );
        } else {
            return <Redirect to={"/welcome/" + this.state.username} />
        }

        
    }

    //Metodo per il login.
    Login = (event) => {
        event.preventDefault(); //Importante percè essendo il login una form, evita che premendo invio la pagina si refreshi.

        //Chiamo la funzione che contatta l'api backend per il login e la generazione del token JWT
        AuthenticationService.JWTAuthServer(this.state.userId, this.state.password, this.state.rememberMe)
            .then((response) => {
                //Non serve fare altro perchè l'API crea un cookie
                this.props.history.push(`/welcome/${this.state.userId}`); //Reindirizzo nella pagina di welcome
            })
            .catch((error) => {
                HandleError.handleError(this, error)
                //Resetto le variabili dello state se trovo un problema
                this.setState({
                    isLogged: false,
                    noLogged: true
                })
            })
    }

    //Metodo che aggiorna le variabili dello state quando vengono modificati i dati all'interno della form
    GestMod = (event) => {

        if (event.target.type === "checkbox") {
            this.setState({
                [event.target.name]: event.target.checked
            })
        } else if (event.target.type === "text" || event.target.type === "password") {
            this.setState({
                [event.target.name]: event.target.value
            })
        } else {
            console.log(event.target.type + " -> " + event.target.name + ": " + event.target.value);
        }
    }

}