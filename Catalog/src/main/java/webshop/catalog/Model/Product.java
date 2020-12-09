package webshop.catalog.Model;

import java.util.Objects;
import java.math.BigDecimal;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class Product   {
  private Long id;
  private String name;
  private String detail;
  private BigDecimal price;
  private Category category;
  private Long categoryId;

  public Product () {}
  
  public Product(String name, String detail, BigDecimal price, Category category) {
      this.name = name;
      this.price = price;
      this.detail = detail;
      this.category = category;
  }

    public Product(String name, String detail, BigDecimal price, Long categoryID) {
        this.name = name;
        this.price = price;
        this.detail = detail;
        this.categoryId = categoryID;
        this.category = new Category(categoryID, null);
    }

  public Long getId() {
	return id;
  }
  
}

