package app.fish.service;

import app.exception.FishNotFoundException;
import app.fish.model.Fish;
import app.fish.property.FishProperties;
import app.fish.property.FishProperties.FishDetails;
import app.fish.repository.FishRepository;
import app.fisherman.model.Fisherman;
import app.fisherman.service.FishermanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FishService {

    private final FishRepository fishRepository;
    private final FishProperties fishProperties;
    private final FishermanService fishermanService;

    public FishDetails getRandomFish() {
        List<FishDetails> availableFishes = fishProperties.getFishes();

        if (availableFishes.isEmpty()) {
            throw new RuntimeException("No fishes left");
        }

        Random random = new Random();
        int randomFishIndex = random.nextInt(availableFishes.size());
        FishDetails randomFish = availableFishes.get(randomFishIndex);

        return randomFish;
    }

    public void removeFromAvailable(FishDetails fishDto) {
        fishProperties.getFishes().remove(fishDto);
    }

    @Transactional
    public void catchFish(UUID fishermanId, FishDetails fishDto) {
        Fisherman fisherman = fishermanService.getById(fishermanId);

        Fish fish = new Fish();
        fish.setSpecies(fishDto.getSpecies());
        fish.setWeight(fishDto.getWeight());
        fish.setType(fishDto.getType().toUpperCase());
        fish.setImageUrl(fishDto.getImageUrl());
        fish.setPrice(fishDto.getPrice());
        fish.setOwner(fisherman);

        fishRepository.save(fish);

        fisherman.getFishes().add(fish);
        fishermanService.save(fisherman);

        removeFromAvailable(fishDto);
    }

    public List<Fish> getAllFishesForSale() {
        return fishRepository.findAllByForSaleTrue();
    }

    @Transactional
    public void putForSale(UUID fishId, UUID fishermanId) {
        Fish fish = getById(fishId);

        if (fish.isForSale()) {
            throw new RuntimeException("Fish already for sale");
        }

        if (!fish.getOwner().getId().equals(fishermanId)) {
            throw new RuntimeException("Not owner");
        }

        fish.setForSale(true);
        fishRepository.save(fish);
    }

    @Transactional
    public void buyFish(UUID fishId, UUID buyerId) {
        Fish fish = getById(fishId);

        Fisherman seller = fish.getOwner();
        Fisherman buyer = fishermanService.getById(buyerId);

        if (buyer.getCash().compareTo(fish.getPrice()) < 0) {
            throw new RuntimeException("Not enough cash");
        }

        buyer.setCash(buyer.getCash().subtract(fish.getPrice()));

        seller.getFishes().remove(fish);
        buyer.getFishes().add(fish);

        fish.setOwner(buyer);
        fish.setForSale(false);

        fishermanService.save(seller);
        fishermanService.save(buyer);
        fishRepository.save(fish);
    }

    public Fish getById(UUID id) {
        return fishRepository.findById(id)
                .orElseThrow(() -> new FishNotFoundException("Fish not found"));
    }
}
