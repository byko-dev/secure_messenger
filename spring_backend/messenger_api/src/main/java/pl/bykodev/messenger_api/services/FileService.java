package pl.bykodev.messenger_api.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.FileEntity;
import java.io.IOException;
import java.util.Optional;

@Service
public interface FileService {

    FileEntity save(MultipartFile file) throws IOException;

    Optional<FileEntity> getFile(String id);
}
