package at.htlle.locationservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private List<Location> knownLocations = new ArrayList<>();

    public LocationController() {
        knownLocations.add(new Location("Leoben", 47.383333, 15.1));
        knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
        knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
        knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
        knownLocations.add(new Location("Graz", 47.066667, 15.45));
        knownLocations.add(new Location("Vienna", 48.2082, 16.3738));
        knownLocations.add(new Location("Linz", 48.3064, 14.2858));
        knownLocations.add(new Location("Graz", 47.0707, 15.4395));
        knownLocations.add(new Location("Salzburg", 47.8095, 13.0550));
        knownLocations.add(new Location("Innsbruck", 47.2682, 11.3923));
        knownLocations.add(new Location("Klagenfurt", 46.6249, 14.3050));
        knownLocations.add(new Location("Villach", 46.6111, 13.8558));
        knownLocations.add(new Location("Wels", 48.1575, 14.0289));
        knownLocations.add(new Location("St. Pölten", 48.2047, 15.6256));
        knownLocations.add(new Location("Dornbirn", 47.4125, 9.7417));
        knownLocations.add(new Location("Wiener Neustadt", 47.8151, 16.2465));
        knownLocations.add(new Location("Bregenz", 47.5031, 9.7471));
        knownLocations.add(new Location("Eisenstadt", 47.8450, 16.5336));
        knownLocations.add(new Location("Leonding", 48.2606, 14.2406));
        knownLocations.add(new Location("Traun", 48.2203, 14.2333));
        knownLocations.add(new Location("Amstetten", 48.1219, 14.8747));
        knownLocations.add(new Location("Klosterneuburg", 48.3053, 16.3256));
        knownLocations.add(new Location("Schwechat", 48.1381, 16.4708));
        knownLocations.add(new Location("Ternitz", 47.7275, 16.0361));
        knownLocations.add(new Location("Baden bei Wien", 48.0069, 16.2308));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Location> getLocationByName(@PathVariable String name) {
        // Hier könnte man eine Datenbankabfrage oder eine andere Methode
        // verwenden, um die Location-Instanz mit dem angegebenen Namen zu finden.
        // Hier verwenden wir einfach eine hartcodierte Beispiel-Location.
        Location location = new Location("Beispielort", 47.1234, 11.5678);
        if (location.getName().equals(name)) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        // Hier könnten wir die empfangene Location-Instanz in einer Datenbank speichern oder
        // anderweitig verarbeiten. Hier geben wir einfach die empfangene Location-Instanz
        // zurück, um zu zeigen, dass die Erstellung erfolgreich war.
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }
}

