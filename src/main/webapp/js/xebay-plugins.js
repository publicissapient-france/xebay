'use strict';

angular.module('xebayApp').controller('pluginsController', ['$scope', '$http', '$xebay', function ($scope, $http, $xebay) {

    $scope.xebay = $xebay;

    $scope.plugins = [{name:'AllItemsInCategory', status:false}, {name:'BankBuyEverything', status:false}];

    $scope.toggleStatus = function(plugin, status) {
        $http({
            method:'PATCH',
            url:"/rest/bidEngine/plugin/" + plugin.name + "?active=" + status,
            headers: {"Authorization": $xebay.userInfo.key}
        }).success(function(){
            plugin.status = status;
        });
    };

}]);
