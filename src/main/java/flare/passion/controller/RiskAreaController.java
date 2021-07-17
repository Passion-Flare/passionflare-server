package flare.passion.controller;


import com.alibaba.fastjson.JSONObject;
import flare.passion.service.RiskAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/query")
public class RiskAreaController {
    @Autowired
    RiskAreaService riskAreaService;


    @GetMapping("risk-area")
    public JSONObject getRiskArea() {
        JSONObject ret = new JSONObject();
        try {
            List<JSONObject> data = riskAreaService.getRiskAreaDataFromDxy_v2();
            ret.put("data", data);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }


}
