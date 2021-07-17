package flare.passion.repository;

import flare.passion.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, String> {

    Reply findById(int id);

    @Query(value = "select * from reply where ask_id = :postId and reply_id is null order by publish_date desc", nativeQuery = true)
    List<Reply> findDirectRepliesByPostId(@Param("postId") int postId);

    List<Reply> findAllByReplyId(int replyId);

    List<Reply> findAllByAskId(int askId);

}
