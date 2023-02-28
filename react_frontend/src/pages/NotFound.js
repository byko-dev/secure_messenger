import {useEffect, useState} from "react";
import {Navigate, useLocation} from "react-router-dom";

const NotFound = () => {

    const location = useLocation();
    const [counter, setCounter] = useState(10);
    const countdown = () => setCounter(counter-1);

    useEffect(() => {
        const timeoutID = setTimeout(countdown, 1000);
        return() => clearTimeout(timeoutID);
    }, [counter]);

    return(<section className="d-flex flex-column align-items-center m-5">
        <h1 style={{color: "#e5e5e5"}}>Page not found - error code 404</h1>
        <p>Redirect to homepage in {counter} seconds...</p>
        <p>No match for <code>{location.pathname}</code></p>
        {counter===0 ? <Navigate replace to="/" /> : ""}
    </section>)
}
export default NotFound;
