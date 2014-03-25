'use strict';

angular.module('xebayApp').controller('playController', ['$scope', '$http', '$timeout', '$xebay', function ($scope, $http, $timeout, $xebay) {

    $scope.xebay = $xebay;

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
        if (timeToLive && timeToLive >= 0) {
            var timeToLiveSeconds = timeToLive / 1000;
            $scope.bidOffer.timeToLiveSeconds = (timeToLiveSeconds < 10 ? "0" : "") + timeToLiveSeconds.toFixed(1);
            $scope.bidOffer.timeToLive -= 100;
            $scope.timeout = $timeout($scope.updateBidOffer, 100);
        } else {
            $scope.getBidOffer();
        }
    };

    $scope.sendBidOffer = function (increment) {
        var bidOffer = {itemName: $scope.bidOffer.item.name, value: $scope.bidOffer.item.value + increment};
        $http.post("/rest/bidEngine/bid", bidOffer, {
            headers: {"Authorization": $xebay.userInfo.key}
        });
    };

    $scope.$on('$xebay.bidOffer', function (event, bidOffer) {
        $scope.bidOffer = bidOffer;
    });

    $scope.$on('$destroy', function () {
        if ($scope.timeout) {
            $timeout.cancel($scope.timeout);
        }
    });

    $scope.getBidOffer();

}]);
