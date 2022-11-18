package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WilderCreationDTO;
import com.example.demo.entity.Wilder;
import com.example.demo.service.WilderService;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/wilders")
public class WilderController {
	
	@Autowired
	WilderService wilderService;
	
	@PostMapping("/create")
	public Wilder createWilder(@RequestBody(required = true) WilderCreationDTO wilderCreationDTO) {
		return wilderService.createWilder(wilderCreationDTO);
	}
	
	@GetMapping("/get-all")
	public Page<Wilder> getWilders() {
		return wilderService.getWilders();
	}
	
	@GetMapping("/get-all-sse")
	public Flux<Page<Wilder>> getWildersSSE() {
		return wilderService.getWildersSSE();
	}

	@DeleteMapping("/delete-all")
	public void deleteAllWilders() {
		wilderService.deleteAllWilders();
	}

}
