
//统计
App.controller('announcementdetailController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$stateParams', '$timeout', '$window',
	function($scope, $http, $location, $cookieStore, $state, $stateParams, $timeout, $window) {
		var id = $stateParams.id;
		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		$scope.page = 1;
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
		
		$scope.getFindByid = function(){
			$http.get(mpoolUI_url + '/msg/findByid?id='+id).success(function (data) {
				if (data.msg == "ok") {
					$scope.itemsValue = data.data;
					$scope.title = $scope.itemsValue.title;
					$scope.date = !!$scope.itemsValue.releaseTime ? $scope.itemsValue.releaseTime.substr(0,10): ''
					$scope.time = !!$scope.itemsValue.releaseTime ? $scope.itemsValue.releaseTime.substr(11,5): ''
					var text = $scope.itemsValue.text;
					$("#notice_text").html(text)
				}else{
					Notify.alert(data.msg, {
						status: 'info'
					})
				}
			})
		}
		
		//初始化
		$scope.init = function () {
			$scope.getFindByid();
			
		}
		$scope.init()
		//跳转用户面板界面
		$scope.userpanel = function () {
	        var url = $state.href("api.dashboard", {}, {reload: true});
	        window.open("api/dashboard", '_self')
	    }

	}
])