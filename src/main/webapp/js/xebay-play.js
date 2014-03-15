'use strict';

angular.module('xebayApp').controller('playController', ['$scope', '$http', '$timeout', 'Xebay', function ($scope, $http, $timeout, Xebay) {

    $scope.xebay = Xebay;

    $scope.bidOffer = {};

    $scope.getBidOffer = function () {
        $http.get('/rest/bidEngine/current').success(function(data) {
            $scope.bidOffer = data;
            $scope.updateBidOffer();
        }).error(function(data){
            $timeout($scope.getBidOffer, 5000);
            $scope.bidOffer = {};
        });
    };

    $scope.updateBidOffer = function () {
        var timeToLive = $scope.bidOffer.timeToLive;
        if (timeToLive && timeToLive > 0) {
            $scope.bidOffer.timeToLiveSeconds = twoDigits(Math.floor(timeToLive / 1000));
            $scope.bidOffer.timeToLiveMilliseconds = Math.floor((timeToLive - ($scope.bidOffer.timeToLiveSeconds * 1000)) / 100);
            $scope.bidOffer.timeToLive -= 100;
            $timeout($scope.updateBidOffer, 100);
        } else {
            $scope.getBidOffer();
        }
    };

    $scope.getBidOffer();

}]);
