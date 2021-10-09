
import java.util.Date;

public class MeasureNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MeasureNotFoundException(Date creationDateTime) {
        super("Could not find measure created on: " + creationDateTime);
    }
}
