package webshop.product.repository;
 
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import webshop.product.Model.Product;
 
public interface ProductsRepository extends JpaRepository<Product, Integer> {

	public List<Product> findByName(String name);
	public List<Product> findByNameContainingAndPriceBetween(String name, BigDecimal min, BigDecimal max);
}