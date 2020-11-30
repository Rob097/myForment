import axios from "axios";
import jwt from "jsonwebtoken"
import * as Constants from "../constants";

//Funzionalità varie per quanto riguarda il login, l'autenticazione e la comunicazione con le API riguardanti gli utenti.
class AuthenticationService {

    state = {
        Server: Constants.DOMAIN
    }

    //Aggiorna il token di accesso
    refreshToken = () => {
        return axios.get(`${this.state.Server}/api/auth/refresh-token`);
    }

    //Con l'autenticazione JWT i parametri non vengono passati con un header ma attraverso il body
    JWTAuthServer = (username, password, rememberMe) => {
        return axios({
            method: 'post', //you can set what request you want to be
            url: `${this.state.Server}/api/auth/signin`,
            data: {
                username: username,
                password: password,
                rememberMe: rememberMe
            }
        })
    }

    //Funzione per ottenere l'username dell'utente loggato
    getUserInfo = () => {
        try {
            let token = jwt.decode(this.getTokenFromCookie());
            if (token !== null && token !== undefined)
                return token.sub;
        } catch (e) {
            console.log(e);
        }
        return null;
    };

    //Funzione per verificare se l'utente è loggato
    isLogged = () => {

        let user = this.getUserInfo();

        if (user === null)
            return false;
        else
            return true;

    }

    //Funcione per ottenere il token JWT dal relativo cookie
    getTokenFromCookie = () => {
        try {
            let cookies = document.cookie.split(';');//contains all the cookies
            let cookieName = []; // to contain name of all the cookies
            let index = -1;
            let token;

            for (let i = 0; i < cookies.length; i++) {
                cookieName[i] = cookies[i].split('=')[0].trim();
            }
            index = cookieName.indexOf(Constants.TOKEN_COOKIE);

            if (index > -1) {
                token = cookies[index].split(/=(.+)/)[1];
                return token;
            }

        } catch (e) {
            console.log(e);
        }

        return null;
    }


    //Funzione fondamentale.
    //Viene chiamata subito all'avvio dell'app da index.js per verificare se l'utente è loggato.
    //Nel caso sia loggato controlla se vuole essere ricordato e altrimenti cancella i cookies
    firstCheckIsLogged() {
        try {
            if (sessionStorage.getItem("isChecked") === null || !sessionStorage.getItem("isChecked")) {
                let cookies = document.cookie.split(';');//contains all the cookies
                let cookieName = []; // to contain name of all the cookies
                let indexR = -1, indexT = -1;

                for (let i = 0; i < cookies.length; i++) {
                    cookieName[i] = cookies[i].split('=')[0].trim();
                }
                indexR = cookieName.indexOf(Constants.REMEMBER_COOKIE);
                indexT = cookieName.indexOf(Constants.TOKEN_COOKIE);

                //Se esiste il cookie con il token e il cookie remember me ed è true
                if (indexT <= -1 || indexR <= -1 || cookies[indexR].split(/=(.+)/)[1] !== "true") {
                    document.cookie = Constants.REMEMBER_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                    document.cookie = Constants.TOKEN_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                }
                sessionStorage.setItem("isChecked", true);
            }
        } catch (ex) {
            sessionStorage.setItem("isChecked", false);
            console.log(ex);
        }
    }

    signUp = (utente) => {
        return axios.post(`${this.state.Server}/api/auth/signup`, utente);
    }

    getAllUsers = () =>{
        return axios.get(`${this.state.Server}/api/auth/getAll`);
    }

    getAllRoles = () =>{
        return axios.get(`${this.state.Server}/api/auth/getAllRoles`);
    }

}

export default new AuthenticationService();