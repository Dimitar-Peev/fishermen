package app.fish.property;

import app.config.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "fishes.yaml", factory = YamlPropertySourceFactory.class)
public class FishProperties {

    private List<FishDetails> fishes;

    @Data
    public static class FishDetails {
        private String species;
        private int weight;
        private String type;
        private String imageUrl;
        private BigDecimal price;
    }

}
