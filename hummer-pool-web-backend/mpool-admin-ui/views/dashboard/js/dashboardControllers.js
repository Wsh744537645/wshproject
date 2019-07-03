//用户面板
App.controller('dashboardController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$interval', '$rootScope', 'ngDialog',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $interval, $rootScope, ngDialog) {
		$http.get(mpoolUI_url + '/auth/cur/currencyName').success(function(r) {
			$scope.currencyName = r.data.currencyName
			$http.get(mpoolUI_url + '/auth/getCurrencyList').success(function (r) {
				angular.forEach(r.data, (d)=> {
					if($scope.currencyName == d.type) {
						$scope.unit = d.unit
					}
				})
			})
		})
		//当前pool算力与矿工活跃数
		$scope.getCurrentPoolInfo = function () {
			$http.get(mpoolUI_url + '/dashboard/currentPoolInfo').success(function (data) {
				if (data.msg == "ok") {
					$scope.dataItems = data.data;
				} else {
					$scope.getError();
				}
			}).error(function (data, status, headers, config) {
				$scope.getError();
			})
		}
		$scope.getError = function () {
			$scope.dataItems = {
				activeCount: 0,
				currentShare: 0,
				block: 0,
				revenue: 0
			}
		}
		
		
		$scope.getCurrentCoin = function(){
			$http.get(mpoolUI_url + '/recommend/getHistoryApartCoin?current=1&size=1',{})
				.success(function (data) {
					$scope.feeSum = data.data.feeSum;
				})
			$http.get(mpoolUI_url + '/recommend/getCurrentCoin')
				.success(function (data) {
					
					for(var i in data.data){
						$scope.coin = data.data[i]
					}
				})
		}

		//过去30 天池算力变化 
		$scope.getPast30DayShare = function () {
			$http.get(mpoolUI_url + '/dashboard/past30DayShare')
				.success(function (data) {
					if (data.msg == "ok") {
						var itemsData = data.data;
						itemsData.sort(function (a, b) {
							let value1 = a.day;
							let value2 = b.day;
							let d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
							return d == 0 ? 1 : d;
						});
						var result = itemsData.map(item => {
							let hashAcceptT = item.hashAcceptT || 0
							let hashRejectT = item.hashRejectT || 0
							// let rejectReta = (hashRejectT / (hashAcceptT + hashRejectT) )*100|| 0
							let rejectRate = item.rejectRate || 0
							let day = moment(new Date(moment(item.day, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000'))).format('YYYY-MM-DD');
							return {
								hashAcceptT: hashAcceptT.toFixed(3),
								rejectRate: (parseFloat(rejectRate) * 100).toFixed(2),
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

						$scope.dashboardItems(dateList, rejectRateList, shareList); //过去30 天池算力变化 
					}
				}).error(function (data, status, headers, config) {
					var dateList = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00']; //时间
					var avgList = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //小时平均算力
					var shareList = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //算力
					$scope.dashboardItems(dateList, avgList, shareList); //过去30 天池算力变化 
				});
		}
		//过去30 天池算力变化   折线图
		$scope.dashboardItems = function (dateList, avgList, shareList) {
			var max = eval("Math.max(" + shareList + ")"); //求Y轴最大值
			var maxs = parseInt(max);
			if (maxs == 0 || maxs < 5) {
				$scope.maxValue2 = 5;
			} else {
				$scope.maxValue2 = maxs + 20;
			}
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
					max: $scope.maxValue2,
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
						yAxisIndex:1,
						smooth: true,
						splitLine: {
							show: false
						},
						data: avgList
					}
				]
			};
			myChart.setOption(option);
			myChart.resize();
		}

		//过去30 天池矿机活跃数
		$scope.getPast30DayWorker = function () {
			$http.get(mpoolUI_url + '/dashboard/past30DayWorker')
				.success(function (data) {
					if (data.msg == "ok") {
						var itemsData = data.data;
						itemsData.sort(function (a, b) {
							let value1 = a.day;
							let value2 = b.day;
							let d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
							return d == 0 ? 1 : d;
						});
						for (let i in itemsData) {
							itemsData[i].day = moment(new Date(moment(itemsData[i].day, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000'))).format('YYYY-MM-DD');
						}
						var dateList = []; //时间
						var workerCountList = []; //日拒绝率
						for (var i = 0; i < itemsData.length; i++) {
							dateList[i] = itemsData[i].day;
							workerCountList[i] = itemsData[i].count;
						}
						$scope.past30DayWorkerItems(dateList, workerCountList); //过去30 天池矿机活跃数
					}
				}).error(function (data, status, headers, config) {
					var dateList = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00']; //时间
					var workerCountList = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //小时平均算力

					$scope.past30DayWorkerItems(dateList, workerCountList); //过去30 天池矿机活跃数
				});
		}
		//过去30 天池算力变化   折线图
		$scope.past30DayWorkerItems = function (dateList, workerCountList) {
			var max = eval("Math.max(" + workerCountList + ")"); //求Y轴最大值
			var maxs = parseInt(max);
			if (maxs == 0 || maxs < 5) {
				$scope.maxValue2 = 5;
			} else {
				$scope.maxValue2 = maxs + 20;
			}
			var myChart = echarts.init(document.getElementById('past30DayWorker'));
			var option = {
				color: ['#169f85'],
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
					trigger: 'axis'
				},
				legend: {
					data: ['活跃数(个)']
				},
				xAxis: [{
					data: dateList,
					axisTick: false,
					boundaryGap: false
				}],
				yAxis: [{
					name: '活跃数(个)',
					type: 'value',
					position: 'left',
					splitNumber: 5,
					min: 0,
					max: $scope.maxValue2,
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
					name: '活跃数(个)',
					type: 'line',
					smooth: true,
					splitLine: {
						show: false
					},
					data: workerCountList
				}]
			};

			myChart.setOption(option);
			myChart.resize();
		}

		//过去24小时 天池算力变化   折线图
		$scope.past24HWorkerItems = function () {
			// 从后端获取数据
			var dateList = []
			var shareList = []
			var listRate = []
			var shareInfo = {
				shareMax: 0
			};

			$http.get(mpoolUI_url + '/dashboard/past24HShare')
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
						// let rejectReta = (hashRejectT / (hashAcceptT + hashRejectT))*100 || 0
						let rejectRate = item.rejectRate || 0
						let hour = moment(new Date(moment(item.hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000'))).format('HH:mm')

						return {
							hashAcceptT: parseFloat(hashAcceptT.toFixed(3)),
							rejectRate: (parseFloat(rejectRate)*100).toFixed(2),
							hour: hour
						}
					})
					result.forEach(item => {
						dateList.push(item.hour)
						shareList.push(item.hashAcceptT)
						listRate.push(item.rejectRate)
					})
					let shareMax = Math.max(...shareList)
					var myChart = echarts.init(document.getElementById('chart_power_24h'));
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
								yAxisIndex:0,
								smooth: true,
								data: shareList
							},
							{
								name: '拒绝率',
								type: 'line',
								yAxisIndex:1,
								smooth: true,
								data: listRate
							}
						]
					};
					myChart.setOption(option);
					myChart.resize();
				})
		}

		$scope.showHistoryBlock = function () {
			ngDialog.openConfirm({
				template: 'showHistoryBlock',
				controller: 'showHistoryBlockController',
				className: 'ngdialog-theme-default',
				width:700
			}).then(function (value) {

			})
		}
		
		//历史分币记录
		$scope.getHistoryApartCoin = function(){
			ngDialog.openConfirm({
				template: 'HistoryApartCoin',
				controller: 'HistoryApartCoinController',
				className: 'ngdialog-theme-default',
				width:700
			}).then(function (value) {

			})
		}
		
		//24小时算力
		$scope.get24HoursData = function(){
			ngDialog.openConfirm({
				template: '24HoursID',
				controller: '24HoursIDController',
				className: 'ngdialog-theme-default',
				width:700
			}).then(function (value) {

			})
		}
		
		$scope.init = function () {
			$scope.getCurrentCoin();
			$scope.getCurrentPoolInfo(); //当前pool算力与矿工活跃数
			$scope.getPast30DayShare(); //过去30 天池算力变化 
			$scope.getPast30DayWorker(); //过去30 天池矿机活跃数
			$scope.past24HWorkerItems() //过去24h矿池算力变化
		}
		$scope.init();
	}
])

