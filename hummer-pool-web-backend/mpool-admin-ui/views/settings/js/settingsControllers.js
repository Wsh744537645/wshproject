//预警列表
App.controller('settingsListController', ['$scope', '$http', '$location', '$state', '$timeout', 'ngDialog', 'Notify',
	function ($scope, $http, $location, $state, $timeout, ngDialog, Notify) {
		$http.get(mpoolUI_url + '/auth/cur/currencyName').success(function(r) {
			$scope.currencyName = r.data.currencyName
		})
		//列表排序		
		var vm = $scope.vm = {};
		vm.sort = {
			column: 'username',
			direction: 1,
			toggle: function (column) {
				if (column.name != 'operation') {
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
		vm.columns = [{
				label: '用户名',
				name: 'username',
				type: 'string'
			},
			{
				label: '操作',
				name: 'operation',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
		};


		//预警列表
		$scope.show_items = true;
		$scope.getSysPermsList = function (objID) {
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/alarm/getAlarmUser/' + objID)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData = data.data;
							$scope.items_num = $scope.itemsData.length;
						} else {
							$scope.items_num = 0;
						}
					}
				}).error(function (data, status, headers, config) {
					$timeout(function () {
						$scope.show_items = true;
					}, 800);
					$scope.itemsData = [];
					$scope.items_num = 0;
				});
		}

		//角色查询
		$scope.parameterQuery = function (objId, index, name) {
			$scope.selected = index;
			$scope.minerName = name;
			$scope.currentSelect = {
				value: '10',
			}
			$scope.permsID = objId; //用户角色id
			$scope.getSysPermsList(objId); //角色查询参数
		}

		//预警面板列表
		$scope.getSysRoleList = function () {
			$http.get(mpoolUI_url + '/alarm/list').success(function (data) {
				if (data.msg == "ok") {
					$scope.itemsData2 = data.data;
					for (let i in $scope.itemsData2) {
						var valueId = $scope.itemsData2[0].id;
						var valueName = $scope.itemsData2[0].templateName;
					}
					$scope.parameterQuery(valueId, 0, valueName); //预警列表
				}
			})
		}

		//模态框   设置告警
		$scope.updatePerms = function () {
			ngDialog.openConfirm({
				template: 'modalGroup2',
				controller: 'GroupCtrls2',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				$http.get(mpoolUI_url + '/alarm/setUserAlarm?id=' + $scope.permsID + '&userId=' + value).success(function (data) {
					if (data.msg == "ok") {
						$scope.parameterQuery($scope.permsID, $scope.selected, $scope.minerName); //预警列表
					}
				})
			})
		}

		//模态框    删除   列表数据
		$scope.deleteWorkerGroup = function (objName, objId) {
			sessionStorage.setItem("objName", objName);
			ngDialog.openConfirm({
				template: 'deleteGroup',
				controller: 'deleteGroupCtrl',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				$http.get(mpoolUI_url + '/alarm/deleteUserAlarm?userId=' + objId)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('删除警告设置成功！！！', {
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
		$scope.init = function () {
			$scope.getSysRoleList(); //预警面板列表
		}
		$scope.init();

		//跳转到编辑页面
		$scope.getSettingedit = function (obj) {
			$state.go('admin.settingedit', {
				id: obj
			});
		}

		//跳转到添加预警界面
		$scope.getAddSettingedit = function () {
			$location.path('admin/addSettings');
		}

		//模态框    删除预警模板
		$scope.deleteSysRole = function (objId, objName) {
			sessionStorage.setItem("name", objName);
			ngDialog.openConfirm({
				template: 'modalDeletes',
				controller: 'DeleteCtrls',
				className: 'ngdialog-theme-default'
			}).then(function () {
				$http.post(mpoolUI_url + '/alarm/deleteTemplate?id=' + objId).success(function (data) {
					if (data.msg == "ok") {
						Notify.alert('删除预警模板成功！！！', {
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

	}
])

//模态框  删除列表数据
App.controller('deleteGroupCtrl', ['$scope', 'ngDialog', '$cookieStore',
	function ($scope, ngDialog, $cookieStore) {
		$scope.objName = sessionStorage.getItem('objName');
	}
])
//模态框  删除预警模板
App.controller('DeleteCtrls', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		$scope.name = sessionStorage.getItem('name');
	}
])

//模态框  设置告警
App.controller('GroupCtrls2', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		//设置告警列表
		$scope.getSysMenuList = function () {
			$http.get(mpoolUI_url + '/alarm/getAlarmUserSelect').success(function (data) {
				if (data.msg == "ok") {
					$scope.itemsModal = data.data;
					var id = $scope.itemsModal[0].userId;
					$scope.groupName = {
						value: id
					}
				}
			})
		}
		$scope.init = function () {
			$scope.getSysMenuList();
		}
		$scope.init();
	}
])


//警告设置
App.controller('settingsController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {

		var id = $stateParams.id; //获取主账户   用户ID

		$scope.time = [{
				name: '半小时',
				value: '1800'
			},
			{
				name: '1小时',
				value: '3600'
			},
			{
				name: '2小时',
				value: '7200'
			}
		];

		//警告设置查询邮箱、手机方法
		$scope.getSettings = function () {
			var j = {

				templateName: '',
				cycle: 1800,
				shareRate: 0,
				workerNumber: 0,
				pay: 0,
				email: false,
				phone: false,
				isEnable: true,
				createBill: false,
				modifyPassd: false
			}
			$http.get(mpoolUI_url + '/alarm/info/' + id).success(function (data) {
				if (data.msg == 'ok') {
					//var itemsData = data.data;
					let itemsData = data.data;
					if (itemsData == null) {
						itemsData = j;
					} else {
						itemsData.pay = itemsData.pay / 100000000
						itemsData.shareRate = itemsData.shareRate * 100
					}
					$scope.itemsData = itemsData
				}
			}).error(function (error) {
				$scope.itemsData = j;
			})
		}

		$scope.getSettings2 = function () {
			let data = { ...$scope.itemsData
			}
			data.pay = data.pay * 100000000
			data.shareRate = data.shareRate / 100
			$http.post(mpoolUI_url + '/alarm/add', data).success(function (data) {
				if (data.msg == 'ok') {
					Notify.alert('设置成功！！！', {
						status: 'success'
					})
				} else {
					Notify.alert(data.msg, {
						status: 'info'
					})
				}
			})

		}

		$scope.init = function () {
			$scope.getSettings();
		}
		$scope.init();

		//返回
		$scope.back = function () {
			$location.path('admin/setting');
		}

	}
])

//添加警告设置
App.controller('addSettingsController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {


		$scope.time = [{
				name: '半小时',
				value: '1800'
			},
			{
				name: '1小时',
				value: '3600'
			},
			{
				name: '2小时',
				value: '7200'
			}
		];
		var j = {
			templateName: '',
			cycle: 1800,
			shareRate: 0,
			workerNumber: 0,
			pay: 0,
			email: false,
			phone: false,
			isEnable: true
		}
		$scope.itemsData = j;

		$scope.getAddSettings = function () {
			let data = { ...$scope.itemsData
			}
			data.pay = data.pay * 100000000
			data.shareRate = data.shareRate / 100
			$http.post(mpoolUI_url + '/alarm/add', data).success(function (data) {
				if (data.msg == 'ok') {
					Notify.alert('设置成功！！！', {
						status: 'success'
					})
				} else {
					Notify.alert(data.msg, {
						status: 'info'
					})
				}
			})

		}

		$scope.init = function () {

		}
		$scope.init();

		//返回
		$scope.back = function () {
			$location.path('admin/setting');
		}

	}
])