package vn.edu.fpt.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.document.entity.Visited;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 11/12/2022 - 17:04
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface VisitedRepository extends MongoRepository<Visited, String> {
}