//历史总爆块
App.controller('showHistoryBlockController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'username',
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
        	{label: '块高度',			name: 'username',	type: 'string'},
        	{label: '块时间',			name: 'roleName',	type: 'string'},
			{label: 'hash',			name: 'operation',	type: 'string'},
			{label: '确认个数',			name: 'operation',	type: 'string'},
			{label: '奖励',			name: 'operation',	type: 'string'},
			{label: '体积',			name: 'operation',	type: 'string'}
		];
		
		$scope.currentSelect = {
			value: '20',
		}
		$scope.p_pernum = 20;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getHistoryBlockList = function (current, size) {
			var start = $scope.date1;
			var end = $scope.date5;
			var url = '/dashboard/getHistoryBlockList?current=' + current + '&size=' + size;
			$scope.url2 = '/apis/dashboard/exportHistoryBlockList?a=1';
			
			date={}
			if(start){
				$scope.start2 = new Date(moment(start).valueOf()).toUTCString();
				date.strTime = $scope.start2;
				$scope.url2 += '&strTime='+$scope.start2;
			}
			if(end){
				$scope.end2 = new Date(moment(end).valueOf()).toUTCString();
				
				date.endTime=$scope.end2
				$scope.url2 += '&endTime='+$scope.end2;
				
			}
			$scope.show_items = false;
			$http.post(mpoolUI_url + url,date).success(function (data) {
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
			$scope.getHistoryBlockList(page, pernum);
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
		
		//  
		$scope.getHistoryBlockRewardsSum = function(){

			date={}
			if($scope.date1){
				var start2 = new Date(moment($scope.date1).valueOf()).toUTCString();
				date.strTime=start2
			}
			if($scope.date5){
				var end2 = new Date(moment($scope.date5).valueOf()).toUTCString();
				
				date.endTime=end2
			}
			$http.post(mpoolUI_url + '/dashboard/getHistoryBlockRewardsSum',date).success(function (data) {
				$scope.dataValue = data.data;
			})
		}
		
		$scope.init = function(){
			$scope.getHistoryBlockList($scope.p_current, $scope.p_pernum); //主账户列表
			$scope.getHistoryBlockRewardsSum();
		}
		$scope.init();
	}
])



