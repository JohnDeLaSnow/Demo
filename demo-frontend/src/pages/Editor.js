import { Link } from "react-router-dom"
import RequestsPass from "./RequestsPass"

const Editor = () => {
    return (
        <section>
            <h1>Editors Page</h1>
            <br />
            <RequestsPass />
            <br />
            <div className="flexGrow">
                <Link to="/">Home</Link>
            </div>
        </section>
    )
}

export default Editor