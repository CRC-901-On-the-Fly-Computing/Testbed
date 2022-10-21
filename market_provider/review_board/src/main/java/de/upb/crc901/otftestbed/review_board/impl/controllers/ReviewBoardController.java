package de.upb.crc901.otftestbed.review_board.impl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import de.upb.crc901.otftestbed.review_board.generated.spring_server.api.ReviewBoardApi;
import de.upb.crc901.otftestbed.review_board.generated.spring_server.api.ReviewBoardApiDelegate;

@Controller
public class ReviewBoardController implements ReviewBoardApi {

	private final ReviewBoardControllerDelegate delegate;

	@Autowired
	public ReviewBoardController(ReviewBoardControllerDelegate delegate) {
		Assert.notNull(delegate, "delegate must not be null");

		this.delegate = delegate;
	}

	@Override
	public ReviewBoardApiDelegate getDelegate() {
		return delegate;
	}
}
