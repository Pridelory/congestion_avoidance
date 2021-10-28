package entity;

/**
 * This class is an implementation of the event object to be inserted in the
 * event list. In the current version it contains just an attribute, the clock value
 * at which the event occurs.
 * It also contains a set of get() and set() methods to retrieve and modify its clock value.
 * The student has to define the constructor of this class and other eventual additional attributes
 * that are necessary to uniquely define the event.
 *
 * @author Daniele Tafani 10/10/09
 */
public class Event {

    /**
     * The clock value of the event.
     */
    private double eventClock;

    /**
     * The type of the event
     * <p>
     * 0 -- arrival
     * 1 -- departure
     * 2 -- statistics
     */
    private Integer eventType;

    /**
     * Constructor of the class
     */
    public Event(double eventClock, Integer eventType) {
        this.eventClock = eventClock;
        this.eventType = eventType;
    }

    public double getEventClock() {
        return eventClock;
    }

    public void setEventClock(double eventClock) {
        this.eventClock = eventClock;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }
}
