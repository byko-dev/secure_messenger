import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import CookiesUtils from "../../utils/CookiesUtils";
import Utils from "../../utils/Utils";
import * as api from "../../utils/api";
import {Breadcrumb} from "react-bootstrap";
import Spinner from "../../elements/Spinner";

const Register = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [retypePassword, setRetypePassword] = useState("");
    const [response, setResponse] = useState("");
    const [fetch, setFetch] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        async function validateAuthToken(){
            const valid = await CookiesUtils.checkTokenIsValid();
            if(valid) navigate("/userpanel");
        }
        validateAuthToken();
    }, [])


    const validateRegisterData = () => {
        setFetch(true);
        if(!Utils.validateData(username, password)){
            setResponse({message: "The password or username must contain at least 6 characters!", valid: false});
            setFetch(false);
        } else if(password !== retypePassword){
            setResponse({message: "Passwords do not match!", valid: false});
            setFetch(false);
        } else {
            register();
        }
    }

    const register = async () => {
        if(!fetch){
            setFetch(true);
            await api.registerRequest(username, password)
                .then(() => {
                    setResponse({message: "Your account has been created! Please login!", valid: true});
                    setFetch(false);
                    setUsername("");
                    setPassword("");
                    setRetypePassword("");
                })
                .catch(error => error.then(err =>{
                    setFetch(false);
                    setResponse({message: err.status, valid: false});
                }))
        }
    }

    const clearVariables = () => {
        setResponse({message: "", valid: false});
        setFetch(false);
    }

    const confirmByEnter = (event) => {
        if(event.keyCode === 13) validateRegisterData()
    }

    return(
        <div>
            <Breadcrumb>
                <Breadcrumb.Item onClick={() => navigate("/")}>Login</Breadcrumb.Item>
                <Breadcrumb.Item active>Register</Breadcrumb.Item>
                <Breadcrumb.Item>Forgot password</Breadcrumb.Item>
            </Breadcrumb>

            <label htmlFor="username" className="label">Username</label>
            <input type="text" placeholder="Type your username" id="username"
                   className="input"
                   value={username} onChange={e => {setUsername(e.target.value); clearVariables();}}
                   onKeyDown={(e) => confirmByEnter(e)}/>

            <label htmlFor="password" className="label">Password</label>
            <input type="password" placeholder="Type your password" id="password"
                   className="input"
                   value={password} onChange={e => {setPassword(e.target.value); clearVariables();}}
                   onKeyDown={(e) => confirmByEnter(e)} />

            <label htmlFor="retype_username" className="label">Retyped Password</label>
            <input type="password" placeholder="Retype your password" id="retype_username"
                   className="input"
                   value={retypePassword} onChange={e => {setRetypePassword(e.target.value); clearVariables();}}
                   onKeyDown={(e) => confirmByEnter(e)} />

            <button type="button" className="button btn btn-outline-info" onClick={() => validateRegisterData()}>Sign up</button>

            {fetch ? <Spinner className={"d-flex gap-2 justify-content-center mt-4"} />: <> </>}
            {Utils.validateData(response.message)? <p className={response.valid? "response response-success": "response"} >{response.message}</p> : <></>}

        </div>
    )

}
export default Register;