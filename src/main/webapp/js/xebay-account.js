'use strict';

angular.module('xebayApp').controller('accountController', ['$scope', '$http', '$cookies', '$xebay', function ($scope, $http, $cookies, $xebay) {

    $scope.xebay = $xebay;

    $scope.userInfo = function () {
        $http.get('/rest/users/info', {
            headers: {"Authorization" : $scope.xebay.userInfo.key}
        }).success(function(data) {
            $xebay.userInfo = data;
        });
    };

    $scope.sendOffer = function (item) {
        var itemOffer = {itemName: item.name, value: item.value};
        $http.post("/rest/bidEngine/offer", itemOffer, {
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function () {
            item.offered = true;
        });
    };

    $scope.$on('$xebay.bidOffer', function (event, bidOffer) {
        if (bidOffer && bidOffer.timeToLive === 0 && bidOffer.owner) {
            $scope.userInfo();
        }
    });

}]);
