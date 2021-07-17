package flare.passion.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/query")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @GetMapping("/risk-airline")
    public JSONObject getAirline(){
        JSONObject ret=new JSONObject();
        JSONArray airline;
        try {
            ret.put("success",true);
            airline=transportService.getFlightInfo();
            ret.put("airline",airline);
        }catch (Exception e){
            ret.put("success",false);
            ret.put("exc",e.getMessage());
        }
        return ret;
    }

    @GetMapping("/risk-train")
    public JSONObject getTrain(){
        JSONObject ret=new JSONObject();
        try {
            ret.put("success",true);
            JSONArray train=transportService.getTrainInfo();
            ret.put("airline",train);
        }catch (Exception e){
            ret.put("success",false);
            ret.put("exc",e.getMessage());
        }
        return ret;
    }
}
