package org.mq.diff.repository;

import java.util.List;
import org.mq.diff.domain.Base64BinaryData;
import org.mq.diff.domain.Base64BinaryDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Base64BinaryDataRepository extends
    JpaRepository<Base64BinaryData, Base64BinaryDataId> {
  List<Base64BinaryData> findAllByIdDiffId(String id);
}
