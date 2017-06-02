'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.view1',
  'myApp.view2',
  'ngAnimate', 'treasure-overlay-spinner'
]).
config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');

  $routeProvider.otherwise({redirectTo: '/view1'});
}]);

angular.module('myApp').run(run);

run.$inject = ['$rootScope'];
function run ($rootScope) {
    $rootScope.spinner = {
        active: false,
        on: function () {
            this.active = true;
        },
        off: function () {
            this.active = false;
        }
    }
}

/*
app.module('myApp').filter('specialfilter', function() {
	  return function(items, search) {
	    if (!search) {
	      return items;
	    }


	    return items.filter(function(element, index, array) {
	      return element.specialty === search;
	    });

	  };
	});
*/