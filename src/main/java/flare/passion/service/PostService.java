package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import flare.passion.model.Ask;
import flare.passion.model.Reply;
import flare.passion.repository.PostRepository;
import flare.passion.repository.ReplyRepository;
import flare.passion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Transactional(rollbackFor = Exception.class)
    public int publishAPost(String title, String content, int userId) {
        Ask ask = new Ask();
        ask.setTitle(title);
        ask.setContent(content);
        ask.setUser(userRepository.findById(userId));
        ask.setPublishDate(new Date());
        postRepository.save(ask);
        return ask.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public int replyToPost(String content, int userId, int postId) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setUser(userRepository.findById(userId));
        reply.setAsk(postRepository.findById(postId));
        reply.setPublishDate(new Date());
        replyRepository.save(reply);

        Ask ask = postRepository.findById(postId);
        ask.setLatestReply(reply);
        postRepository.save(ask);

        return reply.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public int replyToReply(String content, int userId, int repliedId) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setUser(userRepository.findById(userId));
        reply.setReply(replyRepository.findById(repliedId));
        reply.setAsk(replyRepository.findById(repliedId).getAsk());
        reply.setPublishDate(new Date());
        replyRepository.save(reply);

        Reply repliedReply = replyRepository.findById(repliedId);
        Ask ask = repliedReply.getAsk();
        ask.setLatestReply(reply);
        postRepository.save(ask);

        return reply.getId();
    }

    public List<JSONObject> getPosts() {
        List<JSONObject> postsDto = new LinkedList<>();
        List<Ask> posts = postRepository.findAllPosts();
        for (Ask post : posts) {
            JSONObject postDto = new JSONObject();
            postDto.put("id", post.getId());
            postDto.put("title", post.getTitle());
            postDto.put("authorName", post.getUser().getName());
            postDto.put("authorAvatar", post.getUser().getAvatar());
            postDto.put("publishDate", post.getPublishDate());

            Reply latestLevel1Reply = post.getLatestReply();
            if (latestLevel1Reply != null) {
                JSONObject latestReply = new JSONObject();
                latestReply.put("authorName", latestLevel1Reply.getUser().getName());
                latestReply.put("authorAvatar", latestLevel1Reply.getUser().getAvatar());
                latestReply.put("publishDate", latestLevel1Reply.getPublishDate());
                postDto.put("latestReply", latestReply);
            } else {
                postDto.put("latestReply", null);
            }

            postsDto.add(postDto);
        }
        return postsDto;
    }

    private void fillSimpleReplyDto(JSONObject replyDto, Reply reply) {
        replyDto.put("id", reply.getId());
        replyDto.put("authorName", reply.getUser().getName());
        replyDto.put("isOfficial", reply.getUser().getType() == 2);
        replyDto.put("authorAvatar", reply.getUser().getAvatar());
        replyDto.put("publishDate", reply.getPublishDate());
        replyDto.put("content", reply.getContent());
    }

    private JSONObject fillDirectReplyDto(Reply reply) {
        JSONObject replyDto = new JSONObject();
        fillSimpleReplyDto(replyDto, reply);

        List<JSONObject> subRepliesDto = new LinkedList<>();
        // child replies
        List<Reply> subReplies = replyRepository.findAllByReplyId(reply.getId());
        for (Reply subReply : subReplies) {
            JSONObject subReplyDto = new JSONObject();
            fillSimpleReplyDto(subReplyDto, subReply);
            subReplyDto.put("replyTo", subReply.getReply().getUser().getName());
            subRepliesDto.add(subReplyDto);
        }
        replyDto.put("children", subRepliesDto);
        return replyDto;
    }

    public JSONObject getAPost(int postId) {
        JSONObject postDto = new JSONObject();
        Ask post = postRepository.findById(postId);
        postDto.put("id", post.getId());
        postDto.put("title", post.getTitle());
        postDto.put("authorName", post.getUser().getName());
        postDto.put("authorAvatar", post.getUser().getAvatar());
        postDto.put("publishDate", post.getPublishDate());
        postDto.put("content", post.getContent());

        List<JSONObject> officialRepliesDto = new LinkedList<>();
        List<JSONObject> commonRepliesDto = new LinkedList<>();
        // direct replies
        List<Reply> directReplies = replyRepository.findDirectRepliesByPostId(postId);
        for (Reply reply : directReplies) {
            JSONObject replyDto = fillDirectReplyDto(reply);
            if (reply.getUser().getType() == 2) {
                officialRepliesDto.add(replyDto);
            } else {
                commonRepliesDto.add(replyDto);
            }
        }
        List<JSONObject> replies = Stream.concat(
                officialRepliesDto.stream(),
                commonRepliesDto.stream())
                .collect(Collectors.toList());
        postDto.put("replies", replies);

        return postDto;
    }

}
