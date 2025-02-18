package jhkim.mungnyangtoktok.backend.admin.repository;

import jhkim.mungnyangtoktok.backend.admin.entity.CodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeDetailRepository extends JpaRepository<CodeDetail, Long> {
    List<CodeDetail> findByCodeGroup_GroupId(Long groupId);

    @Modifying
    @Query("DELETE FROM CodeDetail c WHERE c.codeGroup.id  = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);

}

