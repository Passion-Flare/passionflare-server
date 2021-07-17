package flare.passion.repository;

import flare.passion.model.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Passage, String> {

    @Query(value = "SELECT COUNT(*) FROM passage WHERE type = 2 AND title LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
    long countByKeyword(String keyword);

    Passage findByTitleAndType(String title, int type);

    @Query(value = "SELECT * FROM passage WHERE type = 2 AND title LIKE CONCAT('%',:keyword,'%') AND banned = false ORDER BY publish_date DESC LIMIT :startPoint, :pageSize", nativeQuery = true)
    List<Passage> findTHEQnAs(@Param("keyword") String keyword, @Param("startPoint") int startPoint, @Param("pageSize") int pageSize);

}
