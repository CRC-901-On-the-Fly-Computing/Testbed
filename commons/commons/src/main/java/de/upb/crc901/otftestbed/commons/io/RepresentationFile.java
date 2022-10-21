package de.upb.crc901.otftestbed.commons.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.converter.JSONConverter;

public class RepresentationFile {

	private static final Logger log = LoggerFactory.getLogger(RepresentationFile.class);


	/**
	 * Reads the entire file as a single {@link Representation} and converts it to type {@code T} with the given
	 * function.
	 *
	 * @param fileName of the file to be read
	 * @param func converter function
	 * @param <T> type to convert to
	 * @return converted representation read from file
	 */
	public static <T> T readFile(String fileName, Function<Representation, T> func) {
		log.debug("Reading representation '{}'", fileName);

		// opening resource file
		InputStream is = RepresentationFile.class.getClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			log.error("Error opening '{}'", fileName);
			return null;
		}

		try (Scanner scanner = new Scanner(is)) {

			// read entire file
			scanner.useDelimiter("\\Z");
			String marshalledObject = scanner.next();

			// convert it to representation
			Representation representation = new JSONConverter().deserialize(marshalledObject);

			return func.apply(representation);

		} catch (NoSuchElementException | IllegalStateException e) {
			log.error("Error reading '{}'", fileName, e);
		}

		return null;
	}

	/**
	 * Reads the entire file with one {@link Representation} per line and converts it to type {@code T} with the given
	 * function.
	 *
	 * @param fileName of the file to be read
	 * @param func converter function
	 * @param <T> type to convert to
	 * @return {@link Collection} of converted representations read from file
	 */
	public static <T> Collection<T> readLines(String fileName, Function<Representation, T> func) {
		log.debug("Reading representation '{}'", fileName);

		// opening resource file
		InputStream is = RepresentationFile.class.getClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			log.error("Error opening '{}'", fileName);
			return Collections.emptyList();
		}

		try (Scanner scanner = new Scanner(is)) {

			JSONConverter converter = new JSONConverter();
			Collection<T> collection = new ArrayList<>();
			Representation representation;

			while(scanner.hasNextLine()) {

				// read line and convert it to representation
				representation = converter.deserialize(scanner.nextLine());

				collection.add(func.apply(representation));
			}

			return collection;

		} catch (NoSuchElementException | IllegalStateException e) {
			log.error("Error reading '{}'", fileName, e);
		}

		return Collections.emptyList();
	}
}
