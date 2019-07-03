
//统计
App.controller('statisticsController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window','$translate',
	function($scope, $http, $location, $cookieStore, $state, $timeout, $window,$translate) {
		
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			$scope.currencyData = r.data
			angular.forEach($scope.currencyData,(d)=> {
				d.coinIcon = "../../img/banner/" + d.type + "1.png"
			})
		})
		//主账户进入后默认选择BTC
		$scope.currencyName= 'BTC'
		$scope.currencyCoin= "../../img/banner/BTC1.png"
		$scope.changeCoin =  function(name) {
			$scope.currencyName = name
			angular.forEach($scope.currencyData, (d)=> {
				if(name == d.type) {
					$scope.currencyCoin = "../../img/banner/" + d.type + "1.png"
				}
			})
			$scope.refresh();
		}
		$scope.refresh = function() {
			$scope.getStatistics(1, $scope.currentSelect.value)
		}
		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		$scope.page = 1;
		//判断用户是否登录 如登录首页展示用户面板按钮，否则隐藏
		$scope.username = $cookieStore.get("nickName");  // 保存登录后返回的账户
		if($scope.username != '' && $scope.username != undefined){
			$scope.control = true;
		}else{
			$scope.control = false;
		}
		//注销
		$scope.logout = function(){
			$http.get(mpoolUI_url + '/user/logout').success(function(data, status, headers, config) {
				if(data.msg == "ok"){
					$cookieStore.remove('nickName');
					$cookieStore.remove('id');
					$scope.display = false;
					$location.path('/login');
					$window.location.reload();
				}
			})
		}
		
		$scope.folders = [
    		{name: 'Block历史记录'}
		];
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'height',
            direction: 1,
            toggle: function (column) {
        		if (column.sortable === false)
                    return;
                if (this.column === column.name) {
                    this.direction = -this.direction || -1;
                } else {
                    this.column = column.name;
                    this.direction = -1;
                }
            }
        };
        vm.columns = [
            {label: $translate.instant('block.Block height'),		name: 'height',			type: 'string',class:'hidden-xs'},
            {label:  $translate.instant('block.Block time'),		name: 'created_at',		type: 'string'},
            {label: 'hash',		                                    name: 'hash',			type: 'string',class:'hidden-xs'},
            {label:  $translate.instant('block.Reward'),	name: 'rewards',		type: 'string'},
            {label:  $translate.instant('block.Size'),		        name: 'size',			type: 'string',class:'hidden-xs'},
            {label:  $translate.instant('block.Confirmation times'),name: 'confirmed_num',  type: 'string',class:'hidden-xs'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		

		$scope.currentSelect = {
	    	value: '10',
	 	}
		//查询统计列表 
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getStatistics = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/pool/found/blocks?current='+current+'&size='+size+ '&currencyName=' +$scope.currencyName)
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
	        $scope.load_page(1, $scope.currentSelect.value);  
	    }  
	    //尾页  
	    $scope.p_last = function(){  
	        $scope.load_page($scope.p_all_page, $scope.currentSelect.value);  
	    }  
	    //加载某一页  
	    $scope.load_page = function(page,pernum){  
	        $scope.getStatistics(page,pernum);  
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
		
		
		//初始化方法
		$scope.init = function(){
			$scope.getStatistics($scope.p_current,$scope.p_pernum);     //查询统计列表
		}
		$scope.init();
		
		//跳转用户面板界面
		$scope.userpanel = function () {
	        var url = $state.href("api.dashboard", {}, {reload: true});
	        window.open("api/dashboard", '_self')
	    }

	}
])