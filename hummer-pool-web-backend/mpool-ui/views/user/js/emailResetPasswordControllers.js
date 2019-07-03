//忘记密码  邮箱找回密码
App.controller('emailResetPasswordController', ['$scope', '$http', '$location', '$translate', '$state', '$timeout', '$interval',
	function($scope, $http, $location, $translate, $state, $timeout, $interval) {
		
		//获取验证邮箱
		$scope.sendCode = true;
		$scope.disCode = true;
		$scope.resEmail = function(){
			var obj = $scope.email;
			// var captchaCode = $scope.captchaCode;
			if(obj == undefined || $scope.obj == ""){
				$scope.emailCheck = $translate.instant('notice.enterem');
			}else{
				// if(captchaCode == undefined|| captchaCode == ""){
				// 	$scope.capCodeCheck = "请输入验证码";
				// }else{
					$http.get(mpoolUI_url + '/user/resetPasswordCode?mail=' + obj).success(function(data){
						if(data.msg == "ok"){
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
					        	}else{
					          		$scope.sendCode = true;
					          		$scope.disCode = true;
					        	}
					      	}  
					      	$interval(function(){CountDown()},1000,302);
						}else{
							$scope.emailCheck = data.msg;
						}
					})
				}
			// }
		}
		
		//点击确认按钮
		$scope.registerSuccess = true;
		$scope.postResetPassword = function(){
			if($scope.regCheck()){
				if(($scope.password == undefined || $scope.password == "") || ($scope.pasConfirm == undefined || $scope.pasConfirm == "") || ($scope.email == undefined || $scope.email == "") || ($scope.emailCode == undefined || $scope.emailCode == "")){
						
				}else{
					//加密
					var base64 = new Base64();
					$scope.pwd = base64.encode($scope.password);
					var json = $scope.pwd  	 		//密码
					
					$http.post(mpoolUI_url + '/user/resetPassword?email='+ $scope.email+'&emailCode='+$scope.emailCode, json)
						.success(function(data){
							if(data.code == "0"){
								$scope.passTitle = $translate.instant('notice.chgsc');
								$scope.passTitle2 = $translate.instant('notice.5sleave');
								$scope.registerSuccess = false;
								$timeout(function() {
									$location.path('/login'); //跳转登录界面					
								}, 5000);
							}else{
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
		$scope.getEmailCode = function(){
			$scope.imgUrl = mpoolUI_url + '/user/getCaptchaCode/' + new Date().getTime();
		}
		//初始化方法
		$scope.init = function(){
			$scope.getEmailCode();
		}
		$scope.init();

		//注册验证
		$scope.sub3 = true;
		$scope.regCheck = function(){
			$scope.changePassword();     //密码
			$scope.changePassCon();      //确定密码
			$scope.changeEmail();        //邮箱
			$scope.changeEmailCode();    //邮箱验证码
			$scope.changeCode2();        //验证码
			return true;
		}
		
		//密码
		$scope.changePassword = function(){
			if($scope.password == undefined || $scope.password == ""){
				$scope.passCheck = $translate.instant('notice.pswNull');
				$scope.sub3 = false;
			}else{
				var pass = pwdValidate.test;
				if(pass.test($scope.password)){
					$scope.passCheck = "";
					$scope.sub3 = true;
				}else{
					$scope.passCheck = $translate.instant('notice.pswV');
					$scope.sub3 = false;
				}
			}
			if(!!$scope.passCheck){
				document.getElementById('psw').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('psw').style.border = '1px solid #dde6e9'
			}
			
		}
		//确定密码
		$scope.changePassCon = function(){
			if($scope.pasConfirm == undefined || $scope.pasConfirm == ""){
				$scope.pasConCheck = $translate.instant('notice.must');
				$scope.sub3 = false;
			}else{
				if($scope.pasConfirm === $scope.password){
					$scope.pasConCheck = "";
					$scope.sub3 = true;
				}else{
					$scope.pasConCheck =  $translate.instant('notice.comfer');
					$scope.sub3 = false;
				}	
			}
			if(!!$scope.pasConCheck){
				document.getElementById('confpsw').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('confpsw').style.border = '1px solid #dde6e9'
			}
		}
		//邮箱
		$scope.changeEmail = function(){
			if($scope.email == undefined || $scope.email == ""){
				$scope.emailCheck = $translate.instant('notice.emNull');
				$scope.sub3 = false;
			}else{
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if(email.test($scope.email)){
					$scope.emailCheck = "";
					$scope.sub3 = true;
				}else{
					$scope.sub3 = false;
					$scope.emailCheck = $translate.instant('notice.crEm');
				}
			}
			if(!!$scope.emailCheck){
				document.getElementById('usererr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('usererr').style.border = '1px solid #dde6e9'
			}
		}
		//邮箱验证码
		$scope.changeEmailCode = function(){
			var emailCode = /^[a-zA-Z0-9]{6}$/;
			if(emailCode.test($scope.emailCode)){
				$scope.codeCheck = "";
				$scope.sub3 = true;
			}else{
				$scope.codeCheck = $translate.instant('notice.must');
				$scope.sub3 = false;
			}
			if(!!$scope.codeCheck){
				document.getElementById('emcodeerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('emcodeerr').style.border = '1px solid #dde6e9'
			}
		}
		//验证码
		$scope.changeCode2 = function(){
			if($scope.captchaCode == undefined || $scope.captchaCode == ""){
				$scope.capCodeCheck = $translate.instant('notice.must');
				$scope.sub3 = false;
			}else{
				$scope.capCodeCheck = "";
				$scope.sub3 = true;
			}
		}
	}
]);