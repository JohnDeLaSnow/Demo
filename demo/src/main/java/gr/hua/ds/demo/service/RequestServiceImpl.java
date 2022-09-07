package gr.hua.ds.demo.service;

import gr.hua.ds.demo.exception.RequestNotFoundException;
import gr.hua.ds.demo.model.Request;
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
}
