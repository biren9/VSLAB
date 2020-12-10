package webshop.product.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;
import webshop.product.Model.Product;
import webshop.product.repository.ProductsRepository;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-12-07T12:32:53.920Z")

@RestController
public class ProductsApiController {

    private static final Logger log = LoggerFactory.getLogger(ProductsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
	
    @Autowired
    private ProductsRepository productRepo;
    
    @Autowired
    public ProductsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    
    @PostMapping("/products")
    public ResponseEntity<Boolean> addProduct(
    		@ApiParam(value = "New product." ,required=true ) @RequestBody Product newProduct) {

            Boolean productExists = !productRepo.findByName(newProduct.getName()).isEmpty();
            if (productExists) {
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
            }

            productRepo.save(newProduct);
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Boolean> deleteProduct(@ApiParam(value = "product Id",required=true) @PathVariable("id") Integer id) {
    	try {
    		Product productExists = productRepo.findById(id).orElse(null);
    		if (productExists == null) {
    			return new ResponseEntity<Boolean>(objectMapper.readValue("false Product with id " + id + "does not exist", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
    		}
			productRepo.deleteById(id);
			return new ResponseEntity<Boolean>(HttpStatus.OK);
    	} catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        } 		
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> exactProduct(
            @NotNull @ApiParam(value = "Contains", required = false) @Valid @RequestParam(value = "contains", required = false) String contains,
            @NotNull @ApiParam(value = "minPrice", required = false) @Valid @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @NotNull @ApiParam(value = "maxPrice", required = false) @Valid @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice
    ) {

        if (contains == null) {
            contains = "";
        }
        if (minPrice == null) {
            minPrice = BigDecimal.valueOf(Double.MIN_VALUE);
        }
        if (maxPrice == null) {
            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        }

        List<Product> allProducts = productRepo.findByNameContainingAndPriceBetween(contains, minPrice, maxPrice);
        return new ResponseEntity<List<Product>>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> exactProducts(@ApiParam(value = "product Id",required=true) @PathVariable("id") Integer id) {
    	try {
    		Product product = productRepo.findById(id).orElse(null);
    		if (product == null) {
    			return new ResponseEntity<Product>(objectMapper.readValue("false", Product.class), HttpStatus.NOT_ACCEPTABLE);
    		}
    		return new ResponseEntity<Product>(product, HttpStatus.OK);    		
    		
    	} catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        } 	
	}
    	
    @PutMapping("/products/{id}")
    public ResponseEntity<Boolean> updateProduct(
    		@ApiParam(value = "product Id",required=true) @PathVariable("id") Integer id,
    		@ApiParam(value = "New product." ,required=true ) @RequestBody Product newProduct) {
    	try {
    		Product product = productRepo.findById(id).orElse(null);
    		if (product == null) {
                 return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
             }
             product.setName(newProduct.getName());
             product.setDetail(newProduct.getDetail());
             product.setPrice(newProduct.getPrice());
             product.setCategoryId(newProduct.getCategoryID());
             productRepo.save(product);
             return new ResponseEntity<Boolean>(HttpStatus.OK);    		
    	} catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        } 	
    }

}

