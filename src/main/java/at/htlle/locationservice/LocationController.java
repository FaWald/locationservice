package at.htlle.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final List<Location> knownLocations;

    public LocationController() {
        knownLocations = new ArrayList<>();
        knownLocations.add(new Location("Leoben", 47.383333, 15.1));
        knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
        knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
        knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
    }

    @GetMapping
    public ResponseEntity<Location> getLocationByName(@RequestParam(value = "name", defaultValue = "World") String name) {
        for (Location location : knownLocations) {
            if (location.getName().equals(name)) {
                return new ResponseEntity<>(location, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /*@GetMapping("/{name}/distance")
    public ResponseEntity<Double> getDistanceToKnownLocation(@PathVariable String name, @RequestParam Double latitude, @RequestParam Double longitude) {
        Location location = getLocationByName(name).getBody();
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Location otherLocation = new Location("Custom Location", latitude, longitude);
            Double distance = location.distanceTo(otherLocation);
            return new ResponseEntity<>(distance, HttpStatus.OK);
        }
    }*/
}
