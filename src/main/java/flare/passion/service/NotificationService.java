package flare.passion.service;

import flare.passion.model.Apply;
import flare.passion.model.Notification;
import flare.passion.model.User;
import flare.passion.repository.ApplyRepository;
import flare.passion.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public void sendApplyStatusChangeMsg(Apply apply, int status) throws Exception {
        User user = apply.getUser();
        if (user != null) {
            String content = "您的认证申请（" + apply.getId() + "）";
            if (status == 1) {
                content = content + "已通过";
            } else if (status == 0) {
                content = content + "未通过，请重新填写申请表单或联系客服";
            }
            Notification notification = new Notification(content, 3, user);
            notificationRepository.save(notification);
        } else {
            throw new Exception("找不到申请人");
        }
    }


}
