package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping
public class LocationController {

    private final List<Location> knownLocations;
    private SrtmFile srtmFile;

    public LocationController() {
        knownLocations = new ArrayList<>();
        knownLocations.add(new Location("Leoben", 47.383333, 15.1));
        knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
        knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
        knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
    }

    /**
     * Diese Methode definiert einen Endpunkt für einen GET-Request auf "/locations", der eine Location zurückgibt, deren Name dem angegebenen Namen entspricht.
     * Wenn keine Location mit diesem Namen gefunden wird, wird ein HTTP-Statuscode "404 Not Found" zurückgegeben.
     *
     * @param name der Name der Location, die zurückgegeben werden soll. Wenn dieser Parameter nicht angegeben wird, wird standardmäßig "World" verwendet.
     * @return eine ResponseEntity-Instanz, die die gefundene Location und den HTTP-Statuscode enthält.
     */
    @GetMapping("/locations")
    public ResponseEntity<Location> getLocationByName(@RequestParam(value = "name", defaultValue = "World") String name) {
        for (Location location : knownLocations) {
            if (location.getName().equals(name)) {
                return new ResponseEntity<>(location, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * Handler-Methode für die "GET /nextknownlocation" Anforderung. Gibt die nächstgelegene bekannte Location
     * basierend auf der gegebenen Längen- und Breitengradkoordinaten zurück.
     *
     * @param latitude  die Längengradkoordinate des aktuellen Standorts
     * @param longitude die Breitengradkoordinate des aktuellen Standorts
     * @return ResponseEntity<Location> - eine Antwort mit dem nächstgelegenen bekannten Ort und dem HTTP-Status-Code "OK"
     */
    @GetMapping("/nextknownlocation")
    public ResponseEntity<Location> getNearestLocation(@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude) {
        Location nearest = Collections.min(knownLocations, Comparator.comparingDouble(l -> l.distanceTo(new Location("", latitude, longitude))));
        return new ResponseEntity<>(nearest, HttpStatus.OK);
    }

    @GetMapping("/altitudeof")
    public ResponseEntity<String> altitude(@RequestParam(value = "latitude") double latitude, @RequestParam(value = "longitude") double longitude) {
        // Auslesen von srtm_40_03.asc
        File file = new File("srtm_40_03.asc");

        // Als File - Objekt der SrtmFile.java übergeben für die Datenverarbeitung
        SrtmFile srtmFile = new SrtmFile(file);

        // Location
        String name = "Picked location";
        Location location = new Location(name, latitude, longitude);

        // Höhe für die Location abrufen
        Optional<Double> altitude = srtmFile.getAltitudeForLocation(location);

        // JSON-String erstellen
        String json = "{\"loc\":{\"name\":\"" + name + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\"},\"altitude\":\"" + altitude + "\"}";

        // ResponseEntity mit JSON-String als Körper zurückgeben
        return ResponseEntity.ok().body(json);
    }


}
