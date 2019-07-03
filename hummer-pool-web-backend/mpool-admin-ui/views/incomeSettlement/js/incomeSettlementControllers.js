
//支付账单
App.controller('incomeSettlementController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog', 'Notify',
	function($scope, $http, $location, $state, $timeout, $stateParams, ngDialog, Notify) {
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'bill_number',
            direction: 1,
            toggle: function (column) {
            	if(column.name != "operation"){
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
        	{label: '单号',			name: 'bill_number',	type: 'string'},
        	{label: 'TXID',			name: 'txid',			type: 'string'},
        	{label: '时间',			name: 'cteate_at',		type: 'string'},
            {label: 'BTC',			name: 'pay_all_btc',	type: 'int'},
			{label: '状态',			name: 'status',			type: 'int'},
			{label: '操作人',       name:'operat' ,         type: 'string'},
            {label: '操作',			name: 'operation',		type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		
//展示用户管理
		$scope.currentSelect = {
	    	value: '10',
	 	}
		//用户管理列表
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getPaybillList = function(current,size){
			
			var startTime = $scope.date1;
			var endTime = $scope.date5;
			var orderId = $scope.orderId;
			var status = $scope.status;
			var txid = $scope.txid;
			var url = '/bill/info?current='+current+'&size='+size;
			if(startTime){
				var start = new Date(moment(startTime).valueOf()).toUTCString();
				url = url+'&startTime='+start
			}
			if(endTime){
				var end = new Date(moment(endTime).valueOf()).toUTCString();
				url = url+'&endTime='+end
			}
			if(orderId){
				url = url + '&orderId=' + orderId
			}
			if(txid){
				url = url + '&txid=' + txid
			}
			if(status){
				url = url + '&status=' + status
			}
			
			$scope.show_items = false;
			$http.get(mpoolUI_url + url).success(function(data){
				if(data.msg == "ok"){
					$timeout(function(){
					   $scope.show_items = true;
          		 	},800);
          		 	$scope.sum = data.data.sum;
					$scope.itemsData = data.data.page.records;
					for(var i in $scope.itemsData){
						$scope.itemsData[i].pay_all_btc = $scope.itemsData[i].pay_all_btc/Math.pow(10,8);
					}
					$scope.items_num = $scope.itemsData.length;
					
					$scope.count = data.data.page.total;     //总页数
	                $scope.totalPage = data.data.page.pages;   //页数
	                $scope.p_current = current;  
	                $scope.p_all_page = Math.ceil($scope.count/size);  
	                reloadPno();  //初始化页码  
				}
			})	
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
	        $scope.getPaybillList(page,pernum);  
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
		//填写TXID方法
		$scope.getPay = function(obj){
			ngDialog.openConfirm({
				template: 'modalGroups',
				controller: 'GroupTXID',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				if(value != undefined && value != ''){
					$http.get(mpoolUI_url + '/bill/pay/'+ obj +'?txid='+ value)
						.success(function(data){
							if(data.msg == "ok"){
								$scope.getPaybillList($scope.p_current,$scope.p_pernum);   //子账户列表
							}else{
								var title1 = data.msg;
								Notify.alert(title1, {status: 'info'})
							}
						})
				}else{
					
				}
			})
		}
		
		//用户账号详情
		$scope.getPayBillDetails = function(obj){
			$state.go('admin.incomeSettlementDetails',{id: obj});
		}
		
		//初始化
		$scope.init = function(){
			$scope.getPaybillList($scope.p_current,$scope.p_pernum);   //子账户列表
		}
		$scope.init();
	}
])
//模态框 填写TXID
App.controller('GroupTXID', ['$http', '$scope', 'ngDialog',
	function($http, $scope, ngDialog) {
		
		$scope.group2 = false;
		$scope.check = function(obj){
			if(obj == ""){
				$scope.group2 = false;
			}else{
				$scope.group2 = true;
			}
		}
	}
])

//账单详情
.controller('incomeSettlementDetailsController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams','ngDialog',
	function($scope, $http, $location, $state, $timeout, $stateParams,ngDialog) {
		
		var id = $stateParams.id;  //获取主账户   用户ID
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'username',
            direction: 1,
            toggle: function (column) {
            	if(column.name != 'checkbox' && column.name != "operation"){
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
        	{label: '户名称',				name: 'username',			type: 'string'},
            {label: '钱包地址',			name: 'wallet_address',		type: 'string'},
            {label: '余额',		name: 'pay_btc',			type: 'string'},
            {label: '创建时间',			name: 'cteate_at',			type: 'string'},
            {label: '操作',			name: 'operation',		type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		

		$scope.postDel = function (id, bill_number) {
			ngDialog.openConfirm({
				template: 'modalDel',
				controller: 'incomeSettlementDetailsController',
				className: 'ngdialog-theme-default'
			}).then(function () {
				const p = [id]
				$http.post(mpoolUI_url + '/bill/rollbackBillItem?bill_number=' + bill_number ,p).success(function (data) {
					if (data.msg == "ok") {
						$scope.getPyBillList($scope.p_current, $scope.p_pernum); 
					}
				})
			})
		}

//展示用户管理
		
	    $scope.show_items = true;
		$scope.getPyBillList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/bill/bill/item/info/'+ id)
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data;
						for(var i in $scope.itemsData){
							$scope.itemsData[i].pay_btc = $scope.itemsData[i].pay_btc/Math.pow(10,8);
						}
					}
				})	
		}
		
		//初始化
		$scope.init = function(){
			$scope.getPyBillList($scope.p_current,$scope.p_pernum);   //子账户列表
		}
		$scope.init();
		
		//返回
		$scope.back = function(){
			$location.path('admin/pool_IncomeSettlement'); 
		}
		
	}
])
