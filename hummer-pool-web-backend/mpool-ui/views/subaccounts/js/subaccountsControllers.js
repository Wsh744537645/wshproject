
//子账户管理
App.controller('subaccountsController', ['$scope', '$http', '$location', '$state', '$timeout', 'ngDialog', '$translate', 'Notify',      
	function($scope, $http, $location, $state, $timeout, ngDialog, $translate, Notify) {
		
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			$scope.currencyData = r.data
			angular.forEach($scope.currencyData,(d)=> {
				d.coinIcon = "../../img/banner/" + d.type + "1.png"
			})
		})
		$scope.currencyName= 'BTC'
		$scope.currencyCoin= "../../img/banner/BTC1.png"
		$scope.changeCoin =  function(name) {
			$scope.currencyName = name
			$scope.getSubaccountList(1,$scope.p_pernum)
		}

		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
		}
		$scope.page = 1;
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'username',
            direction: 1,
            toggle: function (column) {
            	if(column.name != "walletType" && column.name != "operation"){
            		if (column.sortable === false)
	                    return;
	                if (this.column === column.name) {
	                    this.direction = -this.direction || -1;
	                } else {
	                    this.column = column.name;
	                    this.direction = -1;
	                }
            	}
            }
        };
        vm.columns = [
			{label: $translate.instant('subActsMng.sub-accountname'),	name: 'username',		type: 'string'},
			{label: $translate.instant('subActsMng.mining coin'),	name: 'walletType',		type: 'string',class:'hidden-xs'},
        	{label: $translate.instant('subActsMng.Hashrate'),		name: 'currentShareT',	type: 'string',class:'hidden-xs'},
        	{label: $translate.instant('subActsMng.ets'),name: 'currentShareT',	type: 'string',class:'hidden-xs'},
            {label: $translate.instant('subActsMng.withdraw address'),	name: 'walletType',		type: 'string',class:'hidden-xs'},
            {label: $translate.instant('subActsMng.Min Pay'),	name: 'miniPay',		type: 'int',class:'hidden-xs'},
            {label: $translate.instant('subActsMng.Payment Method'),	name: 'payType',		type: 'string',class:'hidden-xs'},
            {label: $translate.instant('subActsMng.operation'),		name: 'operation',		type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		
//展示子账户列表模块代码
		$scope.currentSelect = {
	    	value: '10',
	 	}
		//子账户列表
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getSubaccountList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/userManager/findSubAccount?current='+current+'&size='+size+ '&currencyName=' +$scope.currencyName)
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
              		 	if(data.data != null){
							$scope.itemsData = data.data.records;
							angular.forEach($scope.itemsData,(item)=>{
								item.coinIcon = "../../img/banner/"+item.walletType+"1.png"
								item.addCoin = true
								if(!!item.otherCurrency) {
									if((item.otherCurrency.length + 1) == $scope.currencyData.length) {
										item.addCoin = false
									}
								}
							})
							$scope.items_num = $scope.itemsData.length;
							$scope.count = data.data.total;     //总页数
			                $scope.totalPage = data.data.pages;   //页数
			                $scope.p_current = current;  
			                $scope.p_all_page = Math.ceil($scope.count/size);  
			                reloadPno();  //初始化页码  
		                }else{
	          		 		$scope.items_num = 0;
	          		 	}
					}
				}).error(function(data, status, headers, config) {
					$timeout(function(){
					   $scope.show_items = true;
          		 	},800);
					$scope.itemsData = [];
					$scope.items_num = 0;
				});	
		}
		//首页  
	    $scope.p_index = function(){  
	        $scope.load_page(1, $scope.currentSelect.value);  
	    }  
	    //尾页  
	    $scope.p_last = function(){  
	        $scope.load_page($scope.p_all_page, $scope.currentSelect.value);  
	    }  
	    //加载某一页  
	    $scope.load_page = function(page,pernum){  
	        $scope.getSubaccountList(page,pernum);  
	    };  
	    //初始化页码  
	    var reloadPno = function(){  
	      	$scope.pages = calculateIndexes($scope.p_current,$scope.p_all_page,8);  
	    };  
		//分页算法  
		var calculateIndexes = function (current, length, displayLength) {  
		   var indexes = [];  
		   var start = Math.round(current - displayLength / 2);  
		   var end = Math.round(current + displayLength / 2);  
		    if (start <= 1) {  
		        start = 1;  
		        end = start + displayLength - 1;  
		       if (end >= length - 1) {  
		           end = length - 1;  
		        }  
		    }  
		    if (end >= length - 1) {  
		       end = length;  
		        start = end - displayLength + 1;  
		       if (start <= 1) {  
		           start = 1;  
		        }  
		    }  
		    for (var i = start; i <= end; i++) {  
		        indexes.push(i);  
		    }  
		    return indexes;  
		}

		//切换子账号
		$scope.getsWitchSubAccount = function(objID,objName,type){
			sessionStorage.setItem("obj2", objName); 
			ngDialog.openConfirm({
				template: 'modelGroup',
				controller: 'GroupCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				sessionStorage.setItem('currencyName', type)
				$http.get(mpoolUI_url + '/userManager/switchSubAccount/'+ objID + '?currencyName=' + type)
					.success(function(data){
						if(data.msg == "ok"){
							$location.path('/switchAccount'); //跳转界面
						}else{
							var title1 = data.msg;
							Notify.alert( 
					        	title1, 
					        	{status: 'info'}
					    	)
						}
					})	
			})
		}
		
		
		$scope.getWallet = function(){
			$http.get(mpoolUI_url + '/wallet/user/wallet/info').success(function(data){
				if(data.msg == "ok"){
					$scope.items_Data = data.data;
				}
			})
		}
		
		
		//钱包地址
		$scope.getWalletManger = function(obj,id,item){
			sessionStorage.setItem("objValue", obj);  //存储钱包地址
			$scope.userId = id;
			ngDialog.openConfirm({
				template: 'modalWalletManger',
				controller: 'WalletMangerCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				$scope.addressData = value.addressData;    //修改后的新钱包地址 
				$scope.getWalletInfo(item);  //钱包地址下一步获取验证码
			})
		}
		//钱包地址下一步获取验证码
		$scope.getWalletInfo = function(item){
			sessionStorage.setItem("addressData", $scope.addressData);  //存储钱包地址
			ngDialog.openConfirm({
				template: 'modalNextStep',
				controller: 'NextStepCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				if(value.codeURL == '/wallet/send/phone'){
					$scope.subURL = '/wallet/user/wallet/edit/phone?userId='+$scope.userId+'&phone='+$scope.items_Data.phone+'&phoneCode='+value.workerName
				}else{
					$scope.subURL = '/wallet/user/wallet/edit?userId='+$scope.userId+'&email='+$scope.items_Data.email+'&emailCode='+value.workerName
				}
				//修改钱包信息
				var json = {
					"email": $scope.items_Data.email,               //邮箱
					"phone": $scope.items_Data.phone,		        //手机号
	  				"payType": item.payType,           //结算方式
	  				"regionName": $scope.items_Data.regionName,     //区域
				  	"miniPay": item.miniPay,    	    //打款最小额
				  	"walletAddr": $scope.addressData,   			//钱包地址
				  	"walletId": item.walletId,         //钱包id
				  	"walletType": item.walletType      //挖矿币种
				}
				$http.post(mpoolUI_url + $scope.subURL, json).success(function(data){
					if(data.msg == "ok"){ 
						Notify.alert($translate.instant('notice.wlsc') + '！！！', {status: 'success'});
						$scope.getSubaccountList($scope.p_current,$scope.p_pernum);   //子账户列表
					}else{
						Notify.alert(data.msg,{status: 'info'})
					}
				})
			})
		}
		
		
		
		//起付金额设置
		$scope.getSum = function(objId,objMiniPay,type){
			sessionStorage.setItem("objtype", type); 
			sessionStorage.setItem("objMiniPay", objMiniPay);  //存储
			ngDialog.openConfirm({
				template: 'modalsum123',
				controller: 'sumCtrl123',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var minPay = value.statusValue;
				$http.post(mpoolUI_url + '/wallet/user/wallet/edit/minPay?minPay='+minPay+'&walletType='+ type +'&userId='+objId)
					.success(function(data){
						if(data.msg == "ok"){   
							Notify.alert($translate.instant('notice.qfsc') + '！！！', {status: 'success'});
							$scope.getSubaccountList($scope.p_current,$scope.p_pernum);   //子账户列表
						}else{
							Notify.alert(data.msg,{status: 'info'})
						}
					})
			})
		}



		//分享
		$scope.postCreateWorkerGroup = function(objuserId,type){
			sessionStorage.setItem("objuserId", objuserId); 
			sessionStorage.setItem("currencyName", type); 
			ngDialog.openConfirm({
				template: 'modalSharesub123',
				controller: 'ShareCtrlsub123',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				
			})
		}
		
		
		//报警设置
		$scope.getSettings = function(obj,objCurrentShareT,objworkerActive,type){
			$scope.user_id = obj;
			sessionStorage.setItem("userid2", $scope.user_id);  //存储编号
			sessionStorage.setItem("objCurrentShareT", objCurrentShareT);  //存储编号
			sessionStorage.setItem("objworkerActive", objworkerActive);  //存储编号
			sessionStorage.setItem("subcurrencyName", type);  //存储编号
			ngDialog.openConfirm({
				template: 'modalSettings123',
				controller: 'SettingsCtrl123',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				$scope.getSettingsNextstep(value.cordon, value.cordonNumber);
			})
		}
		//报警设置  下一步
		$scope.getSettingsNextstep = function(objCordon,objCordonNumber){
			sessionStorage.setItem("objCordon", objCordon); 
			sessionStorage.setItem("objCordonNumber", objCordonNumber);  
			sessionStorage.setItem("userid", $scope.user_id);  //存储编号
			ngDialog.openConfirm({
				template: 'modalSettingsNextstep123',
				controller: 'SettingsNextstepCtrl123',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var itemsData = value.itemsData;
				itemsData.userShare = value.dataValue.cordon;
				itemsData.workerActive = value.dataValue.cordonNumber;
				//查询手机号和邮箱
				$http.post(mpoolUI_url + '/alarm/add?userId=' + $scope.user_id, itemsData).success(function(data){
					if(data.msg == 'ok'){
						Notify.alert($translate.instant('notice.setsc') + '！！！', {status: 'success'})
					}else{
						Notify.alert(data.msg, {status: 'info'})
					}
				})
			})
		}
		
		
		//修改密码
		$scope.getChangePassword = function(obj){
			ngDialog.openConfirm({
				template: 'modalChangePassword',
				controller: 'ChangePasswordCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var newPassword = value.newPassword;
				$http.post(mpoolUI_url + '/userManager/master/change/subUserPassword?userId='+obj,newPassword)
					.success(function(data){
						if(data.msg == 'ok'){
							Notify.alert($translate.instant('notice.pswsc') + '！！！', {status: 'success'})
						}else{
							Notify.alert(data.msg, {status: 'info'})
						}
					})
			})
		}
		
		//初始化
		$scope.init = function(){
			$scope.getWallet();  //查询子钱包信息
			$scope.getSubaccountList($scope.p_current,$scope.p_pernum);   //子账户列表
		}
		$scope.init();
		
		//创建子账户
		$scope.addback = function () {
			$location.path('api/addsubaccount');
		}

		$scope.newcoin = function (name, coin, other) {
			$state.go('api.newcoin', {data:{name: name,coin: coin, othercoin: other}})
		}	

	}
])

