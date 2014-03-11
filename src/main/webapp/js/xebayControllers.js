'use strict';

var xebayControllers = angular.module('xebayControllers', []);

xebayControllers.controller('userController', ['$scope', '$http', '$cookies', 'UserData', function ($scope, $http, $cookies, UserData) {

    $scope.template = "tmpl/header.html";

    $scope.userInfo = UserData;

    $scope.signIn = function () {
        $http.get('/rest/users/info', { headers: {"Authorization":$scope.userInfo.key} }).success(function(data) {
            $scope.userInfo = data;
            $cookies.xebay = $scope.userInfo.key;
            $scope.userInfo.logged = true;
            if ($scope.userInfo.name === "admin") {
                $scope.userInfo.role = "admin";
            }
        }).error(function (data) {
            $scope.userInfo.loginFailed = true;
        });
    };

    $scope.signOut = function () {
        $scope.userInfo = {};
        delete $cookies.xebay;
    };

    $scope.connect = function () {
        $scope.userInfo.connected = true;
        // TODO
    };

    $scope.disconnect = function () {
        $scope.userInfo.connected = false;
        // TODO
    };

    if ($cookies.xebay) {
        $scope.userInfo.key = $cookies.xebay;
        $scope.signIn();
    }

}]);

xebayControllers.controller('playController', ['$scope', '$http', '$timeout', 'UserData', function ($scope, $http, $timeout, UserData) {

    $scope.userData = UserData;

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


xebayControllers.controller('settingsController', ['$scope', '$http', '$timeout', 'UserData', function ($scope, $http, $timeout, UserData) {

    $scope.userData = UserData;

}]);
