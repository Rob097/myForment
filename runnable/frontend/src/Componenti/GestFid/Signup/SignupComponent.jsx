import React, { Component } from "react";
import { Formik, Form, Field, ErrorMessage } from 'formik';
import HandleError from "../Errors/HandleError.js"
import AuthenticationService from '../Services/authService.js';

export default class SignupComponent extends Component {

    state = {
        username: "",
        email: "",
        password: "",
        okMsg: null,
        ErrMsg: null
    }

    //Gestisco le risposte positive.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse(response) {

        this.setState({
            username: response.data.username,
            email: response.data.email,
            password: response.data.password,
            okMsg: null
        })

    }

    //Metodo per salvare il nuovo o aggiornare uno esistente
    Salva = (values) => {

        var role = ["user"];
        /*console.log("username: "+ values.username);
        console.log("email: "+ values.email);
        console.log("password: "+ values.password);
        console.log("roles: %O", mySet);*/

        AuthenticationService.signUp({
            username: values.username,
            email: values.email,
            password: values.password,
            roles: role
        })
            .then(
                () => {
                    this.setState({ okMsg: 'Inserimento dati eseguito con successo' });
                    this.props.history.push(`/login`);
                }
            )
            .catch(error => HandleError.handleError(this, error));
    }

    //Metodo per annullare le modifiche e ritornare al login
    Login = () => {
        if (window.confirm("Vuoi tornare al login?")) {
            this.props.history.push(`/login`);
        }
    }

    //Metodo per validare i dati inseriti nella FORM
    Valida = (values) => {
        let errors = {}

        if (!values.username) {
            errors.username = "Inserisci un username"
        }
        else if (values.username.length < 6) {
            errors.username = "L'username deve avere almeno 6 caratteri"
        }

        if (!values.email) {
            errors.email = "Inserisci un'email valida"
        }

        if (!values.password) {
            errors.password = "Inserisci una password"
        }
        else if (values.password.length < 6) {
            errors.password = "La password deve avere almeno 6 caratteri"
        }

        return errors;
    }

    render() {

        //Imposto il valore dei campi della form con quelli salvati nello state. ATTENZIONE, questi nomi devo essere uguali a quelli dello state.
        let { username, email, password } = this.state;

        return (
            <section className="container">
                <div className="card">
                    <div className="card-body">
                        <h3 className="card-title mb-4">Signup</h3>

                        <Formik
                            //Inizio della FORM. Per crearla utilizzo FORMIK
                            //Imposto i valori iniziali. ATTENZIONE, questi nomi devono essere uguali a quelli dell'id dei campi della form
                            initialValues={{ username, email, password }}

                            //Nel submit della FORM chiamo il metodo salva
                            onSubmit={this.Salva}

                            //Con questa proprietà i parametri vengono inizializzati all'avvio
                            enableReinitialize={true}

                            //Valido i valori
                            validate={this.Valida}

                            //Con queste due proprietà, la form verrà validata solamente quando si preme il tasto salva, altrimenti verrebbe validata fin da subito
                            validateOnChange={false}
                            validateOnBlur={false}
                        >

                            {

                                (props) => (

                                    <Form>

                                        <div className="form-group">
                                            <img style={{ width: "100px", "marginBottom": "2rem" }} src={`../logo.png`} className="img-sm rounded-circle border" alt="imgcli" />
                                        </div>

                                        {this.state.okMsg && <div className="alert alert-success"><h5>{this.state.okMsg}</h5></div>}
                                        {this.state.ErrMsg && <div className="alert alert-danger"><h5>{this.state.ErrMsg}</h5></div>}

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Username</label>
                                                <Field type="text" name="username" className="form-control" />
                                                <ErrorMessage name="username" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Email</label>
                                                <Field type="email" name="email" className="form-control" />
                                                <ErrorMessage name="email" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Password</label>
                                                <Field type="password" name="password" className="form-control" />
                                                <ErrorMessage name="password" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div>
                                            <button type="submit" className="btn btn-primary inscli">Salva</button>
                                            <button type="button" onClick={this.Login} className="btn btn-warning inscli">Login</button>
                                        </div>

                                    </Form>

                                )
                            }

                        </Formik>

                    </div>
                </div>
            </section>
        )

    }

}