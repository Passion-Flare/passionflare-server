package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.apply.ManageApply;
import flare.passion.JSONmap.user.*;
import flare.passion.repository.ApplyRepository;
import flare.passion.service.ApplyService;
import flare.passion.service.NotificationService;
import flare.passion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apply")
public class ApplyController {
    @Autowired
    UserService userService;
    @Autowired
    ApplyService applyService;

    @PostMapping("/management")
    public JSONObject manageApply(@RequestBody ManageApply json) {
        JSONObject ret = new JSONObject();
        try {
            int id = json.getId();
            int applyId = json.getApplyId();
            int status = json.getStatus();
            applyService.changeStatus(id, applyId, status);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }
}

















