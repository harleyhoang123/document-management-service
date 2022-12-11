package vn.edu.fpt.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.document.entity._Document;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<_Document, String> {

}
