//分享
App.controller('shareCommonproblemController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', '$stateParams',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, $stateParams) {
		$scope.href = function(url){
			if(url == '') {
				$location.path('share/' + $stateParams.comId)
			}else{
				$location.path('share/' + $stateParams.comId + '/'+url)
			}
		} 
		$scope.cointype = $stateParams.comId.split('-')[1]
		$scope.northNode = []
		$scope.southNode = []
		$http.get(mpoolUI_url + '/user/dashbaord/getPoolNodes?currencyName=' + $scope.cointype).success(function (r) {
			angular.forEach(r.data.north, (adr)=> {
				$scope.northNode.push({adr: adr.address, order: adr.order})
			})
			angular.forEach(r.data.south, (adr)=> {
				$scope.southNode.push({adr: adr.address, order: adr.order})
			})
			$scope.discr = r.data.north[0].discrible
		})
		//如果浏览器支持requestAnimFrame则使用requestAnimFrame否则使用setTimeout  
		window.requestAnimFrame = (function() {
		  return window.requestAnimationFrame ||
			window.webkitRequestAnimationFrame ||
			window.mozRequestAnimationFrame ||
			function(callback) {
			  window.setTimeout(callback, 1000 / 60);
			};
		})();

		$http.get(mpoolUI_url + '/share/rest/' + $stateParams.comId).success(
			function (data) {
				if (data.msg == "ok") {
					if (data.data) {
						$scope.userStatus = data.data.userStatus;
					}
				}
			}
		)
		
	}
])