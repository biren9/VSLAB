package webshop.catalog.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webshop.catalog.Client.CategoryClient;
import webshop.catalog.Client.ProductClient;
import webshop.catalog.Model.Category;
import webshop.catalog.Model.Product;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController()
class CatalogController {

    @Autowired
    private ProductClient productClient;
    @Autowired
    private CategoryClient categoryClient;

    @GetMapping("/catalog/products/{id}")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<Product> exactProducts(
            @ApiParam(value = "product Id", required = true) @PathVariable("id") Long id) {

        Product product = productClient.getProduct(id);

        Category category = categoryClient.getCategory(product.getCategoryID());
        product.setCategory(category);

        if (product == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(product, HttpStatus.OK);
        }
    }

    @GetMapping("/catalog/products")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<List<Product>> fetchCatalog(
            @NotNull @ApiParam(value = "Contains", required = false) @Valid @RequestParam(value = "contains", required = false) String contains,
            @NotNull @ApiParam(value = "minPrice", required = false) @Valid @RequestParam(value = "minPrice", required = false) Double minPrice,
            @NotNull @ApiParam(value = "maxPrice", required = false) @Valid @RequestParam(value = "maxPrice", required = false) Double maxPrice) {

        Iterable<Product> products = productClient.getProducts(contains, minPrice, maxPrice);
        for (Product product : products) {
            Category category = categoryClient.getCategory(product.getCategoryID());
            product.setCategory(category);
        }

        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/catalog/products")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> createCatalog(
            @ApiParam(value = "New product.", required = true) @RequestBody Product newProduct) {

        if (categoryClient.getCategory(newProduct.getCategoryID()) == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        Boolean success = productClient.createProduct(newProduct);

        if (success) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("/catalog/products/{id}")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateCategory(@PathVariable("id") Long id,
            @ApiParam(value = "New product.", required = true) @RequestBody Product newProduct) {
        if (categoryClient.getCategory(newProduct.getCategoryID()) == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        Product product = productClient.getProduct(id);
        if (product == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        product.setCategoryID(newProduct.getCategoryID());
        product.setPrice(newProduct.getPrice());
        product.setDetail(newProduct.getDetail());
        product.setName(newProduct.getName());

        Boolean success = productClient.updateProduct(id, product);

        if (success) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_GATEWAY);
        }
    }

    @DeleteMapping("/catalog/categories/{id}")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        Iterable<Product> products = productClient.getProducts(null, null, null);
        Boolean hasProductWithCurrentCategory = false;

        for (Product product : products) {
            if (product.getCategoryID().equals(id)) {
                hasProductWithCurrentCategory = true;
                break;
            }
        }

        if (!hasProductWithCurrentCategory) {
            Boolean success = categoryClient.deleteCategory(id);
            if (success) {
                return new ResponseEntity(true, HttpStatus.OK);
            } else {
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    // Product forward

    @DeleteMapping("/catalog/products/{id}")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteProduct(
            @ApiParam(value = "product Id", required = true) @PathVariable("id") Long id) {
        if (productClient.deleteProduct(id)) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    // Category forward

    @GetMapping("/catalog/categories")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_USER')")
    public ResponseEntity<List<Category>> fetchCategories() {
        Iterable<Category> categories = categoryClient.getCategories();
        return new ResponseEntity(categories, HttpStatus.OK);
    }

    @PostMapping("/catalog/categories")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> createCategory(
            @ApiParam(value = "New Category.", required = true) @RequestBody Category newCategory) {
        Boolean success = categoryClient.createCategory(newCategory);
        if (success) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("/catalog/categories/{id}")
    @PreAuthorize("#oauth2.hasScope('webshop-client-scope') and hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> updateCategory(@PathVariable("id") Long id,
            @ApiParam(value = "New product.", required = true) @RequestBody Category newCategory) {
        Boolean success = categoryClient.updateCategory(id, newCategory);
        if (success) {
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            return new ResponseEntity(false, HttpStatus.BAD_GATEWAY);
        }
    }

}