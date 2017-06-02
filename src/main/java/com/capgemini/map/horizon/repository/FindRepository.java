package com.capgemini.map.horizon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.capgemini.map.horizon.model.Details;

public interface FindRepository extends MongoRepository<Details, Long>{

	public List<Details> findByZip(Long zip);
	
	public List<Details> findByCity(String city);
	
	public List<Details> findByState(String state);
	
	public List<Details> findByAddress(String address);
}
