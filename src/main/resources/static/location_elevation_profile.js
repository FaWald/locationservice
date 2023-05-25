function fetchData() {
    var latitudeFirst = document.getElementById('latitude_first').value;
    var longitudeFirst = document.getElementById('longitude_first').value;
    var latitudeSecond = document.getElementById('latitude_second').value;
    var longitudeSecond = document.getElementById('longitude_second').value;

    var url = `http://localhost:8080/elevationprofile?latitude_first=${latitudeFirst}&longitude_first=${longitudeFirst}&latitude_second=${latitudeSecond}&longitude_second=${longitudeSecond}&elevationprofilepoints=16`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            var yData = data.slice(0, 16); // Y-Achsen-Daten aus dem Array extrahieren

            var layout = {
                title: 'Höhenprofildiagramm',
                xaxis: {
                    title: 'Entfernung',
                    showgrid: false
                },
                yaxis: {
                    title: 'Höhe',
                    showline: false
                }
            };

            var trace = {
                x: Array.from({length: yData.length}, (_, i) => i + 1), // X-Achsen-Daten erzeugen
                y: yData,
                type: 'scatter'
            };

            var chartData = [trace];

            Plotly.newPlot('chart', chartData, layout);
        })
        .catch(error => {
            console.log('Fehler beim Abrufen der Daten:', error);
        });
}