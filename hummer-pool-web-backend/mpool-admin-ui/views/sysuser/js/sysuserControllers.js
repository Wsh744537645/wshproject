//系统用户管理
App.controller('sysuserController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog', 'Notify',
	function ($scope, $http, $location, $state, $timeout, $stateParams, ngDialog, Notify) {

		//列表排序		
		var vm = $scope.vm = {};
		vm.sort = {
			column: 'username',
			direction: 1,
			toggle: function (column) {
				if (column.name != "operation") {
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
				label: '角色',
				name: 'roleName',
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

		$scope.currentSelect = {
			value: '10',
		}
		//系统用户列表
		$scope.p_pernum = 10;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getSysUserList = function (current, size) {
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/sys/user/list/page?current=' + current + '&size=' + size)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						$scope.itemsData = data.data.records;
						$scope.items_num = $scope.itemsData.length;

						$scope.count = data.data.total; //总页数
						$scope.totalPage = data.data.pages; //页数
						$scope.p_current = current;
						$scope.p_all_page = Math.ceil($scope.count / size);
						reloadPno(); //初始化页码  
					}
				})
		}
		//首页  
		$scope.p_index = function () {
			$scope.load_page(1, $scope.currentSelect.value);
		}
		//尾页  
		$scope.p_last = function () {
			$scope.load_page($scope.p_all_page, $scope.currentSelect.value);
		}
		//加载某一页  
		$scope.load_page = function (page, pernum) {
			$scope.getSysUserList(page, pernum);
		};
		//初始化页码  
		var reloadPno = function () {
			$scope.pages = calculateIndexes($scope.p_current, $scope.p_all_page, 8);
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

		//系统用户管理
		$scope.getAdminInfoDetails = function (objId) {
			$state.go('admin.sys_user_edit', {
				id: objId
			});
		}

		//添加用户
		$scope.addSysUser = function () {
			$location.path('admin/add_sys_user');
		}

		//初始化
		$scope.init = function () {
			$scope.getSysUserList($scope.p_current, $scope.p_pernum); //系统用户列表
		}
		$scope.init();

		//模态框   修改角色
		$scope.postSysRole = function (objID) {
			ngDialog.openConfirm({
				template: 'modalGroup',
				controller: 'GroupROLE',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				$http.post(mpoolUI_url + '/sys/user/change/user/role?userId=' + objID + '&roleId=' + value)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('修改角色成功！！！', {
								status: 'success'
							});
							$scope.getSysUserList($scope.p_current, $scope.p_pernum); //系统用户列表
						} else {
							Notify.alert(data.msg, {
								status: 'info'
							});
						}
					}).error(function (data, status, headers, config) {
						Notify.alert('系统错误!!!', {
							status: 'info'
						});
					});
			})
		}

		$scope.deleteUser = function (userId) {
			$http.get(mpoolUI_url + '/sys/user/del/user?userId=' + userId ).success(function (data) {
				if (data.msg == "ok") {
					Notify.alert('删除用户成功！！！', {
						status: 'success'
					});
					$scope.getSysUserList($scope.p_current, $scope.p_pernum); //系统用户列表
				}else{
					Notify.alert(data.msg, {
						status: 'info'
					});
				}
			})
		}
	}
])
//模态框  修改角色
App.controller('GroupROLE', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		$http.get(mpoolUI_url + '/sys/role/list').success(function (data) {
			if (data.msg == "ok") {
				$scope.itemsData = data.data;
				var id = $scope.itemsData[0].roleId;
				$scope.groupName = {
					value: id
				}
			}
		})
	}
])


//系统用户管理编辑
App.controller('sysuserEditController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {

		var id = $stateParams.id; //获取主账户   用户ID

		//查询邮箱、手机方法
		$scope.getSettings = function () {
			$http.get(mpoolUI_url + '/sys/user/admin/info/' + id).success(function (data) {
				if (data.msg == 'ok') {
					$scope.itemsData = data.data;
				} else {
					$scope.itemsData = {
						username: '',
						email: '',
						telphone: '',
						password: ''
					}
				}
			}).error(function (error) {
				$scope.itemsData = {
					username: '',
					email: '',
					telphone: '',
					password: ''
				}
			})
		}

		//提交编辑方法
		$scope.updateSub = function () {
			if (($scope.itemsData.email == '' || $scope.itemsData.email == null) || ($scope.itemsData.telphone == '' || $scope.itemsData.telphone == null)) {
				$scope.changemail($scope.itemsData.email);
				$scope.changphone($scope.itemsData.telphone);
			} else {
				var json = {
					"email": $scope.itemsData.email,
					"telphone": $scope.itemsData.telphone,
					"userId": id,
					"username": $scope.itemsData.username,
					"password": $scope.itemsData.password
				}
				$scope.sub = false;
				$http.post(mpoolUI_url + '/sys/user/admin/update', json).success(function (data) {
					if (data.msg == 'ok') {
						Notify.alert('修改信息成功！！！', {
							status: 'success'
						});
						$timeout(function () {
							$location.path('admin/sys_user');
						}, 2000);
					} else {
						Notify.alert(data.msg, {
							status: 'info'
						});
						$scope.sub = true;
					}
				})
			}
		}

		$scope.init = function () {
			$scope.getSettings();
		}
		$scope.init();

		//返回
		$scope.back = function () {
			$location.path('admin/sys_user');
		}

		//邮箱
		$scope.sub = true;
		$scope.changemail = function (objEmail) {
			if (objEmail == undefined || objEmail == "") {
				$scope.emilCheck = "请输入邮箱";
				$scope.sub = false;
			} else {
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if (email.test(objEmail)) {
					$scope.emilCheck = "";
					$scope.sub = true;
				} else {
					$scope.emilCheck = "邮箱格式不正确，如 1111@cn.com";
					$scope.sub = false;
				}
			}
		}

		//手机
		$scope.changphone = function (objPhone) {
			if (objPhone == undefined || objPhone == "") {
				$scope.phoneCheck = "请输入手机号";
				$scope.sub = false;
			} else {
				var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
				if (phone.test(objPhone)) {
					$scope.phoneCheck = "";
					$scope.sub = true;
				} else {
					$scope.phoneCheck = "手机号码为标准的11数字";
					$scope.sub = false;
				}
			}
		}
	}
])

