package webshop.product.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import webshop.product.Model.Product;
 
public interface ProductsRepository extends JpaRepository<Product, Integer> {
 
}