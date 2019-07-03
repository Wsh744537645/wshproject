//
App.controller('account_infoController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify','ngDialog',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify,ngDialog) {

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
					label: '主账户/子账户',
					name: 'username',
					type: 'string'
				},
				{
					label: '注册时间',
					name: 'currentShare',
					type: 'string'
				},
				{
					label: '最后一次登录时间',
					name: 'pastDayShare',
					type: 'string'
				},
				{
					label: '注册手机',
					name: 'yesterday',
					type: 'string'
				},
				{
					label: '注册邮箱',
					name: 'totalDue',
					type: 'string'
				},
				{
					label: '账户类型',
					name: 'totalRevenue',
					type: 'string'
				}
			];
			$scope.filter = function () {
				$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
			};

			//展示用户管理
			$scope.currentSelect = {
				value: '20',
			}
			
			//用户管理列表
			$scope.p_pernum = 20;
			$scope.p_current = 1;
			$scope.p_all_page = 0;
			$scope.pages = [];
			$scope.show_items = true;
			$scope.getUserManagementList = function (current, size) {
				var search = $scope.search;
				var url = '/account/getMasterUserInfoList?current=' + current + '&size=' + size + '&descs=createTime';
				$scope.url2 = '/apis/account/exportMasterUserInfoList'
				var json ={
					
				}
				if(search){
					$scope.url2 += '?username=' + search;
					json.username=search;
				}
				
				$scope.show_items = false;
				$http.post(mpoolUI_url + url,json)
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
				$scope.getUserManagementList(page, pernum);
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
				$scope.getUserManagementList($scope.p_current, $scope.p_pernum); //主账户列表
			}
			$scope.init();

		}
	])
