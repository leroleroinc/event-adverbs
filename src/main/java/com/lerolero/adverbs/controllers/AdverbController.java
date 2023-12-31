package com.lerolero.adverbs.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import java.util.function.Function;

import com.lerolero.adverbs.services.AdverbService;

@RestController
@RequestMapping("/adverbs")
public class AdverbController {

	@Autowired
	private AdverbService adverbService;

	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> get(@RequestParam(defaultValue = "1") Integer size) {
		return adverbService.randomAdverbList(size);
	}

	@GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> subscribe(@RequestParam(defaultValue = "200") Integer interval) {
		return adverbService.randomAdverbProducer(interval).onBackpressureDrop();
	}

	@Bean
	public Function<Flux<String>,Flux<String>> adverbfunction() {
		return flux -> flux
			.doOnNext(size -> System.out.println("ADVERBS: Processing " + size))
			.flatMap(size -> adverbService.randomAdverbList(Integer.parseInt(size)));
	}

}
