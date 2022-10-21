package de.upb.crc901.otftestbed.proseco.impl.repository;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import de.upb.crc901.otftestbed.commons.general.StoredJobstate;

/**
 * Allows communication to the Mongo database of the InterviewData in a
 * non-spring fashion. This way the proseco pod which will read the database
 * does not have to communicate with the database in a spring fashion (or, in
 * other words it does not need to be a SpringApplication which has massive
 * overhead).
 * 
 * @author Mirko Jürgens
 *
 */
public class RequestDBConnector {

	private static MongoClient mongoClient;

	private static MongoCollection<StoredJobstate> allRequests;

	private static RequestDBConnector instance;

	public static RequestDBConnector getInstance() {
		if (instance == null)
			instance = new RequestDBConnector();
		return instance;
	}

	/**
	 * Singleton. Reads the connection credentials from the environment variables.
	 */
	private RequestDBConnector() {
		String username = System.getenv("REQUEST_DB_USER");
		char[] password = System.getenv("REQUEST_DB_PASSWORD").toCharArray();
		String hostRef = System.getenv("REQUEST_DB_HOST");
		String host = System.getenv(hostRef);
		String port = System.getenv("REQUEST_DB_PORT");
		String database = "request-data";

		MongoCredential credential = MongoCredential.createCredential(username, database, password);
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		mongoClient = new MongoClient(new ServerAddress(host, Integer.parseInt(port)), Arrays.asList(credential),
				MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());

		allRequests = mongoClient.getDatabase(database).getCollection("jobstates_new", StoredJobstate.class);

	}

	public Optional<StoredJobstate> findById(UUID id) {
		FindIterable<StoredJobstate> queryResult = allRequests.find(eq("requestUUID", id));
		return Optional.ofNullable(queryResult.first());
	}

	public void deleteById(UUID id) {
		allRequests.deleteMany(eq("requestUUID", id));
	}

	public void insert(StoredJobstate jobstate) {
		allRequests.insertOne(jobstate);
	}

}
