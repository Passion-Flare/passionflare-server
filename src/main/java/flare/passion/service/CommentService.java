package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Ask;
import flare.passion.model.Reply;
import flare.passion.repository.ArticleRepository;
import flare.passion.repository.CommentRepository;
import flare.passion.repository.ReplyRepository;
import flare.passion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(rollbackFor = Exception.class)
    public int publishAComment(String content, int userId, int passageId) {
        Ask ask = new Ask();
        ask.setContent(content);
        ask.setUser(userRepository.findById(userId));
        ask.setPassage(articleRepository.findById(passageId));
        ask.setPublishDate(new Date());
        commentRepository.save(ask);
        return ask.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public int replyToComment(String content, int userId, int commentId) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setUser(userRepository.findById(userId));
        reply.setAsk(commentRepository.findById(commentId));
        reply.setPublishDate(new Date());
        replyRepository.save(reply);
        return reply.getId();
    }

    public List<JSONObject> getComments(int passageId) {
        List<JSONObject> commentsDto = new LinkedList<>();

        // all comments under a passage
        List<Ask> questions = commentRepository.findAllCommentsOfAPassage(passageId);
        for (Ask question : questions) {
            JSONObject questionDto = new JSONObject();
            questionDto.put("id", question.getId());
            questionDto.put("authorName", question.getUser().getName());
            questionDto.put("authorAvatar", question.getUser().getAvatar());
            questionDto.put("publishDate", question.getPublishDate());
            questionDto.put("content", question.getContent());

            // all replies to this comment
            List<Reply> replies = replyRepository.findAllByAskId(question.getId());
            List<JSONObject> repliesDto = new LinkedList<>();
            for (Reply reply : replies) {
                JSONObject replyDto = new JSONObject();
                replyDto.put("id", reply.getId());
                replyDto.put("authorName", reply.getUser().getName());
                replyDto.put("authorAvatar", reply.getUser().getAvatar());
                replyDto.put("publishDate", reply.getPublishDate());
                replyDto.put("content", reply.getContent());
                replyDto.put("replyTo", reply.getAsk().getUser().getName());
                repliesDto.add(replyDto);
            }
            questionDto.put("children", repliesDto);

            commentsDto.add(questionDto);
        }

        return commentsDto;
    }

}
