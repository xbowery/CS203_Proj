//Initialize OneMap and select one style
//The parameters are Div name,Map style(default,night grey,original),Zoom, Latitude, Longitude and Opacity
const map = onemap.initializeMap("map", "default", 11, 1.3, 103.8, 0.8);

// Add Layer at the back
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

// //Layer to be removed later
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

axios
  .get(
    "https://swisshack-restaurantmapper.azurewebsites.net/api/v1/restaurants"
  )
  .then((res) => {
    addPinsToMap(res.data);
  })
  .catch((err) => {
    console.error(err);
  });

//Add GeoJSON to map
const addPinsToMap = (data) => {
  const geojson = L.geoJSON(data, {
    onEachFeature: function (feature, layer) {
      const { name, loc, description, numCase } = feature.properties;
      layer.bindPopup(
        "<h1>" +
          name +
          " @ " +
          loc +
          "</h1><p>Description: " +
          description +
          "</p>" +
          "<p>Case Numbers: " +
          numCase +
          "</p>"
      );
      //Sets Icon information
      const icon = L.icon({
        iconUrl:
          numCase < 250 ? "/public/food-icon.png" : "/public/food-icon-red.png",
      });
      layer.setIcon(icon);
    },
  });

  geojson.addTo(map);

  //Fit bound to markers
  map.fitBounds(geojson.getBounds());
};
