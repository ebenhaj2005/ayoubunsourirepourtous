package be.ehb.ayoubunsourirepourtous.site.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");
    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload-dir:uploads}") String uploadDir) throws IOException {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDir);
    }

    /**
     * Sauvegarde un fichier et retourne le nom du fichier stocké (ex: uuid.jpg)
     */
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";

        int dot = original.lastIndexOf('.');
        if (dot >= 0 && dot < original.length() - 1) {
            ext = original.substring(dot); // .jpg
        }

        String storedName = UUID.randomUUID() + ext;
        Path target = uploadDir.resolve(storedName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return storedName;
    }
    public void delete(String filename) {
        try {
            java.nio.file.Path file = rootLocation.resolve(filename);
            java.nio.file.Files.deleteIfExists(file);
        } catch (Exception e) {
            // option : log
        }
    }

    public String save(MultipartFile file) throws IOException {
        return store(file);
    }

    public void deleteIfExists(String filename) throws IOException {
        if (filename == null || filename.isBlank()) return;
        Files.deleteIfExists(uploadDir.resolve(filename));
    }
}
