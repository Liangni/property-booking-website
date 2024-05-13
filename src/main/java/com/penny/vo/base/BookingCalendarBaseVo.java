package com.penny.vo.base;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCalendarBaseVo {
    private Long bookingCalendarId;

    private LocalDate bookingDate;

    private Long propertyId;
}