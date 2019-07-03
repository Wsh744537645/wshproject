
//账户管理（子）
App.controller('sonSubaccountsController', ['$scope', '$http', '$translate', '$state', '$timeout', 'ngDialog', '$cookieStore', 'Notify',      
	function($scope, $http, $translate, $state, $timeout, ngDialog, $cookieStore, Notify) {
		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		
		//查询账户信息
		$scope.getFindUserInfo = function(){
			$http.get(mpoolUI_url + '/userManager/findUserInfo').success(function(data){
				if(data.msg == "ok"){
					$scope.items_Data = data.data;
				}
			})
		}
		
		//钱包地址及打款
		$scope.getSum = function(objMiniPay, objWalletAddr,type){
			sessionStorage.setItem('objtype', type);
			sessionStorage.setItem("objMiniPay", objMiniPay);  //存储最低打款额
			sessionStorage.setItem("objWalletAddr", objWalletAddr);  //存储钱包地址
			ngDialog.openConfirm({
				template: 'modalsum',
				controller: 'sumCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var minPay = value.statusValue;
				$http.post(mpoolUI_url + '/wallet/user/wallet/edit/minPay?minPay='+minPay+'&walletType=' + type)
					.success(function(data){
						if(data.msg == "ok"){ 
							Notify.alert($translate.instant('notice.qfsc') + '！！！', {status: 'success'});
							$scope.getFindUserInfo();   //查询账户信息
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
				template: 'modalSharesub',
				controller: 'ShareCtrlsub',
				className: 'ngdialog-theme-default'
			}).then(function(value){})
		}
		
		//报警设置
		$scope.getSettings = function(type){
			ngDialog.openConfirm({
				template: 'modalSettings',
				controller: 'SettingsCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				$scope.getSettingsNextstep(value.cordon, value.cordonNumber,type);
			})
		}
		//报警设置  下一步
		$scope.getSettingsNextstep = function(objCordon,objCordonNumber,type){
			sessionStorage.setItem("objCordon", objCordon); 
			sessionStorage.setItem("objCordonNumber", objCordonNumber);  
			sessionStorage.setItem("subcurrencyName", type);  
			ngDialog.openConfirm({
				template: 'modalSettingsNextstep',
				controller: 'SettingsNextstepCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var itemsData = value.itemsData;
				itemsData.userShare = value.dataValue.cordon;
				itemsData.workerActive = value.dataValue.cordonNumber;
				
				$http.post(mpoolUI_url + '/alarm/add', itemsData).success(function(data){
					if(data.msg == 'ok'){
						Notify.alert($translate.instant('notice.setsc') + '！！！', {status: 'success'})
					}else{
						Notify.alert(data.msg, {status: 'info'})
					}
				})
			})
		}
		
		//初始化
		$scope.init = function(){
			$scope.getFindUserInfo();    //查询账户信息
		}
		$scope.init();
		
	}
])

//----------------------------------分享
App.controller('ShareCtrlsub', ['$http', '$scope', 'ngDialog', '$translate', 'Notify',
	function($http, $scope, ngDialog, $translate, Notify) {

		$scope.modalshare = true;
		var userId = sessionStorage.getItem('objuserId');
		var currencyName = sessionStorage.getItem('currencyName');
		
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
				$http.post(mpoolUI_url + '/userManager/share?&userId='+userId+'&currencyName=' +currencyName)
					.success(function(data){
						if(data.msg == "ok"){
							$scope.num = data.data;
							// $scope.modalshare = false;
							Notify.alert($translate.instant('sharepage.shsc') + '！！！', {status: 'success'});
						}else{
							$scope.modalshare = true;
							Notify.alert(data.msg,{status: 'info'})
						}
					})
		// 	}else{
		// 		$scope.numberCheck = '输入有效期';
		// 	}
		// }
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


//-----------------------------------------钱包地址及打款
App.controller('sumCtrl', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {
		
		$scope.objtype = sessionStorage.getItem('objtype');
		$scope.objMiniPay = sessionStorage.getItem('objMiniPay');
		var objWalletAddr = sessionStorage.getItem('objWalletAddr');
		if(objWalletAddr == "undefined" || objWalletAddr == "null"){
			$scope.objWalletAddr =  $translate.instant('notice.noset');
		}else{
			$scope.objWalletAddr = objWalletAddr;
		}
		$scope.statusData = []
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
  	// 		{name: '0.1 ',  	id: '0'},
    // 		{name: '0.01 ',  	id: '1'},
    // 		{name: '0.005',  	id: '2'}
		// ];
		// $scope.statusValue = "0.1";
		//设定起付金额
		$scope.getState = function(index2, name){
			$scope.selectedState = parseInt(index2);
			$scope.statusValue = name;
		}
		$scope.getState(0); 
	}
])

//-----------------------------------------报警设置
App.controller('SettingsCtrl', ['$http', '$scope', 'ngDialog', '$cookieStore',
	function($http, $scope, ngDialog, $cookieStore) {
		
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			const name = sessionStorage.getItem('subcurrencyName')
			angular.forEach(r.data, (d)=> {
				if(name == d.type) {
					$scope.unit = d.unit
				}
			})
		})
		//用户列表
		$scope.getSubRuntimeInfo = function(){
			$http.get(mpoolUI_url + '/user/dashbaord/getSubRuntimeInfo')
				.success(function(data){
					if(data.msg == "ok"){
						$scope.items_Data = data.data;
						$scope.share15m = $scope.items_Data.share15m;   //算力值
						$scope.workers = $scope.items_Data.workerActive;
					}
				})
		}
		
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
			$http.get(mpoolUI_url + '/alarm/info').success(function(data){
				if(data.msg == 'ok'){
					$scope.itemsData = data.data.alarmSetting || {};
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
		
		
		$scope.init = function(){
			$scope.getSubRuntimeInfo();
			$scope.getINFO();
		}
		$scope.init();
		
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
			if(obj != ''){
				var share15m = parseInt($scope.share15m);
				if(obj <= share15m && obj > -1){
						$scope.cordonTitle = '';
						$scope.sub001 = false;
					
				}else{
					$scope.cordonTitle = '超过警戒线，算力警戒线应小于当前值 '+$scope.share15m;
					$scope.sub001 = true;
				}
			}else{
				$scope.cordonTitle = '';
				$scope.cordon=0
			}
			
		}
		
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
		$scope.cordonNumberTitle = '';
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
		
	}
])
//报警设置  下一步
App.controller('SettingsNextstepCtrl', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {
		
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
		$http.get(mpoolUI_url + '/alarm/info').success(function(data){
			if(data.msg == 'ok'){
				var itemsData = data.data;
				$scope.itemsData = data.data.alarmSetting;
				$scope.notifyUser = data.data.alarmSetting.notifyUser;
				$scope.notifyUser.forEach(function(item){
					$scope.phones.push(item.phone);
					$scope.emails.push(item.email);
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
				$scope.phoneCheck = $translate.instant('notice.phformat');
			}else{
				var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
				if(phone.test($scope.updatePhone)){
					$scope.phoneCheck = "";
					$scope.queding1 = false;
				}else{
					$scope.phoneCheck = $translate.instant('notice.phformat');
				}
			}
		}
		
		//验证邮箱
		$scope.queding2 = true;
		$scope.changeEmail = function(){
			if($scope.updateEmail == undefined || $scope.updateEmail == ""){
				$scope.emailCheck = $translate.instant('notice.emformat')
			}else{
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if(email.test($scope.updateEmail)){
					$scope.emailCheck = "";
					$scope.queding2 = false;
				}else{
					$scope.emailCheck = $translate.instant('notice.emformat')
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