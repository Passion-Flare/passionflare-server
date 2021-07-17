package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.ChinaCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChinaCityController {
    @Autowired
    ChinaCityService chinaCityService;

    @GetMapping("/chinaCity")
    public JSONObject getChinaCityDiseaseData(){
        JSONObject ret = new JSONObject();
        try{
            JSONObject data = chinaCityService.getChinaCityDataFrom163();
            ret.put("success",true);
            ret.put("data",data);
        }catch (Exception e){
            ret.put("success",false);
            ret.put("exc",e.getMessage());
        }
        return ret;
    }
}
