package SuperVend.model;

import java.nio.file.Path;

public class Move {
    // just a data class
    private final Path origin;
    private final Path destination;
    private final boolean copy;

    public Move(Path origin, Path destination) {
        this(origin, destination, false);
    }

    public Move(Path origin, Path destination, boolean copy) {
        this.origin = origin;
        this.destination = destination;
        this.copy = copy;
    }

    public Path getDestination() {
        return destination;
    }

    public Path getOrigin() {
        return origin;
    }

    public boolean isCopy() {
        return copy;
    }
}
