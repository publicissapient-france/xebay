'use strict';

angular.module('xebayApp').controller('accountController', ['$scope', '$http', '$cookies', '$xebay', function ($scope, $http, $cookies, $xebay) {

    $scope.template = "tmpl/header.html";

    $scope.xebay = $xebay;

    $scope.signIn = function () {
        $http.get('/rest/users/info', {
            headers: {"Authorization" : $scope.xebay.userInfo.key}
        }).success(function(data) {
            $xebay.userInfo = data;
            $cookies.xebay = $xebay.userInfo.key;
            $xebay.userInfo.logged = true;
            if ($xebay.userInfo.name === "admin") {
                $xebay.userInfo.admin = true;
            }
            $xebay.connect();
        }).error(function (data) {
            $xebay.userInfo.loginFailed = true;
            $xebay.error(data);
        });
    };

    $scope.signOut = function () {
        $xebay.userInfo = {};
        delete $cookies.xebay;
        $xebay.disconnect();
    };

    $scope.sendOffer = function (item, value) {
        var itemOffer = {itemName: item.name, value: value};
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
