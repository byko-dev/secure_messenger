import classNames from "classnames";

const Spinner = ({className, classNameElement1, classNameElement2}) => {
    return(
        <div className={classNames("text-center", className)}>
            <div className={classNames("spinner-border", classNameElement1)} role="status">
                <span className="visually-hidden">Loading...</span>
            </div>
            <div className={classNames("spinner-grow", classNameElement2)} role="status">
                <span className="visually-hidden">Loading...</span>
            </div>
        </div>)
}
export default Spinner;