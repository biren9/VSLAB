package webshop.catalog.Model;

import java.util.Objects;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;

public class Category {
    private Long id;
    private String name;

    public Category() {}

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
}