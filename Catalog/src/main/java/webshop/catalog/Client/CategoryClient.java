package webshop.catalog.Client;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import webshop.catalog.Model.Category;

@Component
public class CategoryClient extends BaseClient {

    private final Map<Long, Category> categoryCache = new LinkedHashMap<>();

    private RestTemplate categoryRestTemplate = restTemplate();

    // Fetch all categories and replace the current cache with the new result.
    @HystrixCommand(fallbackMethod = "getCategoriesCache", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Iterable<Category> getCategories() {
        Collection<Category> categories = new HashSet<>();
        Category[] tmpCategories = categoryRestTemplate.getForObject("http://category-service/categories", Category[].class);
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
        Category[] tmpCategories = categoryRestTemplate.getForObject("http://category-service/categories?name=" + name, Category[].class);
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
        return categoryRestTemplate.getForObject("http://category-service/categories/" + categoryId, Category.class);
    }


    // Create new category
    @HystrixCommand(fallbackMethod = "createCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean createCategory(Category payload) {
        categoryRestTemplate.postForObject("http://category-service/categories", payload, Category.class);
        return true;
    }

    // Update category
    @HystrixCommand(fallbackMethod = "updateCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean updateCategory(Long categoryId, Category payload) {
        categoryRestTemplate.put("http://category-service/categories/" + categoryId, payload);
        return true;
    }

    // Delete category
    @HystrixCommand(fallbackMethod = "deleteCategoryFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
    public Boolean deleteCategory(Long categoryId) {
        categoryRestTemplate.delete("http://category-service/categories/" + categoryId);
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
        return false;
    }

    public Boolean updateCategoryFallback(Long categoryId, Category payload){
        return false;
    }

    public Boolean deleteCategoryFallback(Long categoryId){
        return false;
    }

}