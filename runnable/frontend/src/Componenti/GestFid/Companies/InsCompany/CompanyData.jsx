import React, { Component } from "react";
import { Formik, FormikProps, Form, Field, ErrorMessage } from 'formik';
import CompaniesService from '../../Services/Api/Companies/CompaniesAPI.js';
import HandleError from "../../Errors/HandleError.js"
import Select from 'react-select'
import { SelectField } from "./SelectField";
import { MDBDataTable, MDBCard, MDBCardHeader, MDBCardBody, MDBBtn } from 'mdbreact';

export default class CompanyDataComponent extends Component {
    state = {
        id: '',
        name: '',
        logo: 'logo',
        ownerId: '',
        addressLineOne: '',
        addressLineTwo: '',
        city: '',
        cap: '',
        province: '',
        country: '',
        legalName: '',
        email: '',
        sector: '',
        Msg: null,
        okMsg: null,
        ErrMsg: null,
        employees: [],
        users: [],
        utenti: []
    }

    componentDidMount() {

        //Controllo se la props codfid indica un particolare cliente o l'inserimento di uno nuovo (-1)
        let id = this.props.match.params.id;

        if (id != "-1") {
            CompaniesService.getCompanyById(id)
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))

            CompaniesService.searchUsersToInvite(id)
                .then(response => {
                    response.data.forEach(user => {
                        this.setState({
                            users: this.state.users.concat(
                                {
                                    label: user.username,
                                    value: user.id
                                }
                            )
                        })
                    });
                    console.log("USERS: %O", this.state.users);
                })
                .catch(error => HandleError.handleError(this, error))
            
