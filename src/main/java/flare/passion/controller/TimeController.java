package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping("/time")
    public JSONObject getTime() {
        JSONObject ret = new JSONObject();
        JSONObject UTCTime = timeService.getTime();
        ret.put("UTCTime", UTCTime);
        return ret;
    }

}
