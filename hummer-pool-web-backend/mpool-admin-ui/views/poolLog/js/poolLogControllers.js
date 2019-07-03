//矿池日志
App.controller('poolLogController', ['$scope', '$http', '$location', '$state', '$timeout', '$cookieStore', 'Notify',
	function ($scope, $http, $location, $state, $timeout, $cookieStore, Notify) {

		$scope.folders = [{
				name: '注册日志',
				id: '1'
			},
			{
				name: '费率修改日志',
				id: '2'
			},
			{
				name: '打款日志',
				id: '3'
			},
			{
				name: '工单日志',
				id: '4'
			}
		];

		//tab切换
		$scope.tab = function (obj) {
			$scope.selected = obj;
			// if (obj == 0) { //注册日志
			// 	$scope.tabSubaccounts1 = false; //注册日志  
			// 	$scope.tabSubaccounts2 = true; //费率修改日志
			// } else { //费率修改日志
			// 	$scope.tabSubaccounts1 = true;
			// 	$scope.tabSubaccounts2 = false;
			// }
		}

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
				label: '注册时间',
				name: 'createTime',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
		};

		//------------------------------------------------------------------注册日志	
		$scope.currentSelect = {
			value: '10',
		}
		//注册日志
		$scope.p_pernum = 10;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getpoolLogList = function (current, size) {
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/log/getAccountRegisterLog?current=' + current + '&size=' + size + '&descs=createTime')
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData = data.data.records;
							$scope.items_num = $scope.itemsData.length;

							$scope.count = data.data.total; //总页数
							$scope.totalPage = data.data.pages; //页数
							$scope.p_current = current;
							$scope.p_all_page = Math.ceil($scope.count / size);
							reloadPno(); //初始化页码  
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
			$scope.getpoolLogList(page, pernum);
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

		//------------------------------------------------------------------费率修改日志	
		var vm2 = $scope.vm2 = {};
		vm2.sort = {
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
		vm2.columns = [{
				label: '用户名',
				name: 'username',
				type: 'string'
			},
			{
				label: '更新用户名',
				name: 'updateUserName',
				type: 'string'
			},
			{
				label: '修改前费率',
				name: 'oldFppsRate',
				type: 'int'
			},
			{
				label: '修改后费率',
				name: 'newFppsRate',
				type: 'string'
			},
			{
				label: '更新时间',
				name: 'updateTime',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num2 = $filter('filter')($scope.itemsData2, vm2.filter.$).length;
		};
		$scope.currentSelect2 = {
			value: '10',
		}
		$scope.p_pernum2 = 10;
		$scope.p_current2 = 1;
		$scope.p_all_page2 = 0;
		$scope.pages2 = [];
		$scope.show_items2 = true;
		$scope.getFppsRateLogList = function (current, size) {
			$scope.show_items2 = false;
			$http.get(mpoolUI_url + '/log/getFppsRateChangeLog?current=' + current + '&size=' + size + '&descs=createTime')
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items2 = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData2 = data.data.records;
							$scope.items_num2 = $scope.itemsData2.length;

							$scope.count2 = data.data.total; //总页数
							$scope.totalPage2 = data.data.pages; //页数
							$scope.p_current2 = current;
							$scope.p_all_page2 = Math.ceil($scope.count2 / size);
							reloadPno2(); //初始化页码  
						} else {
							$scope.items_num2 = 0;
						}
					}
				}).error(function (data, status, headers, config) {
					$timeout(function () {
						$scope.show_items2 = true;
					}, 800);
					$scope.itemsData2 = [];
					$scope.items_num2 = 0;
				});
		}

		//首页  
		$scope.p_index2 = function () {
			$scope.load_page2(1, $scope.currentSelect2.value);
		}
		//尾页  
		$scope.p_last2 = function () {
			$scope.load_page2($scope.p_all_page2, $scope.currentSelect2.value);
		}
		//加载某一页  
		$scope.load_page2 = function (page, pernum) {
			$scope.getFppsRateLogList(page, pernum);
		};
		//初始化页码  
		var reloadPno2 = function () {
			$scope.pages2 = calculateIndexes2($scope.p_current2, $scope.p_all_page2, 8);
		};
		//分页算法  
		var calculateIndexes2 = function (current, length, displayLength) {
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

		//----------------------------------------------------支付日志

		var vm3 = $scope.vm3 = {};
		vm3.sort = {
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
		vm3.columns = [{
				label: '单号',
				name: 'bill_number',
				type: 'string'
			},
			{
				label: '打款人',
				name: 'username',
				type: 'string'
			},
			{
				label: 'Txid',
				name: 'txid',
				type: 'string'
			},
			{
				label: '打款时间',
				name: 'pay_at',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num3 = $filter('filter')($scope.itemsData3, vm3.filter.$).length;
		};
		$scope.currentSelect3 = {
			value: '10',
		}
		$scope.p_pernum3 = 10;
		$scope.p_current3 = 1;
		$scope.p_all_page3 = 0;
		$scope.pages3 = [];
		$scope.show_items3 = true;
		$scope.getBillPayLogList = function (current, size) {
			$scope.show_items3 = false;
			$http.get(mpoolUI_url + '/log/getBillPayLog?current=' + current + '&size=' + size + '&descs=createTime')
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items3 = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData3 = data.data.records;
							$scope.items_num3 = $scope.itemsData3.length;

							$scope.count3 = data.data.total; //总页数
							$scope.totalPage3 = data.data.pages; //页数
							$scope.p_current3 = current;
							$scope.p_all_page3 = Math.ceil($scope.count3 / size);
							reloadPno3(); //初始化页码  
						} else {
							$scope.items_num3 = 0;
						}
					}
				}).error(function (data, status, headers, config) {
					$timeout(function () {
						$scope.show_items3 = true;
					}, 800);
					$scope.itemsData3 = [];
					$scope.items_num3 = 0;
				});
		}

		//首页  
		$scope.p_index3 = function () {
			$scope.load_page3(1, $scope.currentSelect3.value);
		}
		//尾页  
		$scope.p_last3 = function () {
			$scope.load_page3($scope.p_all_page3, $scope.currentSelect3.value);
		}
		//加载某一页  
		$scope.load_page3 = function (page, pernum) {
			$scope.getBillPayLogList(page, pernum);
		};
		//初始化页码  
		var reloadPno3 = function () {
			$scope.pages3 = calculateIndexes3($scope.p_current3, $scope.p_all_page3, 8);
		};
		//分页算法  
		var calculateIndexes3 = function (current, length, displayLength) {
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
			$scope.tab(1); //tab切换
			$scope.getpoolLogList($scope.p_current, $scope.p_pernum); //注册日志
			$scope.getFppsRateLogList($scope.p_current2, $scope.p_pernum2); //费率修改日志
			$scope.getBillPayLogList($scope.p_current3, $scope.p_pernum3);
		}
		$scope.init();
	}
]);