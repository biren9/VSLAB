package webshop.catalog.Model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;

public class Product   {
  private Long id;
  private String name;
  private String detail;
  private BigDecimal price;
  private Category category;
  private Long categoryID;

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
        this.categoryID = categoryID;
        this.category = new Category(categoryID, null);
    }

  public Long getId() {
	return id;
  }

public Long getCategoryID() {
	// TODO Auto-generated method stub
	return categoryID;
}

public void setCategoryID(@NotNull @Valid Long categoryID) {
	// TODO Auto-generated method stub
	this.categoryID = categoryID;
}

    public BigDecimal getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public String getName() {
        return name;
    }

    public void setPrice(@NotNull @Valid BigDecimal price2) {
	    this.price = price2;
    }

public void setDetail(@NotNull @Valid String detail2) {
	// TODO Auto-generated method stub
	this.detail = detail2;
}

public void setName(@NotNull @Valid String name2) {
	// TODO Auto-generated method stub
	this.name = name2;
}

public void setCategory(Category category2) {
	// TODO Auto-generated method stub
	this.category = category2;
}
  
}

