package de.upb.crc901.otftestbed.buy_processor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.buy_processor.generated.spring_server.api.BuyProcessorApi;
import de.upb.crc901.otftestbed.buy_processor.generated.spring_server.api.BuyProcessorApiDelegate;

@Controller
public class BuyProcessorController implements BuyProcessorApi {

	private final BuyProcessorControllerDelegate delegate;

	@Autowired
	public BuyProcessorController(BuyProcessorControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public BuyProcessorApiDelegate getDelegate() {
		return delegate;
	}
}
