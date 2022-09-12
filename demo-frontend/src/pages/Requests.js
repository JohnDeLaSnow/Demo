import { useState, useEffect } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useNavigate, useLocation } from "react-router-dom";



const Requests = () => {

    const [requests, setRequests] = useState();
    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const location = useLocation();

    const onDelete = async (id) => {
        let isMounted = true;
        const controller = new AbortController();

        const deleteRequest = async () => {
            try {
                const response_tmp = await axiosPrivate.delete(`request/${id}`, {
                    signal: controller.signal
                });
                const response = await axiosPrivate.get('request/status/PASSED', {
                    signal: controller.signal
                });
                console.log(response.data);
                console.log(response_tmp.data);
                isMounted && setRequests(response.data);
            } catch (err) {
                if (err?.code !== 'ERR_CANCELED') {
                    console.error(err);
                    navigate('/login', { state: { from: location }, replace: true });
                }
            }
        }

        deleteRequest();

        return () => {
            isMounted = false;
            controller.abort();
        }

    };

    const onPass = async (id) => {
        let isMounted = true;
        const controller = new AbortController();

        const putStatus = async () => {
            try {
                const response_tmp = await axiosPrivate.put(`request/status/passed/${id}`, {
                    signal: controller.signal
                });
                const response = await axiosPrivate.get('request/status/REGISTERED', {
                    signal: controller.signal
                });
                console.log(response.data);
                console.log(response_tmp.data);
                isMounted && setRequests(response.data);
            } catch (err) {
                if (err?.code !== 'ERR_CANCELED') {
                    console.error(err);
                    navigate('/login', { state: { from: location }, replace: true });
                }
            }
        }

        putStatus();

        return () => {
            isMounted = false;
            controller.abort();
        }

    };

    const loadRequests = async () => {
        let isMounted = true;
        const controller = new AbortController();

        const getRequests = async () => {
            try {
                const response = await axiosPrivate.get('request/status/REGISTERED', {
                    signal: controller.signal
                });
                console.log(response.data);
                isMounted && setRequests(response.data);
            } catch (err) {
                if (err?.code !== 'ERR_CANCELED') {
                    console.error(err);
                    navigate('/login', { state: { from: location }, replace: true });
                }
            }
        }

        getRequests();

        return () => {
            isMounted = false;
            controller.abort();
        }
    };

    useEffect(() => {
        loadRequests();
    }, []);

    return (
        <article>
            <div className="container">
                <h2>Registered Requests</h2>
                {requests?.length
                    ? (
                        <table>
                            <thead>
                                <tr>
                                    <th>Request ID</th>
                                    <th>Description</th>
                                    <th>Status</th>
                                    <th>User</th>
                                    <th>File</th>
                                </tr>
                            </thead>
                            <tbody>
                                {requests.map((request, index) => (
                                    <tr key={index}>
                                        <td>{request?.id}</td>
                                        <td>{request?.description}</td>
                                        <td>{request?.status}</td>
                                        <td>{request?.appUser?.username}</td>
                                        <td></td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : <p><br />There are no registered requests</p>
                }
            </div>
        </article>
    );
};
export default Requests;
