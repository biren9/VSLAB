package webshop.catalog.Client;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import webshop.catalog.Model.Category;

@Component
public class CategoryClient { // extends BaseClient

    @Autowired
    private EurekaClient discoveryClient;

    private final Map<Long, Category> categoryCache = new LinkedHashMap<>();
    @Autowired
    private RestTemplate categoryRestTemplate; // = restTemplate();


    private String getInventoryURL() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka("CATEGORY-SERVICE", false);
        return instance.getHomePageUrl();
    }


    // Fetch all categories and replace the current cache with the new result.
    @HystrixCommand(fallbackMethod = "getCategoriesCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Iterable<Category> getCategories() {
        Collection<Category> categories = new HashSet<>();
        Category[] tmpCategories = categoryRestTemplate.getForObject(getInventoryURL()+"categories", Category[].class);
        Collections.addAll(categories, tmpCategories);
        categoryCache.clear();
        categories.forEach(u -> categoryCache.put(u.getId(), u));
        return categories;
    }

    // Fetch all categories with the "name" constraint. We do not cache this request!
    @HystrixCommand(fallbackMethod = "getCategoriesCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Iterable<Category> getCategories(String name) {
        Collection<Category> categories = new HashSet<>();
        Category[] tmpCategories = categoryRestTemplate.getForObject(getInventoryURL()+"categories?name=" + name, Category[].class);
        Collections.addAll(categories, tmpCategories);
        return categories;
    }

    // Fetch Category for ID
    @HystrixCommand(fallbackMethod = "getCategoryCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRestTemplate.getForObject(getInventoryURL()+"categories/" + categoryId, Category.class);
    }


    // Create new category
    @HystrixCommand(fallbackMethod = "createCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean createCategory(Category payload) {
        System.out.println("-----------" + getInventoryURL()+"categories");
        categoryRestTemplate.postForObject(getInventoryURL()+"categories", payload, Category.class);
        return true;
    }

    // Update category
    @HystrixCommand(fallbackMethod = "updateCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean updateCategory(Long categoryId, Category payload) {
        categoryRestTemplate.put(getInventoryURL()+"categories/" + categoryId, payload);
        return true;
    }

    // Delete category
    @HystrixCommand(fallbackMethod = "deleteCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean deleteCategory(Long categoryId) {
        categoryRestTemplate.delete(getInventoryURL()+"categories/" + categoryId);
        return true;
    }

    // Fallback section

    public Iterable<Category> getCategoriesCache() {
        return categoryCache.values();
    }

    public Iterable<Category> getCategoriesCache(String name) {
        return categoryCache.values();
    }

    public Category getCategoryCache(Long categoryId) {
        return categoryCache.getOrDefault(categoryId, null);
    }

    public Boolean createCategoryFallback(Category payload){
        categoryCache.put(payload.getId(), payload);
        return true;
    }

    public Boolean updateCategoryFallback(Long categoryId, Category payload){
        categoryCache.put(payload.getId(), payload);
        return true;
    }

    public Boolean deleteCategoryFallback(Long categoryId){
        categoryCache.remove(categoryId);
        return true;
    }

}