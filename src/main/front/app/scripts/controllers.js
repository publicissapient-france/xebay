'use strict';

var bidControllers = angular.module('bidControllers', []);

bidControllers.controller('LeaderboardCtrl', ['$scope', '$resource',
  function ($scope, $resource) {
    $scope.currentBidOffer = $resource('currentBidOffer').get();
  }]);