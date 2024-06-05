package com.example.issband.Domain;

import java.util.ArrayList;
import java.util.Date;

public class Event extends Entity{
    private String name;
    private String date;
    private String venue;
    private ArrayList<Band> bands;
    private ArrayList<Ticket> tickets;
    private Audience audience;


    public Event(int id, String name, String date, String venue, ArrayList<Band> bands, ArrayList<Ticket> tickets, Audience audience) {
        super(id);
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.bands = bands;
        this.tickets = tickets;
        this.audience = audience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public ArrayList<Band> getBands() {
        return bands;
    }

    public void setBands(ArrayList<Band> bands) {
        this.bands = bands;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", venue='" + venue + '\'' +
                ", bands=" + bands +
                ", tickets=" + tickets +
                ", audience=" + audience +
                '}';
    }
}
