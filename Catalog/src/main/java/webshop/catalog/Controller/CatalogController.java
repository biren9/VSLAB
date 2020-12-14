package webshop.catalog.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.catalog.Client.CategoryClient;
import webshop.catalog.Client.ProductClient;
import webshop.catalog.Model.Category;
import webshop.catalog.Model.Product;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController()
class CatalogController {

    @Autowired
    private ProductClient productClient;
    @Autowired
    private CategoryClient categoryClient;

    @GetMapping("/catalog/products")
    public ResponseEntity<List<Product>> fetchCatalog(
            @NotNull @ApiParam(value = "Contains", required = false) @Valid @RequestParam(value = "contains", required = false) String contains,
            @NotNull @ApiParam(value = "minPrice", required = false) @Valid @RequestParam(value = "minPrice", required = false) Double minPrice,
            @NotNull @ApiParam(value = "maxPrice", required = false) @Valid @RequestParam(value = "maxPrice", required = false) Double maxPrice
    ) {

        Iterable<Product> products = productClient.getProducts(contains, minPrice, maxPrice);
        for (Product product : products) {
            Category category = categoryClient.getCategory(product.getCategoryID());
            product.setCategory(category);
        }

        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/catalog/products")
    public ResponseEntity<Boolean> createCatalog(
            @ApiParam(value = "New product." ,required=true ) @RequestBody Product newProduct
    ) {

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
    public ResponseEntity<Boolean> updateCategory(
            @PathVariable("id") Long id,
            @ApiParam(value = "New product." ,required=true ) @RequestBody Product newProduct
    ) {
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
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        Iterable<Product> products = productClient.getProducts(null, null, null);
        Boolean hasProductWithCurrentCategory = false;

        for (Product product: products) {
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
}