'use strict';

angular.module('xebayApp').controller('accountController', ['$scope', 'Xebay', function ($scope, Xebay) {

    $scope.xebay = Xebay;

}]);
