//用户中心
App.controller('newAccountsController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', 'ngDialog', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, ngDialog, Notify) {
		//跳转首页界面
		$scope.homepage = function () {
	         var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }

		//查询子用户注册信息
		$scope.getSubAccount = function () {
			$http.get(mpoolUI_url + '/userManager/current/getUserInfo').success(function (data) {
				if (data.msg == "ok") {
					$scope.itemsData = data.data;
				}
			})
		}

		//修改密码
		$scope.getChangePassword = function () {
			sessionStorage.setItem("liveUserPhone", $scope.itemsData.userPhone); //存储手机号
			sessionStorage.setItem("liveUserEmail", $scope.itemsData.userEmail); //存储邮箱
			ngDialog.openConfirm({
				template: 'modalChangePassword123',
				controller: 'ChangePasswordCtrl123',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				//加密
				var base64 = new Base64();
				var oldPassword = base64.encode(value.oldPassword);
				var newPassword = base64.encode(value.newPassword);
				$http.post(mpoolUI_url + '/userManager/current/change/password?oldPassword=' + oldPassword + '&newPassword=' + newPassword)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert( $translate.instant('notice.sucgoto') + '...', {
								status: 'success'
							})
							$cookieStore.remove('nickName');
							$timeout(function () {
								$location.path('/login'); //跳转界面
							}, 2000);
							

						} else {
							Notify.alert(data.msg, {
								status: 'info'
							})
						}
					})

			})
		}

		//绑定邮箱
		$scope.bindingEmail = function (objPhone) {
			$state.go('api.bindingEmail', {
				userPhone: objPhone
			});
		}
		//修改邮箱
		$scope.modifyEmail = function (userEmail) {
			$state.go('api.modifyEmail', {
				modifyuserEmail: userEmail
			});
		}

		//绑定手机
		$scope.bindingPhone = function (objEmail) {
			$state.go('api.bindingPhone', {
				userEmail: objEmail
			});
		}

		//修改手机
		$scope.modifyPhone = function (userPhone) {
			$state.go('api.modifyPhone', {
				modifyuserPhone: userPhone
			});
		}

		//初始化方法
		$scope.init = function () {
			$scope.getSubAccount(); //查询子用户注册信息
		}
		$scope.init();

	}
])

