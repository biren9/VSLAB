package webshop.catalog.Controller;

import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.catalog.Client.CatalogClient;
import webshop.catalog.Client.CategoryClient;
import webshop.catalog.Client.ProductClient;
import webshop.catalog.Model.Category;
import webshop.catalog.Model.Product;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController()
class CatalogController {

    private ProductClient productClient = new ProductClient();
    private CategoryClient categoryClient = new CategoryClient();

    @GetMapping("/catalog/products")
    public ResponseEntity<List<Product>> fetchCatalog(
            @NotNull @ApiParam(value = "Contains", required = false) @Valid @RequestParam(value = "contains", required = false) String contains,
            @NotNull @ApiParam(value = "minPrice", required = false) @Valid @RequestParam(value = "minPrice", required = false) Double minPrice,
            @NotNull @ApiParam(value = "maxPrice", required = false) @Valid @RequestParam(value = "maxPrice", required = false) Double maxPrice
    ) {

        Iterable<Product> products = productClient.getProducts(contains, minPrice, maxPrice);
        for (Product product : products) {
            Category category = categoryClient.getCategory(product.getCategoryId());
            product.setCategory(category);
        }

        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/catalog/products")
    public ResponseEntity<Boolean> createCatalog(
            @NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
            @NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
            @NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
            @NotNull @ApiParam(value = "Category id", required = true) @Valid @RequestParam(value = "categoryID", required = true) Long categoryID
    ) {

        if (categoryClient.getCategory(categoryID) == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        Product newProduct = new Product(name, detail, price, categoryID);
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
            @NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
            @NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
            @NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
            @NotNull @ApiParam(value = "Category id", required = true) @Valid @RequestParam(value = "categoryID", required = true) Long categoryID
    ) {
        if (categoryClient.getCategory(categoryID) == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        Product product = productClient.getProduct(id);
        if (product == null) {
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }

        product.setCategoryId(categoryID);
        product.setPrice(price);
        product.setDetail(detail);
        product.setName(name);

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
            if (product.getCategoryId().equals(id)) {
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