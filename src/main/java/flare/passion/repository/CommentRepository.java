package flare.passion.repository;

import flare.passion.model.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Ask, String> {

    Ask findById(int id);

    @Query(value = "select * from ask where passage_id = :passageId order by publish_date desc", nativeQuery = true)
    List<Ask> findAllCommentsOfAPassage(@Param("passageId") int passageId);

}