//修改密码
App.controller('ChangePasswordCtrl123', ['$http', '$scope', 'ngDialog', '$translate', '$interval', '$timeout',
	function ($http, $scope, ngDialog, $translate, $interval, $timeout) {

		//-----------------------------------------------------修改密码  验证码   验证块			
		$scope.nextStep = true; //点击下一步按钮跳转到下一步操作界面
		$scope.livePhone = sessionStorage.getItem('liveUserPhone'); //存储手机号
		$scope.liveEmail = sessionStorage.getItem('liveUserEmail'); //存储邮箱
		$scope.time = $translate.instant('subActsMng.send');
		
		//判断手机号和邮箱
		$scope.getJudge = function () {
			if (!isNull($scope.livePhone) && !isNull($scope.liveEmail)) {
				$scope.modeEmail = $scope.liveEmail;
				$scope.modephone = $scope.livePhone;
				$scope.showCheck1 = true;
				$scope.showCheck2 = true;
				$scope.getRadio1(0); //选择用手机发送验证码
			} else if (!isNull($scope.liveEmail)) {
				$scope.modeEmail = $scope.liveEmail;
				$scope.showCheck1 = false;
				$scope.showCheck2 = true;
				$scope.getRadio2(1); //选择用邮箱发送验证码
			} else {
				$scope.modephone = $scope.livePhone;
				$scope.showCheck1 = true;
				$scope.showCheck2 = false;
				$scope.getRadio1(0); //选择用手机发送验证码
			}
		}

		//选择用手机发送验证码
		$scope.getRadio1 = function (obj) {
			$scope.title = $translate.instant('subActsMng.enterPhoneVP');
			$scope.checked1 = "checked"; //获取checked框默认值
			$scope.getCheckValue(obj);
		}
		//选择用邮箱发送验证码
		$scope.getRadio2 = function (obj) {
			$scope.title = $translate.instant('login.enterEvc');
			$scope.checked2 = "checked"; //获取checked框默认值
			$scope.getCheckValue(obj);
		}

		//选择验证方式
		$scope.getCheckValue = function (obj) {
			if (obj == 0) {
				$scope.codeURL = '/userManager/getSecurityCode?receiveCode=phone';
			} else {
				$scope.codeURL = '/userManager/getSecurityCode?receiveCode=email'; //获取邮箱验证码 url
			}
		}
		//请求后端获取验证码 
		$scope.getWalletCode = function () {
			$http.get(mpoolUI_url + $scope.codeURL).success(function (data) {
				if (data.msg == "ok") {
					$scope.wallSub = true;
					var maxtime = 1 * 60;

					function CountDown() {
						if (maxtime >= 0) {
							if (maxtime != 60) {
								seconds = Math.floor(maxtime % 60);
							} else {
								seconds = maxtime;
							}
							msg = seconds + "s";
							$scope.time = msg;
							--maxtime;
						} else {
							$scope.wallSub = false;
							$scope.time = $translate.instant('subActsMng.send');
						}
					}
					$interval(function () {
						CountDown()
					}, 1000, 62);
				}
			})
		}

		$scope.sub = true;
		$scope.checkCode = function (obj) {
			if (obj) {
				$scope.sub = false;
			} else {
				$scope.sub = true;
			}
		}

		//点击下一步按钮操作
		$scope.getNextStep = function () {
			$http.get(mpoolUI_url + '/userManager/checkSecurityCode?securityCode=' + $scope.workerName)
				.success(function (data) {
					if (data.msg == 'ok') {
						$scope.nextStep = false; //点击下一步按钮跳转到下一步操作界面
					} else {
						$scope.nextStep = true; //点击下一步按钮跳转到下一步操作界面
						$scope.nameCheck = data.msg; //错误提示
						$(".title1").fadeIn(800);
						$timeout(function () {
							$(".title1").fadeOut(800);
						}, 5000);
					}
				})
		}


		//-----------------------------------------------------修改密码块	
		//验证密码
		$scope.sub2 = true;
		$scope.passwordCheck = function () {
			//原密码
			if ($scope.oldPassword == undefined || $scope.oldPassword == "") {
				$scope.pass_Check = $translate.instant('notice.pswNull');
				$scope.sub2 = true;
				return;
			} else {
				$scope.pass_Check = "";
				$scope.sub2 = false;
			}
			
			if($scope.oldPassword == $scope.newPassword){
				$scope.pass_Check2 = $translate.instant('notice.nwold');
				$scope.sub2 = true;
				return false;
			}else{
				$scope.pass_Check2 = "";
				$scope.sub2 = false;
			}

			if ($scope.newPassword == undefined || $scope.newPassword == "") {
				$scope.pass_Check2 = $translate.instant('notice.pswNull');
				$scope.sub2 = true;
				return;
			} else {
				var pass = pwdValidate.test;
				if (pass.test($scope.newPassword)) {
					$scope.pass_Check2 = "";
					$scope.sub2 = false;
				} else {
					$scope.pass_Check2 = $translate.instant('notice.pswV');
					$scope.sub2 = true;
					return;
				}
			}

			if ($scope.confirm123 == undefined || $scope.confirm123 == "") {
				$scope.pasConCheck1 = $translate.instant('notice.must');
				$scope.sub2 = true;
				return;
			} else {
				if ($scope.confirm123 === $scope.newPassword) {
					$scope.pasConCheck1 = "";
					$scope.sub2 = false;
				} else {
					$scope.pasConCheck1 = $translate.instant('notice.comfer');
					$scope.sub2 = true;
					return;
				}
			}
		}

		$scope.init = function () {
			$scope.getJudge();
		}
		$scope.init();
	}
])