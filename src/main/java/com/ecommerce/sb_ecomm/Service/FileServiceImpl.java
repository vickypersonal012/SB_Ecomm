package com.ecommerce.sb_ecomm.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // 1. Get the original filename (e.g., "my_photo.jpeg")
        String originalFileName = file.getOriginalFilename();

        // 2. Generate a unique ID (UUID)
        String randomId = UUID.randomUUID().toString(); // This generates a string like "d4f3ebc3-5664-41cf-9fa1-b16b4cd5c19a"

        // 3. Extract the file extension from the original filename
        //    Handle cases where there's no extension or originalFileName is null
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // e.g., ".jpeg"
        }
        // If originalFileName is "image" and no extension, fileExtension remains ""

        // 4. Combine the UUID, and the original file extension to form the new, unique filename
        //    The UUID is the prefix.
        String newFileName = randomId + fileExtension; // Example: "d4f3ebc3-5664-41cf-9fa1-b16b4cd5c19a.jpeg"

        // 5. Construct the full path to the target directory
        Path targetDirectory = Paths.get(path);

        // 6. Ensure the directory exists, creating it if necessary
        if (!Files.exists(targetDirectory)) {
            Files.createDirectories(targetDirectory);
        }

        // 7. Construct the full file path including the directory and generated filename
        Path fullFilePath = targetDirectory.resolve(newFileName); // Use newFileName here

        // 8. Copy the file to the target location
        Files.copy(file.getInputStream(), fullFilePath);

        // 9. Return the generated unique filename
        return newFileName;
    }
}
