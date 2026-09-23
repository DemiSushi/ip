import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline date/time.
 */
public class Deadline extends Task {
    protected String byRaw;
    protected LocalDate byDate;

    public Deadline(String description, String by) {
        super(description);
        this.byRaw = by;
        try {
            // Parses standard ISO dates e.g., 2026-10-15
            this.byDate = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            // Falls back gracefully if input is arbitrary text e.g., "Sunday" or "no idea :-p"
            this.byDate = null;
        }
    }

    public String getBy() {
        return byRaw;
    }

    public LocalDate getByDate() {
        return byDate;
    }

    @Override
    public String toString() {
        String formattedDate = byDate != null
                ? byDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                : byRaw;
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}

