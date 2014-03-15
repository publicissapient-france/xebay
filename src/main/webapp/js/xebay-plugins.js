'use strict';

angular.module('xebayApp').controller('pluginsController', ['$scope', '$http', 'Xebay', function ($scope, $http, Xebay) {

    $scope.xebay = Xebay;

    $scope.plugins = [{name:'AllItemsInCategory', status:false}, {name:'BankBuyEverything', status:false}];

    $scope.toggleStatus = function(plugin, status) {
        $http({
            method:'PATCH',
            url:"/rest/bidEngine/plugin/" + plugin.name + "?active=" + status,
            headers: {"Authorization": $scope.xebay.userInfo.key}
        }).success(function(){
            plugin.status = status;
        });
    };

}]);
