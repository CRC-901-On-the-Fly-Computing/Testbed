package de.upb.crc901.otftestbed.service_requester.impl.storage;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    void store(MultipartFile file, UUID reuqestUUID);

    Path load(String filename, UUID requestUUID);

    Resource loadAsResource(String filename, UUID requestUUID);

    void deleteAll();

}