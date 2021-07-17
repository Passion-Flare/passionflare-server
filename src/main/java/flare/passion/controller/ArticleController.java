package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.article.CreateArticle;
import flare.passion.model.Passage;
import flare.passion.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @PostMapping("/create")
    public JSONObject createArticle(@RequestBody CreateArticle json) {
        JSONObject ret = new JSONObject();
        try {
            int id = json.getId();
            String title = json.getTitle();
            String thumbnail = json.getThumbnail();
            String content = json.getContent();
            String source = json.getSource();
            int type = json.getType();
            String rumorExtObj = json.getRumorExtObj();
            Passage passage = articleService.createArticle(id, source, thumbnail, title, type, content, rumorExtObj);
            ret.put("success", true);
            ret.put("id", passage.getId());
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/read")
    public JSONObject readArticle(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int id = Integer.parseInt(params.get("id"));
            JSONObject data = articleService.getAnArticle(id);
            ret.put("success", true);
            ret.put("data", data);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/delete")
    @Transactional
    public JSONObject deleteArticle(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int id = Integer.parseInt(params.get("id"));
            articleService.banAnArticle(id);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/official")
    @Transactional
    public JSONObject getOfficialAccounts() {
        JSONObject ret = new JSONObject();
        try {
            List<JSONObject> officialAccounts = articleService.getOfficialAccounts();
            ret.put("success", true);
            ret.put("data", officialAccounts);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
