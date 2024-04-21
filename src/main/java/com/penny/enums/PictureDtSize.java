package com.penny.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PictureDtSize {
    SIZE_1(1),
    SIZE_2(2),
    SIZE_3(3);

    private final int num;

    public static Stream<PictureDtSize> stream() {
        return Stream.of(PictureDtSize.values());
    }
}
