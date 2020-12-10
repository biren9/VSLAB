package webshop.catalog.Client;

import java.math.BigDecimal;
import java.util.*;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.RequestParam;
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

		Map<String, String> params = new HashMap<>();
		params.put("name", payload.getName());
		params.put("detail", payload.getDetail());
		params.put("price", ""+payload.getPrice());
		params.put("categoryId", ""+payload.getCategoryId());

		String url = "http://"+host+"/products";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, new HttpEntity(headers), String.class);
		return true;
	}
	
	//update Product
	@HystrixCommand(fallbackMethod = "updateProductFallback", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Boolean updateProduct(Long productId, Product payload) {

		Map<String, String> params = new HashMap<>();
		params.put("name", payload.getName());
		params.put("detail", payload.getDetail());
		params.put("price", ""+payload.getPrice());
		params.put("categoryId", ""+payload.getCategoryId());

		String url = "http://"+host+"/products/"+productId;
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, new HttpEntity(headers), String.class);

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