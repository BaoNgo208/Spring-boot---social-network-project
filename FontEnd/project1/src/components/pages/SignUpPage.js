import React, { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from 'react-router-dom';
import bcrypt from 'bcryptjs';

export const SignUpPage = (props) => {
  const [formData, setFormData] = useState({
    emailId: "",
    password: "",
    accName: "",
    roles: "ROLE_ADMIN",
    employee: {
      userName: "",
      dateOfBirth: "",
      mobileNumber: ""
    }
  });

  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;

    if (name === "dateOfBirth" || name === "mobileNumber") {
      setFormData(prevFormData => ({
        ...prevFormData,
        employee: {
          ...prevFormData.employee,
          [name]: value
        }
      }));
    } else {
      setFormData(prevFormData => ({
        ...prevFormData,
        [name]: value
      }));
    }
  };

  const { mutate: signUp, isLoading, isError, data } = useMutation({
    mutationFn: async () => {
      const hashedPassword = await bcrypt.hash(formData.password, 10);
      const response = await fetch('http://localhost:8080/auth/sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          emailId: formData.emailId,
          password: hashedPassword,
          accName: formData.accName,
          roles: formData.roles,
          employee: {
            userName: formData.employee.userName,
            dateOfBirth: formData.employee.dateOfBirth,
            mobileNumber: formData.employee.mobileNumber
          }
        })
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      return response.json();
    },
    onSuccess: (data) => {
      alert('Sign up successful!');
      navigate('/');
    },
    onError: (error) => {
      alert(`Sign up failed: ${error.message}`);
    }
  });

  const handleSignUpClick = () => {
    signUp();
  };

  return (
    <section className="vh-100">
      <div className="container py-5 h-100">
        <div className="row d-flex align-items-center justify-content-center h-100">
          <div className="col-md-8 col-lg-7 col-xl-6">
            <img
              src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg"
              className="img-fluid"
              alt="Phone image"
            />
          </div>
          <div className="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
            <form>
              <div data-mdb-input className="form-outline mb-4">
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="usernameField"
                >
                  Họ và tên
                </label>
                <input
                  className="form-control form-control-lg"
                  type="text"
                  id="usernameField"
                  name="accName"
                  onChange={handleChange}
                ></input>
              </div>
              <div data-mdb-input className="form-outline mb-4">
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="usernameField"
                >
                  Username
                </label>
                <input
                  className="form-control form-control-lg"
                  type="text"
                  id="usernameField"
                  name="userName"
                  onChange={handleChange}
                ></input>
              </div>
              <div data-mdb-input className="form-outline mb-4">
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="emailField"
                >
                  Email
                </label>
                <input
                  className="form-control form-control-lg"
                  type="email"
                  id="emailField"
                  name="emailId"
                  onChange={handleChange}
                ></input>
              </div>
              <div data-mdb-input className="form-outline mb-4">
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="dobField"
                >
                  Date of Birth
                </label>
                <input
                  className="form-control form-control-lg"
                  type="text"
                  id="dobField"
                  name="dateOfBirth"
                  onChange={handleChange}
                ></input>
              </div>
              <div data-mdb-input className="form-outline mb-4">
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="mobileField"
                >
                  Mobile Number
                </label>
                <input
                  className="form-control form-control-lg"
                  type="text"
                  id="mobileField"
                  name="mobileNumber"
                  onChange={handleChange}
                ></input>
              </div>
              <div data-mdb-input-init className="form-outline mb-4">
                <input
                  className="form-control form-control-lg"
                  type="password"
                  id="passwordField"
                  name="password"
                  onChange={handleChange}
                ></input>
                <label
                  className="form-label custom-label d-flex justify-content-start align-items-end pe-3"
                  htmlFor="passwordField"
                >
                  Password
                </label>
              </div>
          
              <button
                type="button"
                data-mdb-button-init
                data-mdb-ripple-init
                className="btn btn-primary btn-lg btn-block"
                onClick={handleSignUpClick}
              >
                Sign Up
              </button>
              <div className="divider d-flex align-items-center my-4">
                <p className="text-center fw-bold mx-3 mb-0 text-muted">OR</p>
              </div>
              <a
                data-mdb-ripple-init
                className="btn btn-primary btn-lg btn-block"
                style={{ backgroundColor: "#3b5998" }}
                href="#!"
                role="button"
              >
                <i className="fab fa-facebook-f me-2"></i>Sign Up with Facebook
              </a>
              <p className="text-center mt-3">
                Already have an account? <a href="/">Login</a>
              </p>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
};
