import React, { Component } from "react";
import { Formik, FormikProps, Form, Field, ErrorMessage } from 'formik';
import CompaniesService from '../../Services/Api/Companies/CompaniesAPI.js';
import HandleError from "../../Errors/HandleError.js"

export default class CompanyDataComponent extends Component {
    state = {
        id: '',
        name: '',
        logo: '',
        addressLineOne: '',
        addressLineTwo: '',
        cap: '',
        city: '',
        province: '',
        country: '',
        legalName: '',
        email: '',
        sector: '',
        okMsg: null,
        ErrMsg: null
    }

    componentDidMount() {

        //Controllo se la props codfid indica un particolare cliente o l'inserimento di uno nuovo (-1)
        let id = this.props.match.params.id;

        if (id != "-1") {
            /*ClientiService.getClientiByCode(CodFid)
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))*/
        }
    }

    //Gestisco le risposte positive.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse(response) {

        this.setState({
            id: response.data.id,
            name: response.data.name,
            logo: response.data.logo,
            addressLineOne: response.data.addressLineOne,
            addressLineTwo: response.data.addressLineTwo,
            cap: response.data.cap,
            city: response.data.city,
            province: response.data.province,
            country: response.data.country,
            legalName: response.data.legalName,
            email: response.data.email,
            sector: response.data.sector,
            okMsg: null,
        })

    }

    //Metodo per salvare il nuovo o aggiornare uno esistente
    Salva = (values) => {

        CompaniesService.insCompany({
            id: values.id,
            name: values.name,
            logo: values.logo,
            addressLineOne: values.addressLineOne,
            addressLineTwo: values.addressLineTwo,
            cap: values.cap,
            city: values.city,
            province: values.province,
            country: values.country,
            legalName: values.legalName,
            email: values.email,
            sector: values.sector
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

        if (!values.name) {
            errors.name = "Inserisci il nome dell'azienda"
        }
        else if (values.name.length < 6) {
            errors.name = "Il nome deve avere almeno 6 caratteri"
        }

        if (!values.addressLineOne) {
            errors.addressLineOne = "Inserisci l' indirizzo dell'azienda"
        }

        if (!values.cap) {
            errors.cap = "Inserisci il cap della città dell'azienda"
        }
        
        if (!values.city) {
            errors.city = "Inserisci la città dell'azienda"
        }

        if (!values.province) {
            errors.province = "Inserisci la privincia della città dell'azienda"
        }

        if (!values.sector) {
            errors.sector = "Inserisci il settore dell'azienda"
        }

        return errors;
    }

    render() {

        //Imposto il valore dei campi della form con quelli salvati nello state. ATTENZIONE, questi nomi devo essere uguali a quelli dello state.
        let { id, name, logo, addressLineOne, addressLineTwo, cap, city, province, country, legalName, email, sector} = this.state;

        return (
            <section className="container">
                <div className="card">
                    <div className="card-body">
                        <h3 className="card-title mb-4">Dati Azienda</h3>

                        <Formik
                            //Inizio della FORM. Per crearla utilizzo FORMIK
                            //Imposto i valori iniziali. ATTENZIONE, questi nomi devono essere uguali a quelli dell'id dei campi della form
                            initialValues={{ id, name, logo, addressLineOne, addressLineTwo, cap, city, province, country, legalName, email, sector }}

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
                                            <img name="logo" style={{ width: "100px", "marginBottom": "2rem" }} src={`../logo.png`} className="img-sm rounded-circle border" alt="imgcli" />
                                        </div>

                                        {this.state.okMsg && <div className="alert alert-success"><h5>{this.state.okMsg}</h5></div>}
                                        {this.state.ErrMsg && <div className="alert alert-danger"><h5>{this.state.ErrMsg}</h5></div>}

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Nome *</label>
                                                <Field type="text" name="name" className="form-control" />
                                                <ErrorMessage name="name" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Indirizzo #1</label>
                                                <Field type="text" name="addressLineOne" className="form-control" />
                                                <ErrorMessage name="addressLineOne" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                            <div className="col form-group">
                                                <label>Cap</label>
                                                <Field type="text" name="cap" className="form-control" />
                                                <ErrorMessage name="cap" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Città</label>
                                                <Field type="text" name="city" className="form-control" />
                                                <ErrorMessage name="city" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Provincia</label>
                                                <Field as="select" name="province" className="form-control" >
                                                    <option value="">Seleziona...</option>
                                                    <option value="SS">Sassari...</option>
                                                    <option value="CA">Cagliari...</option>
                                                    <option value="NU">Nuoro...</option>
                                                    <option value="BZ">Bolzano...</option>
                                                    <option value="TN">Trento...</option>
                                                </Field>                                                
                                                <ErrorMessage name="province" component="span" className="errmsg" />
                                            </div>
                                        </div>

                                        <div className="form-row">
                                        <div className="col form-group">
                                                <label>Settore</label>
                                                <Field as="select" name="sector" className="form-control" >
                                                    <option value="">Seleziona...</option>
                                                    <option value="Macelleria">Macelleria</option>
                                                    <option value="Frutta e Verdura">Frutta e Verdura</option>
                                                    <option value="Informatica">Informatica</option>
                                                    <option value="Idraulica">Idraulica</option>
                                                    <option value="Falegnameria">Falegnameria</option>
                                                </Field>                                                
                                                <ErrorMessage name="sector" component="span" className="errmsg" />
                                            </div>
                                            <div className="col form-group">
                                                <label>Mail</label>
                                                <Field type="text" name="email" className="form-control" />
                                            </div>
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