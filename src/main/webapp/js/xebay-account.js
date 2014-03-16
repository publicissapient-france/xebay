'use strict';

angular.module('xebayApp').controller('accountController', ['$scope', '$http', '$cookies', 'Xebay', function ($scope, $http, $cookies, Xebay) {

    $scope.template = "tmpl/header.html";

    $scope.xebay = Xebay;

    $scope.signIn = function () {
        $http.get('/rest/users/info', {
            headers: {"Authorization" : $scope.xebay.userInfo.key}
        }).success(function(data) {
            $scope.xebay.userInfo = data;
            $cookies.xebay = $scope.xebay.userInfo.key;
            $scope.xebay.userInfo.logged = true;
            if ($scope.xebay.userInfo.name === "admin") {
                $scope.xebay.userInfo.admin = true;
            }
        }).error(function (data) {
            $scope.xebay.userInfo.loginFailed = true;
        });
    };

    $scope.signOut = function () {
        $scope.xebay.userInfo = {};
        delete $cookies.xebay;
    };

    $scope.sendOffer = function (item, value) {
        var itemOffer = {itemName: item.name, value: value};
        $http.post("/rest/bidEngine/offer", itemOffer, {
            headers: {"Authorization": $scope.xebay.userInfo.key}
        }).success(function () {
            // TODO
        });
    };

    $scope.connect = function () {
        $scope.xebay.userInfo.connected = true;
        // TODO
    };

    $scope.disconnect = function () {
        $scope.xebay.userInfo.connected = false;
        // TODO
    };

    if ($cookies.xebay) {
        $scope.xebay.userInfo = {};
        $scope.xebay.userInfo.key = $cookies.xebay;
        $scope.signIn();
    }

}]);
