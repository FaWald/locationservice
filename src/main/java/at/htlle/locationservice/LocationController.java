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
        File file = new File("src/main/resources/srtm_40_03.asc");

        // Als File - Objekt der SrtmFile.java übergeben für die Datenverarbeitung
        SrtmFile srtmFile = new SrtmFile(file);

        // Location
        String name = "Picked location";
        Location location = new Location(name, latitude, longitude);

        // Höhe für die Location abrufen
        Optional<Double> altitude = srtmFile.getAltitudeForLocation(location);

        // JSON-String erstellen
        String json = "{\"loc\":{\"name\":\"" + name + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\"},\"altitude\":\"" + altitude.get() + "\"}";

        // ResponseEntity mit JSON-String als Körper zurückgeben
        return ResponseEntity.ok().body(json);
    }


}
