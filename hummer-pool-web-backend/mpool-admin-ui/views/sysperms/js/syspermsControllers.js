
//权限设置
App.controller('syspermsController', ['$scope', '$http' ,'$location', '$state', '$timeout', 'ngDialog', 'Notify',
	function($scope, $http, $location, $state, $timeout, ngDialog, Notify) {
	    
	    //列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'text',
            direction: 1,
            toggle: function (column) {
            	if(column.name != 'operation'){
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
        	{label: '操作',		name: 'operation',		type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       	};
			
			
	    //权限设置列表
	    $scope.show_items = true;
		$scope.getSysPermsList = function(objID){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/role/menu/get/'+ objID)
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
	          		 	},800);
	          		 	if(data.data != null){
							$scope.itemsData = data.data;
							$scope.items_num = $scope.itemsData.length;
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
		
		//角色查询
		$scope.parameterQuery = function(objId, index, name){
			$scope.selected = index;
			$scope.minerName = name;
			$scope.currentSelect = {
		    	value: '10',
		 	}
			$scope.permsID = objId;    //用户角色id
			$scope.getSysPermsList(objId); //角色查询参数
		}
		
		//角色管理列表
		$scope.getSysRoleList = function(){
			$http.get(mpoolUI_url + '/sys/role/list').success(function(data){
				if(data.msg == "ok"){
					$scope.itemsData2 = data.data;
					for(let i in $scope.itemsData2){
						var valueId = $scope.itemsData2[0].roleId;
						var valueName = $scope.itemsData2[0].roleName;
					}
					$scope.parameterQuery(valueId, 0, valueName);      //权限设置列表
				}
			})	
		}
		
		//模态框  新建  
		$scope.getRoleAdd = function () {
			ngDialog.openConfirm({
				template: 'roleAddModal',
				controller: 'roleAddController',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				var json = {
					"roleName": value
				}
				$http.post(mpoolUI_url + '/apis/sys/role/add',json)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('新建角色成功！！！', {
								status: 'success'
							});
							$scope.getSysRoleList(); 
						}
					}).error(function (data, status, headers, config) {
						Notify.alert('系统错误!!!', {
							status: 'info'
						});
					});
			})
		}
		
		//模态框  编辑  
		$scope.getRoleUpdate = function (id, name) {
			sessionStorage.setItem("name", name);
			ngDialog.openConfirm({
				template: 'RoleUpdateModal',
				controller: 'RoleUpdateController',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				var json = {
					"roleId":id,
					"roleName": value
				}
				$http.post(mpoolUI_url + '/sys/role/update',json)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('编辑角色成功！！！', {
								status: 'success'
							});
							$scope.getSysRoleList(); 
						}
					}).error(function (data, status, headers, config) {
						Notify.alert('系统错误!!!', {
							status: 'info'
						});
					});
			})
		}
		
		//模态框   添加权限
		$scope.updatePerms = function(){
			ngDialog.openConfirm({
				template: 'modalGroup',
				controller: 'GroupCtrls',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var json = [value];
	  			$http.post(mpoolUI_url + '/role/menu/add/' + $scope.permsID, json).success(function(data){
					if(data.msg == "ok"){
						$scope.parameterQuery($scope.permsID, $scope.selected, $scope.minerName);      //权限设置列表
					}
				})
			})
		}
		
		//列表删除
		$scope.deleteMenu=function(menuId){
			$http.delete(mpoolUI_url + '/role/menu/del?id=' + menuId).success(function(data){
				if(data.msg == "ok"){
					$scope.parameterQuery($scope.permsID, $scope.selected, $scope.minerName);      //权限设置列表
				}
			})
		}
		
		//模态框    删除   列表数据
		$scope.deleteMenuDel = function (objId,objName) {
			sessionStorage.setItem("objName", objName);
			ngDialog.openConfirm({
				template: 'deleteDel',
				controller: 'deleteDelCtrl',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				$http.delete(mpoolUI_url + '/sys/role/del?roleId=' + objId)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('删除角色成功！！！', {
								status: 'success'
							});
							$scope.getSysRoleList(); //预警面板列表
						} else {
							Notify.alert(data.msg, {
								status: 'info'
							})
						}
					})
			})
		}
		
		
		
		//初始化方法
		$scope.init = function(){
			$scope.getSysRoleList();    //角色管理列表
		}
		$scope.init();
		
	}
])

//模态框  添加权限
App.controller('GroupCtrls', ['$http', '$scope', 'ngDialog',
	function($http, $scope, ngDialog) {
		
		//菜单管理列表
		$scope.getSysMenuList = function(){
			$http.get(mpoolUI_url + '/sys/menu/list').success(function(data){
				if(data.msg == "ok"){
					$scope.itemsModal = data.data;
					if(!!$scope.itemsModal) {
						var id = $scope.itemsModal[0].menuId;
						$scope.groupName = {
							value : id
						}
					}
				}
			})	
		}
		
		$scope.init = function(){
			$scope.getSysMenuList();
		}
		$scope.init();
	}
])

//模态框 新建
App.controller('roleAddController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {}])

//模态框  删除列表数据
App.controller('deleteDelCtrl', ['$scope', 'ngDialog', '$cookieStore',
	function ($scope, ngDialog, $cookieStore) {
		$scope.objName = sessionStorage.getItem('objName');
	}
])

//模态框  编辑
App.controller('RoleUpdateController', ['$scope', 'ngDialog', '$cookieStore',
	function ($scope, ngDialog, $cookieStore) {
		$scope.roleName = sessionStorage.getItem('name');
	}
])