package com.capgemini.map.horizon.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.map.horizon.model.Details;
import com.capgemini.map.horizon.service.FindService;

/**
 * @author adeshpa1
 *
 */
public class LocationDataWriter implements ItemWriter<Details> {

	@Autowired
	private FindService findService;
	
	@Override
	public void write(List<? extends Details> arg0) throws Exception {
		if(arg0.get(0).getLattitude() == null || arg0.get(0).getLongitude() == null){
			System.out.println("Not saving data : " + arg0.get(0));
			return;
		}
		Details details = findService.addNewLocation(arg0.get(0));
		System.out.println(details);
	}
}
