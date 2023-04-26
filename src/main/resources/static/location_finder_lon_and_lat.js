$(document).ready(function() {
    $("#find-location").click(function() {
        var latitude = $("#latitude").val();
        var longitude = $("#longitude").val();
        $.ajax({
            url: "/nextknownlocation?latitude=" + latitude + "&longitude=" + longitude,
            success: function(result) {
                $("#location-result").html("Found location: " + result.name);
                var mapLinks = '<a href="https://www.google.com/maps/search/?api=1&query=' +
                    result.latitude + ',' + result.longitude + '">Location Link</a><br>' +
                    '<a href="https://www.google.com/maps/dir/?api=1&destination=' +
                    result.latitude + ',' + result.longitude + '&origin=' +
                    latitude + ',' + longitude + '">Route Link</a>';
                $("#map-links").html(mapLinks);
            }
        });
    });
});
