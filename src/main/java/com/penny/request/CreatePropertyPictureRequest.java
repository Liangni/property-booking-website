package com.penny.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyPictureRequest {
    @NotNull
    private Long pictureId;

    @NotNull
    private Integer pictureOrder;
}
