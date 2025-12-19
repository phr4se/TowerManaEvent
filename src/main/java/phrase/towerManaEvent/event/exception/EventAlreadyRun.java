package phrase.towerManaEvent.event.exception;

public class EventAlreadyRun extends RuntimeException {
    public EventAlreadyRun(String message) {
        super(message);
    }
}
