package com.example.demo.controller;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // GET ALL EVENTS
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // GET EVENT BY ID
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    // CREATE EVENT
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    // UPDATE EVENT
    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Long id,
            @RequestBody Event eventDetails) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setVenue(eventDetails.getVenue());
        event.setEventDate(eventDetails.getEventDate());
        event.setTicketPrice(eventDetails.getTicketPrice());
        event.setAvailableSeats(eventDetails.getAvailableSeats());

        return eventRepository.save(event);
    }

    // DELETE EVENT
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        eventRepository.delete(event);

        return "Event deleted successfully";
    }
}