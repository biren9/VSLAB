package webshop.category.Controller;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webshop.category.Model.Category;
import webshop.category.Repository.CategoryRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController()
public class CategoryApiController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> fetchCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return new ResponseEntity(allCategories, HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<Boolean> createCategory(
            @NotNull @ApiParam(value = "Category name", required = true) @Valid @RequestParam(value = "name", required = true) String name
    ) {
        Category newCategory = new Category(name);
        categoryRepository.save(newCategory);
        return new ResponseEntity(true, HttpStatus.OK);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Boolean> updateCategory(
            @PathVariable("id") Long id,
            @NotNull @ApiParam(value = "Category name", required = true) @Valid @RequestParam(value = "name", required = true) String name
    ) {
        Optional<Category> optinalCategory = categoryRepository.findById(id);
        if (optinalCategory.isPresent()) {
            Category category = optinalCategory.get();
            category.setName(name);
            categoryRepository.save(category);
            return new ResponseEntity(true, HttpStatus.OK);
        }
        return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        Optional<Category> optinalCategory = categoryRepository.findById(id);
        if (optinalCategory.isPresent()) {
            Category category = optinalCategory.get();
            categoryRepository.delete(category);
            return new ResponseEntity(true, HttpStatus.OK);
        }
        return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
    }



}
