package webshop.category.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import webshop.category.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
