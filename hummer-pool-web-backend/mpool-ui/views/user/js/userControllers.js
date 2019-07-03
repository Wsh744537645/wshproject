//登录
App.controller('loginController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$browser', '$cookies','$translate',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $browser, $cookies,$translate) {
		$scope.ticket = ''
		$scope.randstr = ''
		$scope.loginStatus = false
		$http.get(mpoolUI_url + '/user/getUserLoginModel').then(function (res) {
			console.log(res);
			$scope.loginStatus = res.data.data
			$scope.loginModel = "aaa"

		})

		$('#enterLogin').bind('keydown', function (event) {
            var event = window.event || arguments.callee.caller.arguments[0];
            if (event.keyCode == 13){
				$scope.showCode()
            }
        });
		$scope.showCode = function () {
			if (!$scope.loginUser) {
				$scope.nameCheck = $translate.instant('login.enterUn');
				$(".loginTitle").fadeIn(1);
				return
			} else {
				$scope.nameCheck = "";
			}
			if (!$scope.loginPassword) {
				$scope.passwordCheck = $translate.instant('login.enterPsw');
				$(".loginTitle").fadeIn(1);
				return
			} else {
				$scope.passwordCheck = "";
			}
			var captcha1 = new TencentCaptcha('2074816991', function (res) {
				// console.log(res)
				$scope.loginBtnDisabled = true
				$scope.ticket = res.ticket
				$scope.randstr = res.randstr
				$scope.postLoginT()
			});
			captcha1.show(); // 显示验证码
		}

		/**
		 *{bizState: undefined, appid: "2074816991", 
		 ret: 0, 
		 ticket: "t02kTH27WgyMVMcUK52LdsgZ-v4U3bkKuAxhbxmBQi95x13aWY…YxCyGWBuXZnwzkPMoui4pkvXwQ4Bn69BE0OEVnXD334t9Fw**", 
		 randstr: "@-SK"}
		 */
		//登录方法 腾讯验证码
		$scope.postLoginT = function () {
			$(".loginTitle").fadeOut()
			if (!$scope.ticket && !$scope.randstr) {
				console.log("没有验证")
			}
			//加密
			var base64 = new Base64();
			$scope.pwd = base64.encode($scope.loginPassword); //加密
			var json = {
				"username": $scope.loginUser,
				"password": $scope.pwd
			}
			$http.post(mpoolUI_url + '/user/login/t?ticket=' + $scope.ticket + '&randstr=' + $scope.randstr, json)
				.success(function (data, status, headers, config) {
					if (data.msg == "ok") {
						var nickName = data.data.nickName;
						//sessionStorage.setItem("IDs", ''); //保存主账号切换到子账号唯一id
						$cookieStore.put('nickName', nickName); //存用户名
						//记住密码存 值
						if ($scope.remember) {
							setCookie("upwd", JSON.stringify(json), 360);
						} else {
							$cookieStore.remove("upwd");
							$cookieStore.remove("checkedPlat");
						}
						$cookieStore.remove('masterUserId');
						$cookieStore.remove('master')
						if (data.data.masterUserId) {
							$cookieStore.put('masterUserId', data.data.masterUserId)
							$(".loader").fadeIn(800);
							$timeout(function () {
								$(".loader").fadeOut(800);
								$location.path('api/sondashboard'); //子账号页面
							}, 2000);
						} else {
							$(".loader").fadeIn(800);
							$timeout(function () {
								$(".loader").fadeOut(800);
								$location.path('api/dashboard'); //主账号页面
							}, 2000);
						}
					} else {
						$(".loginTitle").fadeIn(1);
						$scope.loginMsg = data.msg;
						$scope.loginBtnDisabled = false
					}
				}).error(function (data, status, headers, config) {
					$scope.loginBtnDisabled = false
				});

		}

		//登录方法
		$scope.postLogin = function () {

			if ($scope.check()) {
				//加密
				var base64 = new Base64();
				$scope.pwd = base64.encode($scope.loginPassword); //加密
				var json = {
					"username": $scope.loginUser,
					"password": $scope.pwd
				}
				$(".loginTitle").fadeOut();
				$http.post(mpoolUI_url + '/user/login?captchaCode=' + $scope.loginCode, json)
					.success(function (data, status, headers, config) {
						if (data.msg == "ok") {
							var nickName = data.data.nickName;
							//sessionStorage.setItem("IDs", ''); //保存主账号切换到子账号唯一id
							$cookieStore.put('nickName', nickName); //存用户名
							//记住密码存 值
							if ($scope.remember) {
								setCookie("upwd", JSON.stringify(json), 360);
							} else {
								$cookieStore.remove("upwd");
								$cookieStore.remove("checkedPlat");
							}
							$cookieStore.remove('master')
							if (data.data.masterUserId) {
								$cookieStore.put('masterUserId', data.data.masterUserId)
								$(".loader").fadeIn(800);
								$timeout(function () {
									$(".loader").fadeOut(800);
									$location.path('api/sondashboard'); //子账号页面
								}, 2000);
							} else {
								$cookieStore.remove('masterUserId');
								$(".loader").fadeIn(800);
								$timeout(function () {
									$(".loader").fadeOut(800);
									$location.path('api/dashboard'); //主账号页面
								}, 2000);
							}
						} else {
							$(".loginTitle").fadeIn(1);
							$scope.loginMsg = data.msg;
						}
					}).error(function (data, status, headers, config) {

					});
			}
		}
		//记住密码存 值
		function setCookie(name, value, day) {
			var Days = day;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
			document.cookie = name + "=" + value + ";expires=" + exp.toGMTString();
		}

		//记住密码
		$scope.checkedPlat = function (checkModel) {
			setCookie("checkedPlat", checkModel, 360);
		}

		//前端生成验证码
		$scope.loginEmailCode = function () {
			$scope.imgUrl = mpoolUI_url + '/user/getCaptchaCode/' + new Date().getTime();
		}
		//初始化方法
		$scope.init = function () {

			$scope.loginEmailCode(); //前端生成验证码

			//记住密码
			var check = $cookieStore.get("checkedPlat");
			if (check == null || check == undefined || check == '') {
				check = false;
			}
			$scope.remember = check;
			var upwd = $cookieStore.get("upwd");
			if (check) {
				if (upwd != undefined || upwd != null) {
					//加密
					var base64 = new Base64();
					$scope.decodedString = base64.decode(upwd.password); //解密

					$scope.loginUser = upwd.username;
					$scope.loginPassword = $scope.decodedString;
				}
			}
		}
		$scope.init();
		//跳转首页界面
		$scope.homepage = function () {

			//window.open(url, '_blank');
			window.open("/home.html", '_self')
		}
		//登录验证
		$scope.check = function () {
			$(".loginTitle").fadeOut()
			var b = true
			if ($scope.loginUser == undefined || $scope.loginUser == "") {
				$scope.nameCheck = $translate.instant('login.enterUn');
				$(".loginTitle").fadeIn(1);
				b = false;
			} else {
				$scope.nameCheck = "";
			}
			if ($scope.loginPassword == undefined || $scope.loginPassword == "") {
				$scope.passwordCheck = $translate.instant('login.comfP');
				$(".loginTitle").fadeIn(1);
				b = false;
			} else {
				$scope.passwordCheck = "";
			}
			if ($scope.loginCode == undefined || $scope.loginCode == "") {
				$scope.capCodeCheck = $translate.instant('subActsMng.enterPhoneVP');
				$(".loginTitle").fadeIn(1);
				b = false;
				
				if($scope.loginStatus == false) {
					$scope.capCodeCheck = "";
				}
			} else {
				$scope.capCodeCheck = "";
			}
			$timeout(function () {
				$(".loginTitle").fadeOut();
			}, 500);
			return b;
		}
	}
]);


