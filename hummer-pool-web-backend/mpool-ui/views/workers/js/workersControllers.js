
//我的矿机
App.controller('workersController', ['$scope', '$http' ,'$translate', '$state', '$timeout', 'ngDialog', 'Notify',
	function($scope, $http, $translate, $state, $timeout, ngDialog, Notify) {
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
        	// {label: '',			    name: 'checkbox',				type: 'string'},
        	{label: $translate.instant('worker.Miner Name'),			name: 'workerName',				type: 'string'},
        	// {label: '5分钟算力',		name: 'hashAccept5mT',		type: 'int'},
			{label: $translate.instant('worker.15mHs'),	name: 'hashAccept15mT',		type: 'string'},
			{label: $translate.instant('worker.15mRj'), name: 'accept15mRe',			type: 'string'},
            {label: $translate.instant('worker.1hHs'),	name: 'hashAccept1hT',	    type: 'string',class:'hidden-xs'},
			{label: $translate.instant('worker.1hRj'),	name: 'accept1hRe',				type: 'string',class:'hidden-xs'},
			{label: $translate.instant('worker.Last update'),	name: 'lastShareTime',			type: 'string', class:'hidden-xs'}
		];
				
		//console.log(vm.columns);
		
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       	};
			
		$scope.selectAll =function(allChecked){	
			$scope.itemsData.forEach(function(element ) {
				console.log(allChecked);
				
				element.$checked = allChecked
				if(allChecked){
					$scope.stepCheckedValues.push(element.workerId);
				}else{
					$scope.stepCheckedValues =[]
				}
				
			});

		}
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
			$scope.allchecked=false
			$scope.stepCheckedValues =[]
			if($scope.workerName){
				json = '/worker/workerList?current='+current+'&size='+size+'&status='+$scope.statusValue+'&workerName='+$scope.workerName;                        
			}else{
				json = '/worker/workerList?current='+current+'&size='+size+'&status='+$scope.statusValue;
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
			$scope.group2 = false;      //点击确认组操作按钮后禁用 该按钮
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

		//矿工组列表
		$scope.getWorkerGroupList = function(){
			$http.get(mpoolUI_url + '/worker/workerGroupList').success(function(data){
				if(data.msg == "ok"){
					$scope.itemsData2 = data.data;
				}
			})
		}
		
		//移除矿工多个
		$scope.getWorkerDeleteBatch = function(){
			if($scope.selectedState == 3 && !!$scope.stepCheckedValues.length) {
				var data = []
				$scope.itemsData.forEach(function(element) {
					if(element.$checked){
						data.push(element.workerId)
					}
				});

				ngDialog.openConfirm({
					template: 'deleteWorker',
					//controller: 'GroupCtrls',
					className: 'ngdialog-theme-default'
				}).then(function(value){
					$http.post(mpoolUI_url + '/worker/deleteWorkerBatch',data).success(function(resp){
						if(resp.msg == "ok"){
							$scope.itemsData2 = data.data;
							$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
						}
					})
				})
			}
		}

		//列表多选
		$scope.group2 = false;
        $scope.checkedPlat = function (checkModel, datas) {
			// $("tbody>tr>td:has(:checked)").parent().find("td").addClass("selectedblue")
			$scope.group2 = true;
            var list = datas;
            $scope.stepCheckedValues = [];
            checkModel.$checked = !checkModel.$checked;
            for (var i = 0; i < list.length; i++) {
                if (list[i].$checked ===  true) {
                    $scope.stepCheckedValues.push(list[i].workerId);
                }
			}
			if($scope.stepCheckedValues.length ==0 || $scope.stepCheckedValues.length != $scope.itemsData.length){
				$scope.allchecked = false
			}
			if($scope.stepCheckedValues.length ==$scope.itemsData.length){
				$scope.allchecked = true
			}
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
		
		//模态框    移动矿机
		$scope.postGroup = function(){
			$scope.sizeValue = $scope.stepCheckedValues.length;
			if(!!$scope.sizeValue) {
				sessionStorage.setItem("sizeValue", $scope.sizeValue);
				ngDialog.openConfirm({
					template: 'modalGroup',
					controller: 'GroupCtrls',
					className: 'ngdialog-theme-default'
				}).then(function(value){
					  $http.post(mpoolUI_url + '/worker/change/group?groupId='+value, $scope.stepCheckedValues).success(function(data){
						if(data.msg == "ok"){
							$scope.group2 = false;      //点击确认组操作按钮后禁用 该按钮
							$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
						}
					})
				})
			}
		}
		
		//模态框    删除   矿工组
		$scope.deleteWorkerGroup = function(obj, obj2){
			sessionStorage.setItem("id", obj);
			var id = "";
			if(obj == 1){    //分组删除
				id = obj2.items.groupId;
				sessionStorage.setItem("obj2", obj2.items.groupName);
			}else{          //矿工列表数据删除
				id = obj2.items.workerId;
				sessionStorage.setItem("obj3", obj2.items.workerName);
			}
			ngDialog.openConfirm({
				template: 'deleteGroup',
				controller: 'deleteGroupCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				if(obj == 1){			//分组删除
					$http.delete(mpoolUI_url + '/worker/deleteWorkerGroup?groupId='+ id)
						.success(function(data){
							if(data.msg == "ok"){
								Notify.alert($translate.instant('notice.dlsc') + '！！！', {status: 'success'});
								$scope.getWorkerGroupList();              //矿工组列表
								$scope.parameterQuery(0, -2,'未分组');      //矿工分组查询
							}else{
								var title1 = data.msg;
								Notify.alert( 
									title1, 
									{status: 'info'}
								)
							}
						})
				}else{			//矿工列表数据删除
					$http.delete(mpoolUI_url + '/worker/deleteWorker?workerId='+ id)
						.success(function(data){
							if(data.msg == "ok"){
								Notify.alert($translate.instant('notice.dlwksc') + '！！！', {status: 'success'});
								$scope.parameterQuery(0, -2,'未分组');      //矿工分组查询
							}else{
								var title1 = data.msg;
								Notify.alert( 
									title1, 
									{status: 'info'}
								)
							}
						})
				}
			})
		}
		
		//模态框    矿工组    创建  和  修改   共享方法
		$scope.postCreateWorkerGroup = function(obj, obj2){
			sessionStorage.setItem("obj", obj);  //存储编号
			if(obj == 1){     //obj=1  代表       创建矿工组
				var scrap = obj2;
			}else{			  //obj=2  代表        修改矿工组
				var name = obj2.items.groupName;     //修改矿机组  获取矿机组名
				var id = obj2.items.groupSeq;        //获取矿机组  序号
				$scope.groupId = obj2.items.groupId; //矿工id
				sessionStorage.setItem("name", name);
				sessionStorage.setItem("id", id);
			}
			ngDialog.openConfirm({
				template: 'modalDialogId',
				controller: 'InsideCtrl',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				if(obj == 1){     //obj=1  代表创建矿工组
					var json = {
					  	"groupName": value.groupName,
					  	"groupSeq": value.groupSeq,
					  	"groupCode": ""
		  			}
		  			$http.post(mpoolUI_url + '/worker/createWorkerGroup', json).success(function(data){
	  					if(data.msg == "ok"){
	  						$scope.getWorkerGroupList();  //矿工组列表
	  					}
	  				})
				}else{            //obj=2  代表修改矿工组
					var json2 = {
  						"groupCode": '',
  						"groupName": value.groupName,
  						"groupSeq": value.groupSeq
		  			}
		  			$http.post(mpoolUI_url + '/worker/updateWorkerGroup?groupId='+$scope.groupId, json2)
		  				.success(function(data){
		  					if(data.msg == "ok"){
		  						$scope.getWorkerGroupList();  //矿工组列表
		  					}
		  				})
				}
			})
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

//模态框  创建矿工组     和修改 矿工组   共享方法
App.controller('InsideCtrl', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {

		//验证   组名    序号
		$scope.name = false;
		$scope.seq = false;
		
		//组名
		$scope.changeGroupName = function(groupName){
			if(groupName == ""){
				$scope.nameCheck = $translate.instant('notice.grp');
				$scope.name = false;
				return false;
			}else{
				var check = /^.{4,15}$/;
				if(check.test(groupName)){
					//判断组名可不可用
					$http.get(mpoolUI_url + '/worker/check/groupName?groupName='+ groupName)
		  				.success(function(data){
		  					if(data.msg == "ok"){
		  						$scope.nameCheck = "";
		  						$scope.name = true;
		  					}else{
		  						$scope.nameCheck = data.msg;
		  						$scope.name = false;
		  						return false;
		  					}
		  				})
				}else{
					$scope.nameCheck = $translate.instant('notice.grcheck');
					$scope.name = false;
					return false;
				}
			}
		}
		//序号
		$scope.changeGroupSeq = function(groupSeq){
			if(groupSeq == ""){
				$scope.groupSeqCheck = $translate.instant('notice.rag');
				$scope.seq = false;
				return false;
			}else{
				var check = /^\d+$/;
				if(check.test(groupSeq)){
					$scope.groupSeqCheck = "";
					$scope.seq = true;
				}else{
					$scope.groupSeqCheck = $translate.instant('notice.rag');
					$scope.seq = false;
					return false;
				}
			}
		}
		var number = sessionStorage.getItem('obj');
		if(number == 1){//obj=1  代表创建矿工组
			$scope.groupName = "";   //初始化分组 组名
			$scope.groupSeq = "";	 //初始化 序号
		}else{//obj=2  代表修改矿工组
			//展示修改模态框上参数
			$scope.groupName = sessionStorage.getItem('name');
			$scope.groupSeq = sessionStorage.getItem('id');
			
			//传入判断输入框空指针验证参数
			var group_Name = sessionStorage.getItem('name');
			var group_Seq = sessionStorage.getItem('id');
			var num = sessionStorage.getItem('obj');   
			//初始化修改验证输入框
			if(num == 1){
				$scope.changeGroupName(group_Name);
			}else{
				$scope.name = true;
			}
			$scope.changeGroupSeq(group_Seq);
		}
		
	}
])

//模态框  删除分组
App.controller('deleteGroupCtrl', ['$scope', 'ngDialog', '$cookieStore',
	function($scope, ngDialog, $cookieStore) {
		var id = sessionStorage.getItem('id');     //
		if(id == 1){
			$scope.groupName = sessionStorage.getItem('obj2');   //获取组删除的名称
			$scope.title = $translate.instant('subActsMng.Remove Group');
			$scope.Tips = true;
		}else{
			$scope.groupName = sessionStorage.getItem('obj3');   //获取矿工列表删除的名称
			$scope.title = $translate.instant('notice.delw');
			$scope.Tips = false;
		}
		
	}
])

//模态框  移动矿机
App.controller('GroupCtrls', ['$http', '$scope', 'ngDialog', '$translate',
	function($http, $scope, ngDialog, $translate) {
		$scope.sizeValue = sessionStorage.getItem('sizeValue');     //
		//矿工组列表
		$http.get(mpoolUI_url + '/worker/workerGroupList').success(function(data){
			if(data.msg == "ok"){
				$scope.itemsModal = data.data;
				$scope.itemsModal.push({groupId: 0, groupName: $translate.instant('worker.No Group')})
				var name = $scope.itemsModal[0].groupId;
				$scope.groupName = {
					value : name
				}
			}
		})
		
	}
])

