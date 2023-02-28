import {useEffect, useState} from "react";
import * as api from "../../utils/api";
import {useNavigate} from "react-router-dom";
import StorageUtils from "../../utils/StorageUtils";
import Utils from "../../utils/Utils";
import CookiesUtils from "../../utils/CookiesUtils";
import {Breadcrumb, Spinner} from "react-bootstrap";

const Login = () => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [response, setResponse] = useState("");
    const [fetch, setFetch] = useState(false);
    const navigate = useNavigate();

    const loginRequest = async () => {
        if(!fetch)
            if(Utils.validateData(username, password)){
                setFetch(true);

                await api.loginRequest(username, password)
                    .then(response => {
                        CookiesUtils.setAuthToken(response.jwt);

                        api.getUserRsaKeys(password, response.jwt)
                            .then(response => {
                                setFetch(false);
                                StorageUtils.setPrivateKey(response.privateKey);
                                StorageUtils.setPublicKey(response.publicKey);
                                navigate("/userpanel");
                            }).catch(error => error.then(err => {
                            setResponse(err.error)
                            setFetch(false)
                        }));
                    })
                    .catch(error => error.then(err => {
                        setFetch(false);
                        setResponse(err.error)
                    }));
            }else{
                setResponse("Your username or password length is not valid!")
            }
    }

    useEffect(() => {
        async function validateAuthToken(){
            const valid = await CookiesUtils.checkTokenIsValid();
            if(valid) navigate("/userpanel");
        }
        validateAuthToken();
    }, [])

    const confirmByEnter = (event) => {
        if(event.keyCode === 13) loginRequest()
    }

    return(
        <div>
            <Breadcrumb>
                <Breadcrumb.Item active>Login</Breadcrumb.Item>
                <Breadcrumb.Item onClick={() => navigate("/register")}>
                    Register
                </Breadcrumb.Item>
                <Breadcrumb.Item>Forgot password</Breadcrumb.Item>
            </Breadcrumb>

            <label htmlFor="username" className="label">Username</label>
            <input type="text" placeholder="Type your username" id="username" value={username}
                   className="input"
                   onChange={e => {setUsername(e.target.value); setResponse(""); setFetch(false)}}
                   onKeyDown={(e) => confirmByEnter(e)} />

            <label htmlFor="password" className="label">Password</label>
            <input type="password" placeholder="Type your password" id="password" value={password}
                   className="input"
                   onChange={e => {setPassword(e.target.value); setResponse(""); setFetch(false);}}
                   onKeyDown={(e) => confirmByEnter(e)} />

            <button className="button btn btn-outline-info " onClick={() => loginRequest()}>Sign in</button>

            {fetch ? <Spinner className={"d-flex gap-2 justify-content-center mt-4"} />: <> </>}
            {Utils.validateData(response)? <p className="response">
                {response}
            </p> : <></>}
        </div>
    )
}
export default Login;