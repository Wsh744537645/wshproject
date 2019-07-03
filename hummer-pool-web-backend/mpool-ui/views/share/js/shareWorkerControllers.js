//分享
App.controller('shareWorkerController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', '$stateParams',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, $stateParams) {
		$scope.href = function(url){
			if(url == '') {
				$location.path('share/' + $stateParams.comId)
			}else{
				$location.path('share/' + $stateParams.comId + '/'+url)
			}
		} 
		$scope.cointype = $stateParams.comId.split('-')[1]
		$scope.stepCheckedValues=[]
	    //列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'workerName',
            direction: 1,
            toggle: function (column) {
            	if(column.name != 'checkbox' && column.name != 'operation'){
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
        	{label: $translate.instant('worker.Miner Name'),name: 'workerName',	type: 'string'},
			{label: $translate.instant('worker.15mHs'),	name: 'hashAccept15mT',type: 'string'},
			{label: $translate.instant('worker.15mRj'), name: 'accept15mRe',type: 'string'},
            {label: $translate.instant('worker.1hHs'),	name: 'hashAccept1hT', type: 'string',class:'hidden-xs'},
			{label: $translate.instant('worker.1hRj'),	name: 'accept1hRe',	type: 'string',class:'hidden-xs'},
			{label: $translate.instant('worker.Last update'),	name: 'lastShareTime',	type: 'string', class:'hidden-xs'}
		];
				
		
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       	};
	    //我的矿机列表 
	   	$scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.page = 1;
		$scope.show_items = true;
		
		$('#search-data-input').bind('keydown', function (event) {
            var event = window.event || arguments.callee.caller.arguments[0];
            if (event.keyCode == 13){
                $scope.getWorkerList(1,10)
            }
        });


		$scope.getWorkerList = function(current,size){
			var json = "";
			$scope.stepCheckedValues =[]
			if($scope.workerName){
				json = '/share/workerList?current='+current+'&size='+size+'&status='+$scope.statusValue+'&workerName='+$scope.workerName+'&id='+ $stateParams.comId;                        
			}else{
				json = '/share/workerList?current='+current+'&size='+size+'&status='+$scope.statusValue+'&id='+ $stateParams.comId;
			}
			if($scope.group_Id){
				json = json+'&groupId='+$scope.group_Id
			}
			$scope.show_items = false;
			$http.get(mpoolUI_url + json).success(function(data){
				if(data.msg == "ok"){
					$timeout(function(){
					   $scope.show_items = true;
          		 	},800);
          		 	if(data.data != null){
						$scope.itemsData = data.data.records;
						$scope.items_num = $scope.itemsData.length;
						if($scope.statusValue == "offline"){
							for(var i=0;i<$scope.itemsData.length;i++){
								var offline = 'offline';
								$scope.itemsData[i].offline = offline;
							}
						}
						for(var i in $scope.itemsData){
							$scope.itemsData[i].hashAccept1hT=parseFloat($scope.itemsData[i].hashAccept1hT);
							$scope.itemsData[i].hashAccept1mT=parseFloat($scope.itemsData[i].hashAccept1mT);
							$scope.itemsData[i].hashAccept5mT=parseFloat($scope.itemsData[i].hashAccept5mT);
							$scope.itemsData[i].hashAccept15mT=parseFloat($scope.itemsData[i].hashAccept15mT);
							$scope.itemsData[i].hashReject1hT=parseFloat($scope.itemsData[i].hashReject1hT);
							$scope.itemsData[i].hashReject15mT=parseFloat($scope.itemsData[i].hashReject15mT);
							var accept15mRe = ($scope.itemsData[i].hashReject15mT/ ($scope.itemsData[i].hashReject15mT+$scope.itemsData[i].hashAccept15mT))*100;
							if(!accept15mRe){
								accept15mRe=0.0;
							}
							var accept1hRe= ($scope.itemsData[i].hashReject1hT/($scope.itemsData[i].hashReject1hT+$scope.itemsData[i].hashAccept1hT))*100;
							if(!accept1hRe){
								accept1hRe=0.0;
							}
							$scope.itemsData[i].accept15mRe=accept15mRe
							$scope.itemsData[i].accept1hRe=accept1hRe
						}
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
			// document.getElementById('test').style.height = document.getElementById('row').clientHeight	
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
		
		$scope.folders = [
			{name: $translate.instant('worker.All'),  id: '-1', title: '查询全部矿机'},
			{name: $translate.instant('worker.No Group'),  id: '0', title: '查询未分组矿机'}
		];

		//分组查询
		$scope.parameterQuery = function(obj, index, name){
			// if(obj == '0' || obj =='-1'){
			// 	$scope.selected = -2;
				if(obj == "-1"){
					$scope.selectedState = 0;
					$scope.statusValue = "all";
				}
			// }else{
				$scope.selected = index;
			// }
			$scope.minerName = name;
			$scope.currentSelect = {
		    	value: '10',
		 	}
			$scope.group_Id = obj;   //分组查询矿机列表参数
			$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
		}
		
		$scope.statusData = [
  			{name: $translate.instant('worker.All'),  id: 0, value:"all"},
			{name: $translate.instant('dashboard.active'),  id: 1, value:"active"},
			{name: $translate.instant('dashboard.Inactive'),  id: 2, value:"inactive"},
    		{name: $translate.instant('dashboard.Offline'),  id: 3, value:"offline"}
    		
  		];
		//状态查询
		$scope.getState = function(index2, status2,to){
			$scope.selectedState = index2;
			$scope.statusValue = status2;    //状态参数

			if(!to){
				$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
			}

		}

		$http.get(mpoolUI_url + '/share/rest/' + $stateParams.comId).success(
			function (data) {
				if (data.msg == "ok") {
					if (data.data) {
						$scope.userStatus = data.data.userStatus;
					}
				}
			}
		)
		//矿工组列表
		$scope.getWorkerGroupList = function(){
			$http.get(mpoolUI_url + '/share/workerGroupList?id='+$stateParams.comId).success(function(data){
				if(data.msg == "ok"){
					$scope.itemsData2 = data.data;
				}
			})
		}
		
		//初始化方法
		$scope.init = function(){
			$scope.selectedState = 1;
			$scope.statusValue = "active";
			$scope.getWorkerGroupList();    			//矿工组列表
			$scope.parameterQuery(0, -2,'未分组');      	//矿工分组查询
		}
		$scope.init();
				
		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }
		
		
		//导出
		$scope.downloadFile = function (url){
			$http.get(url).success(function(resp){

				var a = document.createElement('a'); 
      			var url = window.URL.createObjectURL(blob);   // 获取 blob 本地文件连接 (blob 为纯二进制对象，不能够直接保存到磁盘上)
      			var filename = res.headers.get('Content-Disposition'); 
      			a.href = url; 
     			a.download = filename; 
      			a.click(); 
      			window.URL.revokeObjectURL(url); 
      			document.querySelector('#status').innerHTML =   $translate.instant('notice.dlog'); 
			})
		    
		}    
		
	}
])
