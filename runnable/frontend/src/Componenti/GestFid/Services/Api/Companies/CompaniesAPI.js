import axios from "axios";
import * as Constants from "../../../constants";

//Metodi per comunicare con le API backend relative ai clienti.
class CompaniesService {

    state = {
        Server: Constants.DOMAIN,
        BaseURL: "/api/companies"
    }

    getAllCompaniesData = () => {
        return axios.get(`${this.state.Server}${this.state.BaseURL}/cerca/all`);
    }

    getCompanyById = (id) => {
        return axios.get(`${this.state.Server}${this.state.BaseURL}/cerca/id/${id}`);
    }

    delCompanyById = (id) => {
        return axios.delete(`${this.state.Server}${this.state.BaseURL}/elimina/id/${id}`);
    }

    insCompany = (company) => {
        return axios.post(`${this.state.Server}${this.state.BaseURL}/insert`, company);
    }

    searchUsersToInvite = (companyId) => {
        console.log("ID: " + companyId);
        return axios.get(`${this.state.Server}${this.state.BaseURL}/searchUserToInvite/${companyId}`);
    }

    searchCompanysEmployees = (companyId) => {
        return axios.get(`${this.state.Server}${this.state.BaseURL}/searchCompanysEmployees/${companyId}`);
    }

    inviteUsers = (parameters) => {
        console.log("parameters" + " %O", parameters);
        return axios.post(`${this.state.Server}${this.state.BaseURL}/invite`, parameters);
    }

    removeUser = (parameters) => {
        console.log("parameters" + " %O", parameters);
        return axios.post(`${this.state.Server}${this.state.BaseURL}/removeUser`, parameters);
    }

}

export default new CompaniesService();