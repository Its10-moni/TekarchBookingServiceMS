package com.example.TekarchBookingServiceMS.Services;

import com.example.TekarchBookingServiceMS.Models.Booking;
import com.example.TekarchBookingServiceMS.Models.Flight;
import com.example.TekarchBookingServiceMS.Models.User;
import com.example.TekarchBookingServiceMS.Services.Interfaces.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${Dataservice.Booking.URL}")
    String DATASERVICE_BOOKING_URL;
    @Value("${Dataservice.User.URL}")
    String DATASERVICE_USER_URL;
    @Value("${Dataservice.Flight.URL}")
    String DATASERVICE_FLIGHT_URL;

    @Override
    public Booking createBooking(Booking booking) {
        if (booking.getFlightId() == null) {
            throw new IllegalArgumentException("Flight ID cannot be null");
        }
        Flight flight = restTemplate.getForObject(DATASERVICE_FLIGHT_URL + "/" + booking.getFlightId(), Flight.class);
        if (flight == null) {
            throw new RuntimeException("Flight with ID " + booking.getFlightId() + " not found.");
        }
        if (flight.getAvailableSeats() > 0) {
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            restTemplate.put(DATASERVICE_FLIGHT_URL +"/"+ flight.getFlightId(), flight );
             booking.setStatus("Booked");
            return restTemplate.postForObject(DATASERVICE_BOOKING_URL, booking, Booking.class);

        } else {
            throw new RuntimeException("Flight is fully booked.");
        }
    }
    @Override
    public List<Booking> getBookingdetailsBybookingId(Long bookingId) {
        return restTemplate.exchange(DATASERVICE_BOOKING_URL+ "/" + bookingId,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Booking>>() {}).getBody();
    }
    @Override
    public List<Booking> getUserBookingsByuserId(Long userId) {
        return restTemplate.exchange(DATASERVICE_USER_URL + "/" + userId,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Booking>>() {}).getBody();
    }

    @Override
    public void cancelBookingBybookingId(Long bookingId) {
        Booking booking = restTemplate.getForObject(DATASERVICE_BOOKING_URL +"/" + bookingId, Booking.class);
        booking.setStatus("Cancelled");
        restTemplate.put(DATASERVICE_BOOKING_URL +"/"+ bookingId, booking);
    }
}
