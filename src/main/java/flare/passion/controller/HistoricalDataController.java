package flare.passion.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.service.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/historicaldata")
public class HistoricalDataController {

    @Autowired
    private HistoricalDataService historicalDataService;

    @GetMapping("/week")
    public JSONObject getWeeklyHistoricalData(@RequestParam int n) {
        JSONObject ret = new JSONObject();
        try {
            JSONArray jsonArray = historicalDataService.getHistoricalDataByWeek(n);
            ret.put("success", true);
            ret.put("data", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/create")
    public void getWeeklyHistoricalData() {
        try {
            historicalDataService.saveHistoricalDataToLocal();
            historicalDataService.saveHistoricalDataByWeek();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
