package com.example.TekarchBookingServiceMS.Controller;

import com.example.TekarchBookingServiceMS.Models.Booking;
import com.example.TekarchBookingServiceMS.Services.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bookings")
public class BookingServiceController {
    @Autowired
    private BookingServiceImpl bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }
    @GetMapping("/{bookingId}")
    public List<Booking> getBookingdetails(@PathVariable Long bookingId) {
        return bookingService.getBookingdetailsBybookingId(bookingId);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookingsByuserId(userId);
    }

    @DeleteMapping("/{bookingId}")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBookingBybookingId(bookingId);
    }
}
