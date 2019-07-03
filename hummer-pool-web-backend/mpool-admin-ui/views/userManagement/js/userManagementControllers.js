//用户管理
App.controller('userManagementController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify','ngDialog',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify,ngDialog) {

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
					label: '昨日收益',
					name: 'yesterday',
					type: 'string'
				},
				{
					label: '余额',
					name: 'totalDue',
					type: 'string'
				},
				{
					label: '总收益',
					name: 'totalRevenue',
					type: 'string'
				},
				{
					label: '累计转账',
					name: 'totalPaid',
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
				var url = '/account/master/list?current=' + current + '&size=' + size + '&descs=createTime';
				if(search){
					url = url + '&username=' + search
				}
				$scope.show_items = false;
				$http.get(mpoolUI_url + url)
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

			//修改费率方法
			$scope.updateFppsRate = function (userId, fppsRate) {
				$http.put(mpoolUI_url + '/account/updateFppsRate?userId=' + userId + '&fppsRate=' + fppsRate)
					.success(function (data) {
						if (data.msg == "ok") {

						} else {
							$scope.getUserManagementList($scope.p_current, $scope.p_pernum); //主账户列表
							var title1 = data.msg;
							Notify.alert(title1, {
								status: 'info'
							})
						}
					})
			}

			//用户账号详情
			$scope.getUserAccountDetails = function (obj) {
				$state.go('admin.userAccountDetails', {
					id: obj
				});
			}
			
			//模态框   重置密码
			$scope.getUpdatePassword = function (name) {
				sessionStorage.setItem("name", name);
				ngDialog.openConfirm({
					template: 'passwordModal',
					controller: 'passwordController',
					className: 'ngdialog-theme-default'
				}).then(function (value) {
					$http.put(mpoolUI_url + '/account/updatePasswordByUsername?username='+name+'&password='+value)
						.success(function (data) {
							if (data.msg == "ok") {
								Notify.alert('重置密码成功！！！', {
									status: 'success'
								});
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
				$scope.getUserManagementList($scope.p_current, $scope.p_pernum); //主账户列表
			}
			$scope.init();

		}
	])

//模态框  重置密码
App.controller('passwordController', ['$http', '$scope', 'ngDialog',
	function ($http, $scope, ngDialog) {
		
		$scope.name = sessionStorage.getItem('name');
		
	}
])

	//用户账号详情
	.controller('userAccountDetailsController', ['$scope', '$http', '$location', '$state', '$timeout', '$stateParams', 'Notify', 'ngDialog',
		function ($scope, $http, $location, $state, $timeout, $stateParams, Notify,ngDialog) {

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
					label: '昨日收益',
					name: 'yerterday',
					type: 'string'
				},
				{
					label: '余额',
					name: 'totalDue',
					type: 'string'
				},
				{
					label: '总收益',
					name: 'totalRevenue',
					type: 'string'
				},
				{
					label: '累计转账',
					name: 'totalPaid',
					type: 'string'
				},
				{
					label: '24小时算力',
					name: 'share24T',
					type: 'string'
				},
				{
					label: '30日算力',
					name: 'share30T',
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
			$scope.getSubaccountList = function (current, size) {
				$scope.show_items = false;
				$http.get(mpoolUI_url + '/account/member/list/page?masterUserId=' + id + '&current=' + current + '&size=' + size + '&descs=createTime')
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

			//修改子账户费率方法
			$scope.updateFppsRate2 = function (userId, fppsRate) {
				$http.put(mpoolUI_url + '/account/updateFppsRate?userId=' + userId + '&fppsRate=' + fppsRate)
					.success(function (data) {
						if (data.msg == "ok") {

						} else {
							$scope.getSubaccountList($scope.p_current, $scope.p_pernum); //子账户列表
							var title1 = data.msg;
							Notify.alert(title1, {
								status: 'info'
							})
						}
					})
			}


			//用户30天算力变化

			$scope.getUser30Day = function () {
				$http.get(mpoolUI_url + '/account/master/getUser30Day?userId=' + id).success(function (data) {
					var itemsData = data.data;
					itemsData.sort(function (a, b) {
						let value1 = a.day;
						let value2 = b.day;
						let d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
						return d == 0 ? 1 : d;
					});
					var result = itemsData.map(item => {
						let shareAccept = item.shareAccept || 0.0
						let shareReject = item.shareReject || 0.0
						// let rejectReta = (shareReject / (shareAccept + shareReject))*100 || 0
						let rejectRate = item.rejectRate || 0.0
						let day = moment(new Date(moment(item.day, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000'))).format('YYYY-MM-DD');
						return {
							hashAcceptT: parseFloat(shareAccept.toFixed(3)),
							rejectRate: parseFloat(rejectRate * 100).toFixed(2),
							day: day
						}
					})
					var dateList = []; //时间
					var shareList = []; //算力
					var rejectRateList = []
					result.forEach(element => {
						dateList.push(element.day)
						shareList.push(element.hashAcceptT)
						rejectRateList.push(element.rejectRate)
					});
					var maxValue = Math.max(...shareList)
					var myChart = echarts.init(document.getElementById('chart_power_30d'));
					var option = {
						color: ['#169f85', '#E8BD8F'],
						visualMap: [{
							show: false,
							type: 'continuous',
							seriesIndex: 1,
							dimension: 0,
							min: 0,
							max: dateList.length - 1
						}],
						title: [{
							left: 'center'
						}],
						tooltip: {
							trigger: 'axis',
							formatter: function(params) {
					            return params[0].name + '<br/>'
					                   + params[0].seriesName + ' : ' + params[0].value + '<br/>'
					                   + params[1].seriesName + ' : ' + params[1].value + ' %';
					        }
						},
						legend: {
							data: ['1天平均算力(T)','拒绝率']
						},
						xAxis: [{
							data: dateList,
							axisTick: false,
							boundaryGap: false
						}],
						yAxis: [{
							name: '1天平均算力（T）',
							type: 'value',
							position: 'left',
							splitNumber: 5,
							min: 0,
							max: parseInt(maxValue+maxValue*0.2),
							splitLine: {
								show: false
							}
						},
						{
							name: '拒绝率',
							type: 'value',
							position: 'right',
							splitNumber: 5,
							min: 0,
							max: 10,
							axisLabel : {
				                formatter: '{value}%'
				            },
							splitLine: {
								show: false
							}
						}],
						grid: [{
							top: '20%',
							bottom: '10%',
							right: '5%',
							left: '10%'
						}],
						series: [
							{
								name: '1天平均算力(T)',
								type: 'line',
								yAxisIndex:0,
								smooth: true,
								data: shareList
							},
							{
								name: '拒绝率',
								type: 'line',
								smooth: true,
								yAxisIndex:1,
								splitLine: {
									show: false
								},
								data: rejectRateList
							}
						]
					};
					myChart.setOption(option);
					myChart.resize();
				})
			}
			
			//
			$scope.getUser24H = function(id){
				sessionStorage.setItem("id", id);
				ngDialog.openConfirm({
					template: 'User24H',
					controller: 'User24HController',
					className: 'ngdialog-theme-default',
					width:700
				}).then(function (value) {})
			}

			//初始化
			$scope.init = function () {
				$scope.getSubaccountList($scope.p_current, $scope.p_pernum); //子账户列表
				$scope.getUser30Day() //获得用户30天算力曲线图
			}
			$scope.init();

			//返回
			$scope.back = function () {
				$location.path('admin/account_user');
			}
		}
	])
	
//详情
App.controller('User24HController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {
		
		var id = sessionStorage.getItem('id');
		$scope.getUser24H = function(){
			// 从后端获取数据
			var dateList = []
			var shareList = []
			var listRate = []
			var shareInfo = {
				shareMax: 0
			};
			$http.get(mpoolUI_url + '/dashboard/getUser24H?userId=' + id)
				.success(function (data) {
					var itemsData = data.data;
					itemsData.sort(function (a, b) {
						let value1 = a.hour;
						let value2 = b.hour;
						let d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
						return d == 0 ? 1 : d;
					});
					var result = itemsData.map(item => {
						let hashAcceptT = item.hashAcceptT || 0
						let hashRejectT = item.hashRejectT || 0
						let rejectRate = item.rejectRate || 0
						let hour = moment(new Date(moment(item.hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000'))).format('HH:mm')

						return {
							hashAcceptT: parseFloat(hashAcceptT.toFixed(3)),
							rejectRate: parseFloat(rejectRate * 100).toFixed(2),
							hour: hour
						}
					})
					result.forEach(item => {
						dateList.push(item.hour)
						shareList.push(item.hashAcceptT)
						listRate.push(item.rejectRate)
					})
					let shareMax = Math.max(...shareList)
					var myChart = echarts.init(document.getElementById('chart_power_24T'));
					var option = {
						color: ['#169f85', '#E8BD8F'],
						visualMap: [{
							show: false,
							type: 'continuous',
							seriesIndex: 1,
							dimension: 0,
							min: 0,
							max: dateList.length - 1
						}],
						title: [{
							left: 'center'
						}],
						tooltip: {
							trigger: 'axis',
							formatter: function(params) {
					            return params[0].name + '<br/>'
					                   + params[0].seriesName + ' : ' + params[0].value + '<br/>'
					                   + params[1].seriesName + ' : ' + params[1].value + ' %';
					        }
						},
						legend: {
							data: ['小时平均算力(T)','拒绝率']
						},
						xAxis: [{
							data: dateList,
							axisTick: false,
							boundaryGap: false
						}],
						yAxis: [{
							name: '小时平均算力（T）',
							type: 'value',
							position: 'left',
							splitNumber: 5,
							min: 0,
							max: parseInt(shareMax + shareMax * 0.2),
							splitLine: {
								show: false
							}
						},
						{
							name: '拒绝率',
							type: 'value',
							position: 'right',
							splitNumber: 5,
							min: 0,
							max: 10,
							axisLabel : {
				                formatter: '{value}%'
				            },
							splitLine: {
								show: false
							}
						}],
						grid: [{
							top: '20%',
							bottom: '10%',
							right: '5%',
							left: '10%'
						}],
						series: [
							{
								name: '小时平均算力(T)',
								type: 'line',
								smooth: true,
								yAxisIndex:0,
								data: shareList
							},
							{
								name: '拒绝率',
								type: 'line',
								smooth: true,
								yAxisIndex:1,
								splitLine: {
									show: false
								},
								data: listRate
							}
						]
					};
					myChart.setOption(option);
					myChart.resize();
				})
		}

		//24小时在线矿工数
		$scope.getWorker24Online = function () {
			$scope.hourData = [];
			$scope.on_lineData = [];
			$http.get(mpoolUI_url + '/dashboard/getUser24HWorkers?userId=' + id)
				.success(function (data) {
					var itemsData = data.data;
					//排序
					itemsData.sort(function (a, b) {
						var value1 = a.hour;
						var value2 = b.hour;
						var d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
						return d == 0 ? 1 : d;
					});
					for (var i in itemsData) {
						if (!itemsData[i].count) {
							itemsData[i].count = 0;
						}
					}
					if (itemsData.length > 0) {
						for (var i = 0; i < itemsData.length; i++) {
							$scope.hourData[i] = itemsData[i].hour;
							$scope.on_lineData[i] = itemsData[i].count;
							var start = moment(moment(itemsData[i].hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
							var end = moment(moment(itemsData[i].hour, 'YYYYMMDDHH').add('hour', 1).format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
							$scope.hourData[i] = start + "-" + end;
						}
					}
					var max = eval("Math.max(" + $scope.on_lineData.toString() + ")"); //求Y轴最大值
					if (max == 0 || max < 5) {
						$scope.minValue1 = 0;
						$scope.maxValue1 = 5;
					} else {
						$scope.minValue1 = 0;
						$scope.maxValue1 = max;
					}
					$scope.chart_worker_24h(); //24小时在线矿工数   折线图
				}).error(function (data, status, headers, config) {
					$scope.hourData = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00'];
					$scope.on_lineData = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'];
					$scope.chart_worker_24h(); //24小时在线矿工数   折线图
				});
		}
		//24小时在线矿工数   折线图
		$scope.chart_worker_24h = function () {

			var myChart = echarts.init(document.getElementById('chart_worker_24h'));
			var option = {
				visualMap: [{
					show: false,
					type: 'continuous',
					seriesIndex: 1,
					dimension: 0
				}],
				color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],

				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: ['台'],
					x: 'center'
				},
				xAxis: [{
					data: $scope.hourData,
					axisTick: false,
					boundaryGap: false,
					splitLine: {
						show: false
					},
					axisLabel: {
						formatter: function (str) {
							var idx = str.indexOf('-');
							if (idx > 0) {
								var t = str.split('-')[0];
								return t.trim();
							} else {
								return str;
							}
						}
					}
				}],
				yAxis: [{
					name: '台',
					nameTextStyle: {
						fontSize: 12,
						color: '#121212',
						align: 'left'
					},
					type: 'value',
					splitNumber: 5,
					min: $scope.minValue1,
					max: $scope.maxValue1,
					position: 'left',
					splitLine: {
						show: true,
						lineStyle: {
							type: 'dashed'
						}
					}
				}],
				grid: [{
					top: '20%',
					bottom: '10%',
					right: '5%',
					left: '10%'
				}],
				series: [{
					name: '台',
					type: 'line',
					smooth: true,
					showSymbol: false,
					itemStyle: {
						normal: {
							color: "#169f85"
						}
					},
					splitLine: {
						show: false
					},
					data: $scope.on_lineData
				}]
			};
			myChart.setOption(option);
			myChart.resize();

			window.addEventListener("resize", function () {
				myChart.resize();
			});
		}


		//获取用户30天 算力
		$scope.getUser30Day = function () {
			$http.get(mpoolUI_url + '/dashboard/getUser30Share?userId=' + id).success(function (data) {
					var itemsData = data.data;
					//排序
					itemsData.sort(function (a, b) {
						var value1 = a.day;
						var value2 = b.day;
						var d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
						return d == 0 ? 1 : d;
					});
					$scope.result = itemsData.map(function(item) {
						var hashAcceptT = item.hashAcceptT || 0;
						var hashRejectT = item.hashRejectT || 0;
						var date = moment(moment(item.day, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('YYYY-MM-DD');
						var rejectRate = hashRejectT / (hashAcceptT + hashRejectT) * 100 || 0
						return {
							date: date,
							hashAcceptT: hashAcceptT,
							rejectRate: rejectRate
						};
					})
					var hashAcceptTList = [];
					var hashRejectTList = [];
					var dateList = [];
					$scope.result.forEach(function(item) {
						hashAcceptTList.push(item.hashAcceptT.toFixed(3))
						hashRejectTList.push(item.rejectRate.toFixed(3))
						dateList.push(item.date)
					})
					$scope.dashboardItems(dateList, hashRejectTList, hashAcceptTList); //获取用户30天 算力  折线图

				}).error(function (data, status, headers, config) {
					var dateList1 = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00']; //时间
					var rateList1 = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //小时拒绝率
					var valueList2 = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //算力
					$scope.dashboardItems(dateList1, rateList1, valueList2);
				});
		}
		//获取用户30天 算力  折线图
		$scope.dashboardItems = function (dateList, rateList, valueList) {
			var max = eval("Math.max(" + valueList.toString() + ")"); //求Y轴最大值
			var maxs = parseInt(max);
			if (maxs == 0 || maxs < 5) {
				$scope.minValue2 = 0;
				$scope.maxValue2 = 5;
			} else {
				$scope.minValue2 = 0;
				$scope.maxValue2 = maxs + 20;
			}
			var myChart = echarts.init(document.getElementById('chartsub_power_30d'));
			var option = {
				visualMap: [{
					show: false,
					type: 'continuous',
					seriesIndex: 1,
					dimension: 0,
					min: 0,
					max: dateList.length - 1
				}],			
					color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],

				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: ['1天平均算力(T)', '日拒绝率(%)'],
					x: 'center'
				},
				xAxis: [{
					data: dateList,
					axisTick: false,
					boundaryGap: false,
					splitLine: {
						show: false
					}
				}],
				yAxis: [{
					name: '算力(T)',
					nameTextStyle: {
						fontSize: 12,
						color: '#121212',
						align: 'left'
					},
					type: 'value',
					position: 'left',
					splitNumber: 5,
					min: $scope.minValue2,
					max: $scope.maxValue2,
					splitLine: {
						show: true,
						lineStyle: {
							type: 'dashed'
						}
					}
				}, {
					name: '拒绝率（%）',
					min: 0,
					max: 10,
					splitNumber: 5,
					splitLine: {
						show: false
					}
				}],
				grid: [{
					top: '20%',
					bottom: '10%',
					right: '5%',
					left: '10%'
				}],
				series: [{
						name: '1天平均算力(T)',
						type: 'line',
						smooth: true,
						showSymbol: false,
						data: valueList
					},
					{
						name: '日拒绝率(%)',
						type: 'line',
						smooth: true,
						showSymbol: false,
						splitLine: {
							show: false
						},
						yAxisIndex: 1,
						data: rateList
					}
				]
			};
			myChart.setOption(option);
			myChart.resize();

			window.addEventListener("resize", function () {
				myChart.resize();
			});
		}

		
		$scope.init = function(){
			$scope.getUser24H();
			$scope.getWorker24Online();
			$scope.getUser30Day()
		}
		$scope.init();
		
	}
])