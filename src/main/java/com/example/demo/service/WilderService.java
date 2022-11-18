package com.example.demo.service;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.dto.WilderCreationDTO;
import com.example.demo.entity.Wilder;
import com.example.demo.repository.WilderRepository;
import com.github.javafaker.Faker;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class WilderService {

	private Sinks.Many<Page<Wilder>> wildersSink;	
	private Faker faker;
	
	@Autowired
	WilderRepository wilderRepository;

	public WilderService() {
		this.wildersSink = Sinks.many().replay().latest();
		this.faker = Faker.instance(new Locale("fr-FR"));
	}
	
	@Scheduled(fixedRateString= "1000")
	private Wilder createFakeWilder() {
		Wilder wilderToSave = new Wilder();
		wilderToSave.setFirstname(faker.name().firstName());
		wilderToSave.setLastname(faker.name().lastName());
		wilderToSave.setCity(faker.address().city());
		wilderToSave.setCreatedAt(new Date());
		Wilder savedwilder = wilderRepository.save(wilderToSave);
		this.emitWildersSSE();
		try {
	        Thread.sleep(RandomUtils.nextLong(1000, 10000));
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		return savedwilder;
	}

	public Wilder createWilder(WilderCreationDTO wilderCreationDTO) {
		Wilder wilderToSave = new Wilder();
		wilderToSave.setFirstname(wilderCreationDTO.getFirstname());
		wilderToSave.setLastname(wilderCreationDTO.getLastname());
		wilderToSave.setCity(wilderCreationDTO.getCity());
		wilderToSave.setCreatedAt(new Date());
		Wilder savedwilder = wilderRepository.save(wilderToSave);
		this.emitWildersSSE();
		return savedwilder;
	}
	

	public Page<Wilder> getWilders() {
		return wilderRepository.findAll(PageRequest.of(0, 10, Sort.by("id").descending()));
	}

	private void emitWildersSSE() {
		wildersSink.tryEmitNext(this.getWilders());
	}

	public Flux<Page<Wilder>> getWildersSSE() {
		this.emitWildersSSE();
		return wildersSink.asFlux();
	}

	public void deleteAllWilders() {
		wilderRepository.deleteAll();
		this.emitWildersSSE();
	}
}
