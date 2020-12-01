import React, { Component } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom"
import LoginComponent from './Login/LoginComponent'
import LogoutComponent from './Logout/LogoutComponent'
import SignupComponent from './Signup/SignupComponent'
import WelcomeComponent from "./Welcome/Welcome"
import Clienti from "./Clienti/Clienti"
import Companies from "./Companies/Companies"
import DatiClienteComponent from "./Clienti/InsClienti/DatiCliente"
import CompanyDataComponent from "./Companies/InsCompany/CompanyData"
import HeaderComponent from "./Header/HeaderComponent"
import FooterComponent from "./Footer/FooterComponent"
import AuthRoute from "./AuthRoute";
import ForbComponent from "./Forbidden/ForbComponent"

//Componente principale del progetto.
export default class GestFidApp extends Component {

    //State pe salvare i diversi tipi di ruoli
    state = {
        Basic: "ROLE_BASIC",
        Admin: 'ROLE_ADMIN',
        Editor: 'ROLE_EDITOR'
    }

    render() {
        return (

            //Il router permette di attribuire un path ad una pagina e di limitarne l'accesso in base al ruolo
            //In base alla posizione che voglio che abbiano, chiamo anche i componenti di header e di footer
            <div className="GestFidApp">
                <Router>
                    <HeaderComponent />
                    <Switch>
                        <Route path="/" exact component={LoginComponent} />
                        <Route path="/login" component={LoginComponent} />
                        <Route path="/logout" component={LogoutComponent} />
                        <Route path="/signup" component={SignupComponent} />
                        <AuthRoute path="/welcome/:userId" component={WelcomeComponent} role={this.state.Basic} />
                        <AuthRoute path="/inscliente/:codfid" component={DatiClienteComponent} role={this.state.Admin} />
                        <AuthRoute path="/clienti" component={Clienti} role={this.state.Basic} />

                        {/* COMPANIES ROUTES */}
                        <AuthRoute path="/companies" component={Companies} role={this.state.Basic} />
                        <AuthRoute path="/inscompany/:id" component={CompanyDataComponent} role={this.state.Admin} />

                        <Route path="/forbidden" component={ForbComponent} />

                        <Route exact path="/404" component={NotFound} />
                        <Route component={NotFound} />
                    </Switch>
                    <FooterComponent />
                </Router>
            </div>

        );
    }

}

const NotFound = () => {
    return <h1>I am Lost</h1>;
};
const Unauthorized = () => {
    return <h1>I can't do that</h1>;
};

function ErrorComponent() {
    return <div>Errore. Pagina non trovata!</div>
}

function NotFoundPage() {
    return <div>Errore. Pagina non trovata!</div>
}