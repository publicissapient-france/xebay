'use strict';

var bidApp = angular.module('bidApp', [
  'ngResource',
  'ngRoute',
  'bidControllers'
]);

bidApp.config(['$routeProvider',
  function ($routeProvider) {
    $routeProvider.
        when('/', {
          templateUrl: 'views/main.html',
          controller: 'MainCtrl'
        }).
        otherwise({
          redirectTo: '/'
        });
  }]);
