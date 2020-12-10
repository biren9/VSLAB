package webshop.catalog.Client;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import webshop.catalog.Model.*;

@Component
public class ProductClient extends BaseClient {

	private final Map<Long, Product> productCache = new LinkedHashMap<Long, Product>();
	private String host = "localhost:8083"; // product-service
	private RestTemplate restTemplate = restTemplate();

	//get all Products/ filter Products
	@HystrixCommand(fallbackMethod = "getProductsCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Iterable<Product> getProducts(String searchStr, Double minPrice, Double maxPrice) {
		Collection<Product> products = new HashSet<Product>();
		if (searchStr == null) {
			searchStr = "";
		}
		if (minPrice == null) {
			minPrice = Double.MIN_VALUE;
		}
		if (maxPrice == null) {
			maxPrice = Double.MAX_VALUE;
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://"+host+"/products/")
				.queryParam("searchString", searchStr).queryParam("minPrice", minPrice)
				.queryParam("maxPrice", maxPrice);

		Product[] tmpProducts = restTemplate.getForObject(builder.build().encode().toUri(), Product[].class);
		Collections.addAll(products, tmpProducts);
		productCache.clear();
		products.forEach(p -> productCache.put(p.getId(), p));
		return products;
	}
	
	
	//get Product by ID
	@HystrixCommand(fallbackMethod = "getProductCache", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Product getProduct(Long productId) {
		Product tmp = restTemplate.getForObject("http://"+host+"/products/" + productId, Product.class);
		productCache.putIfAbsent(productId, tmp);
		return tmp;
	}

	//create Product
	@HystrixCommand(fallbackMethod = "createProductFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Boolean createProduct(Product payload) {
		restTemplate.postForObject("http://"+host+"/products", payload, Product.class);
		return true;
	}
	
	//update Product
	@HystrixCommand(fallbackMethod = "updateProductFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Boolean updateProduct(Long productId, Product payload) {
		restTemplate.put("http://"+host+"/products/" + productId, payload);
		return true;
	}
	
	@HystrixCommand(fallbackMethod = "deleteProductFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Boolean deleteProduct(Long productId) {
		restTemplate.delete("http://"+host+"/products/" + productId);
		return true;
	}

	
	//Fallback-Methods
	public Iterable<Product> getProductsCache(Integer productId) {
		return productCache.values();
	}
	
	public Product getProductCache(Integer productId) {
		return productCache.getOrDefault(productId, new Product());
	}
	
	public Product createProductFallback(Product payload){
		return payload;
	}

	public Product updateProductFallback(Long productId, Product payload){
		return payload;
	}

	public Long deleteProductFallback(Long productId){
		return productId;
	}
	
	

}