//历史分币记录
App.controller('HistoryApartCoinController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'day',
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
        	{label: '打币时间',			name: 'day',	type: 'string'},
        	{label: '当日分币总数',			name: 'feeSum',	type: 'string'},
			{label: '当日推荐奖励',			name: 'recommendFeeSum',	type: 'string'}
		];
		
		$scope.currentSelect = {
			value: '20',
		}
		$scope.p_pernum = 20;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getHistoryApartCoinList = function (current, size) {
			var start = $scope.date1;
			var end = $scope.date5;
			var url = '/recommend/getHistoryApartCoin?current=' + current + '&size=' + size;
			$scope.url2 = '/apis/recommend/exportHistoryApartCoinList?a=1';
			date={}
			if(start){
				var start2 = new Date(moment(start).valueOf()).toUTCString();
				date.strTime=start2
				url=url+'&strTime='+start2;
				$scope.url2 += '&strTime='+start2;
			}
			if(end){
				var end2 = new Date(moment(end).valueOf()).toUTCString();
				date.endTime=end2
				url=url+'&endTime='+end2;
				$scope.url2 += '&endTime='+end2;
			}
			$scope.show_items = false;
			$http.post(mpoolUI_url + url,date).success(function (data) {
				if (data.msg == "ok") {
					$timeout(function () {
						$scope.show_items = true;
					}, 800);
					
					$scope.feeSum = data.data.feeSum;
					$scope.recommendFeeSum = data.data.recommendFeeSum;
					
					var page=data.data.page
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
			$scope.getHistoryApartCoinList(page, pernum);
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
		$scope.init = function(){
			$scope.getHistoryApartCoinList($scope.p_current, $scope.p_pernum); //主账户列表
		}
		$scope.init();
	}
])


//24小时算力记录
App.controller('24HoursIDController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$window', '$stateParams', 'Notify',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $window, $stateParams, Notify) {
		//列表排序		
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'createdAt',
            direction: 1,
            toggle: function (column) {
            	if(column.name == "operation"){
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
        	{label: '时间',			name: 'createdAt',			type: 'string'},
        	{label: '24小时算力(P)',		name: 'currentHashRate',	type: 'string'},
			{label: '全网算力(P)',		name: 'hashRate',			type: 'string'},
			{label: '全网爆块',		name: 'blocks',				type: 'string'},
			{label: '全网算力占比',		name: 'rate',				type: 'string'}
		];
		
		
		$scope.currentSelect = {
			value: '20',
		}
		//用户列表主账户
		$scope.p_pernum = 20;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getBlockchainInfoList = function (current, size) {
			var start = $scope.date1;
			var end = $scope.date5;
			var url = '/dashboard/getBlockchainInfo?current=' + current + '&size=' + size;
			$scope.url2 = '/apis/dashboard/getBlockchainInfo/export?a=1';
			if(start){
				var start2 = new Date(moment(start).valueOf()).toUTCString();
				url = url+'&strTime='+start2;
				$scope.url2 += '&strTime='+start2;
			}
			if(end){
				var end2 = new Date(moment(end).valueOf()).toUTCString();
				url = url+'&endTime='+end2;
				$scope.url2 += '&endTime='+end2;
			}
			$scope.show_items = false;
			$http.get(mpoolUI_url + url)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						$scope.itemsData = data.data.records;
						for(var i=0;i<$scope.itemsData.length;i++){
							var day = $scope.itemsData[i].day;
							$scope.itemsData[i].day = new Date(moment(day, 'YYYYMMDD').valueOf());
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
			$scope.getBlockchainInfoList(page, pernum);
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
		
		$scope.init = function () {
			$scope.getBlockchainInfoList($scope.p_current, $scope.p_pernum); //主账户列表
		}
		$scope.init();
		
		
		
	}
])