package jhkim.mungnyangtoktok.backend.admin.repository;

import jhkim.mungnyangtoktok.backend.admin.entity.CodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
}
