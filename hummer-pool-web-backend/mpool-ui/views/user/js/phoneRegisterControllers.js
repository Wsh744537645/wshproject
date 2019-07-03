//手机注册
App.controller('phoneRegisterController', ['$scope', '$http', '$location', '$translate', 'ngDialog', '$timeout', '$interval',
	function($scope, $http, $location, $translate, ngDialog, $timeout, $interval) {
		$scope.showTerms = function() {
			if( $translate.instant('nav.homepage') == '首页') {
				ngDialog.openConfirm({
					template: 'modalTerm',
					controller: 'phoneRegisterController',
				}).then(function(value){})
			}else{
				ngDialog.openConfirm({
					template: 'modalTermEn',
					controller: 'phoneRegisterController',
				}).then(function(value){})
			}
		}
		//创建用户
		$scope.registerSuccess = true;
		$scope.getRegister = function(){
			
			if($scope.regCheck()){
				if(($scope.user == undefined || $scope.user == "") || ($scope.password == undefined || $scope.password == "") || ($scope.pasConfirm == undefined || $scope.pasConfirm == "") || ($scope.phone == undefined || $scope.phone == "") || ($scope.phoneCode == undefined || $scope.phoneCode == "")){
						
				}else{
					//加密
					var base64 = new Base64();
					$scope.pwd = base64.encode($scope.password);
					var json = {
						"username": $scope.user,         	//用户名
						"password": $scope.pwd,   	 		//密码
					}
					$http.post(mpoolUI_url + '/user/reg/sms?phoneCode='+$scope.phoneCode+'&phone='+$scope.phone, json).success(function(data){
						if(data.msg == "ok"){
							$scope.registerSuccess = false;
							$timeout(function(){
								$location.path('/login');
							},5000);
						}else{
							$scope.registerSuccess = true;
							$scope.registerTitle = data.msg;
							$(".loginTitle").fadeIn(800);
							$timeout(function(){
								$(".loginTitle").fadeOut(800);
							},5000);
						}
					})
				}
			}
		}
		
		//验证手机号码
		$scope.sendCode = true;
		$scope.getCheckPhone = function(){
			var obj = $scope.phone;
			// var captchaCode = $scope.captchaCode;
			// if(captchaCode != undefined|| captchaCode != ""){
				if(obj == undefined|| obj == ""){
					$scope.phoneCheck = $translate.instant('notice.phnull');
				}else{   
					$http.get(mpoolUI_url + '/user/reg/sendPhone?phoneNumber='+ obj).success(function(data){
						if(data.msg == "ok"){
							$scope.sendCode = false;
							$scope.disCode = false;
							$scope.phoneCheck = "";
							
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
							$scope.capCodeCheck = data.msg;
						}
					})
				}
			// }else{
			// 	$scope.capCodeCheck = "请输入验证码";
			// }
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
		$scope.regCheck = function(){
			$scope.changeUser();		//用户名
			$scope.changePassword();    //密码
			$scope.changePassCon();     //确定密码
			$scope.changePhone();		//手机
			$scope.changePhoneCode();   //手机验证码
			$scope.changeCode();		//验证码
			return true;
		}

		//用户名
		$scope.sub2 = true;
		$scope.changeUser = function(){
			if($scope.user == undefined || $scope.user == ""){
				$scope.nameCheck = $translate.instant('notice.urformat');
			}else{
				var user = userNameValidate.test;
				if(user.test($scope.user)){
					$scope.nameCheck = "";
					//注册时验证用户名是否同名
					$http.get(mpoolUI_url + '/user/checkUsername?username=' + $scope.user).success(function(data){
						if(data.msg == "ok"){
							$scope.sub2 = true;
							$scope.nameCheck = '';
						}else{
							$scope.sub2 = false;
							$scope.nameCheck = data.msg;
						}
					})
				}else{
					$scope.sub2 = false;
					$scope.nameCheck = $translate.instant('notice.userV');
				}
			}
			if(!!$scope.nameCheck){
				document.getElementById('usererr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('usererr').style.border = '1px solid #dde6e9'
			}
		}
		//密码
		$scope.changePassword = function(){
			if($scope.password == undefined || $scope.password == ""){
				$scope.passCheck = $translate.instant('notice.pswformat');
			}else{
				var pass = pwdValidate.test;
				if(pass.test($scope.password)){
					$scope.passCheck = "";
				}else{
					$scope.passCheck = $translate.instant('notice.pswV');
				}
			}
			if(!!$scope.passCheck){
				document.getElementById('pswerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('pswerr').style.border = '1px solid #dde6e9'
			}
		}
		//确定密码
		$scope.changePassCon = function(){
			if($scope.pasConfirm == undefined || $scope.pasConfirm == ""){
				$scope.pasConCheck = $translate.instant('notice.must');
			}else{
				if($scope.pasConfirm === $scope.password){
					$scope.pasConCheck = "";
				}else{
					$scope.pasConCheck =  $translate.instant('notice.comfer');
				}	
			}
			if(!!$scope.pasConCheck){
				document.getElementById('repaserr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('repaserr').style.border = '1px solid #dde6e9'
			}
		}
		
		//手机
		$scope.disCode = false;
		$scope.changePhone = function(){
			if($scope.phone == undefined || $scope.phone == ""){
				$scope.phoneCheck = $translate.instant('notice.phnull');
			}else{
				var phone = phoneValidate.test;
				if(phone.test($scope.phone)){
					$scope.phoneCheck = "";
					$scope.disCode = true;
					//注册时验证手机号是否可用
					$http.get(mpoolUI_url + '/user/checkPhone?phone=' + $scope.phone).success(function(data){
						if(data.msg == "ok"){
							$scope.sub2 = true;
							$scope.phoneCheck = '';
						}else{
							$scope.sub2 = false;
							// $scope.disCode = false;
							$scope.phoneCheck = data.msg;
						}
					})
				}else{
					$scope.sub2 = false;
					$scope.phoneCheck = $translate.instant('notice.crP');
				}
			}	
			if(!!$scope.phoneCheck){
				document.getElementById('phoerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('phoerr').style.border = '1px solid #dde6e9'
			}
		}
		
		//邮箱验证码
		$scope.changePhoneCode = function(){
			if($scope.phoneCode == undefined || $scope.phoneCode == ""){
				$scope.codeCheck = $translate.instant('notice.must');
			}else{
				$scope.codeCheck = "";
			}
			if(!!$scope.codeCheck){
				document.getElementById('phcodeerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('phcodeerr').style.border = '1px solid #dde6e9'
			}
		}
		
		//验证码 
		$scope.changeCode = function(){
			if($scope.captchaCode == undefined || $scope.captchaCode == ""){
				$scope.capCodeCheck = $translate.instant('notice.must');
			}else{
				$scope.capCodeCheck = "";
			}
			// if(!!$scope.capCodeCheck){
			// 	document.getElementById('codeerr').style.border = '1px solid #FF3B30'
			// }else{
			// 	document.getElementById('codeerr').style.border = '1px solid #dde6e9'
			// }
		}
	}
]);