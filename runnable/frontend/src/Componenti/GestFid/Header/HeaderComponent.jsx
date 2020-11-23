import React, { Component } from "react";
import './HeaderComponent.css';
import {withRouter} from "react-router";
import AuthenticationService from "../Services/authService.js";
import { Link } from 'react-router-dom';

//Header e navbar del progetto
class HeaderComponent extends Component {

    render() {

        return (

            <div className="HeaderComponent">
                <Menu />
            </div>

        );

    }

}

export default withRouter(HeaderComponent);

const Menu = () => {
    return (
        <nav className="navbar navbar-dark bg-dark navbar-expand-lg">
            <Link rel="nofollow noopener" target="_blank" className="navbar-brand" to="/" >
                <img src={`../logo.png`} width="30" height="30" className="d-inline-block align-top" alt="" /> Problems Solving
            </Link>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item active">
                        <Link className="nav-link" to="/" >Home <span className="sr-only">(current)</span></Link>
                    </li>
                    <li className="nav-item">                        
                        <Link className="nav-link" to="/clienti" >Clienti</Link>
                    </li>
                    <li className="nav-item dropdown">
                        <a className="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Dropdown
                            </a>
                        <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a className="dropdown-item" href="#">Action</a>
                            <a className="dropdown-item" href="#">Another action</a>
                            <div className="dropdown-divider"></div>
                            <a className="dropdown-item" href="#">Something else here</a>
                        </div>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link disabled" href="#">Disabled</a>
                    </li>
                </ul>
                <Search />
                <User />
            </div>
        </nav>
    );
}

const Search = () => {
    return (
        <div className="searchDiv">
            <form className="form-inline my-2 my-lg-0">
                <input className="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                <button className="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
        </div>
    );
}

const User = () => {
    return (
        <ul className="navbar-nav nav-flex-icons">
            <li style={{ margin: 'auto' }} className="nav-item">
                <a className="nav-link">1
                    <i className="fa fa-envelope"></i>
                </a>
            </li>
            <li className="nav-item avatar dropdown">
                <a className="nav-link dropdown-toggle" id="navbarDropdownMenuLink-55" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                    <img style={{ width: '50px' }} src={`../logo.png`} className="rounded-circle z-depth-0"
                        alt="avatar image" />
                </a>
                <UserInfo/>
            </li>
        </ul>
    );
}

const UserInfo = () => {
    if(AuthenticationService.isLogged()){
        return(

            <div className="dropdown-menu dropdown-menu-lg-right dropdown-secondary" aria-labelledby="navbarDropdownMenuLink-55">
                    <p>Benvenuto {AuthenticationService.getUserInfo()}</p>
                    <Link  className="dropdown-item" to="/logout" >Logout</Link>
            </div>

        );
    }else{
        return(

            <div className="dropdown-menu dropdown-menu-lg-right dropdown-secondary" aria-labelledby="navbarDropdownMenuLink-55">
                    <Link  className="dropdown-item" to="/login" >Login</Link>
                    <Link  className="dropdown-item" to="/registrati" >Registrati</Link>
            </div>

        );
    }
}