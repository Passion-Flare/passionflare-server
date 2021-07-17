package flare.passion.service;

import flare.passion.model.Apply;
import flare.passion.repository.ApplyRepository;
import flare.passion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    @Autowired
    ApplyRepository applyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    public void changeStatus(int id, int applyId, int status) throws Exception {
        int type = userService.getType(id);
        if (type == 3) {
            Apply apply = applyRepository.findById(applyId);
            apply.setStatus(status);
            applyRepository.save(apply);
            notificationService.sendApplyStatusChangeMsg(apply,status);
        } else {
            throw new Exception("此操作需要管理员权限");
        }
    }
}
