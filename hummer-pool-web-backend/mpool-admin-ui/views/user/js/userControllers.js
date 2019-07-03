
//登录
App.controller('loginController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout',
	function($scope, $http, $location, $cookieStore, $state, $timeout) {
		
		//登录方法
		$scope.postLogin = function (){
			//加密
			var base64 = new Base64();
			var pwd = base64.encode($scope.loginPassword);
			//登录参数
			var json = {
				"username": $scope.loginUser,
				"password": pwd
			}
			if($scope.check()){
				$http.post(mpoolUI_url + '/auth/login', json)
					.success(function(data, status, headers, config){
						if(data.msg == "ok"){
							var userName = data.data.username;
							$cookieStore.put('userName', userName);  //存用户名
							$(".loader").fadeIn(800);
							$timeout(function(){
								$(".loader").fadeOut(800);
								$location.path('admin/dashboard'); //跳转界面
							},2000);
						}else{
							$(".loginTitle").fadeIn(1);
							$scope.loginMsg = data.msg;
						}
					}).error(function(data, status, headers, config) {

					});
			}
		}
		
		//登录验证
		$scope.check = function(){
			if($scope.loginUser == undefined || $scope.loginUser == ""){
				$scope.nameCheck = "请输入账号名";
				$(".loginTitle").fadeIn(1);
				return false;
			}else{
				$scope.nameCheck = "";
			}
			if($scope.loginPassword == undefined || $scope.loginPassword == ""){
				$scope.passwordCheck = "请输入密码";
				$(".loginTitle").fadeIn(1);
				return false;
			}else{
				$scope.passwordCheck = "";
			}
			return true;
		}
	}
]);

//登录成功后绑定用户名到top-navbar.html上
App.controller('homeController', ['$scope', '$http', '$location', '$cookieStore', '$state',
	function($scope, $http, $location, $cookieStore, $state) {
		
		$http.get(mpoolUI_url + '/auth/getCurrencyList').success(function (r) {
			$scope.currencyDatas = r.data
			angular.forEach($scope.currencyDatas,(d)=> {
				d.coinIcon = "../../img/banner/" + d.type + "1.png"
			})
		})
		$scope.userName = $cookieStore.get("userName");  // 保存登录后返回的账户
		$http.get(mpoolUI_url + '/auth/cur/currencyName').success(function(r) {
			$scope.currencyName = r.data.currencyName
		})
		$scope.changeCoin = function(type) {
			$scope.currencyName = type
			$http.get(mpoolUI_url + '/auth/switch/currency?currencyName='+type).success(function(data) {
				$state.reload('/admin/dashboard');
			})
		}
		//查询子账号列表
//		$scope.getSubaccountList = function(){
//			$http.get(mpoolUI_url + '/userManager/findSubAccount?current=1&size=5&descs=createTime')
//				.success(function(data){
//					if(data.msg == "ok"){
//						$scope.itemsData = data.data.records;
//					}
//				})
//		}
		
		//注销
		$scope.logout = function(){
			$http.get(mpoolUI_url + '/auth/logout').success(function(data, status, headers, config) {
				if(data.msg == "ok"){
					$cookieStore.remove('userName');
					$scope.display = false;
					$location.path('/login');
				}
			})
		}
		
	}
])
