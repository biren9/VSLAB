package webshop.catalog.Client;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import webshop.catalog.Model.*;

//@Component
//public class CatalogClient {
//
//	private final Map<Long, Product> productCache = new LinkedHashMap<Long, Product>();
//	private final Map<Long, Category> categoryCache = new LinkedHashMap<Long, Category>();
//
//	@Autowired
//	private RestTemplate restTemplate;
//
//	@HystrixCommand(fallbackMethod = "getProductsCache", commandProperties = {
//			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
//	public Product getProduct(Long productId) {
//		Product tmpProduct = restTemplate.getForObject("http://Product/products/" + productId, Product.class);
//		productCache.putIfAbsent(productId, tmpProduct);
//		return tmpProduct;
//	}
//
//	public Product getProductCache(Integer productId) {
//		return productCache.getOrDefault(productId, new Product());
//	}
//}