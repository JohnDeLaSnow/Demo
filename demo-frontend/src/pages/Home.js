import React, { useEffect, useState } from 'react'
import axios from 'axios'
import { Link, } from 'react-router-dom'

const accessToken = localStorage.getItem('access_token');
const apiUrl = "users";

axios.interceptors.request.use(
    config => {
        config.headers.authorization = `Bearer ${accessToken}`;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default function Home() {

    const [users, setUsers] = useState([]);

    //const { id } = useParams();

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        const result = await axios.get(apiUrl);
        setUsers(result.data);
    };

    const deleteUser = async (id) => {
        await axios.delete(`http://localhost:8080/api/user/delete/id/${id}`);
        loadUsers();
    }

    return (
        <div className='container'>
            <div className='py-4'>
                <table className="table border shadow">
                    <thead>
                        <tr>
                            <th scope="col">S.N.</th>
                            <th scope="col">Name</th>
                            <th scope="col">Username</th>
                            <th scope="col">Roles</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>

                        {users.map((user, index) => (
                            <tr>
                                <th scope="row" key={user.id}>{user.id}</th>
                                <td>{user.name}</td>
                                <td>{user.username}</td>
                                <td>{user.roles.map((role, index) => {
                                    return (
                                        <span key={role.id}>
                                            {(index ? ', ' : '') + role.name}
                                        </span>
                                    )
                                })}</td>

                                <td>
                                    <button className='btn btn-primary mx-2'>View</button>
                                    <Link className='btn btn-outline-primary mx-2'
                                        to={`/edituser/${user.id}`}
                                    >Edit</Link>
                                    <button className='btn btn-danger mx-2'
                                        onClick={() => deleteUser(user.id)}
                                    >Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}