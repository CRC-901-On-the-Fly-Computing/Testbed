package de.upb.crc901.otftestbed.commons.otfprovider.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation.OTFContext;
import de.upb.crc901.otftestbed.commons.reputation.ServiceReputation;
import de.upb.crc901.otftestbed.commons.utils.Helpers;

public class OTFProviderToolsAndHelpers {

  private static ObjectMapper mapper = null;

  private OTFProviderToolsAndHelpers() {
    // used to hide the implicit public constructor
  }

  // Default uses main commons
  public static ObjectMapper getMapper() {
    return getMapper(true);
  }

  public static ObjectMapper getMapper(boolean useMainCommons) {
    if (mapper == null) {
      if (useMainCommons)
        mapper = Helpers.getMapper();
      else
        mapper = new ObjectMapper();
    }
    return mapper;
  }

  public static String getScanPackages() {
    return getScanPackages(true);
  }

  public static String getScanPackages(boolean useMainCommons) {

    String main = "";
    if (useMainCommons) {
      main = Helpers.getScanPackages();
    } else {
      main = "io.swagger.resources,";
    }

    return main
        + "de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching.reputation,"
        + "de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.matching,"
        + "de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs.requirements,"
        + "de.upb.crc901.otftestbed.commons.otfprovider.configurator.requests.specs,"
        + "de.upb.crc901.otftestbed.commons.otfprovider.matcher.result"
        + "de.upb.crc901.otftestbed.commons.otfprovider.requirementspec";
  }

  public static double ratingByOTFContext(ServiceReputation reputation, OTFContext context) {
    switch (context) {
      case OTHER:
        return reputation.getOther();
      case OVERALL:
        return reputation.getOverall();
      case PERFORMANCE:
        return reputation.getPerformance();
      case SECURITY:
        return reputation.getSecurity();
      case USABILITY:
        return reputation.getUsability();
      default:
        return 0;
    }
  }

}
