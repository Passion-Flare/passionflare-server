package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @GetMapping("/knowledge")
    public JSONObject getKnowledge(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int pageNo = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("size"));
            String keyword = params.get("keyword");
            List<JSONObject> knowledgeDtoList = knowledgeService.getKnowledge(pageNo, pageSize, keyword);
            ret.put("success", true);
            ret.put("data", knowledgeDtoList);
            ret.put("totalPage", knowledgeService.getKnowledgePageCount(pageSize, keyword));
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/fetchKnowledge")
    public JSONObject getKnowledgeFromQQKnowledge() {
        JSONObject ret = new JSONObject();
        try {
            knowledgeService.getKnowledgeFromWHO();
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
