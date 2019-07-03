//挖矿费率设置
App.controller('pool_fppsController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify', 'ngDialog',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify, ngDialog) {

			//列表排序		
			var vm = $scope.vm = {};
			vm.sort = {
				column: 'username',
				direction: 1,
				toggle: function (column) {
					if (column.name == "operation") {
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
					label: '主账户名',
					name: 'username',
					type: 'string'
				},
				{
					label: '费率',
					name: 'fpps_rate ',
					type: 'string'
				},
				{
					label: '挖矿模式',
					name: 'type',
					type: 'string'
				},
				{
					label: '类型',
					name: 'fppsRate',
					type: 'string'
				},
				{
					label: '推荐人',
					name: 'recommend_username',
					type: 'string'
				},
				{
					label: '推荐费率',
					name: 'fee_rate',
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
			$scope.getUserFppsList = function (current, size) {
				var name = $scope.recommendName;
				var username = $scope.username;
				var group = $scope.group;
				var url = '/account/user/fpps/list?current=' + current + '&size=' + size;
				$scope.url2 = '/apis/account/user/fpps/list/export?a=1';
				if(name){
					url = url+'&recommendName='+name
					$scope.url2 += '&recommendName='+name
				}
				if(username){
					url = url+'&username='+username
					$scope.url2 += '&username='+username
				}
				if(group){
					url = url+'&group='+group
					$scope.url2 += '&group='+group
				}
				$scope.show_items = false;
				$http.get(mpoolUI_url + url)
					.success(function (data) {
						if (data.msg == "ok") {
							$timeout(function () {
								$scope.show_items = true;
							}, 800);
							var page =data.data.page;
							$scope.itemsData = page.records;
							$scope.items_num = $scope.itemsData.length;

							$scope.count = page.total; //总页数
							$scope.totalPage = page.pages; //页数
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
				$scope.getUserFppsList(page, pernum);
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
			
			//模态框   推荐人
			$scope.getCreatRecommendUser = function (id) {
				ngDialog.openConfirm({
					template: 'creatRecommendUserModal',
					controller: 'creatRecommendUserController',
					className: 'ngdialog-theme-default'
				}).then(function (value) {
					var json ={
						"recommendName": value.recommendName,
						"feeRate": value.feeRate,
						"userId": id
					}
					$http.post(mpoolUI_url + '/recommend/creatRecommendUser', json)
						.success(function (data) {
							if (data.msg == "ok") {
								Notify.alert('推荐人成功！！！', {
									status: 'success'
								});
								$scope.getUserFppsList($scope.p_current, $scope.p_pernum); //主账户列表
							}
						}).error(function (data, status, headers, config) {
							Notify.alert('系统错误!!!', {
								status: 'info'
							});
						});
				})
			}
			
			//模态框修改矿池费率
			$scope.updatePoolRate = function () {
				ngDialog.openConfirm({
					template: 'poolRateModal',
					controller: 'poolRateController',
					className: 'ngdialog-theme-default'
				}).then(function (value) {
					$http.get(mpoolUI_url + '/pool/rate/update?poolRate=' +value)
						.success(function (data) {
							if (data.msg == "ok") {
								Notify.alert('修改矿池费率成功！！！', {
									status: 'success'
								});
								$scope.getUserFppsList($scope.p_current, $scope.p_pernum); //主账户列表
							}
						}).error(function (data, status, headers, config) {
							Notify.alert('系统错误!!!', {
								status: 'info'
							});
						});
				})
			}
			
			//修改用户费率
			$scope.updateFppsRate = function (name ,id) {
				sessionStorage.setItem("name", name);
				ngDialog.openConfirm({
					template: 'updateFppsRateModal',
					controller: 'updateFppsRateController',
					className: 'ngdialog-theme-default'
				}).then(function (value) {
					$http.put(mpoolUI_url + '/account/updateFppsRate?userId='+id+'&fppsRate='+value)
						.success(function (data) {
							if (data.msg == "ok") {
								Notify.alert('修改用户费率成功！！！', {
									status: 'success'
								});
								$scope.getUserFppsList($scope.p_current, $scope.p_pernum); //主账户列表
							}
						}).error(function (data, status, headers, config) {
							Notify.alert('系统错误!!!', {
								status: 'info'
							});
						});
				})
			}


			//初始化
			$scope.init = function () {
				$scope.getUserFppsList($scope.p_current, $scope.p_pernum); //主账户列表
			}
			$scope.init();

		}
	])


//模态框  推荐人
App.controller('creatRecommendUserController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		
	}
])

//模态框  修改矿池费率
App.controller('poolRateController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		
	}
])
//模态框  修改用户费率
App.controller('updateFppsRateController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		$scope.name = sessionStorage.getItem('name');
	}
])
