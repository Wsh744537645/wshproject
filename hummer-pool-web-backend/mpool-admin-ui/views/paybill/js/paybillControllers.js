//支付账单
App.controller('paybillController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog', 'Notify',
	function($scope, $http, $location, $state, $timeout, $stateParams, ngDialog, Notify) {
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'cteateAt',
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
        	{label: '账单创建时间',		name: 'cteateAt',		type: 'string'},
        	{label: '打币账户',			name: 'username',		type: 'string'},
        	{label: 'BTC金额',			name: 'payBtc',			type: 'string'},
        	{label: '打币地址',		name: 'walletAddress',	type: 'int'},
        	{label: '主账户名',		name: 'mUsername',		type: 'int'},
        	{label: '操作人',       name:'operat' ,type:'string'},
			{label: '支付时间',		name: 'payAt',			type: 'string'},
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		
		$scope.currentSelect = {
	    	value: '10',
	 	}
		//支付账单列表
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getPaybillList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/bill/pay/bill/list?current='+current+'&size='+size+'&descs=day')
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data.records;
						for(var i in $scope.itemsData){
							$scope.itemsData[i].pay_all_btc = $scope.itemsData[i].pay_all_btc/Math.pow(10,8);
						}
						$scope.items_num = $scope.itemsData.length;
						
						$scope.count = data.data.total;     //总页数
		                $scope.totalPage = data.data.pages;   //页数
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
		
		//初始化
		$scope.init = function(){
			$scope.getPaybillList($scope.p_current,$scope.p_pernum);   //子账户列表
		}
		$scope.init();
	}
])
