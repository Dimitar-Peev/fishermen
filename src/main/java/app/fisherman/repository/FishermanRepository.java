package app.fisherman.repository;

import app.fisherman.model.Fisherman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FishermanRepository extends JpaRepository<Fisherman, UUID> {

    Optional<Fisherman> findByUsername(String username);

}
