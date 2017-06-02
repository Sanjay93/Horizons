package com.capgemini.map.horizon.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.capgemini.map.horizon.model.Details;

/**
 * Maps location fields with file values
 * 
 * @author adeshpa1
 *
 */
public class LocationFieldSetMapper implements FieldSetMapper<Details> {

	@Override
	public Details mapFieldSet(FieldSet arg0) throws BindException {
		Details locationDetail = new Details();
		
		locationDetail.setName(arg0.readString("name"));
		locationDetail.setAddress(arg0.readString("address"));
		locationDetail.setCity(arg0.readString("city"));
		locationDetail.setCounty(arg0.readString("county"));
		locationDetail.setState(arg0.readString("state"));
		locationDetail.setZip(arg0.readLong("zip"));
		locationDetail.setPlan(arg0.readString("plan name"));
		locationDetail.setSpecialty(arg0.readString("specialty"));
		locationDetail.setType(arg0.readString("type"));
		return locationDetail;
	}
}
