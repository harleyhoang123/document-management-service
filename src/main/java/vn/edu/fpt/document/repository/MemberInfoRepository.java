package vn.edu.fpt.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.document.entity.MemberInfo;

import java.util.List;

@Repository
public interface MemberInfoRepository extends MongoRepository<MemberInfo, String> {

    List<MemberInfo> findAllByAccountId(String accountId);
}
