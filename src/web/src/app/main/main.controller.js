(function() {
  'use strict';

  angular
    .module('web')
    .controller('MainController', MainController);

  /** @ngInject */
  function MainController(toastr) {
    var vm = this;

    vm.creationDate = 1458385002457;
    vm.showToastr = showToastr;

    angular.element('a[href*=#]')
      .on('click', function (e) {
        e.preventDefault();
        $('html, body').animate(
          { scrollTop: $($(this).attr('href')).offset().top }, 700, 'linear');
    });

    //function initialize() {
    //  var myLatlng = new google.maps.LatLng(-34.397, 150.644);
    //  var myOptions = {
    //    zoom: 8,
    //    center: myLatlng,
    //    mapTypeId: google.maps.MapTypeId.ROADMAP
    //  }
    //  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    //};
    //var directionsDisplay;
    //var directionsService = new google.maps.DirectionsService();
    //var map;
    //
    //function initMap() {
    //  var directionsDisplay = new google.maps.DirectionsRenderer;
    //  var directionsService = new google.maps.DirectionsService;
    //  var map = new google.maps.Map(document.getElementById('map'), {
    //    zoom: 14,
    //    center: {lat: 37.77, lng: -122.447}
    //  });
    //  directionsDisplay.setMap(map);
    //
    //  calculateAndDisplayRoute(directionsService, directionsDisplay);
    //  document.getElementById('mode').addEventListener('change', function() {
    //    calculateAndDisplayRoute(directionsService, directionsDisplay);
    //  });
    //}
    //
    //function calculateAndDisplayRoute(directionsService, directionsDisplay) {
    //  var selectedMode = document.getElementById('mode').value;
    //  directionsService.route({
    //    origin: {lat: 37.77, lng: -122.447},  // Haight.
    //    destination: {lat: 37.768, lng: -122.511},  // Ocean Beach.
    //    // Note that Javascript allows us to access the constant
    //    // using square brackets and a string value as its
    //    // "property."
    //    travelMode: google.maps.TravelMode[selectedMode]
    //  }, function(response, status) {
    //    if (status == google.maps.DirectionsStatus.OK) {
    //      directionsDisplay.setDirections(response);
    //    } else {
    //      window.alert('Directions request failed due to ' + status);
    //    }
    //  });
    //}

    function showToastr() {
      toastr.info('Fork <a href="https://github.com/Swiip/generator-gulp-angular" target="_blank"><b>generator-gulp-angular</b></a>');
      vm.classAnimation = '';
    }
  }
})();
