
//邮箱注册
App.controller('registerController', ['$scope', '$http', '$location', '$translate', 'ngDialog', '$timeout', '$interval',
	function($scope, $http, $location, $translate,  ngDialog, $timeout, $interval) {
		
		//创建用户
		
		$scope.showTerms = function() {
			if( $translate.instant('nav.homepage') == '首页') {
				ngDialog.openConfirm({
					template: 'modalTerm',
					controller: 'registerController',
				}).then(function(value){})
			}else{
				ngDialog.openConfirm({
					template: 'modalTermEn',
					controller: 'registerController',
				}).then(function(value){})
			}
		}
		$scope.registerSuccess = true;
		$scope.getRegister = function(){
			if($scope.regCheck()){
				if(($scope.user == undefined || $scope.user == "") || ($scope.password == undefined || $scope.password == "") || ($scope.pasConfirm == undefined || $scope.pasConfirm == "") || ($scope.email == undefined || $scope.email == "") || ($scope.emailCode == undefined || $scope.emailCode == "") ){
						
				}else{
					//加密
					var base64 = new Base64();
					$scope.pwd = base64.encode($scope.password);
					var json = {
						"username": $scope.user,         	//用户名
						"password": $scope.pwd,   	 		//密码
					}
					$http.post(mpoolUI_url + '/user/reg?emailCode='+$scope.emailCode+'&email='+$scope.email, json).success(function(data){
						if(data.msg == "ok"){
							$scope.registerSuccess = false;
							$timeout(function(){
								$location.path('/login');
							},3000);
						}else{
							$scope.registerSuccess = true;
							$scope.registerTitle = data.msg;
							$(".loginTitle").fadeIn(800);
							$timeout(function(){
								$(".loginTitle").fadeOut(800);
							},3000);
						}
					})
				}
			}
		}
		
		//验证邮箱
		$scope.sendCode = true;
		$scope.getCheckEmail = function(){
			var obj = $scope.email;
			// var captchaCode = $scope.captchaCode;
			// if(captchaCode != undefined|| captchaCode != ""){
				if(obj == undefined || obj == "" ){
					$scope.emailCheck = $translate.instant('notice.enterem');
				}else{
					$http.get(mpoolUI_url + '/user/sendEmail?mail=' + obj).success(function(data){
						if(data.msg == "ok"){
							$scope.sendCode = false;
							$scope.disCode = false;
							$scope.emailCheck = "";
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
			$scope.changeUser();        //用户名
			$scope.changePassword();    //密码
			$scope.changePassCon();     //确定密码
			$scope.changeEmail();       //邮箱
			$scope.changeEmailCode();   //邮箱验证码
			$scope.changeCode();        //验证码
			return true;
		}

		//用户名
		$scope.sub1 = true;
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
							$scope.sub1 = true;
							$scope.nameCheck = '';
						}else{
							$scope.sub1 = false;
							$scope.nameCheck = data.msg;
						}
					})
				}else{
					$scope.sub1 = false;
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
		
		//邮箱
		$scope.disCode = false;
		$scope.changeEmail = function(){
			if($scope.email == undefined || $scope.email == ""){
				$scope.emailCheck = $translate.instant('notice.emNull');
			}else{
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if(email.test($scope.email)){
					$scope.emailCheck = "";
					$scope.disCode = true;
					//注册时验证用户名是否同名
					$http.get(mpoolUI_url + '/user/checkMail?mail=' + $scope.email).success(function(data){
						if(data.msg == "ok"){
							$scope.sub1 = true;
							$scope.emailCheck = '';
						}else{
							$scope.sub1 = false;
							// $scope.disCode = false;
							$scope.emailCheck = data.msg;
						}
					})
				}else{
					$scope.sub1 = false;
					$scope.emailCheck = $translate.instant('notice.crEm');
				}
			}
			if(!!$scope.emailCheck){
				document.getElementById('emailerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('emailerr').style.border = '1px solid #dde6e9'
			}	
		}
		
		//邮箱验证码
		$scope.changeEmailCode = function(){
			if($scope.emailCode == undefined || $scope.emailCode == ""){
				$scope.codeCheck = $translate.instant('notice.must');
			}else{
				$scope.codeCheck = "";
			}
			if(!!$scope.codeCheck){
				document.getElementById('emailcodeerr').style.border = '1px solid #FF3B30'
			}else{
				document.getElementById('emailcodeerr').style.border = '1px solid #dde6e9'
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