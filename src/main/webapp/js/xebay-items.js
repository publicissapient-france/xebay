'use strict';

angular.module('xebayApp').controller('itemsController', ['$scope', '$http', 'Xebay', function ($scope, $http, Xebay) {

    $scope.xebay = Xebay;

}]);
