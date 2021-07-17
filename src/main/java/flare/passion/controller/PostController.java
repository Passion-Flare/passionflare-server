package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.post.NewPost;
import flare.passion.JSONmap.post.ReplyToPost;
import flare.passion.JSONmap.post.ReplyToReply;
import flare.passion.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/one")
    public JSONObject newPost(@RequestBody NewPost newPost) {
        JSONObject ret = new JSONObject();
        try {
            String title = newPost.getTitle();
            String content = newPost.getContent();
            int userId = newPost.getUserId();
            if (title == null || content == null || String.valueOf(userId).equals("null")) {
                throw new Exception("NO");
            }
            int postId = postService.publishAPost(title, content, userId);
            ret.put("success", true);
            ret.put("id", postId);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/replyToPost")
    public JSONObject replyToPost(@RequestBody ReplyToPost replyToPost) {
        JSONObject ret = new JSONObject();
        try {
            String content = replyToPost.getContent();
            int userId = replyToPost.getUserId();
            int postId = replyToPost.getAskId();
            if (content == null || String.valueOf(userId).equals("null") || String.valueOf(postId).equals("null")) {
                throw new Exception("NO");
            }
            int replyId = postService.replyToPost(content, userId, postId);
            ret.put("success", true);
            ret.put("id", replyId);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/replyToReply")
    public JSONObject replyToReply(@RequestBody ReplyToReply replyToReply) {
        JSONObject ret = new JSONObject();
        try {
            String content = replyToReply.getContent();
            int userId = replyToReply.getUserId();
            int repliedId = replyToReply.getReplyId();
            if (content == null || String.valueOf(userId).equals("null") || String.valueOf(repliedId).equals("null")) {
                throw new Exception("NO");
            }
            int replyId = postService.replyToReply(content, userId, repliedId);
            ret.put("success", true);
            ret.put("id", replyId);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/list")
    public JSONObject getPostList() {
        JSONObject ret = new JSONObject();
        try {
            List<JSONObject> posts = postService.getPosts();
            ret.put("success", true);
            ret.put("data", posts);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/one")
    public JSONObject getAPost(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int postId = Integer.parseInt(params.get("id"));
            JSONObject post = postService.getAPost(postId);
            ret.put("success", true);
            ret.put("data", post);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
