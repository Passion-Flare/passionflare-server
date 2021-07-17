package flare.passion.repository;

import flare.passion.model.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RumorRepository extends JpaRepository<Passage, String> {

    @Query(value = "SELECT COUNT(*) FROM passage WHERE type = 3 AND title LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
    long countByKeyword(String keyword);

    @Query(value = "SELECT * FROM passage WHERE type = 3 ORDER BY publish_date DESC LIMIT 0, 1;", nativeQuery = true)
    Passage findLatestRumor();

    boolean existsByExternalIdAndType(String externalId, int type);

    @Query(value = "SELECT * FROM passage WHERE type = 3 AND title LIKE CONCAT('%',:keyword,'%') AND banned = false ORDER BY publish_date DESC LIMIT :startPoint, :pageSize", nativeQuery = true)
    List<Passage> findTHERUMORS(@Param("keyword") String keyword, @Param("startPoint") int startPoint, @Param("pageSize") int pageSize);

}
