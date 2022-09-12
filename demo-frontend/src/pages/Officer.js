import { Link } from "react-router-dom"
import RequestsApprove from "./RequestsApprove"

const Officer = () => {
    return (
        <section>
            <h1>Officer Page</h1>
            <br />
            <RequestsApprove />
            <br />
            <div className="flexGrow">
                <Link to="/">Home</Link>
            </div>
        </section>
    )
}

export default Officer