//模态框 切换子账号
App.controller('GroupCtrl', ['$scope', 'ngDialog', '$cookieStore',
	function($scope, ngDialog, $cookieStore) {
		$scope.name = sessionStorage.getItem('obj2');   //获取删除的名称
	}
])

//子账号切换住账号控制器
App.controller('switchAccountController', ['$scope', '$location','$cookieStore','$http','$rootScope','$timeout',
	function($scope, $location,$cookieStore,$http,$rootScope,$timeout) {
		$timeout(function(){
			$scope.show=true;
		},500)
		$http.get(mpoolUI_url + '/userManager/current/getUserInfo').success(function(resp) {
			var masterUserId = resp.data.masterUserId
			var currUsername= $cookieStore.get("nickName")
			if(masterUserId){
				$cookieStore.put("masterUserId",masterUserId)
				$cookieStore.put("master",{username:currUsername,userId:masterUserId})
				$location.path('api/sondashboard'); //子账号页面
			}else{
				$cookieStore.remove("masterUserId")                                                                                                              
				$cookieStore.remove("master")
				$location.path('api/dashboard'); //主账号页面
			}
		})
	}
])


//----------------------------------分享
App.controller('ShareCtrlsub123', ['$http', '$scope', 'ngDialog', '$cookieStore', 'Notify', '$translate',
	function($http, $scope, ngDialog, $cookieStore, Notify, $translate) {
		
		$scope.currencyName = sessionStorage.getItem('currencyName'); 
		$scope.modalshare = true;
		var userId = sessionStorage.getItem('objuserId');
		$scope.itemsModal = [{name:'天',value:'d'},
							 {name:'时',value:'h'},
							 {name:'分',value:'m'}]
		$scope.groupName = {name:'天',value:'d'};
		
		//验证
		$scope.checkData = function(){
			var check = /^\d+$/;
			if(check.test($scope.data)){
				$scope.numberCheck = "";
			}else{
				$scope.numberCheck = '输入有效期';
			}
		}
		
		// $scope.getShare = function(){
		// 	var val = $scope.groupName.value;
		// 	var check = /^\d+$/;
		// 	$scope.numberCheck = "";
		// 	if(check.test($scope.data)){
	  	// 		$http.post(mpoolUI_url + '/userManager/share?' + val+ "=" + $scope.data+'&userId='+userId).success(function(data){
  		// 			if(data.msg == "ok"){
		// 				$scope.num = data.data;
		// 				$scope.modalshare = false;
		// 				Notify.alert('分享链接获取成功！！！', {status: 'success'});
  		// 			}else{
  		// 				$scope.modalshare = true;
		// 				Notify.alert(data.msg,{status: 'info'})
  		// 			}
  		// 		})
	  	// 	}else{
		// 		$scope.numberCheck = '输入有效期';
		// 	}
		// }	
		$http.post(mpoolUI_url + '/userManager/share?&userId='+userId+'&currencyName=' + $scope.currencyName).success(function(data){
			if(data.msg == "ok"){
				$scope.num = data.data;
				// $scope.modalshare = false;
				Notify.alert($translate.instant('sharepage.shsc') + '！！！', {status: 'success'});
			}else{
				$scope.modalshare = true;
				Notify.alert(data.msg,{status: 'info'})
			}
		})
		
		//复制方法
		$scope.butCopy = $translate.instant('subActsMng.copy');
		$scope.clickCopy = function(){
			var Url1=document.getElementById("copy");
		  	Url1.select(); //选择对象
		  	var tag = document.execCommand("Copy"); //执行浏览器复制命令
			if(tag){
			  	$scope.butCopy = $translate.instant('subActsMng.copyed');
			}
		}
	}
])


