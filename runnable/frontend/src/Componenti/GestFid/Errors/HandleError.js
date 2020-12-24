//File per la gestione di alcuni errori

//Classe per ritornare un messaggio di errore personalizzato in base al codice dell'errore.
class HandleError {

    handleError = (obj, error) => {
        if (error.response.status === 403) {
            obj.setState({
                ErrMsg: "Non hai il permesso di eseguire questa funzionaltà",
                ErrWebApi: true
            })
        } else {
            obj.setState({
                ErrMsg: error.response.data.message,
                ErrWebApi: true
            })
        }
    }

}

export default new HandleError();