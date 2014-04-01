'use strict';

angular.module('xebayApp').controller('pluginsController', ['$scope', '$http', '$xebay', function ($scope, $http, $xebay) {

    $http({
        method: 'GET',
        url: "/rest/bidEngine/news",
        headers: {"Authorization": $xebay.userInfo.key}
    }).success(function (data) {
        $scope.plugins = data;
    });

    $scope.toggleActivation = function (plugin) {
        $http({
            method: 'PATCH',
            url: "/rest/bidEngine/plugin/" + plugin.name + "/" + (plugin.activated ? "deactivate" : "activate"),
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function () {
            plugin.activated = !plugin.activated;
        });
    };

}]);
