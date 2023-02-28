import React from 'react';
import ReactDOM from 'react-dom/client';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import "../src/assets/main.scss";

import reportWebVitals from './reportWebVitals';
import SignLayout from "./layouts/sign_layout/SignLayout";
import Login from "./pages/login/Login";
import Register from "./pages/register/Register";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<SignLayout className={"login-form"}> <Login /> </SignLayout>} />
            <Route path="/register" element={<SignLayout className={"register-form"}> <Register /> </SignLayout>} />
        </Routes>
    </BrowserRouter>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
