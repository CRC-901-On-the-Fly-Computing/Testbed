package de.upb.crc901.otftestbed.verificator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.verificator.generated.spring_server.api.CompositionAnalysisApi;
import de.upb.crc901.otftestbed.verificator.generated.spring_server.api.CompositionAnalysisApiDelegate;

@Controller
public class CompositionAnalysisController implements CompositionAnalysisApi {

	private final CompositionAnalysisControllerDelegate delegate;

	@Autowired
	public CompositionAnalysisController(CompositionAnalysisControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public CompositionAnalysisApiDelegate getDelegate() {
		return delegate;
	}
}
