package flare.passion.repository;

import flare.passion.model.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Ask, String> {

    Ask findById(int id);

    @Query(value = "select * from ask a left join reply r " +
            "on a.latest_reply_id = r.id where a.passage_id is NULL " +
            "order by (select ifnull(r.publish_date, a.publish_date) as publish_date) desc;", nativeQuery = true)
    List<Ask> findAllPosts();

}
