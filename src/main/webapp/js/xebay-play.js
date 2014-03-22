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

    $scope.connect = function() {
        if (!$scope.socket) {
            $scope.socket = new WebSocket('ws://' + window.location.host + '/socket/bidEngine/' + $xebay.userInfo.key);
            $scope.socket.onerror = $scope.onerror;
            $scope.socket.onmessage = $scope.onmessage;
        }
    };

    $scope.onmessage = function (message) {
        var socketMessage = JSON.parse(message.data);
        if (socketMessage.started) {
            console.info("started %O", socketMessage.started);
            $scope.bidOffer = socketMessage.started;
        }
        if (socketMessage.updated) {
            console.info("updated %O", socketMessage.updated);
            $scope.bidOffer = socketMessage.updated;
        }
        if (socketMessage.resolved) {
            console.info("resolved %O", socketMessage.resolved);
        }
        if (socketMessage.messages && socketMessage.messages.length > 0) {
            console.info("messages %O", socketMessage.messages);
        }
    };

    $scope.onerror = function() {
        $timeout($scope.connect, 5000);
    };

    $scope.disconnect = function() {
        if ($scope.socket) {
            $scope.socket.close();
            delete $scope.socket;
        }
    };

    $scope.$on('$xebay.connect', $scope.connect);

    $scope.$on('$xebay.disconnect', $scope.disconnect);

    $scope.$on('$destroy', function () {
        $scope.disconnect();
        if ($scope.timeout) {
            $timeout.cancel($scope.timeout);
        }
    });

    if ($xebay.logged) {
        $xebay.connect();
    }

    $scope.getBidOffer();

}]);
