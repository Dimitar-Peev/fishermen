package app.fish.repository;

import app.fish.model.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FishRepository extends JpaRepository<Fish, UUID> {

    List<Fish> findAllByForSaleTrue();
}
