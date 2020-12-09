package webshop.category.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import webshop.category.Model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findById(Long id);
}
