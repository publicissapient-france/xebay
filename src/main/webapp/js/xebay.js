'use strict';

angular.module('xebayApp', ['ngRoute','ngCookies', 'ui.gravatar']);

angular.module('xebayApp').config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/api', {
        templateUrl: 'tmpl/api.html',
        controller: 'apiController'
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

angular.module('xebayApp').factory('$xebay', ['$rootScope', '$timeout', function($rootScope, $timeout) {
    var xebay = {};
    xebay.userInfo = {};
    xebay.connect = function() {
        if (!xebay.socket) {
            xebay.socket = new WebSocket('ws://' + window.location.host + '/socket/bidEngine/' + xebay.userInfo.key);
            xebay.socket.onerror = onerror;
            xebay.socket.onmessage = xebay.onmessage;
        }
    };
    xebay.disconnect = function() {
        if (xebay.socket) {
            xebay.socket.close();
            delete xebay.socket;
        }
    };
    xebay.onmessage = function(message) {
        var socketMessage = JSON.parse(message.data);
        if (socketMessage.info) {
            $rootScope.$broadcast('$xebay.bidOffer', socketMessage.info);
        }
        if (socketMessage.error) {
            console.error("error %s", socketMessage.error);
        }
    };
    xebay.onerror = function() {
        $timeout(xebay.connect, 5000);
    };
    xebay.error = function(message) {
        $rootScope.$broadcast('$xebay.error', message);
    };
    return xebay;
}]);
