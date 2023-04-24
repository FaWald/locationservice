package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private List<Location> knownLocations = new ArrayList<>();

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

