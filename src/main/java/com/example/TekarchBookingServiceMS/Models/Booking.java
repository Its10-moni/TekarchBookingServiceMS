package com.example.TekarchBookingServiceMS.Models;


import lombok.Data;

@Data
public class Booking {
    private Long bookingId;
    private Long userId;
    private Long flightId;
    private String status;

}
