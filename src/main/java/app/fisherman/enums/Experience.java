package app.fisherman.enums;

import lombok.Getter;

@Getter
public enum Experience {

    BEGINNER(100),
    INTERMEDIATE(200),
    EXPERT(300);

    private final int initialCash;

    Experience(int initialCash) {
        this.initialCash = initialCash;
    }

}
