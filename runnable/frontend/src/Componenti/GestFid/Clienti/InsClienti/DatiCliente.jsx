import React, { Component } from "react";
import './DatiCliente.css';
import { Formik, FormikProps, Form, Field, ErrorMessage } from 'formik';
import ClientiService from '../../Services/Api/Clienti/ClientiAPI.js';
import HandleError from "../../Errors/HandleError.js"
import AuthRoute from "../../AuthRoute"


//Classe per la tabella di modifica di un cliente e di inserimento di nuovi clienti. 
export default class DatiClienteComponent extends Component {

    state = {
        id: '',
        codfid: '',
        nominativo: '',
        indirizzo: '',
        comune: '',
        cap: '',
        prov: '',
        telefono: '',
        mail: '',
        attivo: true,
        cards: {
            "bollini": 0,
            "ultimaspesa": "2020-01-01"
        },
        okMsg: null,
        ErrMsg: null
    }

    componentDidMount() {

        //Controllo se la props codfid indica un particolare cliente o l'inserimento di uno nuovo (-1)
        let CodFid = this.props.match.params.codfid;

        if (CodFid != "-1") {
            ClientiService.getClientiByCode(CodFid)
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))
        }
    }

    //Gestisco le risposte positive.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse(response) {

        this.setState({
            id: response.data.id,
            codfid: response.data.codfid,
            nominativo: response.data.nominativo,
            indirizzo: response.data.indirizzo,
            comune: response.data.comune,
            cap: response.data.cap,
            prov: response.data.prov,
            telefono: response.data.telefono,
            mail: response.data.mail,
            attivo: response.data.attivo,
            cards: response.data.cards,
            okMsg: null
        })

    }

    //Metodo per salvare il nuovo o aggiornare uno esistente
    Salva = (values) => {

        ClientiService.insCliente({
            id: values.id,
            codfid: values.codfid,
            nominativo: values.nominativo,
            indirizzo: values.indirizzo,
            comune: values.comune,
            cap: values.cap,
            prov: values.prov,
            telefono: values.telefono,
            mail: values.mail,
            attivo: values.attivo,
            cards: values.cards,
        })
            .then(
                () => {
                    this.setState({ okMsg: 'Inserimento dati eseguito con successo' });
                }
            )
            .catch(error => HandleError.handleError(this, error));
    }

    //Metodo per annullare le modifiche e ritornare a tutti i clienti
    Annulla = () => {
        if (window.confirm("Abbandoni le modifiche?")) {
            this.props.history.push(`/clienti`);
        }
    }

    //Metodo per validare i dati inseriti nella FORM
    Valida = (values) => {
        let errors = {}

        if (!values.codfid) {
            errors.codfid = "Inserisci il codice fidelity del cliente"
        }
        else if (values.codfid.length !== 8) {
            errors.codfid = "Il codice fidelity deve avere 8 caratteri"
        }

        if (!values.nominativo) {
            errors.nominativo = "Inserisci il nome del cliente"
        }
        else if (values.nominativo.length < 6) {
            errors.nominativo = "Il nome deve avere almeno 6 caratteri"
        }

        if (!values.indirizzo) {
            errors.indirizzo = "Inserisci il indirizzo del cliente"
        }

        if (!values.comune) {
            errors.comune = "Inserisci il comune del cliente"
        }

        if (!values.telefono) {
            errors.telefono = "Inserisci il telefono del cliente"
        }

        return errors;
    }

    render() {

        //Imposto il valore dei campi della form con quelli salvati nello state. ATTENZIONE, questi nomi devo essere uguali a quelli dello state.
        let { id, codfid, nominativo, indirizzo, comune, cap, prov, telefono, mail, attivo, cards } = this.state;

        return (
            <section className="container">
                <div className="card">
                    <div className="card-body">
                        <h3 className="card-title mb-4">Dati Cliente Fidelity</h3>

                        <Formik
                            //Inizio della FORM. Per crearla utilizzo FORMIK
                            //Imposto i valori iniziali. ATTENZIONE, questi nomi devono essere uguali a quelli dell'id dei campi della form
                            initialValues={{ id, codfid, nominativo, indirizzo, comune, cap, prov, telefono, mail, attivo, cards }}

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
                                                <label>Codice Fidelity *</label>
                                                <Field type="text" name="codfid" className="form-control" />
                                                <ErrorMessage name="codfid" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Nominativo *</label>
                                                <Field type="text" name="nominativo" className="form-control" />
                                                <ErrorMessage name="nominativo" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Indirizzo</label>
                                                <Field type="text" name="indirizzo" className="form-control" />
                                                <ErrorMessage name="indirizzo" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Comune</label>
                                                <Field type="text" name="comune" className="form-control" />
                                                <ErrorMessage name="comune" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Cap</label>
                                                <Field type="text" name="cap" className="form-control" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Provincia</label>
                                                <Field as="select" name="prov" className="form-control" >
                                                    <option value="">Seleziona...</option>
                                                    <option value="SS">Sassari...</option>
                                                    <option value="CA">Cagliari...</option>
                                                    <option value="NU">Nuoro...</option>
                                                    <option value="BZ">Bolzano...</option>
                                                    <option value="TN">Trento...</option>
                                                </Field>
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Telefono</label>
                                                <Field type="text" name="telefono" className="form-control" />
                                                <ErrorMessage name="telefono" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Mail</label>
                                                <Field type="text" name="mail" className="form-control" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <label className="custom-control custom-checkbox">
                                                <Field type="checkbox" name="attivo" className="custom-control-input" />
                                                <div className="custom-control-label">Attivo</div>
                                            </label>
                                        </div>

                                        <div>
                                            <button type="submit" className="btn btn-primary inscli">Salva</button>
                                            <button type="button" onClick={this.Annulla} className="btn btn-warning inscli">Annulla</button>
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