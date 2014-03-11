'use strict';

function twoDigits(value) {
    return (value < 10 ? "0" : "") + value;
}

var xebayApp = angular.module('xebayApp',['ngRoute','ngCookies','xebayControllers']);

xebayApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/play', {
        templateUrl: 'tmpl/play.html',
        controller: 'playController'
    }).when('/collection', {
        templateUrl: 'tmpl/collection.html',
        controller: 'collectionController'
    }).when('/api', {
        templateUrl: 'tmpl/api.html',
        controller: 'playController'
    }).when('/leaderboard', {
        templateUrl: 'tmpl/leaderboard.html',
        controller: 'playController'
    }).when('/settings', {
        templateUrl: 'tmpl/settings.html',
        controller: 'settingsController'
    }).otherwise({
        redirectTo: '/play'
    });
}]);

xebayApp.factory('UserData', function() {

    return { message: "Data shared amongst all controllers" };
});
