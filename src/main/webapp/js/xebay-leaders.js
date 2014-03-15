'use strict';

angular.module('xebayApp').controller('leadersController', ['$scope', '$http', 'Xebay', function ($scope, $http, Xebay) {

    $scope.xebay = Xebay;

    $scope.leaders = [];

    $scope.fetch = function() {
        $http.get('/rest/users/publicUsers').success(function(data){
            $scope.leaders = data;
        });
    };

    $scope.fetch();

}]);
