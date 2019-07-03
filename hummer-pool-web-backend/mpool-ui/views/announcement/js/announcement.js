
//统计
App.controller('announcementController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window',
	function($scope, $http, $location, $cookieStore, $state, $timeout, $window) {
		
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
		
		//跳转用户面板界面
		$scope.userpanel = function () {
	        var url = $state.href("api.dashboard", {}, {reload: true});
	        window.open("api/dashboard", '_self')
	    }

		


		
		//查看公告
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getIncomeSettlementList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/msg/list?current='+current+'&size=10')
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data.records;
						for(var i in $scope.itemsData){
							if(!!$scope.itemsData[i].releaseTime) {
								$scope.itemsData[i].time = $scope.itemsData[i].releaseTime.substr(0,10);
							}
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
	        $scope.getIncomeSettlementList(page,pernum);  
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
		
		$scope.init = function(){
			$scope.getIncomeSettlementList($scope.p_current,$scope.p_pernum)
		}
		$scope.init();	
		
		$scope.getFindByDetails = function(id){
			$state.go('announcementdetail', {id: id});
		}
	}
	
])