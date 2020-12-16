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
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}

