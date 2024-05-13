package com.penny.request;

import com.penny.request.SearchPropertyRequest;
import com.penny.request.SearchPropertyRequestDTO;

import com.penny.util.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SearchPropertyRequestDTOMapper implements Function<SearchPropertyRequest, SearchPropertyRequestDTO> {
    private final DateHelper dateHelper;
    @Override
    public SearchPropertyRequestDTO apply(SearchPropertyRequest searchRequest) {
        return SearchPropertyRequestDTO
                .builder()
                .numOfAvailableDays(searchRequest.getNumOfAvailableDays())
                .startAvailableDate(dateHelper.parseDateString(searchRequest.getStartAvailableDateString()))
                .endAvailableDate(dateHelper.parseDateString(searchRequest.getEndAvailableDateString()))
                .numOfGuests(searchRequest.getNumOfGuests())
                .numOfBedrooms(searchRequest.getNumOfBedrooms())
                .numOfBeds(searchRequest.getNumOfBeds())
                .numOfBathrooms(searchRequest.getNumOfBathrooms())
                .maxPrice(searchRequest.getMaxPrice())
                .minPrice(searchRequest.getMinPrice())
                .propertyMainTypeId(searchRequest.getPropertyMainTypeId())
                .propertyShareTypeId(searchRequest.getPropertyShareTypeId())
                .amenityIds(searchRequest.getAmenityIds())
                .districtId(searchRequest.getDistrictId())
                .build();
    }
}
