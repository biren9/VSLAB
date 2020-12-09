package webshop.catalog.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import webshop.catalog.Model.Product;
import webshop.catalog.Model.Products;

import javax.ws.rs.core.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class ProductClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    private WebTarget createTarget() {
        Client client = ClientBuilder.newClient();
        return client.target(URI.create("http://localhost:8083"));
    }

    public  List<Product> fetchProducts() {
        WebTarget target = createTarget();
        Response response = target.path("/products").request().accept(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() >= 400) {
            System.out.println("Could not connect to Product endpoint!");
            return null;
        }

        try {
            String body = response.readEntity(String.class);
            System.out.println("##########################################");
            System.out.println(body);
            System.out.println("##########################################");

            Products products = objectMapper.readValue(body, Products.class);
            return products.products;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
