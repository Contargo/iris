package net.contargo.iris.address.staticsearch.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.notExists;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressFileService {

    private final Path location;

    public StaticAddressFileService(String directory) {

        location = Paths.get(directory);

        if (notExists(location)) {
            try {
                Files.createDirectories(location);
            } catch (IOException e) {
                throw new StaticAddressFileStorageException("Cannot create csv directory " + location, e);
            }
        }
    }

    public void saveFile(MultipartFile file) {

        try {
            Files.copy(file.getInputStream(), this.location.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StaticAddressFileStorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
}
