
//用户中心     -----   绑定手机  提交流程
App.controller('bindingPhoneController', ['$scope', '$http', '$location', '$translate', '$state', '$timeout', '$interval', 'ngDialog', 'Notify', '$stateParams', 
	function($scope, $http, $location, $translate, $state, $timeout, $interval, ngDialog, Notify, $stateParams) {
		//跳转首页界面
		$scope.homepage = function () {
	         var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		//返回
		$scope.Return = function(){
			$location.path('api/newAccounts'); //用户中心
		}
		$scope.emailParameter = $stateParams.userEmail;   //邮箱参数
		
		//步骤流程
		$scope.show1 = true;
		$scope.initStep = function (obj) {
			$(".steps").step({
			  	stepNames: [$translate.instant('notice.Verem'), $translate.instant('notice.bpsc'), $translate.instant('notice.Done')],
			  	initStep: obj
			})
			if(obj == 1){
				$scope.show1 = true;
				$scope.show2 = false;
				$scope.show3 = false;
			}else if(obj == 2){
				$scope.show1 = false;
				$scope.show2 = true;
				$scope.show3 = false;
			}else if(obj == 3){
				$scope.show1 = false;
				$scope.show2 = false;
				$scope.show3 = true;
			}
		}
		
//------------------------------第一步   邮箱验证			
		//获取邮箱验证码
		$scope.time = $translate.instant('subActsMng.send');
		$scope.checkEmailCode = true;
		$scope.getCheckEmail = function(){
			$http.get(mpoolUI_url + '/userManager/getSecurityCode?receiveCode=email')
				.success(function(data){
					if(data.msg == "ok"){
						$scope.checkEmailCode = false;
						var maxtime = 1 * 60; 
				      	function CountDown() {
				        	if (maxtime >= 0) {
				        		if(maxtime != 60){
				        			seconds = Math.floor(maxtime % 60);
				        		}else{
				        			seconds = maxtime;
				        		}
				          		msg = seconds + "s";
				          		$scope.time = msg;
				          		--maxtime;
				        	}else{
				        		$scope.checkEmailCode = true;
				          		$scope.time = $translate.instant('subActsMng.send');
				        	}
				      	}  
				      	$interval(function(){CountDown()},1000,62);
					}else{
						$scope.emailCheck = data.msg;  //错误提示
					}
				})
		}
		//点击下一步时   判断邮箱验证码是否正确
		$scope.getCodeEmail = function(){
			$http.get(mpoolUI_url + '/userManager/checkSecurityCode?securityCode='+ $scope.codeEmail)
				.success(function(data){
					if(data.msg == 'ok'){
						$scope.initStep(2);   //步骤流程
					}else{
						$scope.emailTitle = data.msg;   //错误提示
						$(".title1").fadeIn(800);
						$timeout(function(){
							$(".title1").fadeOut(800);
						},5000);
					}
				})
		}
		//邮箱   验证码空指针判断
		$scope.changeEmail = function(){
			if($scope.codeEmail == undefined || $scope.codeEmail == ""){
				$scope.sub0 = false;
			}else{
				$scope.sub0 = true;
			}
		}
		
//------------------------------第二步   绑定手机		
		//验证手机是否被重用
		$scope.disCode2 = false;   //禁用发送验证码按钮
		$scope.sub1 = false;       //禁用点击下一步按钮
		$scope.changePhone = function(){
			$scope.phoneNum = $scope.phone;  //第三步绑定到界面
			if($scope.phone == undefined || $scope.phone == ""){
				$scope.phoneCheck = $translate.instant('notice.phformat');
				$scope.disCode2 = false;
			}else{
				var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
				if(phone.test($scope.phone)){
					$scope.phoneCheck = "";
					//注册时验证用户名是否同名
					$http.get(mpoolUI_url + '/user/checkPhone?phone=' + $scope.phone).success(function(data){
						if(data.msg == "ok"){
							$scope.disCode2 = true;
							$scope.phoneCheck = "";
						}else{
							$scope.disCode2 = false;
							$scope.phoneCheck = data.msg;
						}
					})
				}else{
					$scope.disCode2 = false;
					$scope.phoneCheck = $translate.instant('notice.phformat');
				}
			}
		}
		
		//获取手机验证
		$scope.time2 = $translate.instant('subActsMng.send');
		$scope.getCheckPhone = function(){
			$http.get(mpoolUI_url + '/userManager/send/phone?phone=' + $scope.phone)
				.success(function(data){
					if(data.msg == "ok"){
						$scope.disCode2 = false;
						var maxtime = 1 * 60; 
				      	function CountDown() {
				        	if (maxtime >= 0) {
				          		if(maxtime != 60){
				        			seconds = Math.floor(maxtime % 60);
				        		}else{
				        			seconds = maxtime;
				        		}
				          		msg = seconds + "s";
				          		$scope.time2 = msg;
				          		--maxtime;
				        	}else{
				        		$scope.disCode2 = true;
				          		$scope.time2 = $translate.instant('subActsMng.send');
				        	}
				      	}  
				      	$interval(function(){CountDown()},1000,62);
					}else{
						$scope.phoneCodeCheck = data.msg;
					}
				})
		}
		//手机   验证码空指针判断
		$scope.changePhoneCode = function(){
			if($scope.codePhone == undefined || $scope.codePhone == ""){
				$scope.sub1 = false;
			}else{
				$scope.sub1 = true;
			}
		}
		
		//修改手机号提交接口     数据提交     
		$scope.postPhone = function(){
			$http.get(mpoolUI_url + '/userManager/checkSecurityCode?securityCode='+ $scope.codePhone)
				.success(function(data){
					if(data.msg == 'ok'){
						$scope.initStep(3);   //步骤流程
					}else{
						$scope.titleData1 = data.msg;   //错误提示
						$(".title2").fadeIn(800);
						$timeout(function(){
							$(".title2").fadeOut(800);
						},5000);
					}
				})
			
		}
		
//------------------------------第三步   完成		
		$scope.complete = function(){
			$http.post(mpoolUI_url + '/userManager/current/change/phone?phone='+ $scope.phone)
				.success(function(data){
					if(data.msg == "ok"){
						$location.path('api/newAccounts'); //用户中心
					}else{
						$scope.titleData2 = data.msg;
						$(".title3").fadeIn(800);
						$timeout(function(){
							$(".title3").fadeOut(800);
						},5000);
					}
				})	
			
		}
		
		//初始化方法
		$scope.init = function(){
			$scope.initStep(1);        //步骤流程
		}
		$scope.init();
	}
])
