
//收益记录
App.controller('incomeRecordController', ['$scope', '$http' ,'$location', '$state', '$timeout', 'Notify',
	function($scope, $http, $location, $state, $timeout, Notify) {
		
	    //列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'pay_at',
            direction: 1,
            toggle: function (column) {
            	if(column.name != 'text_id' && column.name != 'pay_btc'){
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
        	{label: '序号',			name: 'text_id',		type: 'string'},
        	{label: 'TXID',			name: 'txid',			type: 'string'},
        	{label: '支付金额',		name: 'pay_btc',		type: 'string'},
            {label: '时间',  			name: 'pay_at',	    	type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       	};
			
	    //收益记录查询列表方法
	   	$scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getWorkerList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/wallet/pay/record?current='+current+'&size='+size)
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
	          		 	},800);
	          		 	if(data.data != null){
	          		 		$scope.itemsData = data.data.records;
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
	        $scope.load_page(1,$scope.currentSelect.value);  
	    }  
	    //尾页  
	    $scope.p_last = function(){  
	        $scope.load_page($scope.p_all_page,$scope.currentSelect.value);  
	    }  
	    //加载某一页  
	    $scope.load_page = function(page,pernum){  
	        $scope.getWorkerList(page,pernum); 
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

		//付款记录查询
		$scope.parameterQuery = function(){
			$scope.currentSelect = {
		    	value: '10',
		 	}
			$scope.getWorkerList(1,$scope.p_pernum);	//付款记录查询列表方法
		}
		
		//初始化方法
		$scope.init = function(){
			$scope.parameterQuery();      //付款记录查询
		}
		$scope.init();
		
	}
])



