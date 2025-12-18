package app.fish.model;

import app.fisherman.model.Fisherman;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private int weight;

    @Pattern(regexp = "^(ORNAMENTAL|EATABLE)$", message = "Type must be either ORNAMENTAL or EATABLE")
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private Fisherman owner;

    @Column
    private boolean forSale;
}
