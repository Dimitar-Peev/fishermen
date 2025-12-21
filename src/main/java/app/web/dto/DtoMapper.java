package app.web.dto;

import app.fisherman.model.Fisherman;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EditProfileRequest fromUser(Fisherman user) {

        return EditProfileRequest.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .profilePicture(user.getImageUrl())
                .build();
    }
}