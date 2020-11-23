import React, { Component } from "react";
import './Clienti.css';
import ClientiService from "../Services/Api/Clienti/ClientiAPI.js"
import AuthenticationService from '../Services/authService.js';
import { MDBDataTable, MDBCard, MDBCardHeader, MDBCardBody, MDBBtn } from 'mdbreact';
import HandleError from "../Errors/HandleError.js"
import jwt from "jsonwebtoken"


//Classe principale per la gestione dei clienti
export default class Clienti extends Component {

    state = {
        clienti: [],
        CodFid: "",
        ErrWebApi: false,
        ErrMsg: "",
        NumCli: 0,
        OkMsg: null
    }

    //Metodo ideale per eseguire le operazioni di interfacciamento con i webservices all'avvio della scehrmata
    componentDidMount() {
        //Appena caricata la classe, cerco tutti i clienti presenti nel DB
        this.CercaTutti();
    }

    //Metodo per cercare tutti i cleitni nel DB richiamanto il metodo che contatta l'API
    CercaTutti = () => {
        ClientiService.getAllClientiData()
            .then(response => this.handleResponse(response))
            .catch(error => {
                console.log("ERRORE: %O", error);
                HandleError.handleError(this, error)
            })
    }

    //Metodo per resettare tutti i valori dello state
    ResetValue = () => {
        this.setState({ clienti: [], ErrWebApi: false, ErrMsg: "", NumCli: 0 });
    }

    //Metodo per cercare un cliente in base al suo ID
    Cerca = () => {

        this.ResetValue();

        //Se non viene passato nessun ID allora ritorno tutti i cleinti.
        if (this.state.CodFid === "") {
            ClientiService.getAllClientiData()
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))
        } else {
            ClientiService.getClientiByCode(this.state.CodFid)
                .then(response => this.handleResponse(response))
                .catch(error => HandleError.handleError(this, error))
        }
    }

    //MEtodo per eliminare un cliente in base al suo ID
    Elimina = (codfid) => {

        ClientiService.delClienteByCode(codfid)
            .then(() => {
                this.setState({ OkMsg: `Eliminazione cliente ${codfid} eseguita con successo!` })
                this.ResetValue();
                this.CercaTutti();
            })
            .catch(error => HandleError.handleError(this, error))
    }

    //Metodo per visualizzare la form di modifica di un partiolare cliente in base all'ID. Se l'Id è -1 si intende un nuovo cliente (Form vuota).
    Modifica = (codfid) => {
        this.props.history.push(`/inscliente/${codfid}`);
    }

    //Metodo per inserire un nuovo cliente. Uguale a Modifica() ma viene passato -1 come parametro
    Inserisci = () => {
        console.log("Premuto il tasto Inserimento!");

        this.props.history.push(`/inscliente/-1`);
    }


    //Metodo per gestire le risposte positive dei metodi.
    //Le risposte negative vengono gestite da handleError.js nella cartella Errors
    handleResponse = (response) => {

        this.setState({
            clienti: this.state.clienti.concat(response.data)
        })

        //Questo secondo set state serve perchè se metto tutto in uno, il valore non si aggiorna perchè clienti non è ancora settato. Normale andamento del thread.
        this.setState({
            NumCli: this.state.clienti.length
        })

    }

    //Ritorno la pagina visualizzata
    render() {
        return (
            <div>
                <section id="clientiSection">
                    <h3>Clienti Disponibili: {this.state.NumCli}</h3>

                    <div>
                        <button style={{ display: "inline-block" }} type="button" className="btn btn-primary" onClick={this.Cerca}><i className="fa fa-search"></i></button>
                        <div style={{ display: "inline-block" }} className="filter-group">
                            <label>Fltro: </label>
                            <input name="CodFid" type="text" className="form-control" value={this.state.CodFid} onChange={this.GestMod} />
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

        //Bottoni di modifica e eliminazione di un cliente. Index è il codfid.
        const buttonDelete = (index) => { return <button id={"delete-" + index} onClick={e => window.confirm(`Confermi l'eliminazione del cliente ${index}?`) && this.Elimina(index)} type="button" className="btn btn-danger rounded" size="sm">Elimina</button> }
        const buttonEdit = (index) => { return <button id={"edit-" + index} onClick={() => this.Modifica(index)} type="button" className="btn btn-warning rounded" size="sm">Modifica</button> }

        //Aggiungo a c tutti i clienti
        this.state.clienti.map((cliente, index) => {
            cliente.cards != null
                ? c[index] = { codfid: cliente.codfid, nominativo: cliente.nominativo, bollini: cliente.cards.bollini, data: cliente.cards.ultimaspesa, edit: buttonEdit(cliente.codfid), delete: buttonDelete(cliente.codfid) }
                : c[index] = { codfid: cliente.codfid, nominativo: cliente.nominativo, bollini: 0, data: "Sconosciuta", edit: buttonEdit(cliente.codfid), delete: buttonDelete(cliente.codfid) }
        })

        //Dati della DataTable. Colonne statiche e Righe dinamiche
        const data = {
            columns: [
                {
                    label: 'CodFid',
                    field: 'codfid',
                    sort: 'asc',
                    width: 150
                },
                {
                    label: 'Name',
                    field: 'nominativo',
                    sort: 'asc',
                    width: 150
                },
                {
                    label: 'Bollini',
                    field: 'bollini',
                    sort: 'asc',
                    width: 270
                },
                {
                    label: 'Data',
                    field: 'data',
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
                    <br /><button onClick={this.Inserisci} type="button" className="btn btn-info rounded"><i className="fa fa-plus"></i> Aggiungi nuovo cliente</button>
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