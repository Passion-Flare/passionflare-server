package flare.passion.service;

import com.alibaba.fastjson.JSONObject;
import com.tencent.cloud.CosStsClient;
import flare.passion.model.User;
import flare.passion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class AvatarService {

    @Autowired
    private UserRepository userRepository;

    public JSONObject getCredential() {
        TreeMap<String, Object> config = new TreeMap<>();

        try {
            // 云 api 密钥 SecretId
            config.put("secretId", "");
            // 云 api 密钥 SecretKey
            config.put("secretKey", "");

            // 临时密钥有效时长，单位是秒
            config.put("durationSeconds", 120);

            // 换成你的 bucket
            config.put("bucket", "heaven-1305422781");
            // 换成 bucket 所在地区
            config.put("region", "ap-chengdu");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径，例子： a.jpg 或者 a/* 或者 * (使用通配符*存在重大安全风险, 请谨慎评估使用)
            config.put("allowPrefix", "*");

            // 密钥的权限列表。简单上传和分片需要以下的权限，其他权限列表请看 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);

            org.json.JSONObject credential = CosStsClient.getCredential(config);
//            System.out.println(credential.toString(4));
            org.json.JSONObject credentials = credential.getJSONObject("credentials");

            JSONObject credentialsToReturn = new JSONObject();
            credentialsToReturn.put("tmpSecretId", credentials.get("tmpSecretId"));
            credentialsToReturn.put("tmpSecretKey", credentials.get("tmpSecretKey"));
            credentialsToReturn.put("sessionToken", credentials.get("sessionToken"));
            JSONObject credentialToReturn = new JSONObject();
            credentialToReturn.put("credentials", credentialsToReturn);
            credentialToReturn.put("requestId", credential.get("requestId"));
            credentialToReturn.put("expiration", credential.get("expiration"));
            credentialToReturn.put("startTime", credential.get("startTime"));
            credentialToReturn.put("expiredTime", credential.get("expiredTime"));
            return credentialToReturn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret!");
        }
    }

    public void registerAvatar(int userId, String avatarUrl) {
        User user = userRepository.findById(userId);
        user.setAvatar(avatarUrl);
        userRepository.save(user);
    }

}
