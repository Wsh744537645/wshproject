
//通知公告
App.controller('noticeController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog', 'Notify',
	function($scope, $http, $location, $state, $timeout, $stateParams, ngDialog, Notify) {
		
		$scope.itemsData2 = [
			{templateName: '查看',id: '0'},
			{templateName: '新增',id: '1'}
		];
		$scope.parameterQuery = function (objId) {
			$scope.selected = objId;
			$scope.currentSelect = {
		    	value: '10',
		 	}
			if(objId == 0){
				$scope.getIncomeSettlementList($scope.p_current,$scope.p_pernum);   //查看公告
			}else{
				//清空
				$scope.title = '';
				editor.txt.html('');
				$scope.noticeTitle = '新增公告';
				$scope.but = "保存";
			}
		}
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'title',
            direction: 1,
            toggle: function (column) {
            	if(column.name == "operation"){
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
        	{label: '标题',			name: 'title',			type: 'string'},
        	{label: '作者',			name: 'releaseUser',	type: 'string'},
        	{label: '发布时间',		name: 'releaseTime',	type: 'string'},
            {label: '操作',			name: 'operation',		type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
        };
		
		//查看公告
	    $scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
	    $scope.show_items = true;
		$scope.getIncomeSettlementList = function(current,size){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/msg/list?current='+current+'&size='+size)
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data.records;
						for(var i in $scope.itemsData){
							$scope.itemsData[i].pay_btc = $scope.itemsData[i].pay_btc/Math.pow(10,8);
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
		
		//查看
		$scope.getFindByDetails = function(id){
			$state.go('admin.noticeDetails', {id: id});
		}
		
		//获取某条数据进行修改公告
		$scope.getFindByid = function(id){
			$http.get(mpoolUI_url + '/msg/findByid?id='+id)
       			.success(function (data) {
					if (data.msg == "ok") {
						
						$scope.$index = 2;
						$scope.selected = 2;
						$scope.noticeTitle = '修改公告';
						$scope.but = "修改";
						
						$scope.itemsValue = data.data;
						$scope.updataId = $scope.itemsValue.id;
						$scope.title = $scope.itemsValue.title
						editor.txt.html($scope.itemsValue.text);
					}else{
						Notify.alert(data.msg, {
							status: 'info'
						})
					}
				})
		}
		
		//发布与撤回 
        $scope.getRelease = function(id ,time){
        	var json = {
				"id": id,
				"releaseTime": time
       		}
       		$http.post(mpoolUI_url + '/msg/release',json).success(function (data) {
				if (data.msg == "ok") {
					if(time == ''){
						Notify.alert('撤回成功！！！', {
							status: 'success'
						});
					}else{
						Notify.alert('发布成功！！！', {
							status: 'success'
						});
					}
					$scope.parameterQuery(0);
				}else{
					Notify.alert(data.msg, {
						status: 'info'
					})
				}
			})
        }
        
        //隐藏显示  
        $scope.getEnable = function(id, obj){
       		$http.get(mpoolUI_url + '/msg/enable?id='+id+'&enable='+obj)
       			.success(function (data) {
					if (data.msg == "ok") {
						if(obj == true){
							Notify.alert('隐藏成功！！！', {
								status: 'success'
							});
						}else{
							Notify.alert('显示成功！！！', {
								status: 'success'
							});
						}
						$scope.parameterQuery(0);
					}else{
						Notify.alert(data.msg, {
							status: 'info'
						})
					}
				})
        }
		
		
		//新增公告 以及  修改公告
		var E = window.wangEditor
        var editor = new E('#editor')
		$scope.getAddUE = function(id){
			var title = $scope.title;
       		var ssText = editor.txt.html();
       		var url = "";
       		var start2 = new Date(moment($scope.date1).valueOf());
       		if(id == undefined){  //新增公告
       			var json = {
					"text": ssText,
					"title": title,
					"releaseTime":start2
	       		}
       			url = '/msg/add';
       		}else{   //修改公告
       			var json = {
       				"id": id,
					"text": ssText,
					"title": title,
					"releaseTime":start2
	       		}
       			url = '/msg/edit';
       		}
       		if(title != ""){
       			$http.post(mpoolUI_url + url,json).success(function (data) {
					if (data.msg == "ok") {
						if(id == undefined){
							Notify.alert('公告保存成功！！！', {
								status: 'success'
							});
						}else{
							Notify.alert('修改公告成功！！！', {
								status: 'success'
							});
						}
						$scope.parameterQuery(0);
					}else{
						Notify.alert(data.msg, {
							status: 'info'
						})
					}
				})
       		}else{
       			Notify.alert('标题以及内容不能为空！！！', {
					status: 'info'
				})
       		}
		}
		
		//模态框    删除 公告
		$scope.deleteNotice = function (id, title) {
			sessionStorage.setItem("title", title);
			ngDialog.openConfirm({
				template: 'noticeDelete',
				controller: 'noticeDeleteController',
				className: 'ngdialog-theme-default'
			}).then(function () {
				$http.delete(mpoolUI_url + '/msg/delete?id=' + id)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('删除公告成功！！！', {
								status: 'success'
							});
							$scope.parameterQuery(0);
						} else {
							Notify.alert(data.msg, {
								status: 'info'
							})
						}
					})
			})
		}
		
		$scope.init = function(){
			$scope.parameterQuery(0);
			editor.create();//新增公告  初始化
		}
		$scope.init();
	}
])

//模态框  删除公告
App.controller('noticeDeleteController', ['$scope', 'ngDialog', '$cookieStore',
	function ($scope, ngDialog, $cookieStore) {
		$scope.titleValue = sessionStorage.getItem('title');
	}
])


//详情
.controller('noticeDetailsController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify','$sce',
	function ($scope, $http, $location, $state, $timeout, $stateParams, Notify,$sce) {

		var id = $stateParams.id; //获取主账户   用户ID
		
		var E = window.wangEditor
        var editor = new E('#editor')
        
        
		$scope.getFindByid = function(){
			$http.get(mpoolUI_url + '/msg/findByid?id='+id)
	   			.success(function (data) {
					if (data.msg == "ok") {
						$scope.itemsValue = data.data;
						$scope.title = $scope.itemsValue.title;
						var text = $scope.itemsValue.text;
						$("#notice_text").html(text)
					}else{
						Notify.alert(data.msg, {
							status: 'info'
						})
					}
				})
		}
		

		//初始化
		$scope.init = function () {
			
			$scope.getFindByid();
			
		}
		$scope.init();

		//返回
		$scope.back = function () {
			$location.path('admin/notice');
			
		}
	}
])