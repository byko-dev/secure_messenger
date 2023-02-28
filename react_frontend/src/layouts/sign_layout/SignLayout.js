import classNames from "classnames";

const SignLayout = ({children, className}) => {

    return (
        <section className="sign-layout-container" >
            <div className={classNames("gradient-border", className)} >
                <h3>Secured Messenger</h3>
                {children}
            </div>
            <p className="mt-3 text-muted">&copy; Developed by
                <a href="https://github.com/byko-dev" target="_blank" rel="noreferrer">byko-dev</a> 2022</p>
        </section>
    )
}

export default SignLayout;