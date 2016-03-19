(function() {
  'use strict';

  angular
    .module('web')
    .factory('CalculationService', CalculationService);

  /** @ngInject */
  function CalculationService($http) {
    var host = 'http://localhost:8080';

    var service = {
      calculatePacking: calculatePacking
    };

    return service;

    function calculatePacking(data) {
      return $http.post(host + '/packing', data)
    }
  }
})();
