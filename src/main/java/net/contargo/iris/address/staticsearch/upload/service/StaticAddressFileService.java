package net.contargo.iris.address.staticsearch.upload.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.notExists;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressFileService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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


    public void delete(String file) {

        try {
            Files.delete(this.location.resolve(file));
        } catch (IOException e) {
            LOG.error("Failed to delete file", e);
        }
    }
}
