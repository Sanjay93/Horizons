package com.capgemini.map.horizon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capgemini.map.horizon.model.Details;
import com.capgemini.map.horizon.repository.FindRepository;

@Component
public class FindService {

	@Autowired
	private FindRepository findRepository;

	public List<Details> findByZip(Long zip, String city, String state) {
		if (zip != null) {
			return findRepository.findByZip(zip);
		} else if (city != null) {
			return findRepository.findByCity(city);
		} else if (state != null) {
			return findRepository.findByState(state);
		}
		return null;
	}

	public Details addNewLocation(Details details) {
		return findRepository.save(details);
	}

	public List<Details> findByAddress(String address){
		return findRepository.findByAddress(address);
	}
}
