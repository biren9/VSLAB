package webshop.category.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class CategoryApiController {

    @GetMapping("/categories")
    public String fetchCategories() {
        return "OK";
    }

    @PostMapping("/categories")
    public String updateCategory() {
        return "OK";
    }

    @PutMapping("/categories/{id}")
    public String createCategory(@PathVariable("id") Integer id) {
        return "OK" + id;
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        return "OK" + id;
    }



}
