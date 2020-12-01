import React, { Component } from "react";
import CompaniesService from "../Services/Api/Companies/CompaniesAPI.js"
import { MDBDataTable, MDBCard, MDBCardHeader, MDBCardBody, MDBBtn } from 'mdbreact';
import HandleError from "../Errors/HandleError.js"


//Classe principale per la gestione dei clienti
export default class Companies extends Component {

    state = {
        companies: [],
        id: "",
        ErrWebApi: false,
        ErrMsg: "",
        NumCom: 0,
        OkMsg: null
    }

    //Metodo ideale per eseguire le operazioni di interfacciamento con i webservices all'avvio della scehrmata
    componentDidMount() {
        //Appena caricata la classe, cerco tutti i clienti presenti nel DB
        this.CercaTutte();
    }

    //Metodo per cercare tutti i cleitni nel DB richiamanto il metodo che contatta l'API
    CercaTutte = () => {
        CompaniesService.getAllCompaniesData()
            .then(response => this.handleResponse(response))
            .catch(error => {
                console.log("ERRORE: %O", error);
                HandleError.handleError(this, error)
            })
    }

    //Metodo per resettare tutti i valori dello state
    ResetValue = () => {
        this.setState({ companies: [], ErrWebApi: false, ErrMsg: "", NumCom: 0 });
    }

    //Metodo per cercare un cliente in base al suo ID
    Cerca = () => {

        this.ResetValue();

        //Se non viene passato nessun ID allora ritorno tutti i cleinti.
        if (this.state.id === "") {
            CompaniesService.getAllCompaniesData()
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))
        } else {
            CompaniesService.getCompanyById(this.state.id)
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))
        }
    }

    //MEtodo per eliminare un cliente in base al suo ID
    Elimina = (id) => {

        CompaniesService.delCompanyById(id)
            .then(() => {
                this.setState({ OkMsg: `Eliminazione azienda ${id} eseguita con successo!` })
                this.ResetValue();
                this.CercaTutti();
            })
            .catch(error => HandleError.handleError(this, error))
    }

    //Metodo per visualizzare la form di modifica di un partiolare cliente in base all'ID. Se l'Id è -1 si intende un nuovo cliente (Form vuota).
    Modifica = (id) => {
        this.props.history.push(`/inscompany/${id}`);
    }

    //Metodo per inserire un nuovo cliente. Uguale a Modifica() ma viene passato -1 come parametro
    Inserisci = () => {
        console.log("Premuto il tasto Inserimento!");

        this.props.history.push(`/inscompany/-1`);
    }


    //Metodo per gestire le risposte positive dei metodi.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse = (response) => {

        this.setState({
            companies: this.state.companies.concat(response.data)
        })

        //Questo secondo set state serve perchè se metto tutto in uno, il valore non si aggiorna perchè clienti non è ancora settato. Normale andamento del thread.
        this.setState({
            NumCom: this.state.companies.length
        })

    }

    //Ritorno la pagina visualizzata
    render() {
        return (
            <div>
                <section id="companiesSection">
                    <h3>Aziende Disponibili: {this.state.NumCom}</h3>

                    <div>
                        <button style={{ display: "inline-block" }} type="button" className="btn btn-primary" onClick={this.Cerca}><i className="fa fa-search"></i></button>
                        <div style={{ display: "inline-block" }} className="filter-group">
                            <label>Fltro: </label>
                            <input name="id" type="text" className="form-control" value={this.state.id} onChange={this.GestMod} />
                        </div>
                    </div>
                    {this.DatatablePage()}
                </section>
            </div>
        );
    };

    //Metodo che aggiorna le variabili dello state quando vengono modificati i dati all'interno della form
    GestMod = (event) => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }

    //Metodo per la generazione dinamica della DataTable. Uso mdbreact.
    DatatablePage = () => {

        //Array di lavoro
        var c = [];

        //Bottoni di modifica e eliminazione di un cliente. Index è il id.
        const buttonDelete = (index) => { return <button id={"delete-" + index} onClick={e => window.confirm(`Confermi l'eliminazione dell'azienda ${index}?`) && this.Elimina(index)} type="button" className="btn btn-danger rounded" size="sm">Elimina</button> }
        const buttonEdit = (index) => { return <button id={"edit-" + index} onClick={() => this.Modifica(index)} type="button" className="btn btn-warning rounded" size="sm">Modifica</button> }

        //Aggiungo a c tutti i clienti
        this.state.companies.map((company, index) => {
            console.log("%O", company);
            c[index] = { id: company.idCom, name: company.name, sector: company.sector, address: company.address.addressLineOne + ", " + company.address.city + ", " + company.address.cap + ", " + company.address.province, edit: buttonEdit(company.idCom), delete: buttonDelete(company.idCom) }
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
                    label: 'sector',
                    field: 'sector',
                    sort: 'asc',
                    width: 270
                },
                {
                    label: 'Address',
                    field: 'address',
                    sort: 'asc',
                    width: 270
                },
                {
                    label: 'Modifica',
                    field: 'edit',
                    sort: 'asc',
                    width: 270
                },
                {
                    label: 'Delete',
                    field: 'delete',
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
                    Table Editable
                    <br /><button onClick={this.Inserisci} type="button" className="btn btn-info rounded"><i className="fa fa-plus"></i> Aggiungi nuova azienda</button>
                </MDBCardHeader>
                <MDBCardBody>
                    
                    {/* Sezione Avvisi */}
                    {/*Significa che se OkMsg non è null appare l'alert altrimenti nulla.*/}
                    {this.state.OkMsg && <div className="alert alert-success">{this.state.OkMsg}</div>}
                    <ErrWebApiMsg ErrWebApi={this.state.ErrWebApi} ErrMsg={this.state.ErrMsg} obj={this}/>

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