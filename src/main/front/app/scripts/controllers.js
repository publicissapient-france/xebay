'use strict';

var bidControllers = angular.module('bidControllers', []);

bidControllers.controller('MainCtrl', ['$scope', '$resource',
  function ($scope, $resource) {
    console.log('this is where we\'ll put some logic');
    var currentBidOfferResource = $resource('currentBidOffer');

    console.log('currentBidOffer : ', currentBidOfferResource.get());
  }]);
