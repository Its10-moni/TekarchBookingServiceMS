package com.example.TekarchBookingServiceMS.Services.Interfaces;

import com.example.TekarchBookingServiceMS.Models.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getBookingdetailsBybookingId(Long bookingId);
    List<Booking> getUserBookingsByuserId(Long userId);
    void cancelBookingBybookingId(Long bookingId);
}
