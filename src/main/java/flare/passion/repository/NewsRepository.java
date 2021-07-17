package flare.passion.repository;

import flare.passion.model.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<Passage, String> {

    @Query(value = "SELECT COUNT(*) FROM passage WHERE type = 1 AND title LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
    long countNewsByKeyword(String keyword);

    int countByType(int type);

    @Query(value = "SELECT * FROM passage WHERE type = 1 ORDER BY publish_date DESC LIMIT 0, 1;", nativeQuery = true)
    Passage findLatestNews();

    boolean existsByExternalIdAndType(String externalId, int type);

    @Query(value = "SELECT * FROM passage WHERE type = 1 AND title LIKE CONCAT('%',:keyword,'%') AND banned = false ORDER BY publish_date DESC LIMIT :startPoint, :pageSize", nativeQuery = true)
    List<Passage> findTHENEWS(@Param("keyword") String keyword, @Param("startPoint") int startPoint, @Param("pageSize") int pageSize);

    @Query(value = "SELECT * FROM passage WHERE type = 4 AND banned = false ORDER BY publish_date DESC LIMIT :startPoint, :pageSize", nativeQuery = true)
    List<Passage> findTHEANNOUNCEMENT(@Param("startPoint") int startPoint, @Param("pageSize") int pageSize);

}
