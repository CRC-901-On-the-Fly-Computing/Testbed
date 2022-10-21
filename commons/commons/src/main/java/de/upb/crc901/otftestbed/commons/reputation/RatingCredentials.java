package de.upb.crc901.otftestbed.commons.reputation;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by horlov on 12.02.2017.
 */
public class RatingCredentials {

  private String serializedRatingToken;
  private String serializedItemPublicKey;

  public RatingCredentials(String srt, String sipk) {
    if ("".equals(srt) || "".equals(sipk)) {
      return;
    }
    serializedRatingToken = srt;
    serializedItemPublicKey = sipk;
  }

  public RatingCredentials() {
    // Empty constructor to make this class compatible with Jacksons json deserializer.
  }

  public static String serializedRatingCredentials(RatingCredentials input)
      throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(input);
  }

  public static RatingCredentials deserializeRatingCredentials(String serialized)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(serialized, RatingCredentials.class);
  }

  public String getSerializedRatingToken() {
    return serializedRatingToken;
  }

  public void setSerializedRatingToken(String serializedRatingToken) {
    this.serializedRatingToken = serializedRatingToken;
  }

  public String getSerializedItemPublicKey() {
    return serializedItemPublicKey;
  }

  public void setSerializedItemPublicKey(String serializedItemPublicKey) {
    this.serializedItemPublicKey = serializedItemPublicKey;
  }
}
