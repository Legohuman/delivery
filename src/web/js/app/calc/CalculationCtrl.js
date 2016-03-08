(function (app, undefined) {
    'use strict';

    app.controller('CalculationCtrl', function (CalculationService, util, $scope) {
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

        this.addProduct = function () {
            $scope.data.products.push({
                uuid: util.uuid(),
                code: '',
                length: 1000,
                width: 1000,
                height: 1000
            })
        };

        this.deleteProduct = function (uuid) {
            var elemIndex = $scope.data.products.findIndex(function (e) {
                return e.uuid === uuid;
            });
            $scope.data.products.splice(elemIndex, 1);
        };

        this.calculatePacking = function () {
            CalculationService.calculatePacking($scope.data.products).then(function (response) {
                console.log(response.data);
            });
        }
    });
})(_deliveryApp);

