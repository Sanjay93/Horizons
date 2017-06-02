'use strict';

angular
		.module('myApp.view2', [ 'ngRoute' ])

		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/view2', {
				templateUrl : 'view2/view2.html',
				controller : 'View2Ctrl'
			});
		} ])

		.controller(
				'View2Ctrl',
				function($rootScope, $scope, $location, $http) {
					$rootScope.first = false;

					if ($rootScope.mainData == undefined
							|| $rootScope.mainData.length == 0)
						$location.path("/view1")
					var mapOptions = {
						zoom : 6,
						// center: new google.maps.LatLng(40.74727,
						// -73.9800645),
						mapTypeId : google.maps.MapTypeId.ROADMAP,

						styles : [ {
							featureType : "poi.business",
							elementType : "labels",
							stylers : [ {
								visibility : "off"
							} ]
						} ]
					}

					$scope.map = new google.maps.Map(document
							.getElementById('right'), mapOptions);

					$scope.filterView = [];
					
					$scope.filteredData = $scope.mainData;

					$scope.markers = [];

					var bounds = new google.maps.LatLngBounds();
					var createMarker = function(info) {
						var image = {
							    url: 'marker-img-small-4.jpg',
							    // This marker is 20 pixels wide by 32 pixels high.
							    size: new google.maps.Size(20, 32),
							    // The origin for this image is (0, 0).
							    origin: new google.maps.Point(0, 0),
							    // The anchor for this image is the base of the flagpole at (0, 32).
							    anchor: new google.maps.Point(0, 32),
							    
							    color: info.color
							  };
						
						var marker = new google.maps.Marker({
							map : $scope.map,
							position : new google.maps.LatLng(info.lattitude,
									info.longitude),
							title : info.name,
							specialty : info.specialty,
							//icon: image,
							/*
							icon:
							{
						        path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
						        strokeColor: info.color,
						        scale: 6
						    }*/
						});
						marker.content = '<div class="infoWindowContent" style="background-color:'+info.color+'"><h5>'
								+ info.address + '<br>' + info.city + '<br>'
								+ info.county + '<br>' + info.zip
								+ '</h5></div>';

						var infoWindow = new google.maps.InfoWindow({content: marker.content});

						google.maps.event.addListener(infoWindow, 'domready', function() {

							   // Reference to the DIV which receives the contents of the infowindow using jQuery
							   var iwOuter = $('.gm-style-iw');

							   /* The DIV we want to change is above the .gm-style-iw DIV.
							    * So, we use jQuery and create a iwBackground variable,
							    * and took advantage of the existing reference to .gm-style-iw for the previous DIV with .prev().
							    */
							   var iwBackground = iwOuter.prev();

							   // Remove the background shadow DIV
							   iwBackground.children(':nth-child(2)').css({'display' : 'none'});

							   // Remove the white background DIV
							   iwBackground.children(':nth-child(4)').css({'display' : 'none'});

							});
						google.maps.event.addListener(marker, 'click',
								function() {
									infoWindow.open($scope.map, marker);
								});
						bounds.extend(marker.getPosition());
						$scope.markers.push(marker);
					}

					if ($rootScope.mainData != undefined) {
						var i;
						for (i = 0; i < $rootScope.mainData.length; i++) {
							createMarker($rootScope.mainData[i]);
							$rootScope.mainData[i].marker = $scope.markers[i]
							console.log($scope.markers)
						}
					}
					$scope.map.fitBounds(bounds);
					$scope.openInfoWindow = function(e, selectedMarker) {
						e.preventDefault();
						google.maps.event.trigger(selectedMarker, 'click');
					}
					
					$scope.submitZip = function(zipCode) {
						if(zipCode == null){
							alert("Kindly provide zip code to search");
							return;
						}
						$rootScope.spinner.on();
						$http.defaults.headers.common = {};
						$http.defaults.headers.post = {};
						$http.defaults.headers.get = {};
						$http.defaults.headers.put = {};
						$http.defaults.headers.patch = {};

						var dataObj = {
							zip : zipCode
						};

						var config = {
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						}

						$http.post("/find/", angular.toJson(dataObj), config)
								.then(function(response) {
									callback(response.data);
								}, function(error) {
									callback(false);
								});
					}

					$scope.filterByCategory = function() {
						var filterCriteria = $scope.item;// .specialty;
						for (i = 0; i < $scope.markers.length; i++) {
							if (filterCriteria == 'all'
									|| filterCriteria == $scope.markers[i].specialty) {
								$scope.markers[i].setVisible(true);
							} else {
								$scope.markers[i].setVisible(false);
							}
						}
						$scope.filteredData = [];
						var j = 0;
						for (i = 0; i < $scope.mainData.length; i++) {
							if (filterCriteria == 'all'
									|| filterCriteria == $scope.mainData[i].specialty) {
								//$scope.filterView[i] = false;
								$scope.filteredData[j++] = $scope.mainData[i];
							} else {
								//$scope.filterView[i] = true;
							}
						}
						initPager();
					}

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
					
					function callback(data) {
						$rootScope.spinner.off();
						if (data == undefined || data.length == 0) {
							alert("No Records !!!")
						} else {
							$scope.map = new google.maps.Map(document
									.getElementById('right'), mapOptions);
							$scope.markers = [];
							$rootScope.mainData = assignColorCategory(data);
							bounds = new google.maps.LatLngBounds();
							for (i = 0; i < $rootScope.mainData.length; i++) {
								createMarker($rootScope.mainData[i]);
								$rootScope.mainData[i].marker = $scope.markers[i]
								console.log($scope.markers)
							}
							$scope.map.fitBounds(bounds);
							$scope.filterByCategory();
						}
					}
					
				///////////////////////////////////      Paging      ////////////////////////////////////////
			        $scope.setPage = function(page) {
			            if (page < 1 || page > $scope.pager.totalPages || $scope.filteredData == undefined) {
			                return;
			            }

			            // get pager object from service
			            $scope.pager = getPager($scope.filteredData.length, page, 6);

			            // get current page of items
			            $scope.itemsPerPage = $scope.filteredData.slice($scope.pager.startIndex, $scope.pager.endIndex + 1);
			            
			            reDrawMarkers();
			        }

					initPager();
					
					function initPager(){
				       	$scope.pager = {};
				       	$scope.setPage(1);
				    }
				        
					// service implementation
			        function getPager(totalItems, currentPage, pageSize) {
			            // default to first page
			            currentPage = currentPage || 1;

			            // calculate total pages
			            var totalPages = Math.ceil(totalItems / pageSize);

			            var startPage, endPage;
			            if (totalPages <= 10) {
			                // less than 10 total pages so show all
			                startPage = 1;
			                endPage = totalPages;
			            } else {
			                // more than 10 total pages so calculate start and end pages
			                if (currentPage <= 6) {
			                    startPage = 1;
			                    endPage = 10;
			                } else if (currentPage + 4 >= totalPages) {
			                    startPage = totalPages - 9;
			                    endPage = totalPages;
			                } else {
			                    startPage = currentPage - 5;
			                    endPage = currentPage + 4;
			                }
			            }

			            // calculate start and end item indexes
			            var startIndex = (currentPage - 1) * pageSize;
			            var endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

			            // return object with all pager properties required by the view
			            return {
			                totalItems: totalItems,
			                currentPage: currentPage,
			                pageSize: pageSize,
			                totalPages: totalPages,
			                startPage: startPage,
			                endPage: endPage,
			                startIndex: startIndex,
			                endIndex: endIndex,
			            };
			        }
			        
			        function reDrawMarkers(){
			        	for (i = 0; i < $scope.markers.length; i++) {
							$scope.markers[i].setVisible(false);
						}
			        	for (i = 0; i < $scope.itemsPerPage.length; i++){
			        		$scope.itemsPerPage[i].marker.setVisible(true);
			        	}
			        }
				})

		.filter(
				'unique',
				function() {
					return function(items, filterOn) {

						if (filterOn === false) {
							return items;
						}

						if ((filterOn || angular.isUndefined(filterOn))
								&& angular.isArray(items)) {
							var hashCheck = {}, newItems = [];

							var extractValueToCompare = function(item) {
								if (angular.isObject(item)
										&& angular.isString(filterOn)) {

									var resolveSearch = function(object,
											keyString) {
										if (typeof object == 'undefined') {
											return object;
										}
										var values = keyString.split(".");
										var firstValue = values[0];
										keyString = keyString.replace(
												firstValue + ".", "");
										if (values.length > 1) {
											return resolveSearch(
													object[firstValue],
													keyString);
										} else {
											return object[firstValue];
										}
									}

									return resolveSearch(item, filterOn);
								} else {
									return item;
								}
							};

							angular.forEach(items, function(item) {
								var valueToCheck, isDuplicate = false;

								for (var i = 0; i < newItems.length; i++) {
									if (angular.equals(
											extractValueToCompare(newItems[i]),
											extractValueToCompare(item))) {
										isDuplicate = true;
										break;
									}
								}
								if (!isDuplicate) {
									if (typeof item != 'undefined') {
										newItems.push(item);
									}
								}

							});
							items = newItems;
						}
						return items;
					};
				});
