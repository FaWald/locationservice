package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;
import java.text.DecimalFormat;

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
        Location nearestLocation = Collections.min(knownLocations, Comparator.comparingDouble(l -> l.distanceTo(new Location("", latitude, longitude))));
        Double azimuth = nearestLocation.directionTo(new Location("", latitude, longitude));
        String direction = "";
        if (azimuth >= 337.5 || azimuth < 22.5) {
            direction = "Nördlich";
        } else if (azimuth >= 22.5 && azimuth < 67.5) {
            direction = "Nordöstlich";
        } else if (azimuth >= 67.5 && azimuth < 112.5) {
            direction = "Östlich";
        } else if (azimuth >= 112.5 && azimuth < 157.5) {
            direction = "Südöstlich";
        } else if (azimuth >= 157.5 && azimuth < 202.5) {
            direction = "Südlich";
        } else if (azimuth >= 202.5 && azimuth < 247.5) {
            direction = "Südwestlich";
        } else if (azimuth >= 247.5 && azimuth < 292.5) {
            direction = "Westlich";
        } else if (azimuth >= 292.5 && azimuth < 337.5) {
            direction = "Nordwestlich";
        } else {
            direction = "Unbekannte Richtung";
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String name = direction + " von " + nearestLocation.getName() + " " + decimalFormat.format(nearestLocation.distanceTo(new Location("", latitude, longitude))) + " km";
        Location location = new Location(name, latitude, longitude);

        // Höhe für die Location abrufen
        Optional<Double> altitude = srtmFile.getAltitudeForLocation(location);

        // JSON-String erstellen
        String json = "{\"loc\":{\"name\":\"" + name + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\"},\"altitude\":\"" + altitude.get() + "\"}";

        // ResponseEntity mit JSON-String als Körper zurückgeben
        return ResponseEntity.ok().body(json);
    }

    /**
     *
     * @param latitude_first_place
     * @param longitude_first_place
     * @param latitude_second_place
     * @param longitude_second_place
     * @param elevation_profile_points
     * @return
     */
    @GetMapping("/elevationprofile")
    public ResponseEntity<List<Double>> getElevationProfileBetween2Places(@RequestParam(value = "latitude_first") double latitude_first_place, @RequestParam(value = "longitude_first") double longitude_first_place, @RequestParam(value = "latitude_second") double latitude_second_place, @RequestParam(value = "longitude_second") double longitude_second_place, @RequestParam(value = "elevationprofilepoints") int elevation_profile_points) {
        //First Location
        Location first_location = new Location("First Place",latitude_first_place, longitude_first_place);
        Location second_location = new Location("Second Place",latitude_second_place, longitude_second_place);

        // Calculate the intermediate locations between the two points
        List<Location> intermediateLocations = first_location.calculateIntermediatelocations(second_location, elevation_profile_points);

        // Get the elevation information for each intermediate location
        List<Double> elevations = new ArrayList<>();
        for (Location intermediateLocation : intermediateLocations) {
            File file = new File("src/main/resources/srtm_40_03.asc");
            SrtmFile srtmFile = new SrtmFile(file);
            Optional<Double> altitude = srtmFile.getAltitudeForLocation(intermediateLocation); //Here is starting the 'bottleneck'
            elevations.add(altitude.get());
        }

        // Return the elevation profile
        System.out.println(elevations);
        return ResponseEntity.ok(elevations);
        /*
        localhost:8080/elevationprofile?latitude_first=&longitude_first=&latitude_second=&longitude_second=&elevationprofilepoints=
        Works: localhost:8080/elevationprofile?latitude_first=47&longitude_first=15&latitude_second=47&longitude_second=16&elevationprofilepoints=10
        localhost:8080/elevationprofile?latitude_first=47.0&longitude_first=15.0&latitude_second=47.0&longitude_second=16.0&elevationprofilepoints=10

        */


    }

}



