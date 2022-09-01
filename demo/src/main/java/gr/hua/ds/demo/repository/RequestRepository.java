package gr.hua.ds.demo.repository;

import gr.hua.ds.demo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request,Long>{
}
