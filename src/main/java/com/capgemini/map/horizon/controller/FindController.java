package com.capgemini.map.horizon.controller;

import java.util.List;


import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.map.horizon.model.Details;
import com.capgemini.map.horizon.service.FindService;
import com.capgemini.map.horizon.utils.logger.AbstractLoggerFactory;


@RestController
public class FindController {
	
	//private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FindController.class);
	
	//private static final com.capgemini.map.horizon.utils.logger.Logger LOGGER = AbstractLoggerFactory.getLogger(FindController.class); 

	@Autowired
	private FindService findService;

	@Autowired
	private RestClient restClient;

	@RequestMapping(value="/find", method = RequestMethod.POST)
	public List<Details> findZip(@RequestBody Details queryDetails){
		
		List<Details> details = findService.findByZip(queryDetails.getZip(),queryDetails.getCity(),queryDetails.getState());
		//LOGGER.debug("Details {}" + details);
		return updateLocation(details);
		//	return details;
	}


	private List<Details> updateLocation(List<Details> details){
		for(Details detail:details){
			if(detail.getLattitude() == null || detail.getLongitude() == null){
				String response = restClient.callForGet("http://maps.googleapis.com/maps/api/geocode/json?address="+detail.getAddress()+","+detail.getCity()+","+detail.getCounty()+","+detail.getZip(), String.class);
				JSONObject responseObject = new JSONObject(response);
				detail.setLattitude(responseObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
				detail.setLongitude(responseObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
			}
		}
		return details;
	}
}
