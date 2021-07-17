package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    @PostMapping("/register")
    public JSONObject registerAvatar(@RequestBody Map<String, String> params) {
        JSONObject ret = new JSONObject();
        try {
            int userId = Integer.parseInt(params.get("user_id"));
            String avatarUrl = params.get("avatar_url");
            avatarService.registerAvatar(userId, avatarUrl);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
