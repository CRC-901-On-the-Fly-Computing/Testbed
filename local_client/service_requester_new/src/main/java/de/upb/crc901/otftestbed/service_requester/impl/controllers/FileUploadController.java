package de.upb.crc901.otftestbed.service_requester.impl.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.upb.crc901.otftestbed.service_requester.impl.components.DataHolderComponent;
import de.upb.crc901.otftestbed.service_requester.impl.storage.StorageFileNotFoundException;
import de.upb.crc901.otftestbed.service_requester.impl.storage.StorageService;

@Controller
public class FileUploadController {

	@Autowired
	DataHolderComponent holder;

	private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	private final StorageService storageService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/requests/{requestUUID}/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename,
			@PathVariable("requestUUID") UUID requestUUID) {
		Resource file = storageService.loadAsResource(filename, requestUUID);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/requests/{requestUUID}/upload")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
			@PathVariable("requestUUID") UUID requestUUID) {
		logger.debug("Storing {}", file.getOriginalFilename());
		storageService.store(file, requestUUID);
		logger.debug("Successfully uploaded file. Retrieving link...");
		StringBuilder builder = new StringBuilder("http://").append(request.getHeader("Host")).append("/api/")
				.append("requests/").append(requestUUID).append("/files/").append(file.getOriginalFilename());
		logger.debug("The file link is {} ", builder);
		return ResponseEntity.ok(builder.toString());
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<Void> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}