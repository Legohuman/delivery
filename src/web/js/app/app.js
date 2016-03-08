var _deliveryApp = (function (undefined) {
    'use strict';

    return angular
        .module('DeliveryApp', ['ngMaterial', 'ngMessages'])
        .constant('conf', {
            baseUrl: 'http://localhost:8080'
        });
})();

