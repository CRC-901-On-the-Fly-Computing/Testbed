package de.upb.crc901.otftestbed.commons.pmcollectiongenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model.HelperAttributes;
import de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model.Methods;
import de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model.PMCollection;
import de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model.PathVariables;
import de.upb.crc901.otftestbed.commons.pmcollectiongenerator.model.Request;

public class PMCollectionGenerator {

  public static final String JSON_HEADER = "Content-Type: application/json\n\"";
  public static final String TEXT_HEADER = "Content-Type: text/plain\n\"";


  private List<Request> requests;
  private int requestID;
  private String collectionID;
  private static final String OUTPUT_FILE = "PostManCollection.json";

  private static final Logger log = LoggerFactory.getLogger(PMCollectionGenerator.class);

  public PMCollectionGenerator(String collectionID) {
    requests = new ArrayList<>();
    requestID = 0;
    this.collectionID = collectionID;
  }

  public void addRequest(String header, String url, Methods method, String name,
      String rawModelData) {
    Request request = new Request();
    request.setId(String.valueOf(requestID));
    // increment id for the next request
    requestID++;
    request.setHeaders(header);
    request.setUrl(url);
    request.setPreRequestScript(null);
    PathVariables pathVariables = new PathVariables();
    // pathVariables.setAdditionalProperty(name, value); // TODO
    request.setPathVariables(pathVariables);
    request.setMethod(method.toString());
    request.setData(new ArrayList<>()); // TODO
    request.setDataMode("raw");
    request.setVersion(2);
    request.setTests(null);
    request.setCurrentHelper("normal");
    request.setHelperAttributes(new HelperAttributes());
    request.setTime(createTimeStamp());
    request.setName(name);
    request.setDescription("");
    request.setCollectionId(collectionID);
    request.setResponses(new ArrayList<>());
    request.setRawModeData(rawModelData);
    requests.add(request);
  }


  public void generateCollection() {
    PMCollection pmc = new PMCollection();
    pmc.setId(collectionID);
    pmc.setName("OTFTestbed-Requests");
    pmc.setDescription("");
    pmc.setFolders(new ArrayList<>());
    // pmc.setTimestamp(new Date().getSeconds());
    pmc.setTimestamp(createTimeStamp());
    pmc.setOwner("OTFTestbedTeam");
    pmc.setPublic(false);
    pmc.setRequests(requests);
    pmc.setOrder(generateOrder(requests));
    printToFile(pmc);
  }

  private void printToFile(PMCollection pmc) {
    ObjectMapper mapper = new ObjectMapper();
    try (FileWriter fw = new FileWriter(OUTPUT_FILE)) {
      String json = mapper.writeValueAsString(pmc);
      fw.write(json.toCharArray());
    } catch (IOException e) {
      log.error("Couldn't get json or write to file.", e);
    }
  }

  private Integer createTimeStamp() {
    return Integer.valueOf((int) new Date().getTime());
  }

  private List<String> generateOrder(List<Request> requests) {
    List<String> orderedRequests = new ArrayList<>();
    for (Request request : requests) {
      orderedRequests.add(request.getId());
    }
    return orderedRequests;
  }
}
