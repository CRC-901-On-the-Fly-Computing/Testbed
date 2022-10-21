package de.upb.crc901.otftestbed.service_requester.impl.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.servlet.MultipartConfigElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

@Service
public class FileSystemStorageService implements StorageService {

  Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipart = new CommonsMultipartResolver();
    multipart.setMaxUploadSize(100 * 1024 * 1024);
    return multipart;
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    return new MultipartConfigElement("");
  }

  @Bean
  @Order(0)
  public MultipartFilter multipartFilter() {
    MultipartFilter multipartFilter = new MultipartFilter();
    multipartFilter.setMultipartResolverBeanName("multipartReso‌​lver");
    return multipartFilter;
  }

  private final Path rootLocation;

  private Path resolveUUID(UUID requestUUID) {
    return rootLocation.resolve(requestUUID.toString());
  }

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  public void store(MultipartFile file, UUID requestUUID) {
    logger.debug("Storing {}", file.getOriginalFilename());
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      Files.createDirectories(resolveUUID(requestUUID));
      logger.debug("Checking if file is empty...");
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // This is a security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory " + filename);
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, resolveUUID(requestUUID).resolve(filename),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }

  @Override
  public Path load(String filename, UUID path) {
    return resolveUUID(path).resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename, UUID requestUUID) {
    try {
      Path file = load(filename, requestUUID);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException("Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }
}
