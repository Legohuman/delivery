(function() {
  'use strict';

  angular
    .module('web')
    .directive('d3Section', d3Section)
//test remove after
    .factory('util', function () {
      return {
        uuid: function () {
          return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : r & 0x3 | 0x8;
            return v.toString(16);
          });
        }

      }
    });


  /** @ngInject */
  function d3Section() {
    var directive = {
      restrict: 'E',
      templateUrl: 'app/components/d3Section/d3Section.html',
      scope: {
        //creationDate: '='
      },
      controller: D3Controller,
      controllerAs: 'vm',
      bindToController: true
    };

    return directive;

    /** @ngInject */
    function D3Controller(CalculationService, RenderService, $scope, util) {
      var vm = this;

      $scope.data = {
        products: [
          {
            uuid: util.uuid(),
            code: '1010.23.12',
            length: 1000,
            width: 1000,
            height: 1000
          },
          {
            uuid: util.uuid(),
            code: '1010.23.45',
            length: 1000,
            width: 1500,
            height: 1500
          }
        ]
      };


      vm.paint = paint;

      function paint() {
        CalculationService.calculatePacking($scope.data.products).then(function (response) {
          console.log(response.data);
          RenderService.update(response.data);
        });
      }

      RenderService.start();
    }
  }

})();
