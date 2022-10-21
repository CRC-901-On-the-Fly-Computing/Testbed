package de.upb.crc901.otftestbed.verificator.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.commons.flow.ExtendedFlow;
import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.otftestbed.verificator.generated.spring_server.api.CompositionAnalysisApiDelegate;
import de.upb.crc901.sse.analyze.controlflowVerification.ControlflowVerificator;
import de.upb.crc901.sse.analyze.controlflowVerification.IControlflowVerificator;
import de.upb.crc901.sse.analyze.datamodel.error.IErrorReport;

@Component
public class CompositionAnalysisControllerDelegate implements CompositionAnalysisApiDelegate {

	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(CompositionAnalysisControllerDelegate.class);

//	@Autowired
//	private TelemetryService telemetry;

	private void logAnalysis(long startTime, UUID requestId, boolean error) {
//		telemetry.logAnalysisComposition(startTime, System.currentTimeMillis() - startTime, requestId.toString(), 1L,
//				(error ? 1L : 0L));
	}


	/**
	 * Evaluates the given extended flow. Downloads ontology files if needed. May
	 * return 500 if any file couldn't be loaded. Returns 200 if flow could be
	 * evaluated. Status code 200 does <b>not</b> mean that the flow is correct.
	 * 
	 * @param body The flow which should be evaluated.
	 * @return Response depending on the evaluation result. 500 if any error occurs
	 *         when loading files. 200 if verification worked. Body contains
	 *         true/false if no verification errors occurred. Contains a string with
	 *         the error code if an error is thrown.
	 */
	@Override
	public ResponseEntity<Object> postFlow(ExtendedFlow body) {
		log.debug("Received flow object.");

		if ((body == null) || (body.getFlow() == null)) {
			log.warn("Null object received as flow or no flow contained in extended flow object.");
			return new ResponseEntity<>("Flow wrong", HttpStatus.BAD_REQUEST);
		}

		List<File> ontologyFiles = new ArrayList<>();
		List<File> ontologyRuleFiles = new ArrayList<>();

		for (String urlString : body.getOntologies()) {
			URL url;
			try {
				url = new URL(urlString);
				// FIXME: Change this back
				// if (flow.isRefreshOntologies()) {
				File file = downloadFile(urlString, FilenameUtils.getName(url.getPath()));
				// }
				// File file = new File(FilenameUtils.getName(url.getPath()));
				ontologyFiles.add(file);
			} catch (MalformedURLException e) {
				log.error("Couldn't load ontology {}", urlString, e);
				StringWriter out = new StringWriter();
				PrintWriter writer = new PrintWriter(out);
				e.printStackTrace(writer);
				writer.flush();
				deleteFiles(ontologyFiles);
				deleteFiles(ontologyRuleFiles);
				return new ResponseEntity<>("URL error." + out.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}

		for (String urlString : body.getOntologieRules()) {
			URL url;
			try {
				url = new URL(urlString);
				// FIXME: change this back!
				// if (flow.isRefreshOntologieRules()) {
				File file = downloadFile(urlString, FilenameUtils.getName(url.getPath()));
				// }
				// File file = new File(FilenameUtils.getName(url.getPath()));
				ontologyRuleFiles.add(file);
			} catch (MalformedURLException e) {
				log.error("Couldn't load ontology {}", urlString, e);
				StringWriter out = new StringWriter();
				PrintWriter writer = new PrintWriter(out);
				e.printStackTrace(writer);
				writer.flush();
				deleteFiles(ontologyFiles);
				deleteFiles(ontologyRuleFiles);
				return new ResponseEntity<>("URL error." + out.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}

		long startTime = System.currentTimeMillis();

		IControlflowVerificator verificator = new ControlflowVerificator();
		IErrorReport errorReport;
		try {
			errorReport = verificator.verifyForLinearFlows(body.getFlow(), body.getServiceSpecifications(),
					ontologyFiles, ontologyRuleFiles);
		} catch (Exception e) {
			log.error("Verification failed.", e);
			// FIXME: this is always thrown when an error occurred, (wrong configurations
			// included)
			// We should fix this and check how we can do this better.
			StringWriter out = new StringWriter();
			PrintWriter writer = new PrintWriter(out);
			e.printStackTrace(writer);
			writer.flush();

			logAnalysis(startTime, body.getUuid(), true);

			deleteFiles(ontologyFiles);
			deleteFiles(ontologyRuleFiles);
			return new ResponseEntity<>("Verification failed. Error: " + e.getMessage() + "\n" + out.toString(), HttpStatus.OK);
		}

		boolean error = false;
		ResponseEntity<Object> response;
		if (errorReport.isSuccessfullVerification()) {
			response = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
		} else {
			error = true;
			response = new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
		}

		logAnalysis(startTime, body.getUuid(), error);

		deleteFiles(ontologyFiles);
		deleteFiles(ontologyRuleFiles);

		return response;
	}

	private File downloadFile(String urlString, String fileName) {
		URL url;
		FileSystem fs = FileSystems.getDefault();
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			log.error("Invalid url {}.", urlString, e);
			return null;
		}
		try {
			UUID uuid = UUID.randomUUID();
			java.nio.file.Path path = fs.getPath(fileName + "_" + uuid.toString());
			Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
			return path.toFile();
		} catch (IOException e) {
			log.error("Couldn't download file from {} to {}", url, fileName, e);
			return null;
		}

	}

  private void deleteFiles(List<File> files) {
    for (File file : files) {
      //TODO: check deleted
      boolean deleted = file.delete();
      if (!deleted) {
        log.warn("Couldn't delete file {}.", file.getPath());
      }
    }
  }
}
