package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.RumorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class RumorController {

    @Autowired
    private RumorService rumorService;

    @GetMapping("/rumors")
    public JSONObject getRumors(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int pageNo = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("size"));
            String keyword = params.get("keyword");
            List<JSONObject> rumorDtoList = rumorService.getRumors(pageNo, pageSize, keyword);
            ret.put("success", true);
            ret.put("data", rumorDtoList);
            ret.put("totalPage", rumorService.getRumorsPageCount(pageSize, keyword));
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/fetchRumors")
    public JSONObject updateRumorRecords() {
        JSONObject ret = new JSONObject();
        try {
            rumorService.getRumorsFromWebsiteBelow();
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
