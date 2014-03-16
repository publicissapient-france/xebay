'use strict';

function twoDigits(value) {
    return (value < 10 ? "0" : "") + value;
}

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

angular.module('xebayApp').factory('Xebay', function() {
    return {
        'userInfo': {}
    };
});
