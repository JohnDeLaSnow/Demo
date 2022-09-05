import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useParams } from "react-router-dom";

const accessToken = localStorage.getItem('access_token');

axios.interceptors.request.use(
    config => {
        config.headers.authorization = `Bearer ${accessToken}`;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default function ViewUser() {

    const [user, setUser] = useState({
        name: "",
        username: "",
        password: "",
        roles: null
    })

    const { id } = useParams();

    useEffect(() => {
        loadUser()
    }, [])

    const loadUser = async () => {
        const result = await axios.get(`user/get/id/${id}`);
        setUser(result.data)
    }

    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
                    <h2 className="text-center m-4">User Detais</h2>
                    <div className="card">
                        <div className="card-header">
                            Detais of user: {user.id}
                            <ul className="list-group list group-flush">
                                <li className="list-group-item">
                                    <div className="fw-bold">Name:</div>
                                    {user.name}
                                </li>
                                <li className="list-group-item">
                                    <div className="fw-bold">Username:</div>
                                    {user.username}
                                </li>
                            </ul>
                        </div>
                    </div>
                    <Link className="btn btn-primary my-2" to={"/"}>Return to Home</Link>
                </div>
            </div>
        </div>

    );
}
