package de.upb.crc901.otftestbed.commons.otfprovider.utils;

public class EMFPackageHelper {

  private static boolean initialized = false;

  private EMFPackageHelper() {
    // Private C'tor to hide implicit public one
  }

  public static void initPackages() {
    if (initialized) {
      return;
    }

    initialized = true;

    // Initialize packages
    de.upb.crc901.sse.reputation_request.impl.Reputation_requestPackageImpl.init();

    de.upb.crc901.sse.reputation.trustmodel.impl.TrustModelPackageImpl.init();

    // contains most important stuff from SSE; is contained in
    // de.upb.crc901.sse.core.structure
    de.upb.crc901.sse.core.structure.impl.StructurePackageImpl.init();

    // contains most important stuff from PCM; is contained in
    // de.uka.ipd.sdq.pcm
    de.uka.ipd.sdq.pcm.repository.impl.RepositoryPackageImpl.init();

    // contains more important stuff from PCM; is contained in
    // de.uka.ipd.sdq.identifier
    de.uka.ipd.sdq.identifier.impl.IdentifierPackageImpl.init();

  }
}
