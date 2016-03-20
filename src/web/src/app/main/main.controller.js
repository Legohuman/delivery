(function() {
  'use strict';

  angular
    .module('web')
    .controller('MainController', MainController);

  /** @ngInject */
  function MainController() {
    var vm = this;

    vm.creationDate = 1458385002457;
    vm.points = [
      {
        value: "chicago, il",
        name: "Chicago"
      },
      {
        value: "st louis, mo",
        name: "St Louis"
      },
      {
        value: "joplin, mo",
        name: "Joplin, MO"
      },
      {
        value: "oklahoma city, ok",
        name: "Oklahoma City"
      }
    ];


    angular.element('a[href*=#]')
      .on('click', function (e) {
        e.preventDefault();
        $('html, body').animate(
          { scrollTop: $($(this).attr('href')).offset().top }, 700, 'linear');
    });
  }
})();
