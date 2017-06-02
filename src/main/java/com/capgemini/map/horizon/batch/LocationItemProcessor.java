package com.capgemini.map.horizon.batch;

import java.util.List;

import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.capgemini.map.horizon.controller.RestClient;
import com.capgemini.map.horizon.model.Details;
import com.capgemini.map.horizon.service.FindService;

/**
 * Processes each location item to find Latitude and Longitude
 * 
 * @author adeshpa1
 *
 */
@Scope("step")
public class LocationItemProcessor implements ItemProcessor<Details, Details> {

	private enum RETRY{
		FIRST,
		
		SECOND,
		
		LAST
	}
	
	private static RETRY retryAttempt = RETRY.FIRST;
	
	@Autowired
	private RestClient restClient;
	
	@Autowired
	private FindService findService;

	@Override
	public Details process(Details locationDetail) throws Exception {
		return findAndSetLatLong(locationDetail);
	}
	
	private Details findAndSetLatLong(Details locationDetail){
		List<Details> details =  findService.findByAddress(locationDetail.getAddress());
		if(details != null  && ! details.isEmpty()){
			return null;
		}
		if(locationDetail.getLattitude() == null || locationDetail.getLongitude() == null){
			reSetAttempt();
			String response = null;
			do{
				response = askGoogle("http://maps.googleapis.com/maps/api/geocode/json?address="+locationDetail.getAddress()+","+locationDetail.getCity()+","+locationDetail.getCounty()+","+locationDetail.getZip());
				if(response == null){
					setAttempt();
				}
			}while(response == null && retryAttempt != RETRY.LAST);
			if(response == null){
				return null;
			}
			JSONObject responseObject = new JSONObject(response);
			if(responseObject.getJSONArray("results").length() != 0){
				locationDetail.setLattitude(responseObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
				locationDetail.setLongitude(responseObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
			}
		}
		return locationDetail;
	}
	
	private String askGoogle(String query){
		return restClient.callForGet(query, String.class);
	}
	
	private static void setAttempt(){
		switch(retryAttempt){
		case FIRST:
			retryAttempt = RETRY.SECOND;
			break;
		case SECOND:
			retryAttempt = RETRY.LAST;
		case LAST:
		default:
			retryAttempt = RETRY.FIRST;
		}
	}
	
	private static void reSetAttempt(){
		retryAttempt = RETRY.FIRST;
	}
}
