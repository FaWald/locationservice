package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping
public class LocationController {

    private final List<Location> knownLocations;

    public LocationController() {
        knownLocations = new ArrayList<>();
        knownLocations.add(new Location("Leoben", 47.383333, 15.1));
        knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
        knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
        knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
    }

    @GetMapping("/locations")
    public ResponseEntity<Location> getLocationByName(@RequestParam(value = "name", defaultValue = "World") String name) {
        for (Location location : knownLocations) {
            if (location.getName().equals(name)) {
                return new ResponseEntity<>(location, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/nextknownlocation")
    public ResponseEntity<Location> getNearestLocation(@RequestParam(value = "latitude") double latitude,
                                                       @RequestParam(value = "longitude") double longitude) {
        Location nearest = Collections.min(knownLocations, Comparator.comparingDouble(l -> l.distanceTo(new Location("", latitude, longitude))));
        return new ResponseEntity<>(nearest, HttpStatus.OK);
    }

}
