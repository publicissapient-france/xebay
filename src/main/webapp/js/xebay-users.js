'use strict';

angular.module('xebayApp').controller('usersController', ['$scope', '$http', 'Xebay', function ($scope, $http, Xebay) {

    $scope.xebay = Xebay;

    $scope.users = [];

    $scope.fetch = function () {
        $http.get("/rest/users", {
            headers: {"Authorization": $scope.xebay.userInfo.key}
        }).success(function(data) {
            $scope.users = data;
        });
    };

    $scope.register = function() {
        $http.get("/rest/users/register?name=" + $scope.newUserName, {
            headers: {"Authorization": $scope.xebay.userInfo.key}
        }).success(function(data) {
            $scope.fetch();
        });
    };

    $scope.unregister = function (key) {
        $http.delete("/rest/users/unregister?key=" + key, {
            headers: {"Authorization": $scope.xebay.userInfo.key}
        }).success(function(data){
            $scope.fetch();
        });
    };

    $scope.fetch();

}]);
