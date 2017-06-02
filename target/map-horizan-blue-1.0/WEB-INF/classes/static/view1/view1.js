'use strict';

angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/view1', {
		templateUrl: 'view1/view1.html',
		controller: 'View1Ctrl'
	});
}])

.controller('View1Ctrl', function($scope,$rootScope,$location,$http,$timeout) {
	$rootScope.first=true;
	
	function assignColorCategory(data){
		var categoryMap = {};
		for (var i = 0; i < data.length; i++){
			if(categoryMap[data[i].specialty] != undefined){
				data[i].color = categoryMap[data[i].specialty];
			}else{
				data[i].color = '#' + (Math.random() * 0xFFFFFF << 0).toString(16);
				categoryMap[data[i].specialty] = data[i].color;
			}
			console.log(data[i].color);
		}
		return data;
	}
	
	function createSpecialtyValue(data){
		for (var i = 0; i < data.length; i++){
			var specialty = data[i].specialty.split(" ");
			var specialtyValue;
			for(var i = 0; i < specialty.length; i++){
				if(specialtyValue){
					specialtyValue = specialtyValue + "_";
				}
			}
			data[i].specialtyValue = specialtyValue;
			console.log(data[i].specialtyValue);
		}
		return data;
	}
	
	function callback(data){
		$rootScope.spinner.off();
		if(data == undefined || data.length ==0){
			alert("No Records !!!")
		}
		else{
			$rootScope.mainData = assignColorCategory(data);
			$rootScope.mainData = createSpecialtyValue(data);
			$location.path('/view2');
		}
	}
	$scope.submitZip = function(zipCode, cityName, stateName){
		if(zipCode == null && cityName == null && stateName == null){
			alert("Kindly provide at least one search criteria or search by your current location");
			return;
		}
		
		$rootScope.spinner.on();
		$http.defaults.headers.common = {};
		$http.defaults.headers.post = {};
		$http.defaults.headers.get = {};
		$http.defaults.headers.put = {};
		$http.defaults.headers.patch = {};
		var dataObj = {
				zip : zipCode,
				city : cityName,
				state : stateName
		};
		
		var config = {
                headers : {
                    'Content-Type': 'application/json',
                    'Accept' : 'application/json'
                }
            }
		
		$http.post("/find", angular.toJson(dataObj), config).then(
			function (response) {
				callback(response.data);
			}, function (error) {
				console.log(error);
				callback(false);
		});
	}
	
	$scope.isDisabled = false;
	
	$scope.searchBy = 'manual';
	
	$scope.changeSearchBy = function(searchBy){
		if(searchBy == 'auto'){
			$scope.isDisabled = true;
			$rootScope.spinner.on();
			findLocation();
		}else{
			$scope.isDisabled = false;
			$scope.zipCode = null;
			$scope.cityName = null;
			$scope.stateName = null;
		}
	}
	///////////// Geo location code ///////////////////////////
	var isLocationFound = null;
	
	function findLocation(){
		if (navigator.geolocation){
			isLocationFound = null;
			navigator.geolocation.getCurrentPosition(updateLocation);
			$timeout(verifyLocationFound, 10000);
		}else{
			alert("Sorry Geo location is either not supported or disabled by your Admin");
		}
	}
	
	function verifyLocationFound(){
		if(isLocationFound != true){
			isLocationFound = false;
			alert("Unable to fetch your current location at the moment. Kindly provide information manually.");
		}
        $rootScope.spinner.off();
	}
	
	function updateLocation(position) {
		
		$scope.latitude = position.coords.latitude;
		$scope.longitude = position.coords.longitude;
	    var url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + $scope.latitude + "," + $scope.longitude + "&sensor=true";
	    $http.get(url)
	        .then(function(result) {
	            var address = result.data.results[0];
	            if(isLocationFound != false){
	            	$scope.zipCode = address.address_components[7].long_name;
	            	$scope.cityName = address.address_components[4].long_name;
	            	$scope.stateName = address.address_components[5].long_name;
		        	isLocationFound = true;
	            }
	            $rootScope.spinner.off();
	        });
	}
});