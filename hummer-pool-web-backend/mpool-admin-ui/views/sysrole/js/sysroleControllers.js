
//角色管理
App.controller('sysroleController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog',
	function($scope, $http, $location, $state, $timeout, $stateParams, ngDialog) {
		
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'roleName',
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
        	{label: '户名称',				name: 'roleName',			type: 'string'},
        	{label: '创建时间',			name: 'createTime',			type: 'string'},
            {label: '最新更新时间',			name: 'lastupdateTime',		type: 'string'},
            {label: '操作',				name: 'operation',			type: 'string'}
        ];
		$scope.filter= function () {
            $scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
       };
       
		//角色管理列表
	    $scope.show_items = true;
		$scope.getSysRoleList = function(){
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/sys/role/list')
				.success(function(data){
					if(data.msg == "ok"){
						$timeout(function(){
						   $scope.show_items = true;
              		 	},800);
						$scope.itemsData = data.data;
					}
				})	
		}
		
		//模态框    删除角色
		$scope.deleteSysRole = function(objId, objName){
			sessionStorage.setItem("name", objName);   // 删除角色名称
			ngDialog.openConfirm({
				template: 'modalDeletes',
				controller: 'DeleteCtrls',
				className: 'ngdialog-theme-default'
			}).then(function(){
	  			$http.delete(mpoolUI_url + '/sys/role/del?roleId='+ objId).success(function(data){
					if(data.msg == "ok"){
						$scope.getSysRoleList();   //角色管理列表
					}
				})
			})
		}
        
        //模态框    创建角色
		$scope.postSysRole = function(){
			ngDialog.openConfirm({
				template: 'modalGroup',
				controller: 'SysRoleDialog',
				className: 'ngdialog-theme-default'
			}).then(function(value){
				var json = {
				  	"roleName": value
				}
	  			$http.post(mpoolUI_url + '/sys/role/add', json).success(function(data){
					if(data.msg == "ok"){
						$scope.getSysRoleList();   //角色管理列表
					}
				})
			})
		}
		
		//初始化
		$scope.init = function(){
			$scope.getSysRoleList();   //账户管理列表
		}
		$scope.init();
		
	}
])

//模态框  创建角色
App.controller('SysRoleDialog', ['$http', '$scope', 'ngDialog',
	function($http, $scope, ngDialog) {
		console.log('init');
		
		$scope.group = false;
		$scope.changeTXID = function(obj){
			console.log(obj)
			if(obj == ""){
				$scope.group = false;
			}else{
				$scope.group = true;
			}
		}
	}
])

//模态框  删除角色
App.controller('DeleteCtrls', ['$http', '$scope', 'ngDialog',
	function($http, $scope, ngDialog) {
		console.log('init')
		$scope.name = sessionStorage.getItem('name');     // 删除角色名称
	}
])