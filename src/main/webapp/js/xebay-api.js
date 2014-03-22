'use strict';

angular.module('xebayApp').controller('apiController', ['$scope', '$xebay', function ($scope, $xebay) {

    $scope.xebay = $xebay;

    $scope.host = window.location.host;

}]);
