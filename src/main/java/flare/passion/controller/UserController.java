package flare.passion.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import flare.passion.JSONmap.user.*;
import flare.passion.model.Apply;
import flare.passion.model.User;
import flare.passion.repository.ApplyRepository;
import flare.passion.repository.UserRepository;
import flare.passion.service.ApplyService;
import flare.passion.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public JSONObject addUserByNickname(@RequestBody Register json) {
        JSONObject ret = new JSONObject();
        try {
            String email = json.getEmail();
            String username = json.getUsername();
            String password = json.getPassword();
            userService.register(email, username, password);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/login")
    public JSONObject login(@RequestBody Login json) {
        JSONObject ret = new JSONObject();
        try {
            String email = json.getEmail();
            String password = json.getPassword();
            User user = userService.login(email, password);
            String token = userService.getToken(email);
            List<Apply> applyList = applyRepository.findByUserOrderByIdDesc(user);
            ret.put("success", true);
            ret.put("email", email);
            JSONObject data = new JSONObject();
            data.put("token", token);
            data.put("id", user.getId());
            data.put("username", user.getName());
            data.put("email", user.getEmail());
            data.put("isManager", user.getType() == 3);
            data.put("avatar", user.getAvatar());
            int applyStatus = -1;
            if (applyList.size() > 0) {
                applyStatus = applyList.get(0).getStatus();
            }
            data.put("applyStatus", applyStatus);
            ret.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }


    @PostMapping("/reset/password")
    public JSONObject resetPassword(@RequestBody ResetPassword json) {
        JSONObject ret = new JSONObject();
        try {
            String email = json.getEmail();
            String oldPassword = json.getOldPassword();
            String newPassword = json.getNewPassword();
            userService.resetPassword(email, oldPassword, newPassword);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/reset/emandun")
    public JSONObject resetEmAndUn(@RequestBody ResetEmailAndUsername json) {
        JSONObject ret = new JSONObject();
        try {
            int id = json.getId();
            String email = json.getEmail();
            String username = json.getUsername();
            userService.resetEmailAndUsername(id, email, username);
            ret.put("success", true);
            JSONObject data = new JSONObject();
            String token = userService.getToken(email);
            ret.put("data", data);
            data.put("newEmail", email);
            data.put("newUsername", username);
            data.put("newToken", token);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/certification")
    public JSONObject applyForCertification(@RequestBody ApplyForCertification json) {
        JSONObject ret = new JSONObject();
        try {
            int id = json.getId();
            String reason = json.getReason();
            String contact = json.getContact();
            String organization = json.getOrganization();
            userService.createApply(id, reason, contact, organization);
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("exc", e.getMessage());
        }
        return ret;
    }
}
