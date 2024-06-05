package com.example.issband.Domain;

public class Ticket extends Entity{
    private Event event;
    private Audience audience;
    //TODO sterge audience ul de tot
    public Ticket(int id, Event event, Audience audience) {
        super(id);
        this.event = event;
        this.audience = audience;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "event=" + event +
                ", audience=" + audience +
                '}';
    }
}
