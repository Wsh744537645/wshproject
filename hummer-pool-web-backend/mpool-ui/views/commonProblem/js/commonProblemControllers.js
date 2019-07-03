
//常见问题
App.controller('commonProblemController', ['$scope', '$http' ,'$location', '$state', '$timeout', 'Notify',
	function($scope, $http, $location, $state, $timeout, Notify) {
		
		$scope.currencyName= 'BTC'
		$scope.currencyCoin= "../../img/banner/BTC1.png"
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			$scope.currencyData = r.data
			angular.forEach($scope.currencyData,(d)=> {
				d.coinIcon = "../../img/banner/" + d.type + "1.png"
			})
		})
		
		$scope.changeCoin =  function(name) {
			$scope.currencyName = name
			angular.forEach($scope.currencyData, (d)=> {
				if(name == d.type) {
					$scope.currencyCoin = "../../img/banner/" + d.type + "1.png"
				}
			})
			$scope.getNode();
		}
		$scope.getNode = function() {
			
			$scope.northNode = []
			$scope.southNode = []
			$http.get(mpoolUI_url + '/user/dashbaord/getPoolNodes?currencyName=' + $scope.currencyName).success(function (r) {
				angular.forEach(r.data.north, (adr)=> {
					$scope.northNode.push({adr: adr.address, order: adr.order})
				})
				angular.forEach(r.data.south, (adr)=> {
					$scope.southNode.push({adr: adr.address, order: adr.order})
				})
				$scope.discr = r.data.north[0].discrible
			})
		}

		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		
		
		
		//初始化方法
		$scope.init = function(){
			$scope.getNode()
		}
		$scope.init();
		
	}
])



