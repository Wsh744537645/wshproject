
//工具下载
App.controller('downloadToolController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window',
	function($scope, $http, $location, $cookieStore, $state, $timeout, $window) {
		
		//判断用户是否登录 如登录首页展示用户面板按钮，否则隐藏
		$scope.username = $cookieStore.get("nickName");  // 保存登录后返回的账户
		if($scope.username != '' && $scope.username != undefined){
			$scope.control = true;
		}else{
			$scope.control = false;
		}
		//注销
		$scope.logout = function(){
			$http.get(mpoolUI_url + '/user/logout').success(function(data, status, headers, config) {
				if(data.msg == "ok"){
					$cookieStore.remove('nickName');
					$cookieStore.remove('id');
					$scope.display = false;
					$location.path('/login');
					$window.location.reload();
				}
			})
		}
		//跳转用户面板界面
		$scope.userpanel = function () {
	        var url = $state.href("api.dashboard", {}, {reload: true});
	        window.open(url,'_blank');
	    }
		
	}
])