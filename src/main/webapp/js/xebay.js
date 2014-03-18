'use strict';

angular.module('xebayApp', ['ngRoute','ngCookies']);

angular.module('xebayApp').config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/api', {
        templateUrl: 'tmpl/api.html'
    }).when('/account', {
        templateUrl: 'tmpl/account.html',
        controller: 'accountController'
    }).when('/play', {
        templateUrl: 'tmpl/play.html',
        controller: 'playController'
    }).when('/leaders', {
        templateUrl: 'tmpl/leaders.html',
        controller: 'leadersController'
    }).when('/users', {
        templateUrl: 'tmpl/users.html',
        controller: 'usersController'
    }).when('/plugins', {
        templateUrl: 'tmpl/plugins.html',
        controller: 'pluginsController'
    }).otherwise({
        redirectTo: '/play'
    });
}]);

angular.module('xebayApp').factory('$xebay', ['$rootScope', function($rootScope) {
    var xebay = {};
    xebay.userInfo = {};
    xebay.connect = function() {
        $rootScope.$broadcast('$xebay.connect');
    };
    xebay.isConnected = function () {
        return typeof xebay.userInfo != 'undefined' && typeof xebay.userInfo.key != 'undefined';
    };
    xebay.disconnect = function() {
        $rootScope.$broadcast('$xebay.disconnect');
    };
    xebay.error = function (message) {
        $rootScope.$broadcast('$xebay.error', message);
    };
    return xebay;
}]);
