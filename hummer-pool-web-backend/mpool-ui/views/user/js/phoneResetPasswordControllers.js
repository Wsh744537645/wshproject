//忘记密码  手机号找回密码
App.controller('phoneResetPasswordController', ['$scope', '$http', '$location', '$translate', '$state', '$timeout', '$interval',
	function ($scope, $http, $location, $translate, $state, $timeout, $interval) {

		//获取验证邮箱
		$scope.sendCode = true;
		$scope.disCode = true;
		$scope.resPhone = function () {
			var obj = $scope.phone;
			var captchaCode = $scope.captchaCode;
			if (obj == undefined || $scope.obj == "") {
				$scope.phoneCheck = $translate.instant('subActsMng.Enter phoneNum');
			} else {
				// if (captchaCode == undefined || captchaCode == "") {
				// 	$scope.capCodeCheck = "请输入验证码";
				// } else {
					$http.get(mpoolUI_url + '/user/resetPassword/phoneCode?phoneNumber=' + obj + '&captchaCode=' + captchaCode).success(function (data) {
						if (data.msg == "ok") {
							$scope.sendCode = false;
							$scope.disCode = false;
							$scope.capCodeCheck = "";

							var maxtime = 5 * 60;

							function CountDown() {
								if (maxtime >= 0) {
									minutes = Math.floor(maxtime / 60);
									seconds = Math.floor(maxtime % 60);
									if(minutes == 0){
										msg = "(" + seconds + $translate.instant('notice.seconds') +")";
									}else{
										msg = "(" + minutes + $translate.instant('notice.minutes') + seconds + $translate.instant('notice.seconds') +")";
									}
									$scope.time = msg;
									--maxtime;
								} else {
									$scope.sendCode = true;
									$scope.disCode = true;
								}
							}
							$interval(function () {
								CountDown()
							}, 1000, 302);
						} else {
							$scope.phoneCheck = data.msg;
						}
					})
				// }
			}
		}

		//点击确认按钮
		$scope.registerSuccess = true;
		$scope.phoneResetPassword = function () {
			if ($scope.regCheck()) {
				if (($scope.password == undefined || $scope.password == "") || ($scope.pasConfirm == undefined || $scope.pasConfirm == "") || ($scope.phone == undefined || $scope.phone == "") || ($scope.phoneCode == undefined || $scope.phoneCode == "")) {

				} else {
					//加密
					var base64 = new Base64();
					$scope.pwd = base64.encode($scope.password);
					var json = $scope.pwd //密码


					$http.post(mpoolUI_url + '/user/resetPassword/phone?phoneCode=' + $scope.phoneCode + '&phone=' + $scope.phone, json)
						.success(function (data) {
							if (data.code == "0") {
								$scope.passTitle = $translate.instant('notice.chgsc');
								$scope.passTitle2 = $translate.instant('notice.5sleave');
								$scope.registerSuccess = false;
								$timeout(function () {
									$location.path('/login'); //跳转登录界面					
								}, 5000);
							} else {
								$scope.registerSuccess = false;
								$scope.passTitle = $translate.instant('notice.finder');
								$scope.passTitle2 = $translate.instant('notice.holdon');
							}
						})
				}
			}
		}

		//跳转首页界面
		$scope.homepage = function () {

			//window.open(url, '_blank');
			window.open("/home.html", '_self')
		}
		
		//前端生成验证码
		$scope.getEmailCode = function () {
			$scope.imgUrl = mpoolUI_url + '/user/getCaptchaCode/' + new Date().getTime();
		}
		//初始化方法
		$scope.init = function () {
			$scope.getEmailCode();
		}
		$scope.init();

		//注册验证
		$scope.sub4 = true;
		$scope.regCheck = function () {
			$scope.changePassword(); //密码
			$scope.changePassCon(); //确定密码
			$scope.changePhone(); //邮箱
			$scope.changePhoneCode(); //邮箱验证码
			$scope.changeCode3(); //验证码
			return true;
		}

		//密码
		$scope.changePassword = function () {
			if ($scope.password == undefined || $scope.password == "") {
				$scope.passCheck = $translate.instant('notice.pswNull');
				$scope.sub4 = false;
			} else {
				var pass = pwdValidate.test;
				if (pass.test($scope.password)) {
					$scope.passCheck = "";
					$scope.sub4 = true;
				} else {
					$scope.passCheck = $translate.instant('notice.pswV');
					$scope.sub4 = false;
				}
			}
			if(!!$scope.passCheck){
				document.getElementById('psw').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('psw').style.border = '1px solid #dde6e9'
			}
		}
		//确定密码
		$scope.changePassCon = function () {
			if ($scope.pasConfirm == undefined || $scope.pasConfirm == "") {
				$scope.pasConCheck = $translate.instant('notice.must');
				$scope.sub4 = false;
			} else {
				if ($scope.pasConfirm === $scope.password) {
					$scope.pasConCheck = "";
					$scope.sub4 = true;
				} else {
					$scope.pasConCheck =  $translate.instant('notice.comfer');
					$scope.sub4 = false;
				}
			}
			if(!!$scope.pasConCheck){
				document.getElementById('conpsw').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('conpsw').style.border = '1px solid #dde6e9'
			}
		}
		//手机
		$scope.changePhone = function () {
			if ($scope.phone == undefined || $scope.phone == "") {
				$scope.phoneCheck = $translate.instant('notice.phnull');
				$scope.sub4 = false;
			} else {
				var phone = phoneValidate.test;
				if (phone.test($scope.phone)) {
					$scope.sub4 = true;
					$scope.phoneCheck = "";
				} else {
					$scope.sub4 = false;
					$scope.phoneCheck = $translate.instant('notice.crP');
				}
			}
			if(!!$scope.phoneCheck){
				document.getElementById('usererr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('usererr').style.border = '1px solid #dde6e9'
			}

		}
		//手机验证码
		$scope.changePhoneCode = function () {
			if ($scope.phoneCode == undefined || $scope.phoneCode == "") {
				$scope.code_Check = $translate.instant('notice.must');
				$scope.sub4 = false;
			} else {
				$scope.code_Check = "";
				$scope.sub4 = true;
			}
			if(!!$scope.code_Check){
				document.getElementById('phcodeerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('phcodeerr').style.border = '1px solid #dde6e9'
			}
		}
		//验证码
		$scope.changeCode3 = function () {
			if ($scope.captchaCode == undefined || $scope.captchaCode == "") {
				$scope.capCodeCheck = $translate.instant('notice.must');
				$scope.sub4 = false;
			} else {
				$scope.capCodeCheck = "";
				$scope.sub4 = true;
			}
		}
	}
]);