import React, { Component } from "react";
import { Link } from 'react-router-dom'
import './ForbComponent.css'

//Questa è la classe per la pagina forbidden che viene chiamata quando si cerca di accedere ad una pagina che richiede un ruolo che l'utente non possiede
export default class ForbComponent extends Component {

    render() {
        return (
            <div>
                <div className="gandalf">
                    <div className="fireball"></div>
                    <div className="skirt"></div>
                    <div className="sleeves"></div>
                    <div className="shoulders">
                        <div className="hand left"></div>
                        <div className="hand right"></div>
                    </div>
                    <div className="head">
                        <div className="hair"></div>
                        <div className="beard"></div>
                    </div>
                </div>
                <div className="message">
                    <h1>403 - You Shall Not Pass</h1>
                    <p>Uh oh, Gandalf is blocking the way!<br />Maybe you have a typo in the url? Or you meant to go to a different location? Like...Hobbiton?</p>
                </div>
                <Link className="btn btn-danger" to="/login">Login</Link>
                <Link className="btn btn-info" to="/clienti">Clienti</Link>
            </div>
        )
    }

}