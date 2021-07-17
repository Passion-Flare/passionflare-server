package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.transformer.AddToCollection;
import flare.passion.model.Transformer;
import flare.passion.service.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transformer")
public class TransformerController {

    @Autowired
    private TransformerService transformerService;

    @PostMapping("/addToCollection")
    public JSONObject addATransformerToCollection(@RequestBody AddToCollection json) {
        JSONObject ret = new JSONObject();
        try {
            String name = json.getName();
            String type = json.getType();
            int transformerId = transformerService.addATransformerToCollection(name, type);
            ret.put("success", true);
            ret.put("transformerId", transformerId);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/getOne")
    public JSONObject addATransformerToCollection(@RequestParam Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            String id = params.get("id");
            Transformer transformer = transformerService.getATransformer(id);
            ret.put("success", true);
            ret.put("transformer", transformer);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
