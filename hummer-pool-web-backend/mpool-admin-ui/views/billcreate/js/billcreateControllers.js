//创建支付单
App.controller('billcreateController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'ngDialog', 'Notify',
	function ($scope, $http, $location, $state, $timeout, $stateParams, ngDialog, Notify) {
		$http.get(mpoolUI_url + '/auth/cur/currencyName').success(function(r) {
			$scope.currencyName = r.data.currencyName
		})
		//列表排序		
		var vm = $scope.vm = {};
		vm.sort = {
			column: 'username',
			direction: 1,
			toggle: function (column) {
				if (column.name != 'checkbox' && column.name != "operation") {
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

			{
				label: '户名称',
				name: 'username',
				type: 'string'
			},
			{
				label: '钱包地址',
				name: 'wallet_address',
				type: 'string'
			},
			{
				label: '余额',
				name: 'pay_btc',
				type: 'string'
			},
			{
				label: '创建时间',
				name: 'cteate_at',
				type: 'string'
			},
			{
				label: '描述',
				name: 'desc',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
		};

		//展示用户管理
		$scope.currentSelect = {
			value: '10',
		}
		//用户管理列表
		$scope.p_pernum = 10;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getIncomeSettlementList = function (current, size) {
			var username = $scope.username;

			var url = '/bill/due/items?current=' + current + '&size=' + size;
			var url2 = '/bill/due/PayBtcSum';
			$scope.url3 = '/apis/bill/exportDuePayItems?a=1';
			if (username) {
				url = url + '&username=' + username;
				url2 = url2 + '?username=' + username;
				$scope.url3 = $scope.url3 + '&username=' + username;
			}

			$http.get(mpoolUI_url + url2).success(function (data) {
				$scope.totalValue = data.data;
			})

			$scope.show_items = false;
			$http.get(mpoolUI_url + url)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						$scope.itemsData = data.data.records;
						for (var i in $scope.itemsData) {
							$scope.itemsData[i].pay_btc = $scope.itemsData[i].pay_btc / Math.pow(10, 8);
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
			$scope.getIncomeSettlementList(page, pernum);
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

		
		$scope.selectAll = function () {
			$scope.stepCheckedValues = [];
			$scope.selectValue=0
			for (var i in $scope.itemsData) {
				$scope.itemsData[i].checked = $scope.select
				if ($scope.select) {
					$scope.stepCheckedValues.push($scope.itemsData[i].id);
					$scope.selectValue+=$scope.itemsData[i].pay_btc
				}
			}
		}
		//列表多选

		$scope.checkedPlat = function (checkModel, datas) {
			$scope.group2 = true;
			var list = datas;
			$scope.stepCheckedValues = [];
			$scope.selectValue=0
			checkModel.checked = !checkModel.checked;
			for (var i = 0; i < list.length; i++) {
				if (list[i].checked === true) {
					
					$scope.stepCheckedValues.push(list[i].id);
					$scope.selectValue+=$scope.itemsData[i].pay_btc
				}
			}

		}

		//模态框    创建账单
		$scope.postBill = function () {
			ngDialog.openConfirm({
				template: 'modalGroup',
				controller: 'GroupCtrls',
				className: 'ngdialog-theme-default'
			}).then(function () {
				$http.post(mpoolUI_url + '/bill/createBillNumber', $scope.stepCheckedValues).success(function (data) {
					if (data.msg == "ok") {
						//点击确认组操作按钮后禁用 该按钮
						$scope.getIncomeSettlementList($scope.p_current, $scope.p_pernum); //子账户列表
					}
				})
			})
		}

		//模态框    新增
		$scope.postAdd = function () {
			ngDialog.openConfirm({
				template: 'postAddModal',
				controller: 'postAddController',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				var json = {
					"username": value.username,
					"payBtc": value.payBtc,
					"desc": value.desc
				}
				$http.post(mpoolUI_url + '/bill/addPayBillInfo', json).success(function (data) {
					if (data.msg == "ok") {
						Notify.alert('新增成功！！！', {
							status: 'success'
						});
						$scope.getIncomeSettlementList($scope.p_current, $scope.p_pernum); //子账户列表
					} else {
						Notify.alert('新增失败!!!', {
							status: 'info'
						});
					}
				})
			})
		}

		//模态框    批量
		$scope.updateListPayBct = function () {
			ngDialog.openConfirm({
				template: 'updateListPayBctModal',
				controller: 'updateListPayBctController',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				$http.put(mpoolUI_url + '/bill/due/updateListPayBct?payBctRate=' + value).success(function (data) {
					if (data.msg == "ok") {
						if (data.msg == "ok") {
							Notify.alert('调整比率成功！！！', {
								status: 'success'
							});
							$scope.getIncomeSettlementList($scope.p_current, $scope.p_pernum); //子账户列表
						} else {
							Notify.alert('系统错误!!!', {
								status: 'info'
							});
						}

					}
				})
			})
		}


		//初始化
		$scope.init = function () {
			$scope.getIncomeSettlementList($scope.p_current, $scope.p_pernum); //子账户列表
		}
		$scope.init();

	}
])

//模态框  创建账单
App.controller('GroupCtrls', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {}
])

//模态框  新增
App.controller('postAddController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {

		$scope.subName = true;
		$scope.subPayBtc = true;
		$scope.subDesc = true;
		$scope.getUsername = function (name) {
			if (name == "") {
				$scope.nameCheck1 = "";
				$scope.nameCheck2 = "";
				$scope.subName = true;
			} else {
				$http.get(mpoolUI_url + '/account/check/username?username=' + name)
					.success(function (data) {
						if (data.data == true) {
							$scope.nameCheck1 = "用户名可用"
							$scope.nameCheck2 = ""
							$scope.subName = false;
						} else {
							$scope.nameCheck2 = "用户名不存在"
							$scope.nameCheck1 = ""
							$scope.subName = true;
						}
					})
			}
		}

		$scope.getPayBtc = function (payBtc) {
			if (payBtc == "") {
				$scope.payBtcCheck1 = "金额不能为空";
				$scope.payBtcCheck2 = "";
				$scope.subPayBtc = true;
			} else {
				$scope.payBtcCheck2 = "";
				$scope.payBtcCheck1 = "";
				$scope.subPayBtc = false;
			}
		}

		$scope.getDesc = function (desc) {
			if (desc == "") {
				$scope.descCheck1 = "选择新增类型";
				$scope.descCheck2 = "";
				$scope.subDesc = true;
			} else {
				$scope.descCheck2 = "";
				$scope.descCheck1 = "";
				$scope.subDesc = false;
			}
		}

	}
])

//模态框  批量
App.controller('updateListPayBctController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {

	}
])