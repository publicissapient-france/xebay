'use strict';

angular.module('xebayApp').controller('leadersController', ['$scope', '$http', '$xebay', function ($scope, $http, $xebay) {

    $scope.xebay = $xebay;

    $scope.leaders = [];

    $scope.fetch = function() {
        $http.get('/rest/users/publicUsers').success(function(data){
            $scope.leaders = data;
        });
    };

    $scope.fetch();

}]);
