package app.fisherman.service;

import app.exception.FishermanNotFoundException;
import app.exception.IncorrectUsernameOrPassword;
import app.fisherman.model.Fisherman;
import app.fisherman.repository.FishermanRepository;
import app.web.dto.EditProfileRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class FishermanService {

    private final FishermanRepository fishermanRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest registerRequest) {

        Optional<Fisherman> existingUser = fishermanRepository.findByUsername(registerRequest.getUsername());
        if (existingUser.isPresent()) {
            log.info("Failed to create user account. User already exists.");
            return false;
        } else {

            Fisherman user = new Fisherman();

            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setNickname(registerRequest.getNickname());
            user.setExperience(registerRequest.getExperience());
            user.setCash(BigDecimal.valueOf(registerRequest.getExperience().getInitialCash()));

            fishermanRepository.save(user);
            log.info("Successfully created new user account for username [{}] and id [{}]", user.getUsername(), user.getId());
            return true;
        }
    }

    public Fisherman login(LoginRequest loginRequest) {

        Fisherman fisherman = fishermanRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IncorrectUsernameOrPassword("Username or password incorrect!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), fisherman.getPassword())) {
            throw new IncorrectUsernameOrPassword("Username or password incorrect!");
        }

        log.info("Successfully logged user account with username [{}]", loginRequest.getUsername());
        return fisherman;
    }

    public Fisherman getById(UUID userId) {

        return fishermanRepository.findById(userId)
                .orElseThrow(() -> new FishermanNotFoundException("Fisherman not found"));
    }

    public void updateProfile(UUID id, EditProfileRequest editProfileRequest) {

        Fisherman user = getById(id);

        user.setUsername(editProfileRequest.getUsername());
        user.setNickname(editProfileRequest.getNickname());
        user.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));
        user.setImageUrl(editProfileRequest.getProfilePicture());

        fishermanRepository.save(user);
    }

    public void save(Fisherman fisherman) {
        fishermanRepository.save(fisherman);
    }

}
