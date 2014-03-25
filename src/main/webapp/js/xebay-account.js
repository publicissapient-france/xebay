'use strict';

angular.module('xebayApp').controller('accountController', ['$scope', '$http', '$cookies', '$xebay', function ($scope, $http, $cookies, $xebay) {

    $scope.template = "tmpl/header.html";

    $scope.xebay = $xebay;

    $scope.signIn = function () {
        $http.get('/rest/users/info', {
            headers: {"Authorization" : $scope.xebay.userInfo.key}
        }).success(function(data) {
            $xebay.userInfo = data;
            $xebay.logged = true;
            $cookies.xebay = $xebay.userInfo.key;
            if ($xebay.userInfo.name === "admin") {
                $xebay.admin = true;
            }
            $xebay.connect();
        }).error(function (data) {
            $xebay.userInfo.loginFailed = true;
            $xebay.error(data);
        });
    };

    $scope.signOut = function () {
        $xebay.disconnect();
        if ($xebay.admin) {
            $xebay.admin = false;
        }
        delete $cookies.xebay;
        $xebay.logged = false;
        $xebay.userInfo = {};
    };

    $scope.sendOffer = function (item) {
        var itemOffer = {itemName: item.name, value: item.value};
        $http.post("/rest/bidEngine/offer", itemOffer, {
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function () {
            item.offered = true;
        });
    };

    if ($cookies.xebay) {
        $xebay.userInfo = {};
        $xebay.userInfo.key = $cookies.xebay;
        $scope.signIn();
    }

}]);
