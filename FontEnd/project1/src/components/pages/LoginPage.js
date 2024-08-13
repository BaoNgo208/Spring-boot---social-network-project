import React from "react"
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

export  const LoginPage = (props)=> {

  const navigate = useNavigate();

  const handleSignUpRedirect = () => {
    navigate("/signup");
  };
    return(
        
        <section className="vh-100">
        <div className="container py-5 h-100">
          <div className="row d-flex align-items-center justify-content-center h-100">
            <div className="col-md-8 col-lg-7 col-xl-6">
              <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg"
                className="img-fluid" alt="Phone image" /> 
            </div>
            <div className="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
            <form>
                  <div data-mdb-input className="form-outline mb-4">
                    <label className="form-label custom-label d-flex justify-content-start align-items-end pe-3" htmlFor="emailField" >Email</label>

                    <input className="form-control form-control-lg" type="email" id="emailField" name="username" onChange={props.setParams}></input>
      
                  </div>
      
                  <div data-mdb-input-init className="form-outline mb-4">
                      <input className="form-control form-control-lg" type="password" id="passwordField" name="password" onChange={props.setParams}></input>
                      <label className="form-label custom-label d-flex justify-content-start align-items-end pe-3" htmlFor="emailField">Password</label>
      
                  </div>
              
                <button type="button" data-mdb-button-init data-mdb-ripple-init className="btn btn-primary btn-lg btn-block" onClick={props.login}>Login</button>
                
                <div className="divider d-flex align-items-center my-4">
                    <p className="text-center fw-bold mx-3 mb-0 text-muted">OR</p>
                </div>

                <Link
                to="/signup"
                className="btn btn-primary btn-lg btn-block"
                style={{ backgroundColor: "#3b5998" }}
                role="button"
              >
                <i className="fab fa-facebook-f me-2"></i>Sign Up
              </Link>

              </form>
            </div>
          </div>
        </div>
      </section>
    )

}