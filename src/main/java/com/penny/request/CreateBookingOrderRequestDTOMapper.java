package com.penny.request;

import com.penny.util.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreateBookingOrderRequestDTOMapper implements Function<CreateBookingOrderRequest, CreateBookingOrderRequestDTO> {
    private final DateHelper dateHelper;
    @Override
    public CreateBookingOrderRequestDTO apply(CreateBookingOrderRequest createRequest) {
        return CreateBookingOrderRequestDTO
                .builder()
                .checkinDate(dateHelper.parseDateString(createRequest.getCheckinDateString()))
                .checkoutDate(dateHelper.parseDateString(createRequest.getCheckoutDateString()))
                .guestName(createRequest.getGuestName())
                .guestEmail(createRequest.getGuestEmail())
                .guestPhone(createRequest.getGuestPhone())
                .arrivalTime(createRequest.getArrivalTime())
                .guestMessage(createRequest.getGuestMessage())
                .propertyId(createRequest.getPropertyId())
                .build();
    }
}
