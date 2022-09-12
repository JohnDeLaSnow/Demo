import { useState, useEffect } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useNavigate, useLocation } from "react-router-dom";
import axios from 'axios';
import useAuth from "../hooks/useAuth";



const Users = () => {

    const { auth } = useAuth();
    const [users, setUsers] = useState();
    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const location = useLocation();

    const onDelete = async (username) => {
        await axios({
            url: `http://localhost:8080/api/user/${username}`,
            method: "DELETE",
            headers: {
                'Authorization': `Bearer ${auth?.access_token}`
            },
            data: {}
        })
        loadUsers();
    };

    const loadUsers = async () => {
        let isMounted = true;
        const controller = new AbortController();

        const getUsers = async () => {
            try {
                const response = await axiosPrivate.get('users', {
                    signal: controller.signal
                });
                console.log(response.data);
                isMounted && setUsers(response.data);
            } catch (err) {
                if (err?.code !== 'ERR_CANCELED') {
                    console.error(err);
                    navigate('/login', { state: { from: location }, replace: true });
                }
            }
        }

        getUsers();

        return () => {
            isMounted = false;
            controller.abort();
        }
    };

    useEffect(() => {
        loadUsers();
    }, []);

    return (
        <article>
            <div className="container">
                <h2>Users List</h2>
                {users?.length
                    ? (
                        <table>
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Fisrt_Name</th>
                                    <th>Last_Name</th>
                                    <th>Roles</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((user, index) => (
                                    <tr key={index}>
                                        <td>{user?.username}</td>
                                        <td>{user?.firstName}</td>
                                        <td>{user?.lastName}</td>
                                        <td>{user?.roles?.map((role, index) => (<h5>{role.name}</h5>))}</td>
                                        <td>
                                            <button>Edit</button>
                                            <button
                                                onClick={() => onDelete(user?.username)}
                                            >Delete</button></td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : <p>No users to display</p>
                }
            </div>
        </article>
    );
};
export default Users;
