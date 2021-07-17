package flare.passion.service;

import flare.passion.model.Apply;
import flare.passion.model.User;
import flare.passion.repository.ApplyRepository;
import flare.passion.repository.UserRepository;
import flare.passion.util.BCryptUtil;
import flare.passion.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BCryptUtil bCryptUtil;

    public void register(String email, String nickname, String password) throws Exception {
        if (userRepository.findByEmail(email) != null) {
            throw new Exception("邮件已被占用");
        } else {
            User user = new User(email, nickname, bCryptUtil.hashPassword(password));
            userRepository.save(user);
        }
    }

    public User login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (bCryptUtil.checkPassword(password, user.getPassword())) {
            } else {
                throw new Exception("用户名密码错误");
            }
        }
        return user;
    }

    public void resetPassword(String email, String oldPassword, String newPassword) throws Exception {
        try {
            login(email, oldPassword);
        } catch (Exception e) {
            throw new Exception("用户名与密码不匹配");
        }
        User user = userRepository.findByEmail(email);
        user.setPassword(bCryptUtil.hashPassword(newPassword));
        userRepository.save(user);
    }


    public void resetEmailAndUsername(int id, String email, String username) throws Exception {
        User user = userRepository.findById(id);
        if (user != null) {
            String oldEmail = user.getEmail();
            User tryUser = userRepository.findByEmail(email);
            if (!oldEmail.equals(email) && (tryUser != null)) {
                throw new Exception("邮箱已使用");
            }
            user.setEmail(email);
            user.setName(username);
            userRepository.save(user);
        } else {
            throw new Exception("未找到id为 " + id + " 的用户");
        }
    }

    public void createApply(int id, String reason, String contact, String organization) throws Exception {
        User user = userRepository.findById(id);
        if (user != null) {
            if(user.getType()==2){
                throw new Exception("您已经是认证用户，不得申请");
            }
            if (applyRepository.existsByUserIdAndStatus(id, 2)) {
                throw new Exception("您之前发过申请，请耐心等待处理！");
            }
            Apply apply = new Apply(reason, contact, organization, user);
            applyRepository.save(apply);
        } else {
            throw new Exception("未找到id为 " + id + " 的用户");
        }
    }

    public int getType(int id) throws Exception {
        User user = userRepository.findById(id);
        if (user != null) {
            return user.getType();
        } else {
            throw new Exception("未找到id为 " + id + " 的用户");
        }
    }

    public String getToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return jwtTokenUtil.getToken(user);
        } else {
            return null;
        }
    }
}
