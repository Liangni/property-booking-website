package com.penny.request.district;

import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class DistrictSearchRequest{
    private String keyword;

    private Integer page;

    private Integer limit;
}
