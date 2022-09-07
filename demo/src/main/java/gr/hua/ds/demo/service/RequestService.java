package gr.hua.ds.demo.service;

import gr.hua.ds.demo.model.Request;

import java.util.List;

public interface RequestService {
    Request getRequest(Long id);
    List<Request> getRequests();
    Request saveRequest(Request request);
}
