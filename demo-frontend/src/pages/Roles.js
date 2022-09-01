import React, { useEffect, useState } from 'react'
import axios from 'axios'

const accessToken = localStorage.getItem('access_token');
const apiUrl = "roles";

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

    const [roles, setRoles] = useState([]);

    useEffect(() => {
        loadRoles();
    }, []);

    const loadRoles = async () => {
        const result = await axios.get(apiUrl);
        setRoles(result.data)
        console.log(result.data)
    }

    return (
        <div className='container'>
            <div className='py-4'>
                <table className="table border shadow">
                    <thead>
                        <tr>
                            <th scope="col">S.N.</th>
                            <th scope="col">Name</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {roles.map((role, index) => (
                            <tr>
                                <th scope="row" key={index}>{index + 1}</th>
                                <td>{role.name}</td>
                                <td>
                                    <button className='btn btn-primary mx-2'>View</button>
                                    <button className='btn btn-outline-primary mx-2'>Edit</button>
                                    <button className='btn btn-danger mx-2'>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}