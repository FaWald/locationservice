function searchLocation() {
    // Lese den eingegebenen Ortsnamen aus dem Eingabefeld
    var locationName = document.getElementById("locationName").value;

    // Erstelle die URL für den Webservice-Aufruf
    var url = "http://localhost:8080/locations?name=" + encodeURIComponent(locationName);

    // Erstelle die XHR-Anfrage an den Webservice
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Verarbeite die empfangenen Daten
            var location = JSON.parse(xhr.responseText);

            // Aktualisiere die HTML-Seite mit den Koordinaten
            document.getElementById("latitude").textContent = location.latitude;
            document.getElementById("longitude").textContent = location.longitude;

            // Erstelle den Link zu Google Maps
            var mapLink = "https://www.google.com/maps/search/?api=1&query=" + location.latitude + "," + location.longitude;
            document.getElementById("mapLink").href = mapLink;
            document.getElementById("mapLink").style.display = "block";
        }
    };
    xhr.send();
}