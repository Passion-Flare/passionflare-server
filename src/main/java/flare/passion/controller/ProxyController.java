package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @GetMapping("/api/proxy")
    public JSONObject getNews(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            String url = params.get("url");
            Object data = proxyService.request(url);
            ret.put("success", true);
            ret.put("data", data);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
