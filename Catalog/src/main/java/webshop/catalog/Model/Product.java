package webshop.catalog.Model;

import java.util.Objects;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;


public class Product   {

  private Integer id = null;

  private String name = null;

  private String detail = null;

  private BigDecimal price = null;

  public Product () {}
  
  public Product(String name, String detail, BigDecimal price) {
      this.name = name;
      this.price = price;
      this.detail = detail;
  }
  
  
  public Product id(Integer id) {
    this.id = id;
    return this;
  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Product name(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Product detail(String detail) {
    this.detail = detail;
    return this;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public Product price(BigDecimal price) {
    this.price = price;
    return this;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(this.id, product.id) &&
        Objects.equals(this.name, product.name) &&
        Objects.equals(this.detail, product.detail) &&
        Objects.equals(this.price, product.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, detail, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Product {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

