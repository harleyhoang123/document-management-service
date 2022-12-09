package vn.edu.fpt.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.document.entity.MemberInfo;
import vn.edu.fpt.document.entity._Page;

@Repository
public interface PageRepository extends MongoRepository<_Page, String> {
}
