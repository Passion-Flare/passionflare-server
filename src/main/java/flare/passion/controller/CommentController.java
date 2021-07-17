package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.comment.NewComment;
import flare.passion.JSONmap.comment.ReplyToComment;
import flare.passion.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/one")
    public JSONObject newComment(@RequestBody NewComment newComment) {
        JSONObject ret = new JSONObject();
        try {
            String content = newComment.getContent();
            int userId = newComment.getUserId();
            int passageId = newComment.getPassageId();
            if (content == null || String.valueOf(userId).equals("null") || String.valueOf(passageId).equals("null")) {
                throw new Exception("NO");
            }
            int commentId = commentService.publishAComment(content, userId, passageId);
            ret.put("success", true);
            ret.put("id", commentId);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/replyToComment")
    public JSONObject replyToComment(@RequestBody ReplyToComment replyToComment) {
        JSONObject ret = new JSONObject();
        try {
            String content = replyToComment.getContent();
            int userId = replyToComment.getUserId();
            int commentId = replyToComment.getCommentId();
            if (content == null || String.valueOf(userId).equals("null") || String.valueOf(commentId).equals("null")) {
                throw new Exception("NO");
            }
            int replyId = commentService.replyToComment(content, userId, commentId);
            ret.put("success", true);
            ret.put("id", replyId);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/ofAnArticle")
    public JSONObject getCommentList(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int passageId = Integer.parseInt(params.get("id"));
            List<JSONObject> comments = commentService.getComments(passageId);
            ret.put("success", true);
            ret.put("data", comments);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
