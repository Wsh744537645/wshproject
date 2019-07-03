
//菜单管理
App.controller('sysmenuController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog',
	function($scope, $http, $location, $state, $timeout, $stateParams, ngDialog) {
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'text',
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
        	{label: '菜单名称',			name: 'text',				type: 'string'},
        	{label: '创建时间',			name: 'createTime',			type: 'string'},
            {label: '最新更新时间',			name: 'lastupdateTime',		type: 'string'},
            {label: '操作',				name: 'operation',			type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       };
       
		//菜单管理列表
	    $scope.show_items = true;
		$scope.getSysMenuList = function(){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/sys/menu/list')
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data;
					}
				})	
		}
		
		//模态框    修改菜单
		$scope.updateSysMenu = function(objId, objName){
			sessionStorage.setItem("name", objName);   // 获取修改菜单名称
			ngDialog.openConfirm({
				template: 'modalUpdate',
				controller: 'updateCtrls',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var json = {
					"menuId": objId,
				  	"orderNum": 0,
				  	"parentId": 0,
				  	"perms": "string",
				  	"sref": "string",
				  	"text": value,
				  	"translate": "string",
				  	"type": 0
				}
	  			$http.post(mpoolUI_url + '/sys/menu/update', json).success(function(data){
					if(data.msg == "ok"){
						$scope.getSysMenuList();   //菜单管理列表
					}
				})
			})
		}
		
		//初始化
		$scope.init = function(){
			$scope.getSysMenuList();   //菜单管理列表 
		}
		$scope.init();
		
		//模态框    添加菜单
//		$scope.postSysMenu = function(){
//			ngDialog.openConfirm({
//				template: 'modalGroup',
//				controller: 'GroupCtrls',
//				className: 'ngdialog-theme-default'
//			}).then(function(value){
//				var json = {
//					"text": value
//				}
//	  			$http.post(mpoolUI_url + '/sys/menu/add', json).success(function(data){
//					if(data.msg == "ok"){
//						$scope.getSysMenuList();   //角色管理列表
//					}
//				})
//			})
//		}
		
	}
])

//模态框  修改菜单
App.controller('updateCtrls', ['$http', '$scope', 'ngDialog',
	function($http, $scope, ngDialog) {
		$scope.name = sessionStorage.getItem('name');     // 获取修改菜单名称
		$scope.group2 = false;
		$scope.changeTextName = function(obj){
			if(obj == ""){
				$scope.group2 = false;
			}else{
				$scope.group2 = true;
			}
		}
	}
])

//模态框  添加菜单
//App.controller('GroupCtrls', ['$http', '$scope', 'ngDialog',
//	function($http, $scope, ngDialog) {
//		
//		$scope.group = false;
//		$scope.changeMenuName = function(obj){
//			if(obj == ""){
//				$scope.group = false;
//			}else{
//				$scope.group = true;
//			}
//		}
//	}
//])

