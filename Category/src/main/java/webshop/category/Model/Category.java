package webshop.category.Model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

	public void setName(@NotNull @Valid String name2) {
		// TODO Auto-generated method stub
		this.name = name2;
	}
}
