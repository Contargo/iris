//leaflet map
var map;

//leaflet marker
var marker;

// id of latitude element
var latId;

//id of longitude element
var lonId;

/**
 * Displays a modal dialog with the current (input fields) geo coordinates.
 * If there are no coords, default coords are used (germany).
 * 
 * @param latId latitude id
 * @param lonId longitude id
 */
function showMap(latId, lonId) {

    this.latId = latId;
    this.lonId = lonId;
    
    //show modal dialog
    $('#mappicker').modal();
    
    if (!map) {
        //init map
        initializeMap();
    }
    
    //set only new values, if there are some
    if (latCoord != "" && lonCoord != "") {

        var latCoord = getValueFromGeofield(latId);
        var lonCoord = getValueFromGeofield(lonId);
        var zoom = 13;
        
        var latlng = new L.LatLng(latCoord, lonCoord);
        console.log("set new coordinates: " + latlng.toString());

        map.setView(latlng, zoom, true);
        marker.setLatLng(latlng);
        marker.bindPopup("Coordinates: " + latlng.toString()).openPopup();
        marker.update();
    }
}

/**
 *  Update for input fields and hide the dialog.
 */
function selectCoordinates() {
    
    console.log("Update field " + latId + " with value: " + marker.getLatLng().lat);
    console.log("Update field " + lonId + " with value: " + marker.getLatLng().lng);
    
    //update input fields
    $('#' + latId).attr('value',marker.getLatLng().lat);
    $('#' + lonId).attr('value',marker.getLatLng().lng);
    
    //hide dialog
    $('#mappicker').modal('hide');
}

/**
 * Get the value (lat / lon) from input field, by given id.
 * 
 * @param id
 */
function getValueFromGeofield(id) {

    return $('#' + id).attr('value');
}


function initializeMap() {

    //init mappicker for germany
    var latCoord = 51.0;
    var lonCoord = 10.0;
    var zoom = 6;
    
    // create new map
    map = new L.Map('map', {
        center: [latCoord, lonCoord],
        zoom: zoom
    });

    var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
    var osmAttrib='Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>';
    var osm = new L.tileLayer(osmUrl,{
        minZoom:4,
        maxZoom:18,
        attribution:osmAttrib
    });

    map.addLayer(osm);

    var latlng = new L.LatLng(latCoord, lonCoord);
    console.log("init marker with coordinates: " + latlng.toString());
    
    marker = new L.marker(latlng, {draggable: true}).addTo(map);
    marker.bindPopup("Select geolocation").openPopup();
    marker.on('dragend', function(){
        marker.bindPopup("Coordinates: " + marker.getLatLng().toString()).openPopup();
    });
}



