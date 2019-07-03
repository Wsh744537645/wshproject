//创建子账户模块代码
App.controller('newcoinController', ['$scope', '$http', '$location', '$state', '$timeout', '$translate', '$cookieStore', '$stateParams',      
	function($scope, $http, $location, $state, $timeout, $translate, $cookieStore, $stateParams) {
        $scope.subacname = $stateParams.data.name
		//挖矿币种、最小打款额、结算方式
		$scope.amountOfMoney = function(){
			$http.get(mpoolUI_url + '/wallet/select/map').success(function(data){
				$scope.dataBtc = data.data;
				$scope.wallet_Type = [];    //币种
				$scope.pay_Type = data.data.payType;   //结算方法
				for(var i in data.data.walletType){
					if(!$stateParams.data.othercoin) {
						if(i !== $stateParams.data.coin) {
							$scope.wallet_Type.push({key:i,value:data.data.walletType[i]})
						}
					}else{
						$stateParams.data.othercoin.forEach(other => {
							if(i !== other.currencyName && i !== $stateParams.data.coin) {
								$scope.wallet_Type.push({key:i,value:data.data.walletType[i]})
							}
						});
					}
				}
				$scope.walletType = {   //初始化币种
			    	money: $scope.wallet_Type[0].key
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
			// for(var i=0;i<$scope.wallet_Type.length;i++){
			// 	var name = $scope.wallet_Type[i].key;
			// 	if(name == $scope.walletType.money){
			// 		$scope.aMoney = $scope.wallet_Type[i].value;
			// 		$scope.MiniPay = $scope.aMoney[0];
			// 	}
			// }
		}
		
		//创建新币种
		$scope.newcoinsc = true;
		$scope.walletAddr = '';
		$scope.getRegister = function(){
			$scope.json = {
				"username": $scope.subacname,        		//子账户名
				"walletType": $scope.walletType.money,					//钱包
				"walletAddress": $scope.walletAddr,				//结算方式
			};
			$http.post(mpoolUI_url + '/userManager/createPay', $scope.json).success(function(data){
				if(data.msg == "ok"){
					$scope.newcoinsc = false;
					$scope.empty();  //清空input输入框
					$timeout(function(){
						$scope.newcoinsc = true;
					},3000);
				}else{
					$scope.newcoinsc = true;
					$scope.registerTitle = data.msg;
					$(".title1").fadeIn(800);
					$timeout(function(){
						$(".title1").fadeOut(800);
					},5000);
				}
			})
		}
		
		//清空创建子账户 input输入框
		$scope.empty = function(){
			$scope.username = '';        	//子账户名
			$scope.password = '';        	//密码
			$scope.pasConfirm = '';         //确定密码
		}
		
		// 验证地址
		$scope.walletCheck = ''
		$scope.checkAddr = function(obj){
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

		
		//初始化
		$scope.init = function(){
			$scope.amountOfMoney();    //最小打款额
		}
		$scope.init();
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