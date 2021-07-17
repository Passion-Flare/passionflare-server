package flare.passion.controller;

import com.alibaba.fastjson.JSONObject;
import flare.passion.service.AvatarService;
import flare.passion.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private AvatarService avatarService;

    @PostMapping("/upload")
    public JSONObject uploadFile(@RequestParam MultipartFile picture) {
        JSONObject ret = new JSONObject();
        try {
            String filePath = fileService.uploadFile(picture);
            ret.put("success", true);
            ret.put("path", filePath);
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

    @GetMapping("/sts")
    public JSONObject getCredential() {
        JSONObject ret = new JSONObject();
        try {
            ret.put("success", true);
            ret.put("data", avatarService.getCredential());
        } catch (Exception e) {
            ret.put("success", false);
            e.printStackTrace();
            ret.put("exc", e.getMessage());
        }
        return ret;
    }

}
