package webshop.catalog.Model;

import java.util.Objects;
import java.math.BigDecimal;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class Category {
    private Long id;
    private String name;
}