//-----------------------------------------起付金额设置
App.controller('sumCtrl123', ['$http', '$scope', 'ngDialog', '$cookieStore',
	function($http, $scope, ngDialog, $cookieStore) {
		$scope.statusData = []
		$scope.objtype = sessionStorage.getItem('objtype');
		$scope.objMiniPay = sessionStorage.getItem('objMiniPay');
		$http.get(mpoolUI_url + '/wallet/select/map').success(function(data){
			const minpay = data.data.walletType[$scope.objtype]
			angular.forEach(minpay, (m, index)=>{
				const one = {
					name: m,
					id: index
				}
				$scope.statusData.push(one)
			})
		})
		
		// $scope.statusData = [
  		// 	{name: '0.1 ',  id: '0'},
    	// 	{name: '0.01 ',  id: '1'},
    	// 	{name: '0.005',  id: '2'}
  		// ];
  		//设定起付金额
		$scope.getState = function(index2, name){
			$scope.selectedState = parseInt(index2);
			$scope.statusValue = name;
		}
		$scope.getState(0); 
	}
])

//-----------------------------------------钱包地址
App.controller('WalletMangerCtrl', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {
		var obj = sessionStorage.getItem('objValue');
		if(!isNull(obj)){
			$scope.address = obj;
		}else{
			$scope.address =  $translate.instant('notice.noset');
		}
		
		$scope.sub = true;
		$scope.check = function(obj){
			if(obj){
				$http.get(mpoolUI_url + '/wallet/walletAddress/validated?walletAddress='+$scope.addressData)
					.success(function(data){
						if(data.msg == 'ok'){
							$scope.sub = false;
						}else{
							$scope.walletCheck = data.msg;
							$scope.sub = true;
						}
					})
			}else{
				$scope.walletCheck = '';
				$scope.sub = true;
			}
		}
	}
])
//钱包地址下一步获取验证码
App.controller('NextStepCtrl', ['$http', '$scope', 'ngDialog', '$cookieStore', '$interval', '$translate',
	function($http, $scope, ngDialog, $cookieStore, $interval, $translate) {
		
		$scope.addressData = sessionStorage.getItem('addressData');
		$scope.time = $translate.instant('subActsMng.send')
		//查询子钱包信息
		$http.get(mpoolUI_url + '/wallet/user/wallet/info').success(function(data){
			if(data.msg == "ok"){
				$scope.itemsData = data.data;
				//验证方法
				$scope.checkMode = [];
				if($scope.itemsData.email != null && $scope.itemsData.phone != null){
					$scope.modeEmail = $scope.itemsData.email;
					$scope.modephone = $scope.itemsData.phone;
					$scope.getRadio1(0);		//选择用手机发送验证码
				}else if($scope.itemsData.email != null){
					$scope.modeEmail = $scope.itemsData.email;
					$scope.getRadio2(1);		//选择用邮箱发送验证码
				}else{
					$scope.modephone = $scope.itemsData.phone;
					$scope.getRadio1(0);		//选择用手机发送验证码
				}
			}
		})
		
		//选择用手机发送验证码
		$scope.getRadio1 = function(obj){
			var code = obj;
			$scope.getCheckValue(code);
		}
		//选择用邮箱发送验证码
		$scope.getRadio2 = function(obj){
			$scope.title = $translate.instant('login.enterEvc');
			var code = obj;
			$scope.getCheckValue(code);
		}
		
		//选择验证方式
		$scope.getCheckValue = function(obj){
			if(obj == 0){
				$scope.codeURL = '/wallet/send/phone';
			}else{
				$scope.codeURL = '/wallet/send/email';    //获取邮箱验证码 url
			}
		}
		//请求后端获取验证码 
		$scope.getWalletCode = function(){
			$http.get(mpoolUI_url + $scope.codeURL).success(function(data){
				if(data.msg == "ok"){
					var maxtime = 1 * 60; 
			      	function CountDown() {
			        	if (maxtime >= 0) {
			          		minutes = Math.floor(maxtime / 60);
			          		seconds = Math.floor(maxtime % 60);
			          		msg = "" + seconds + "s";
			          		$scope.time = msg;
			          		--maxtime;
			        	}else{
			        		$scope.time = $translate.instant('subActsMng.send')
			        	}
			      	}  
			      	$interval(function(){CountDown()},1000,61);
				}else{
					$scope.wallCode = data.msg;
				}
			})	
		}
		
		$scope.sub = true;
		$scope.checkCode = function(obj){
			if(obj){
				$scope.sub = false;
			}else{
				$scope.sub = true;
			}
		}
	}
])

