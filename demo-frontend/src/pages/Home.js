import { useNavigate, Link } from "react-router-dom";
import { useContext } from "react";
import AuthContext from "../context/AuthProvider";

const Home = () => {
    const { setAuth } = useContext(AuthContext);
    const navigate = useNavigate();

    const logout = async () => {
        // if used in more components, this should be in context 
        // axios to /logout endpoint 
        setAuth({});
        navigate('/linkpage');
    }

    return (
        <section>
            <h1>Home</h1>
            <br />
            <article>
                <h3>For public use:</h3>
                <br />
                <Link to="/request">Make new request</Link>
                <br />
                <Link to="/linkpage">Go to the link page</Link>
                <br />
            </article>
            <br />
            <article>
                <h3>For editors:</h3>
                <br />
                <Link to="/editor">Pass requests</Link>
                <br />
            </article>
            <br />
            <article>
                <h3>For officers:</h3>
                <br />
                <Link to="/officer">Approve requests</Link>
                <br />
            </article>
            <br />
            <article>
                <h3>For Administrators:</h3>
                <br />
                <Link to="/admin">Manage users</Link>
                <br />
                <Link to="/lounge">Go to the Lounge</Link>
                <br />
            </article>
            <br />
            <div className="flexGrow">
                <button onClick={logout}>Sign Out</button>
            </div>
        </section>
    )
}

export default Home