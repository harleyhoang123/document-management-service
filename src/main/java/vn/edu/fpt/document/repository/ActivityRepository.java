package vn.edu.fpt.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.document.entity.Activity;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
}