//-----------------------------------------报警设置
App.controller('SettingsCtrl123', ['$http', '$scope', 'ngDialog', '$cookieStore',
	function($http, $scope, ngDialog, $cookieStore) {
		var userid = sessionStorage.getItem('userid2');
		$scope.share15m = sessionStorage.getItem('objCurrentShareT');
		$scope.workers = sessionStorage.getItem('objworkerActive');
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			const name = sessionStorage.getItem('subcurrencyName')
			angular.forEach(r.data, (d)=> {
				if(name == d.type) {
					$scope.unit = d.unit
				}
			})
		})
		var j = {
			cycle:1800,
			userShare:0.0,
			workerActive:0,
			email:null,
			phone:null,
			isEnable:false,
			userShare: 0,
			workerActive: 0
		}
		//查询手机号和邮箱
		$scope.getINFO = function(){
			$http.get(mpoolUI_url + '/alarm/info/'+userid).success(function(data){
				if(data.msg == 'ok'){
					var itemsData = data.data;
					$scope.itemsData = data.data.alarmSetting ||{};
					$scope.cordon = $scope.itemsData.userShare || 0;
					$scope.con = $scope.cordon;
					$scope.cordonNumber = $scope.itemsData.workerActive || 0;
					$scope.cor = $scope.cordonNumber;
					if($scope.itemsData == null){
						$scope.itemsData = j;
						$scope.cordon = $scope.itemsData.userShare;
						$scope.cordonNumber = $scope.itemsData.workerActive;
					}
				}
			})
		}
		
		
		//算力警告  开、关
		$scope.disCordon = true;
		$scope.warning = function(obj){
			if(obj == false){
				$scope.disCordon = true;
				$scope.cordon = $scope.con;
				$scope.getCordon($scope.cordon);
			}else{
				$scope.disCordon = false;
			}
		}
		$scope.cordonTitle = '';
		//算力警戒线
		$scope.getCordon = function(obj){
			var share15m = parseInt($scope.share15m);
			if(obj <= share15m && obj > -1){
				if(obj != ""){
					$scope.cordonTitle = '';
					$scope.sub001 = false;
				}else{
					$scope.sub001 = true;
				}
			}else{
				$scope.cordonTitle = '超过警戒线，算力警戒线应小于当前值 '+$scope.share15m;
				$scope.sub001 = true;
			}
		}
		$scope.cordonNumber=''
		//矿机数量警告  开、关
		$scope.disCordon2 = true;
		$scope.machine = function(obj){
			if(obj == false){
				$scope.disCordon2 = true;
				$scope.cordonNumber = $scope.cor;
				$scope.sub001 = false;
			}else{
				$scope.disCordon2 = false;
			}
		}
		
		//减
		$scope.reduce = function(obj){
			$scope.cordonNumber -= obj;
			var wor = parseInt($scope.workers);
			var cordons = $scope.cordonNumber;
			if(cordons > -1){
				if(cordons <= wor){
					$scope.cordonNumberTitle = '';
					$scope.sub001 = false;
				}else{
					$scope.cordonNumberTitle = '超过当前活跃矿机值，活跃矿机值应小于 '+wor;
					$scope.sub001 = true;
				}
			}else{
				$scope.cordonNumber = 0;
				$scope.sub001 = false;
			}
		}
		$scope.cordonNumberTitle = '';
		//加
		$scope.plus = function(obj){
			$scope.cordonNumber = parseInt($scope.cordonNumber || 0) + obj;
			var wor = parseInt($scope.workers);
			if($scope.cordonNumber <= wor && $scope.cordonNumber > -1){
				if($scope.cordonNumber != ""){
					$scope.cordonNumberTitle = '';
					$scope.sub001 = false;
				}else{
					$scope.sub001 = true;
				}
			}else{
				$scope.cordonNumberTitle = '超过当前活跃矿机值，活跃矿机值应小于 '+wor;
				$scope.sub001 = true;
			}
		}
		
		//手动输入活跃矿机警戒线
		$scope.getcordonNumber = function(obj){
			var worS = parseInt(obj);
			var wor = parseInt($scope.workers);
			var posPattern = /^\d+$/;
			if(posPattern.test(worS)){
				$scope.sub001 = false;
				if(worS <= wor && worS > -1){
					$scope.cordonNumberTitle = '';
					$scope.sub001 = false;
				}else{
					$scope.cordonNumberTitle = '超过当前活跃矿机值，活跃矿机值应小于 '+wor;
					$scope.sub001 = true;
				}
			}else{
				$scope.cordonNumber = 0;
				$scope.cordonNumberTitle = '';
			}
		}
		
		$scope.init = function(){
			//查询手机号和邮箱
			$scope.getINFO();
		}
		$scope.init();
	}
])
//报警设置  下一步
App.controller('SettingsNextstepCtrl123', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {
		var userid = sessionStorage.getItem('userid');
		var cordon = sessionStorage.getItem('objCordon');
		var cordonNumber = sessionStorage.getItem('objCordonNumber');
		$scope.dataValue = {
			"cordon": cordon,
			"cordonNumber": cordonNumber
		}
		var j = {
			cycle:1800,
			userShare:0.0,
			workerActive:0,
			email:null,
			phone:null,
			isEnable:false
		}
		$scope.phones = [];
		$scope.emails = [];
		//查询手机号和邮箱
		$http.get(mpoolUI_url + '/alarm/info/'+userid).success(function(data){
			if(data.msg == 'ok'){
				var itemsData = data.data;
				$scope.itemsData = data.data.alarmSetting;
				$scope.notifyUser = data.data.alarmSetting.notifyUser;
				$scope.notifyUser.forEach(function(item){
					$scope.phones.push(item.phone)
					$scope.emails.push(item.email)
				})
				if($scope.itemsData == null){
					$scope.itemsData = j;
				}
			}
		})
		
		//修改手机号
		$scope.modifyPhone = function(){
			$scope.phoneShow = true;   //手机号显示隐藏
			$scope.mailShow = false;   //手机号显示隐藏
			$scope.sub = true;    //按钮禁用
		}
	
		//修改邮箱
		$scope.modifyMail = function(){
			$scope.mailShow = true;   	//手机号显示隐藏
			$scope.phoneShow = false;   //手机号显示隐藏
			$scope.sub = true;   //按钮禁用
		}
		
		//新手机号
		$scope.getUpdatePhone = function(){
			$scope.itemsData.phone = $scope.updatePhone;
			$scope.sub = false;   //按钮禁用
		}
		
		//新邮箱
		$scope.getUpdateEmail = function(){
			$scope.itemsData.email = $scope.updateEmail;
			$scope.sub = false;   //按钮禁用
		}
		
		//关
		$scope.shut = function(){
			$scope.mailShow = false;
			$scope.phoneShow = false;
			$scope.sub = false;   //按钮禁用
		}
		
		//联系人
		$scope.changeContacts1 = function(){
			if($scope.contacts1 == undefined || $scope.contacts1 == ""){
				$scope.contacts1Check = $translate.instant('notice.peformat');
			}else{
				$scope.contacts1Check = "";
			}
		}
		
		//验证手机号
		$scope.queding1 = true;
		$scope.changePhone = function(){
			if($scope.updatePhone == undefined || $scope.updatePhone == ""){
				$scope.phoneCheck = "";
				$scope.queding1 = true
			}else{
				var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
				if(phone.test($scope.updatePhone)){
					$scope.phoneCheck = "";
					$scope.queding1 = false;
				}else{
					$scope.phoneCheck = $translate.instant('notice.phformat');
					$scope.queding1 = true
				}
			}
		}
		
		//验证邮箱
		$scope.queding2 = true;
		$scope.changeEmail = function(){
			if($scope.updateEmail == undefined || $scope.updateEmail == ""){
				$scope.emailCheck = "";
				$scope.queding2 = true;
			}else{
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if(email.test($scope.updateEmail)){
					$scope.emailCheck = "";
					$scope.queding2 = false;
				}else{
					$scope.emailCheck = $translate.instant('notice.emformat')
					$scope.queding2 = true
				}
			}
		}
		$scope.phoneCheck=''
		$scope.emailCheck=''
		//新代码		
		//新代码		
		//新增联系人手机号
		$scope.butAddPhone = function(){
			if($scope.contacts1 != '' && $scope.contacts1 != undefined){
				$scope.titleCheck = "";
				$scope.contacts1Check= "";
				var objphones = $scope.updatePhone?$scope.phones.contains($scope.updatePhone):false;
				var objemails = $scope.updateEmail?$scope.emails.contains($scope.updateEmail):false;
				if(objphones == false && objemails == false){
					var flag = false;
					var arrStr = {nickName:$scope.contacts1}
					if(objphones ==false){
						if($scope.updatePhone){
							$scope.phones.push($scope.updatePhone)
							arrStr.phone=$scope.updatePhone
							flag=true
						}
					}
					if(objemails ==false){
						if($scope.updateEmail){
							$scope.emails.push($scope.updateEmail)
							arrStr.email=$scope.updateEmail
							flag=true
						}
					}
					if(flag){
						$scope.updateEmail=''
						$scope.updatePhone=''
						$scope.contacts1=''
						$scope.notifyUser.push(arrStr);
					}else{
						$scope.titleCheck = "手机或邮箱为空";
					}
				}else{
					$scope.titleCheck = "数据重复提交";
				}
			}else{
				$scope.contacts1Check = $translate.instant('notice.peformat');
			}
		}
		
		//添加联系人按钮
		$scope.getPlus = function(obj){
			$scope.phoneShow = obj;
		}
		
		//删除
		$scope.deleteObj = function(obj,value){
			$scope.notifyUser.remove(obj);
			if(value.phone != "" && value.phone != undefined){
				$scope.phones.removeValue(value.phome);
			}
			if(value.email != "" && value.email != undefined){
				$scope.emails.removeValue(value.email);
			}
		}
		
	}
])


//-----------------------------------------修改密码
App.controller('ChangePasswordCtrl', ['$http', '$scope', 'ngDialog', '$cookieStore',
	function($http, $scope, ngDialog, $cookieStore) {
		$scope.sub = true;
		$scope.checkPassword = function(){
			if($scope.newPassword == undefined || $scope.newPassword == ""){
				$scope.passwordCheck = $translate.instant('notice.pswNull');
				$scope.sub = true;
			}else{
				var pass = pwdValidate.test;
				if(pass.test($scope.newPassword)){
					$scope.passwordCheck = "";
					$scope.sub = false;
				}else{
					$scope.passwordCheck = $translate.instant('notice.pswV');
					$scope.sub = true;
				}
			}
		}
	}
])







