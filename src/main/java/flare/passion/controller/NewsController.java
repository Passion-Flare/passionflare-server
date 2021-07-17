package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public JSONObject getNews(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int pageNo = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("size"));
            String keyword = params.get("keyword");
            List<JSONObject> newsDtoList = newsService.getNews(pageNo, pageSize, keyword);
            ret.put("success", true);
            ret.put("data", newsDtoList);
            ret.put("totalPage", newsService.getNewsPageCount(pageSize, keyword));
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/fetchNews")
    public JSONObject getNewsFromQQNews() {
        JSONObject ret = new JSONObject();
        try {
            newsService.getNewsFromQQNews();
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/announcement")
    public JSONObject getAnnouncement(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int pageNo = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("size"));
            List<JSONObject> list = newsService.getAnnouncement(pageNo, pageSize);
            ret.put("success", true);
            ret.put("data", list);
            ret.put("totalPage", newsService.getAnnouncementPageCount(pageSize));
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
