'use strict';

angular.module('xebayApp').controller('usersController', ['$scope', '$http', '$xebay', function ($scope, $http, $xebay) {

    $scope.xebay = $xebay;

    $scope.users = [];

    $scope.fetch = function () {
        $http.get("/rest/users", {
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function(data) {
            $scope.users = data;
        });
    };

    $scope.register = function() {
        $http.get("/rest/users/register?name=" + $scope.newUserName, {
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function(data) {
            $scope.fetch();
        });
    };

    $scope.unregister = function (key) {
        $http.delete("/rest/users/unregister?key=" + key, {
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function(data){
            $scope.fetch();
        });
    };

    $scope.fetch();

}]);
