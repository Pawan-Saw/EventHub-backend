package com.example.demo.booking;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;

    public BookingController(
            BookingRepository bookingRepository,
            EventRepository eventRepository) {

        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/event/{eventId}")
    public Booking createBooking(
            @PathVariable Long eventId,
            @RequestBody Booking booking) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + eventId));

        if (booking.getNumberOfTickets() <= 0) {
            throw new RuntimeException("Number of tickets must be greater than 0");
        }

        if (booking.getNumberOfTickets() > event.getAvailableSeats()) {
            throw new RuntimeException("Not enough seats available");
        }

        double totalAmount =
                booking.getNumberOfTickets() * event.getTicketPrice();

        event.setAvailableSeats(
                event.getAvailableSeats() - booking.getNumberOfTickets()
        );

        booking.setEvent(event);
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");

        eventRepository.save(event);

        return bookingRepository.save(booking);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found with id: " + id));
    }
}