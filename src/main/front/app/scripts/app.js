'use strict';

var bidApp = angular.module('bidApp', [
  'ngResource',
  'ngRoute',
  'bidControllers'
]);

bidApp.config(['$routeProvider', '$locationProvider',
  function ($routeProvider, $locationProvider) {
    $routeProvider.
        when('/', {
          templateUrl: 'views/main.html'
        }).
        when('/leaderboard', {
          templateUrl: 'views/leaderboard.html',
          controller: 'LeaderboardCtrl'
        }).
        otherwise({
          redirectTo: '/'
        });
    $locationProvider.html5Mode(true).hashPrefix('!');
  }]);
