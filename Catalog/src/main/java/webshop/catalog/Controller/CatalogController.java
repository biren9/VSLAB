package webshop.catalog.Controller;

import org.springframework.http.HttpStatus;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.catalog.Client.ProductClient;
import webshop.catalog.Model.Product;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController()
class CatalogController {

    @GetMapping("/catalog/products")
    public ResponseEntity<List<Product>> fetchCatalog(
            @NotNull @ApiParam(value = "Contains", required = false) @Valid @RequestParam(value = "contains", required = false) String contains,
            @NotNull @ApiParam(value = "minPrice", required = false) @Valid @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @NotNull @ApiParam(value = "maxPrice", required = false) @Valid @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice
    ) {


        ProductClient client = new ProductClient();
        List<Product> products = client.fetchProducts();
        System.out.println(products.size());


        return new ResponseEntity(new ArrayList<Product>(), HttpStatus.OK);
    }

    @PostMapping("/catalog/products")
    public ResponseEntity<Boolean> createCategory(
            @NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
            @NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
            @NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
            @NotNull @ApiParam(value = "Category id", required = true) @Valid @RequestParam(value = "categoryID", required = true) Long categoryID
    ) {

        return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/catalog/products/{id}")
    public ResponseEntity<Boolean> updateCategory(
            @PathVariable("id") Long id,
            @NotNull @ApiParam(value = "Product name", required = true) @Valid @RequestParam(value = "name", required = true) String name,
            @NotNull @ApiParam(value = "Product detail", required = true) @Valid @RequestParam(value = "detail", required = true) String detail,
            @NotNull @ApiParam(value = "Product price", required = true) @Valid @RequestParam(value = "price", required = true) BigDecimal price,
            @NotNull @ApiParam(value = "Category id", required = true) @Valid @RequestParam(value = "categoryID", required = true) Long categoryID
    ) {

        return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/catalog/products/{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
    }
}