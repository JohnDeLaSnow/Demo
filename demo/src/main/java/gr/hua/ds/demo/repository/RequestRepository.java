package gr.hua.ds.demo.repository;

import gr.hua.ds.demo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long>{
    //JPQL
    @Query("SELECT req FROM request req WHERE req.status=?1")
    public List<Request> getRequestsByStatus (String status);
}
