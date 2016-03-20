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
    function D3Controller(CalculationService, RenderService, util) {
      var vm = this;
      vm.addProduct = addProduct;
      vm.paint = paint;
      vm.deleteProduct = deleteProduct;

      vm.loading = false;

      vm.data = {
        products: [
          {
            uuid: util.uuid(),
            code: '1105 10 000 0',
            length: 1,
            width: 1,
            height: 1,
            count: 1
          },
          {
            uuid: util.uuid(),
            code: '1521 10 000 0',
            length: 0.5,
            width: 0.5,
            height: 0.5,
            count: 1
          },
          {
            uuid: util.uuid(),
            code: '1602 49 110 0',
            length: 0.5,
            width: 0.5,
            height: 0.5,
            count: 1
          }
        ]
      };

      function addProduct() {
        vm.data.products.push({
          uuid: util.uuid(),
          code: '',
          length: 1,
          width: 1,
          height: 1,
          count: 1
        })
      }

      function deleteProduct(uuid) {
        var elemIndex = vm.data.products.findIndex(function (e) {
          return e.uuid === uuid;
        });
        vm.data.products.splice(elemIndex, 1);
      }


      function paint() {
        vm.loading = !vm.loading;
        CalculationService.calculatePacking(vm.data.products).then(function (response) {
          console.log(response.data);
          RenderService.update(response.data);
          vm.loading = !vm.loading;
        });
      }

      setTimeout(function () {
        RenderService.start();
      }, 400);

    }
  }

})();
