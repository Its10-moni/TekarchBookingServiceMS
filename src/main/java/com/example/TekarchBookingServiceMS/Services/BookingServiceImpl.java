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

import java.util.ArrayList;
import java.util.Collections;
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
        User user = restTemplate.getForObject(DATASERVICE_USER_URL +"/"+ booking.getUserId() ,User.class);

        Flight flight = restTemplate.getForObject(DATASERVICE_FLIGHT_URL + "/" + booking.getFlightId(), Flight.class);
        if (flight == null) {
            throw new RuntimeException("Flight with ID " + booking.getFlightId() + " not found.");
        }
        if (flight.getAvailableSeats() > 0) {
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            restTemplate.put(DATASERVICE_FLIGHT_URL +"/"+ flight.getFlightId(), flight );
            booking.setUser(user);
            booking.setFlight(flight);
            booking.setStatus("Booked");
            return restTemplate.postForObject(DATASERVICE_BOOKING_URL,booking, Booking.class);


        } else {
            throw new RuntimeException("Flight is fully booked.");
        }
    }
    @Override
    public List<Booking> getBookingdetailsBybookingId(Long bookingId) {
        Booking booking=restTemplate.getForObject(DATASERVICE_BOOKING_URL +"/" +bookingId , Booking.class);
        List<Booking> bookings = new ArrayList<>();
        if (booking != null) {
            bookings.add(booking);
        }
        return bookings;

    }
    @Override
    public List<Booking> getUserBookingsByuserId(Long userId) {
        //Booking booking=new Booking();
        User user = restTemplate.getForObject(DATASERVICE_USER_URL +"/"+ userId ,User.class);
        String url = DATASERVICE_BOOKING_URL + "?userId=" + userId;
        List<Booking> bookings = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Booking>>() {}
        ).getBody();

        // Return the list of bookings. If no bookings are found, return an empty list.
        return bookings != null ? bookings : Collections.emptyList();

    }

    @Override
    public void cancelBookingBybookingId(Long bookingId) {
        restTemplate.delete(DATASERVICE_BOOKING_URL + "/" + bookingId);
        Booking booking=new Booking();
        booking.setStatus("Cancelled");
       // restTemplate.put(DATASERVICE_BOOKING_URL +"/"+ bookingId, booking);*/
    }
}
