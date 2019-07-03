//子账号管理列表
App.controller('ratioController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify', 'ngDialog',
	function ($scope, $http, $location, $state, $timeout, $stateParams, Notify, ngDialog) {

		//列表排序		
		var vm = $scope.vm = {};
		vm.sort = {
			column: '',
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
				label: '时间',
				name: 'day',
				type: 'string'
			},
			{
				label: '矿工费系数',
				name: 'ratio',
				type: 'string'
			},
			{
				label: '修正后',
				name: 'newRatio',
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
		$scope.getFppsRateAndUserTypeList = function (current, size) {
			var start = $scope.date1;
			var end = $scope.date5;
			var url = '/fee/miner/rate?current=' + current + '&size=' + size;
			if(start){
				var start2 = new Date(moment(start).valueOf()).toUTCString();
				url = url+'&strTime='+start2
			}
			if(end){
				var end2 = new Date(moment(end).valueOf()).toUTCString();
				url = url+'&endTime='+end2
			}
			$scope.show_items = false;
			$scope.idsRate = true;
			$http.get(mpoolUI_url + url)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						var time = data.data.day;
						
						$scope.itemsData = data.data.page.records;
						for(var i=0;i<$scope.itemsData.length;i++){
							var day = $scope.itemsData[i].day;
							if(day == time){
								$scope.itemsData[i].ids = 1;
								$scope.idsRate = false;
							}else{
								$scope.itemsData[i].ids = 0;
							}
						}
						$scope.items_num = $scope.itemsData.length;

						$scope.count = data.data.page.total; //总页数
						$scope.totalPage = data.data.page.pages; //页数
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
			$scope.getFppsRateAndUserTypeList(page, pernum);
		};
		//初始化页码  
		var reloadPno = function () {
			$scope.pages = calculateIndexes($scope.p_current, $scope.p_all_page, 7);
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
		
		//模态框   修改
		$scope.getRate = function () {
			//sessionStorage.setItem("ratio", ratio);
			ngDialog.openConfirm({
				template: 'rateModal',
				controller: 'rateController',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				var rate = value;
				$http.put(mpoolUI_url + '/fee/miner/rate?rate='+rate)
					.success(function (data) {
						if (data.msg == "ok") {
							Notify.alert('修改矿工费成功！！！', {
								status: 'success'
							});
							$scope.getFppsRateAndUserTypeList($scope.p_current, $scope.p_pernum); //主账户列表
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
			$scope.getFppsRateAndUserTypeList($scope.p_current, $scope.p_pernum); //主账户列表
		}
		$scope.init();
	}
])

//模态框  修改
App.controller('rateController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		
		//$scope.ratio = sessionStorage.getItem('ratio');
		
	}
])
	