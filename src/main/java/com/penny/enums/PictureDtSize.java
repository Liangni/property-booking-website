package com.penny.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PictureDtSize {
    SIZE_1(1, 331, 314.88),
    SIZE_2(2, 428.47, 582.06),
    SIZE_3(3,520.5,511.39);

    private final int num;
    private final double widthInPx;
    private final double heightInPx;

    public static Stream<PictureDtSize> stream() {
        return Stream.of(PictureDtSize.values());
    }
}
