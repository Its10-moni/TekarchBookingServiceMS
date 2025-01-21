package com.example.TekarchBookingServiceMS.Models;


import lombok.Data;

@Data
public class Booking {
    private Long bookingId;
    private User userId;
    private Flight flightId;
    private String status;
}
