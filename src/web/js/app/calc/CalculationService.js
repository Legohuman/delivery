(function (app, undefined) {
    'use strict';

    app.factory('CalculationService', function ($http, conf) {
        return {
            calculatePacking: function (data) {
                return $http.post(conf.baseUrl + '/packing', data)
            }

        }
    });
})(_deliveryApp);