//登录成功后绑定用户名到top-navbar.html上
App.controller('homeController', ['$scope', '$http', '$location', '$cookieStore', '$state', 'Notify', '$window', '$timeout', 'ngDialog',
	function ($scope, $http, $location, $cookieStore, $state, Notify, $window, $timeout, ngDialog) {


		//切换主账号
		$scope.getsWitchSubAccount = function (objID, objName) {
			//navBar 子账户切换回主账户 默认切换到BTC
			$http.get(mpoolUI_url + '/userManager/switchSubAccount/' + objID + '?currencyName=BTC')
				.success(function (data) {
					if (data.msg == "ok") {
						// $cookieStore.put("nickName", data.data.username)
						// //masterUserId是无效值
						// if (!data.data.masterUserId) {
						// 	$cookieStore.remove('master')
						// 	$cookieStore.remove('masterUserId')
						// }

						// $timeout(function () {
						// 	Notify.alert('切换成主账号成功！！！', {
						// 		status: 'success'
						// 	});
						// }, 1000);
						$location.path('/switchAccount'); //跳转界面
					} else {
						var title1 = data.msg;
						Notify.alert(title1, {
							status: 'info'
						})
					}
				})
		}

		//注销
		$scope.logout = function () {
			$http.get(mpoolUI_url + '/user/logout').success(function (data, status, headers, config) {
				if (data.msg == "ok") {
					$cookieStore.remove('nickName')
					$cookieStore.remove('masterUserId')
					$cookieStore.remove("master")
					$scope.display = false;
					$location.path('/login');
					$window.location.reload();
				}
			})
		}

		$scope.init = function () {
			$http.get(mpoolUI_url + '/userManager/current/getUserInfo').success(function (resp) {
			$scope.currencyName = sessionStorage.getItem('currencyName')
			$scope.username = resp.data.username
				if (resp.data.masterUserId) {
					//子账号管理
					$scope.master = $cookieStore.get("master");
					//子账号
					$scope.userType = 1
				} else {
					//主账号
					$scope.userType = 0
				}
			})


		}


		//跳转首页界面
		$scope.homepage = function () {

			//window.open(url, '_blank');
			window.open("/home.html", '_self')
		}

		//子账户管理
		$scope.getSubaccounts = function () {
			$location.path('/api/subaccounts');
		}

		//用户中心
		$scope.getAccounts = function () {
			$location.path('/api/newAccounts');
		}

		$scope.init()
	}
]);