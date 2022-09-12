package gr.hua.ds.demo.service;

import gr.hua.ds.demo.exception.AppUserNotFoundException;
import gr.hua.ds.demo.exception.RequestNotFoundException;
import gr.hua.ds.demo.exception.RoleNotFoundException;
import gr.hua.ds.demo.model.AppUser;
import gr.hua.ds.demo.model.Request;
import gr.hua.ds.demo.model.Role;
import gr.hua.ds.demo.repository.AppUserRepository;
import gr.hua.ds.demo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService{
    //Repository
    private final RequestRepository requestRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public Request addUserToRequest(String username, Long requestId) {
        log.info("Adding user {} to request {}", username,requestId);
        AppUser appUser = appUserRepository.findById(username).orElseThrow(()->new AppUserNotFoundException(username));
        Request request = requestRepository.findById(requestId).orElseThrow(()->new RequestNotFoundException(requestId));
        request.setAppUser(appUser);
        return request;
    }

    @Override
    public Request getRequest(Long id) {
        log.info("Fetching the request with id {}", id);
        return requestRepository.findById(id).orElseThrow(()->new RequestNotFoundException(id));
    }

    @Override
    public List<Request> getRequests() {
        log.info("Fetching all requests");
        return requestRepository.findAll();
    }

    @Override
    public Request saveRequest(Request request) {
        log.info("Saving new request with id {} to the database", request.getId());
        return requestRepository.save(request);
    }

    @Override
    public Request updateStatusRegistered (Long id){
        Request request=this.getRequest(id);
        log.info("Updating request {} status to registered",id);
        request.setStatus("REGISTERED");
        return request;
    }

    @Override
    public Request updateStatusPassed (Long id){
        Request request=this.getRequest(id);
        log.info("Updating request {} status to registered",id);
        request.setStatus("PASSED");
        return request;
    }
    @Override
    public Request updateStatusApproved (Long id){
        Request request=this.getRequest(id);
        log.info("Updating request {} status to registered",id);
        request.setStatus("APPROVED");
        return request;
    }

    @Override
    public List<Request> getRequestsByStatus(String status) {
        return requestRepository.getRequestsByStatus(status);
    }

    @Override
    public void deleteRequest(Long id) {
        log.info("Deleting request {}", id);
        requestRepository.deleteById(id);
    }

}
