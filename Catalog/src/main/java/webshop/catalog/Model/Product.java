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

  public Product () {}
  
  public Product(String name, String detail, BigDecimal price) {
      this.name = name;
      this.price = price;
      this.detail = detail;
  }
}