//添加 系统用户管理
App.controller('addSysuserController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {

		//添加用户
		$scope.getAddSysuser = function () {
			if (($scope.fullName == '' || $scope.fullName == null) ||($scope.user == '' || $scope.user == null) || ($scope.password == '' || $scope.password == null) || !$scope.roleId) {
				$scope.changUser($scope.user);
				$scope.changFullName($scope.fullName);
				$scope.changPassword($scope.password);
				$scope.fRoleIdvalidate($scope.roleId)
			} else {
				var json = {
					"password": $scope.password,
					"username": $scope.user,
					"fullName": $scope.fullName
				}
				if ($scope.email) {
					json.email = $scope.email
				}
				if ($scope.phone) {
					json.telphone = $scope.phone
				}
				console.log($scope.roleId)

				$scope.sub = false;
				$http.post(mpoolUI_url + '/sys/user/add?roleId=' + $scope.roleId, json).success(function (data) {
					if (data.msg == 'ok') {
						Notify.alert('添加系统用户成功！！！', {
							status: 'success'
						});
						$timeout(function () {
							$location.path('admin/sys_user');
						}, 2000);
					} else {
						Notify.alert(data.msg, {
							status: 'info'
						});
						$scope.sub = true;
					}
				})
			}
		}

		//返回
		$scope.back = function () {
			$location.path('admin/sys_user');
		}

		//用户名
		$scope.sub = true;
		$scope.changUser = function (usernameValue) {
			if (usernameValue == undefined || usernameValue == "") {
				$scope.user_Check = "请输入用户名";
				$scope.sub = false;
			} else {
				var user = /^[a-zA-Z0-9_@-]{6,32}$/;
				if (user.test(usernameValue)) {
					$scope.user_Check = "";
					$scope.sub = true;
				} else {
					$scope.user_Check = "用户名至少6位，且包含大小写、数字及@_-特殊字符";
					$scope.sub = false;
				}
			}
		}
		
		$scope.changFullName = function(fullNameValue){
			if (fullNameValue == undefined || fullNameValue == "") {
				$scope.fullNameCheck = "请输入真实姓名";
				$scope.sub = false;
			} else {
				var user = /[\u4E00-\u9FA5]/;
				if (user.test(fullNameValue)) {
					$scope.fullNameCheck = "";
					$scope.sub = true;
				} else {
					$scope.fullNameCheck = "真实姓名至少6位，且包含大小写、数字及@_-特殊字符";
					$scope.sub = false;
				}
			}
		}
		
		//密码
		$scope.changPassword = function (passValue) {
			if (passValue == undefined || passValue == "") {
				$scope.password_Check = "请输入密码";
				$scope.sub = false;
			} else {
				var pas = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
				if (pas.test(passValue)) {
					$scope.password_Check = "";
					$scope.sub = true;
				} else {
					$scope.password_Check = "密码至少8位，只包含大小写、数字，不包含特殊符号";
					$scope.sub = false;
				}
			}
		}
		//邮箱
		$scope.changEmail = function (emailValue) {
			if (emailValue == undefined || emailValue == "") {
				$scope.emil_Check = "请输入邮箱";
				$scope.sub = false;
			} else {
				var email = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,64})$/;
				if (email.test(emailValue)) {
					$scope.emil_Check = "";
					$scope.sub = true;
				} else {
					$scope.emil_Check = "邮箱格式不正确，如 1111@cn.com";
					$scope.sub = false;
				}
			}
		}

		//手机
		$scope.changPhone = function (phoneValue) {
			if (phoneValue == undefined || phoneValue == "") {
				$scope.phone_Check = "请输入手机号";
				$scope.sub = false;
			} else {
				var phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
				if (phone.test(phoneValue)) {
					$scope.phone_Check = "";
					$scope.sub = true;
				} else {
					$scope.phone_Check = "手机号码为标准的11数字";
					$scope.sub = false;
				}
			}
		}
		$scope.fRoleIdvalidate = function (roleId) {
			if (roleId) {
				$scope.roleIdvalidate = false
			} else {
				$scope.roleIdvalidate = true
			}
		}
		//初始化数据
		$scope.init = function () {
			$http.get(mpoolUI_url + '/sys/role/list').success(function (data) {
				if (data.msg == "ok") {
					$scope.roles = data.data;
					$scope.roles.unshift({
						roleName: "请选择"
					})
				}
			})
		}
		$scope.init()
	}
])