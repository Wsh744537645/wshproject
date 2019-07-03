//创建子账户模块代码
App.controller('addsubaccountsController', ['$scope', '$http', '$location', '$state', '$timeout', '$translate', '$cookieStore', 'Notify',      
	function($scope, $http, $location, $state, $timeout, $translate, $cookieStore, Notify) {
		//查询区域
		$scope.getSelectRegion = function(){
			$http.get(mpoolUI_url + '/region/selectRegion').success(function(data){
				if(data.msg == "ok"){
					$scope.itemsRegion = data.data;
					var data = data.data[0].regionId;
					//初始化获取区域
					$scope.regionId = {
				    	status: data
				  	}
				}
			})
		}
		
		//挖矿币种、最小打款额、结算方式
		$scope.amountOfMoney = function(){
			$http.get(mpoolUI_url + '/wallet/select/map').success(function(data){
				$scope.dataBtc = data.data;
				$scope.wallet_Type = [];    //币种
				$scope.pay_Type = data.data.payType;   //结算方法
				for(var i in data.data.walletType){
					$scope.wallet_Type.push({key:i,value:data.data.walletType[i]})
				}
				var dataKey = $scope.wallet_Type[0].key;
				$scope.walletType = {   //初始化币种
			    	money: dataKey,
			  	}
				$scope.getMoney(); //最小打款额	
				
				//结算方法
				$scope.payType = {
			    	name: $scope.pay_Type[0],
			    }
			})
		}
		//最小打款额
		$scope.getMoney = function(){
			for(var i=0;i<$scope.wallet_Type.length;i++){
				var name = $scope.wallet_Type[i].key;
				if(name == $scope.walletType.money){
					$scope.aMoney = $scope.wallet_Type[i].value;
					$scope.btcMiniPay = $scope.aMoney[0];
				}
			}
		}
		//验证方式
		$scope.checkMode = {0:'手机号码', 1:'邮箱'};
		$scope.checkValue = "0";  //设置默认值
		//选择验证方式
		$scope.checkMethod = false;
		$scope.getCheckValue = function(){
			//0:手机号码           1:邮箱
			var sequence = $scope.checkValue;   //获取界面 选择验证方法参数值
			if(sequence == 0){
				$scope.checkMethod = false;
			}else{
				$scope.checkMethod = true;
			}
		}
		
		//创建子账户
		$scope.subaccount = true;
		$scope.getRegister = function(){
			if($scope.regCheck()){
				if(($scope.username == undefined || $scope.username == "") || ($scope.password == undefined || $scope.password == "") || ($scope.btcMiniPay == undefined || $scope.btcMiniPay == "")){	
				}else{
					$scope.sub = false;
					$scope.wallet =	[{
				    	"walletType": $scope.walletType.money,  //挖矿币种
				    	"walletAddr": $scope.walletBtcAddr,     //btc钱包地址
				    	"miniPay": $scope.btcMiniPay            //btc最小打款额
				   	}];
				   	var base64 = new Base64();
					var passwords = base64.encode($scope.password);
					$scope.json = {
						"username": $scope.username,        		//子账户名
						"regionId": $scope.regionId.status,      	//区域
						"password": passwords,        				//密码					
					  	"payType": $scope.payType.name,				//结算方式
					  	"wallets": $scope.wallet,					//钱包
					  	"copyMaster": false							//是否拷贝主账号邮箱和手机 
					};
					$http.post(mpoolUI_url + '/userManager/createSubAccount', $scope.json).success(function(data){
						if(data.msg == "ok"){
							$scope.sub = true;
							$scope.subaccount = false;
							$scope.empty();  //清空input输入框
							$timeout(function(){
								$scope.subaccount = true;
							},3000);
						}else{
							$scope.sub = false;
							$scope.subaccount = true;
							$scope.registerTitle = data.msg;
							$(".title1").fadeIn(800);
							$timeout(function(){
								$(".title1").fadeOut(800);
							},5000);
						}
					})
					
				}
			}
		}
		
		//清空创建子账户 input输入框
		$scope.empty = function(){
			$scope.username = '';        	//子账户名
			$scope.password = '';        	//密码
			$scope.pasConfirm = '';         //确定密码
		}
		
		//注册验证
		$scope.regCheck = function(){
			//用户名
			$scope.changeUsername();
			//密码
			$scope.changePassword();
			//确定密码
			$scope.changePassCon();
			return true;
		}

		//用户名
		$scope.sub = true;
		$scope.changeUsername = function(){
			if($scope.username == undefined || $scope.username == ""){
				$scope.sub = false;
				$scope.nameCheck = $translate.instant('notice.phnull');
			}else{
				var user = userNameValidate.test;
				if(user.test($scope.username)){
					$scope.nameCheck = "";
					//注册子账号时验证用户名是存在
					$http.get(mpoolUI_url + '/user/checkUsername?username=' + $scope.username)
						.success(function(data){
							if(data.msg == "ok"){
								$scope.sub = true;
							}else{
								$scope.sub = false;
								$scope.nameCheck = data.msg;
							}
						})
				}else{
					$scope.sub = false;
					$scope.nameCheck = $translate.instant('notice.userV');
				}
			}
		}
		$scope.walletCheck = ''
		//钱包最小打款额 验证
		$scope.check = function(obj){
			if(!isNull(obj)){
				$http.get(mpoolUI_url + '/wallet/walletAddress/validated?walletAddress='+obj)
					.success(function(data){
						if(data.msg == 'ok'){
							$scope.walletCheck = '';
						}else{
							$scope.walletCheck = $translate.instant('notice.ader');
						}
					})
			}else{
				$scope.walletCheck = '';
			}
		}
		
		//密码
		$scope.changePassword = function(){
			if($scope.password == undefined || $scope.password == ""){
				$scope.passCheck = $translate.instant('login.enterPsw');
				$scope.sub = false;
			}else{
				var pas = pwdValidate.test;
				if(pas.test($scope.password)){
					$scope.passCheck = "";
					$scope.sub = true;
				}else{
					$scope.passCheck = $translate.instant('notice.pswV')
					$scope.sub = false;
				}
			}
		}
		//确定密码
		$scope.changePassCon = function(){
			if($scope.pasConfirm == undefined || $scope.pasConfirm == ""){
				$scope.pasConCheck = $translate.instant('notice.must');
				$scope.sub = false;
			}else{
				if($scope.pasConfirm === $scope.password){
					$scope.pasConCheck = "";
					$scope.sub = true;
				}else{
					$scope.pasConCheck =  $translate.instant('notice.comfer');
					$scope.sub = false;
				}	
			}
		}

		//初始化
		$scope.init = function(){
			$scope.amountOfMoney();    //最小打款额
			$scope.getSelectRegion();   //查询区域
		}
		$scope.init();
		
		//创建子账户
		$scope.back = function () {
			$location.path('api/subaccounts');
		}

		//跳转首页界面
		$scope.homepage = function () {
	         var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		
	}
])