            this.searchEmployees(id);

        }
    }


    searchEmployees(id){
        this.setState({
            employees: []
        });
        CompaniesService.searchCompanysEmployees(id)
            .then(response => {
                console.log("employees: %O", response)
                this.setState({
                    employees: this.state.employees.concat(response.data)
                })
            })
            .catch(error => HandleError.handleError(this, error))
    }

    //Gestisco le risposte positive.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse(response) {

        this.setState({
            id: response.data.id,
            name: response.data.name,
            ownerId: response.data.ownerId,
            addressLineOne: response.data.address.addressLineOne,
            addressLineTwo: response.data.address.addressLineTwo,
            city: response.data.address.city,
            cap: response.data.address.cap,
            province: response.data.address.province,
            country: response.data.address.country,
            legalName: response.data.legalName,
            email: response.data.email,
            sector: response.data.sector,
            okMsg: null,
        })
    }

    handleSubmit = (values, { resetForm, setSubmitting }) => {
        console.log("handleSubmit values", values);

        CompaniesService.inviteUsers({
            companyId: this.state.id,
            utenti: values.utenti
            //utenti: ["prova", "prova 2"]
        })
            .then(
                () => {
                    this.setState({
                        okMsg: 'Invito eseguito correttamente',
                        Msg: null
                    });
                }
            )
            .catch(error => {
                this.setState({
                    Msg: null
                });
                HandleError.handleError(this, error)
            });

        //resetForm();
        //setSubmitting(false);
    };

    //Metodo per salvare il nuovo o aggiornare uno esistente
    Salva = (values) => {

        this.setState({
            Msg: "Creazione azienda in corso..."
        })

        CompaniesService.insCompany({
            id: values.id,
            name: values.name,
            logo: "logo",
            ownerId: values.ownerId,
            address: {
                addressLineOne: values.addressLineOne,
                addressLineTwo: values.addressLineTwo,
                city: values.city,
                cap: values.cap,
                province: values.province,
                country: values.country
            },
            legalName: values.legalName,
            email: values.email,
            sector: values.sector
        })
            .then(
                () => {
                    this.setState({
                        okMsg: 'Inserimento dati eseguito con successo',
                        Msg: null
                    });
                }
            )
            .catch(error => {
                this.setState({
                    Msg: null
                });
                HandleError.handleError(this, error)
            });
    }

    //Metodo per annullare le modifiche e ritornare a tutti i clienti
    Annulla = () => {
        if (window.confirm("Abbandoni le modifiche?")) {
            this.props.history.push(`/companies`);
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

    //MEtodo per eliminare un cliente in base al suo ID
    Remove = (id) => {

        CompaniesService.removeUser({
            userId: id,
            companyId: this.state.id
        })
            .then(() => {
                this.setState({ OkMsg: `Rimozione utente ${id} eseguita con successo!` })
                //this.ResetValue();
                //this.CercaTutte();
            })
            .catch(error => HandleError.handleError(this, error))


        this.searchEmployees(this.state.id);
    }



    render() {

        //Imposto il valore dei campi della form con quelli salvati nello state. ATTENZIONE, questi nomi devo essere uguali a quelli dello state.
        let { id, name, logo, addressLineOne, addressLineTwo, city, province, country, cap, legalName, email, sector, utenti } = this.state;

        return (
            <section className="container">
                <div className="card">
                    <div className="card-body">
                        <h3 className="card-title mb-4">Dati Azienda</h3>


                        <Formik
                            //Inizio della FORM. Per crearla utilizzo FORMIK
                            //Imposto i valori iniziali. ATTENZIONE, questi nomi devono essere uguali a quelli dell'id dei campi della form
                            initialValues={{ id, name, logo, addressLineOne, addressLineTwo, city, province, country, cap, legalName, email, sector }}

                            //Nel submit della FORM chiamo il metodo salva
                            onSubmit={(values) => this.Salva(values)}

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

                                        {this.state.Msg && <div className="alert alert-info"><h5>{this.state.Msg}</h5></div>}
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
                                            <button type="submit" className="btn btn-primary insCom">Salva</button>
                                            <button type="button" onClick={this.Annulla} className="btn btn-warning insCom">Annulla</button>
                                        </div>

                                    </Form>

                                )

                            }

                        </Formik>
                    </div>
                </div>

                <div className="card">
                    <div className="card-body">
                        <h3 className="card-title mb-4">Impiegati assunti</h3>
                        <div>
                            {this.DatatablePageUsers()}
                        </div>
                        <h3 className="card-title mb-4">Utenti da invitare</h3>

                        <Formik
                            initialValues={this.state.utenti}
                            onSubmit={(values, props) => this.handleSubmit(values, props)}
                            render={({
                                utenti,
                                //touched,
                                setFieldValue,
                                //setFieldTouched,
                                //isSubmitting
                            }) => (
                                    <Form>
                                        <SelectField
                                            id="utenti"
                                            name="utenti"
                                            label="Invita utenti"
                                            placeholder="Seleziona utenti"
                                            options={this.state.users}
                                            value={utenti}
                                            isMulti={true}
                                            onChange={setFieldValue}
                                            //onBlur={setFieldTouched}
                                            //touched={touched.fieldOfResearch}
                                            isClearable={true}
                                            backspaceRemovesValue={true}
                                        />

                                        <button type="submit" /*disabled={isSubmitting}*/>
                                            <h3>Submit</h3>
                                        </button>
                                    </Form>
                                )}
                        />



















                    </div>
                </div>
            </section>
        )
    }

    //Metodo per la generazione dinamica della DataTable. Uso mdbreact.
    DatatablePageUsers = () => {

        //Array di lavoro
        var c = [];

        //Bottoni di modifica e eliminazione di un cliente. Index è il id.
        const buttonRemove = (id) => { return <button id={"remove-" + id} onClick={e => window.confirm(`Confermi l'eliminazione dell'utente ${id} dall'azienda?`) && this.Remove(id)} type="button" className="btn btn-danger rounded" size="sm">Rimuovi</button> }

        //Aggiungo a c tutti i clienti
        this.state.employees.map((utente, index) => {
            console.log("%O", utente);
            c[index] = { id: utente.id, name: utente.name, email: utente.email, remove: buttonRemove(utente.id) }
        })

        //Dati della DataTable. Colonne statiche e Righe dinamiche
        const data = {
            columns: [
                {
                    label: 'id',
                    field: 'id',
                    sort: 'asc',
                    width: 150
                },
                {
                    label: 'Name',
                    field: 'name',
                    sort: 'asc',
                    width: 150
                },
                {
                    label: 'email',
                    field: 'email',
                    sort: 'asc',
                    width: 270
                },
                {
                    label: 'Rimuovi',
                    field: 'remove',
                    sort: 'asc',
                    width: 270
                }
            ],
            rows: c
        };

        //Renderizzo la DataTable
        return (
            <MDBCard style={{ width: "90%", margin: "2rem auto", "maxWidth": "90vw" }}>
                <MDBCardHeader tag="h3" className="text-center font-weight-bold text-uppercase py-4">
                    Impiegati assunti
            </MDBCardHeader>
                <MDBCardBody>

                    {/* Sezione Avvisi */}
                    {/*Significa che se OkMsg non è null appare l'alert altrimenti nulla.*/}
                    {this.state.OkMsg && <div className="alert alert-success">{this.state.OkMsg}</div>}
                    <ErrWebApiMsg ErrWebApi={this.state.ErrWebApi} ErrMsg={this.state.ErrMsg} obj={this} />

                    {/* Tabella */}
                    <MDBDataTable btn striped bordered hover entriesOptions={[5, 20, 25]} entries={5} data={data} />
                </MDBCardBody>
            </MDBCard>
        );
    }

}

function ErrWebApiMsg(props) {
    if (props.ErrWebApi) {
        return (
            <div>
                <div className="alert alert-danger" role="alert"><h3>{props.ErrMsg}</h3></div>
            </div>
        )
    }

    return null;
}
