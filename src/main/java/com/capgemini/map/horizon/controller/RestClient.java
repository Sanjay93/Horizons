package com.capgemini.map.horizon.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

	@Autowired
	private RestTemplate restTemplate;

	public <T> T callForGet(String url, Class<T> responseType) {
		return callForGetWithParams(url,responseType,null);
	}

	public <T> T callForGetWithParams(String url, Class<T> responseType, Map<String, ?> params) {
		T outputObject = null;
		try{
			HttpEntity<?> requestEntity = new HttpEntity<>(new HttpHeaders());
			ResponseEntity<T> result;
			if (params == null) {
				result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
			}
			else{
				result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, params);
			}
			outputObject = result.getBody();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return outputObject;
	}

	public <T, R> R callForPost(String url, T input, Class<R> responseType) {
		R outputObject = null;
		try{
			HttpEntity<T> requestEntity = new HttpEntity<>(input, new HttpHeaders());
			ResponseEntity<R> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
			outputObject = result.getBody();
		}catch (Exception e) {
			throw e;
		}
		return outputObject;
	}

}