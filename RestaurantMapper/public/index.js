//Initialize OneMap and select one style
//The parameters are Div name,Map style(default,night grey,original),Zoom, Latitude, Longitude and Opacity
const map = onemap.initializeMap("map", "default", 11, 1.3, 103.8, 0.8);

// const center = L.bounds([1.56073, 104.11475], [1.16, 103.502]).getCenter();
// const map = L.map("map").setView([center.x, center.y], 12);

// const basemap = L.tileLayer(
//   "https://maps-{s}.onemap.sg/v3/Night/{z}/{x}/{y}.png",
//   {
//     detectRetina: true,
//     maxZoom: 18,
//     minZoom: 11,
//   }
// );

// map.setMaxBounds([
//   [1.56073, 104.1147],
//   [1.16, 103.502],
// ]);

// basemap.addTo(map);

//Add Layer at the back
const backLayer = onemap.addBackLayer(
  map,
  L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    detectRetina: true,
    attribution: "© OpenStreetMap contributors",
    maxZoom: 18,
    minZoom: 0,
    opacity: 1,
  })
);

//Add Layer at the front
const frontLayer = onemap.addFrontLayer(
  map,
  L.tileLayer("https://maps-{s}.onemap.sg/v3/Default/{z}/{x}/{y}.png", {
    detectRetina: true,
    attribution: "© OneMap",
    maxZoom: 18,
    minZoom: 0,
    opacity: 0.5,
  })
);

//Layer to be removed later
const removeLayer = onemap.addFrontLayer(
  map,
  L.tileLayer("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    detectRetina: true,
    attribution: "© OpenStreetMap contributors",
    maxZoom: 18,
    minZoom: 0,
    opacity: 1,
  })
);
//Removed Layer from Map
onemap.removeLayer(map, removeLayer);

//Setup configuration for REST API Services (Your Access Token)
//Our Documentation @  https://docs.onemap.sg
onemap.config(
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjgxNjEsInVzZXJfaWQiOjgxNjEsImVtYWlsIjoieWluZ2tlYXR3b25AZ21haWwuY29tIiwiZm9yZXZlciI6ZmFsc2UsImlzcyI6Imh0dHA6XC9cL29tMi5kZmUub25lbWFwLnNnXC9hcGlcL3YyXC91c2VyXC9zZXNzaW9uIiwiaWF0IjoxNjM2NTI1Nzk0LCJleHAiOjE2MzY5NTc3OTQsIm5iZiI6MTYzNjUyNTc5NCwianRpIjoiOWZjMzg0Njc2NTViM2RjMjc5ZGYxN2U1NWY4ZjFiZjEifQ.KYtmL1fJ-RBzisSM-9co064qA7CtUll-pteib4-_jwA"
);

//Call Theme Services
const themeObj = onemap.retrieveTheme("kindergartens");

//Convert Themes that are Points into GeoJSON for overlaying
const data = onemap.pointTheme2GeoJSON(themeObj);
console.log(data);
//Add GeoJSON to map
const geojson = L.geoJSON(data, {
  onEachFeature: function (feature, layer) {
    layer.bindPopup(
      "<h1>" +
        feature.properties.NAME +
        "</h1><p>Description: " +
        feature.properties.DESCRIPTION +
        "</p>"
    );
    //Sets Icon information
    const icon = L.icon({
      iconUrl: feature.properties.ICON,
    });
    layer.setIcon(icon);
  },
});

geojson.addTo(map);

//Fit bound to markers
map.fitBounds(geojson.getBounds());
