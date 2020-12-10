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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    		@NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
    		@NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
    		@NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
    		@NotNull @ApiParam(value = "Category Id", required = true) @Valid @RequestParam(value = "categoryId", required = true) Long categoryId) {
        
        try {
            Boolean productExists = !productRepo.findByName(name).isEmpty();
            if (productExists) {
                return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
            }
        	Product newProduct = new Product (name, detail, price, categoryId);
            productRepo.save(newProduct);
            return new ResponseEntity<Boolean>(HttpStatus.ACCEPTED);
            
        } catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
    public ResponseEntity<List<Product>> exactProduct() {
        return new ResponseEntity<List<Product>>(productRepo.findAll(), HttpStatus.OK);
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
    		@NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
    		@NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
    		@NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
    		@NotNull @ApiParam(value = "Category Id", required = true) @Valid @RequestParam(value = "categoryId", required = true) Long categoryId) {
    	try {
    		Product product = productRepo.findById(id).orElse(null);
    		if (product == null) {
                 return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_ACCEPTABLE);
             }
             product.setName(name);
             product.setDetail(detail);
             product.setPrice(price);
             product.setCategoryId(categoryId);
             productRepo.save(product);
             return new ResponseEntity<Boolean>(HttpStatus.OK);    		
    	} catch (IOException e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        } 	
    }

}

