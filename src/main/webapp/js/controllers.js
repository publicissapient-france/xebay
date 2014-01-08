'use strict';

var bidControllers = angular.module('bidControllers', []);

bidControllers.controller('LeaderboardCtrl', ['$scope', '$resource',
  function ($scope, $resource) {
    $scope.currentBidOffer = $resource('rs/bidEngine').get();
  }]);

bidControllers.controller('RegisterCtrl', ['$scope',
  function ($scope) {
    console.log('hello');
  }]);
