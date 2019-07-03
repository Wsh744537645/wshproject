//用户列表
App.controller('accountUserListController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify) {

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
					label: '主账户名',
					name: 'username',
					type: 'string'
				},
				{
					label: '当前总算力(15分钟)',
					name: 'currentShare',
					type: 'string'
				},
				{
					label: '过去一天总算力',
					name: 'pastDayShare',
					type: 'string'
				},
				{
					label: '预收益',
					name: 'totalDue',
					type: 'string'
				},
				{
					label: '总收益',
					name: 'userType',
					type: 'string'
				},
				{
					label: '累计转账',
					name: 'totalPaid',
					type: 'string'
				},
				{
					label: '注册时间',
					name: 'createTime',
					type: 'string'
				},
				{
					label: '最后一次登录',
					name: 'lastIp',
					type: 'int'
				}
			];
			$scope.filter = function () {
				$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
			};

			$scope.currentSelect = {
				value: '10',
			}
			//用户列表主账户
			$scope.p_pernum = 10;
			$scope.p_current = 1;
			$scope.p_all_page = 0;
			$scope.pages = [];
			$scope.show_items = true;
			$scope.getAccountUserList = function (current, size) {
				$scope.show_items = false;
				$http.get(mpoolUI_url + '/account/master/list?current=' + current + '&size=' + size + '&descs=createTime')
					.success(function (data) {
						if (data.msg == "ok") {
							$timeout(function () {
								$scope.show_items = true;
							}, 800);
							$scope.itemsData = data.data.records;
							for (let i in $scope.itemsData) {
								if ($scope.itemsData[i].fppsRate) {
									$scope.itemsData[i].fppsRate = $scope.itemsData[i].fppsRate / 1000;
								} else {
									$scope.itemsData[i].fppsRate = 0;
								}
							}
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
				$scope.getAccountUserList(page, pernum);
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

			//用户列表详情
			$scope.getAccountUserListDetails = function (obj) {
				$state.go('admin.accountUserListDetails', {
					id: obj
				});
			}

			//初始化
			$scope.init = function () {
				$scope.getAccountUserList($scope.p_current, $scope.p_pernum); //主账户列表
			}
			$scope.init();

		}
	])


	//用户列表详情
	.controller('accountUserListDetailsController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify) {

			var id = $stateParams.id; //获取主账户   用户ID

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
					label: '子账户名',
					name: 'username',
					type: 'string'
				},
				{
					label: '当前总算力',
					name: 'currentShare',
					type: 'string'
				},
				{
					label: '过去一天总算力',
					name: 'pastDayShare',
					type: 'string'
				},
				{
					label: '余额',
					name: 'totalDue',
					type: 'int'
				},
				{
					label: '总收益',
					name: 'userType',
					type: 'string'
				},
				{
					label: '累计转账',
					name: 'userType',
					type: 'string'
				},
				{
					label: '注册时间',
					name: 'createTime',
					type: 'string'
				},
				{
					label: '最后一次登录',
					name: 'lastTime',
					type: 'int'
				}
			];
			$scope.filter = function () {
				$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
			};

			$scope.currentSelect = {
				value: '10',
			}
			//用户列表详情  主账户下的所有子账户
			$scope.p_pernum = 10;
			$scope.p_current = 1;
			$scope.p_all_page = 0;
			$scope.pages = [];
			$scope.show_items = true;
			$scope.getSubaccountList = function (current, size) {
				$scope.show_items = false;
				$http.get(mpoolUI_url + '/account/member/list?masterUserId=' + id + '&current=' + current + '&size=' + size + '&descs=createTime')
					.success(function (data) {
						if (data.msg == "ok") {
							$timeout(function () {
								$scope.show_items = true;
							}, 800);
							$scope.itemsData = data.data.records;
							for (let i in $scope.itemsData) {
								if ($scope.itemsData[i].fppsRate) {
									$scope.itemsData[i].fppsRate = $scope.itemsData[i].fppsRate / 1000;
								} else {
									$scope.itemsData[i].fppsRate = 0;
								}
							}
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
				$scope.getSubaccountList(page, pernum);
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

			//初始化
			$scope.init = function () {
				$scope.getSubaccountList($scope.p_current, $scope.p_pernum); //子账户列表
			}
			$scope.init();

			//返回
			$scope.back = function () {
				$location.path('admin/account_user_list');
			}
		}
	])