package gr.hua.ds.demo.service;

import gr.hua.ds.demo.model.Request;

import java.util.List;

public interface RequestService {
    Request addUserToRequest(String username,Long requestId);
    Request getRequest(Long id);
    List<Request> getRequests();
    Request saveRequest(Request request);
    Request updateStatusRegistered (Long id);
    Request updateStatusPassed (Long id);
    Request updateStatusApproved (Long id);
    List<Request> getRequestsByStatus (String status);
    void deleteRequest(Long id);
}
