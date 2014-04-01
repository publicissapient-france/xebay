'use strict';

angular.module('xebayApp').controller('headerController', ['$scope', '$http', '$cookies', '$location', '$xebay', function ($scope, $http, $cookies, $location, $xebay) {

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
        $location.path('/leaders');
    };

    if ($cookies.xebay) {
        $xebay.userInfo = {};
        $xebay.userInfo.key = $cookies.xebay;
        $scope.signIn();
    }

}]);
