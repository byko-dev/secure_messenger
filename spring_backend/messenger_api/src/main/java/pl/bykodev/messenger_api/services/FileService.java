package pl.bykodev.messenger_api.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.FileEntity;
import pl.bykodev.messenger_api.database.repository.FileEntityRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileService {

    private FileEntityRepository fileRepository;

    public FileService(FileEntityRepository fileRepository)
    {
        this.fileRepository = fileRepository;
    }

    public FileEntity save(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setSize(file.getSize());
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setData(file.getBytes());
        fileEntity.setContentType(file.getContentType());

        return fileRepository.save(fileEntity);
    }

    public Optional<FileEntity> getFile(String id){
        return fileRepository.findById(id);
    }
}
