package net.contargo.iris.address.staticsearch.upload.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.lang.invoke.MethodHandles;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.notExists;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
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

    public InputStream read(String filename) {

        Path path = location.resolve(filename);

        LOG.debug("Reading file {}", path);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new StaticAddressFileStorageException("Failed to read file " + path, e);
        }
    }


    public void saveFile(MultipartFile file) {

        String filename = file.getOriginalFilename();
        Path destination = location.resolve(filename);

        LOG.debug("Saving file {} to {}", filename, destination);

        try {
            Files.copy(file.getInputStream(), destination);
        } catch (IOException e) {
            throw new StaticAddressFileStorageException("Failed to store file " + filename, e);
        }
    }


    public void delete(String file) {

        Path destination = location.resolve(file);

        LOG.debug("Deleting file {}", destination);

        try {
            Files.delete(destination);
        } catch (IOException e) {
            LOG.error("Failed to delete file", e);
        }
    }
}
