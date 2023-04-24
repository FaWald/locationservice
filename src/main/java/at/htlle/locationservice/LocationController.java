package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final List<Location> knownLocations = new ArrayList<>();

    static {
        knownLocations.add(new Location("Leoben", 47.383333, 15.1));
        knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
        knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
        knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Location> getLocationByName(@PathVariable String name) {
        for (Location location : knownLocations) {
            if (location.getName().equals(name)) {
                return new ResponseEntity<>(location, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        // Hier könnten wir die empfangene Location-Instanz in einer Datenbank speichern oder
        // anderweitig verarbeiten. Hier geben wir einfach die empfangene Location-Instanz
        // zurück, um zu zeigen, dass die Erstellung erfolgreich war.
        knownLocations.add(location);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }
}
