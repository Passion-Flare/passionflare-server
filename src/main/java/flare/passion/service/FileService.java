package flare.passion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileService {

    @Value("${files.path}")
    private String filePath;

    public String uploadFile(MultipartFile file) throws Exception {
        String newFileName = "";
        String originalFilename = file.getOriginalFilename();
        String[] allowedExtensionNames = {"jpg", "jpeg", "png", "gif"};
        if (originalFilename != null) {
            String extensionName = originalFilename.substring(originalFilename.lastIndexOf(".")).substring(1);
            if (Arrays.asList(allowedExtensionNames).contains(extensionName)) {
                newFileName = UUID.randomUUID() + "." + extensionName;
            } else {
                throw new Exception("not-allowed extension name");
            }
        } else {
            throw new Exception("didn't get the original file name");
        }
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(filePath + "/pictures/" + newFileName));

        return ("/pictures/" + newFileName);
    }

}
