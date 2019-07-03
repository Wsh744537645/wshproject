//用户面板
App.controller('sondashboardController', ['$scope', '$http', '$location', '$translate', '$state', '$timeout', '$window', 'ngDialog', 'Notify', '$interval', '$rootScope',
	function ($scope, $http, $location, $translate, $state, $timeout, $window, ngDialog, Notify, $interval, $rootScope) {
		$scope.currencyName = sessionStorage.getItem('currencyName')
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			angular.forEach(r.data, (d)=> {
				if($scope.currencyName == d.type) {
					$scope.unit = d.unit
				}
			})
		})
		//用户列表
		$scope.getSubRuntimeInfo = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getSubRuntimeInfo')
				.success(function (data) {
					if (data.msg == "ok") {
						$scope.items_Data = data.data;
					}
				}).error(function (data, status, headers, config) {

				});
		}
		
		$scope.getpoolNode = function() {
			$scope.northNode = []
			$scope.southNode = []
			$http.get(mpoolUI_url + '/user/dashbaord/getPoolNodes?currencyName=' + $scope.currencyName).success(function (r) {
				angular.forEach(r.data.north, (adr)=> {
					$scope.northNode.push({adr: adr.address, order: adr.order})
				})
				angular.forEach(r.data.south, (adr)=> {
					$scope.southNode.push({adr: adr.address, order: adr.order})
				})
				$scope.discr = r.data.north[0].discrible
			})
		}
		$interval(function () {
			$scope.getSubRuntimeInfo()
		}, 30000, -1);

		//获取用户24小时 算力
		$scope.getUser24H = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getUser24H')
				.success(function (data) {
					var itemsData = data.data;

					var total = 0;
					$scope.result = [];
					if (data.msg == "ok" && !!itemsData) {
						itemsData.sort(function (a, b) {
							var value1 = a.hour;
							var value2 = b.hour;
							var d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
							return d == 0 ? 1 : d;
						});

						$scope.result = itemsData.map(function(item) {
							var hashAcceptT = item.hashAcceptT || 0;
							var hashRejectT = item.hashRejectT || 0;
							var start = moment(moment(item.hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
							var end = moment(moment(item.hour, 'YYYYMMDDHH').add('hour', 1).format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
							var rejectRate = hashRejectT / (hashAcceptT + hashRejectT) * 100 || 0
							return {
								date: start + "-" + end,
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
						$scope.get24HLine(dateList, hashRejectTList, hashAcceptTList);
					}
				}).error(function (data, status, headers, config) {
					var dateList1 = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00']; //时间

					var rateList1 = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //小时拒绝率
					var valueList2 = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //算力
					$scope.get24HLine(dateList1, rateList1, valueList2); //24小时算力折线图
				});
		}
		//24小时算力折线图
		$scope.get24HLine = function (dateList, rateList, valueList) {
			var maxs = eval("Math.max(" + valueList.toString() + ")"); //求Y轴最大值
			var max = parseInt(maxs);
			if (max == 0 || max < 5) {
				$scope.minValue = 0;
				$scope.maxValue = 5;
			} else {
				$scope.minValue = 0;
				$scope.maxValue = max + 10;
			}

			var myChart1 = echarts.init(document.getElementById('chart_power_24h'));
			var option = {
				visualMap: [{
					show: false,
					type: 'continuous',
					seriesIndex: 1,
					dimension: 0,
					min: 0,
					max: dateList.length + 1
				}],
				color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],
			
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: [$translate.instant('sharepage.1hhs') +'(T)', $translate.instant('sharepage.1hrj') +'(%)'],
					x: 'left'
				},
				xAxis: [{
					data: dateList,
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
					name:  $translate.instant('dashboard.Hashrate') +'(T)',
					nameTextStyle: {
						fontSize: 12,
						color: '#121212',
						align: 'left'
					},
					type: 'value',
					position: 'left',
					splitNumber: 5,
					min: $scope.minValue,
					max: $scope.maxValue,
					splitLine: {
						show: true,
						lineStyle: {
							type: 'dashed'
						}
					}
				}, {
					name: $translate.instant('sharepage.rj') +'(%)',
					min: 0,
					max: 10,
					splitNumber: 5,
					splitLine: {
						show: false
					}
				}],
				grid: [{
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
				}],
				series: [{
					name: $translate.instant('sharepage.1hhs') +'(T)',
						type: 'line',
						smooth: true,
						showSymbol: false,
						data: valueList
					},
					{
						name: $translate.instant('sharepage.1hrj') +'(%)',
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
			myChart1.setOption(option);
			myChart1.resize();

			window.addEventListener("resize", function () {
				myChart1.resize();
			});
		}




		//24小时在线矿工数
		$scope.getWorker24Online = function () {
			$scope.hourData = [];
			$scope.on_lineData = [];
			$http.get(mpoolUI_url + '/user/dashbaord/getWorker24Online')
				.success(function (data) {
					var itemsData = data.data;
					//排序
					if(!!itemsData) {
						itemsData.sort(function (a, b) {
							var value1 = a.hour;
							var value2 = b.hour;
							var d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
							return d == 0 ? 1 : d;
						});
						for (var i in itemsData) {
							if (!itemsData[i].on_line) {
								itemsData[i].on_line = 0;
							}
						}
						if (itemsData.length > 0) {
							for (var i = 0; i < itemsData.length; i++) {
								$scope.hourData[i] = itemsData[i].hour;
								$scope.on_lineData[i] = itemsData[i].on_line;
								var start = moment(moment(itemsData[i].hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
								var end = moment(moment(itemsData[i].hour, 'YYYYMMDDHH').add('hour', 1).format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
								$scope.hourData[i] = start + "-" + end;
							}
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
					data: [$translate.instant('common.tai')],
					x: 'left'
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
					name: $translate.instant('common.tai'),
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
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
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
			$http.get(mpoolUI_url + '/user/dashbaord/getUser30Day')
				.success(function (data) {
					var itemsData = data.data;
					if(!!itemsData){
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
					}
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
					$scope.dashboardItems(dateList1, rateList1, valueList2); //24小时算力折线图
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

			var myChart = echarts.init(document.getElementById('chart_power_30d'));
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
					data: [$translate.instant('sharepage.1dhs') +'(T)', $translate.instant('sharepage.1drj') +'(%)'],
					x: 'left'
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
					name:  $translate.instant('dashboard.Hashrate') +'(T)',
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
					name: $translate.instant('sharepage.rj') +'(%)',
					min: 0,
					max: 10,
					splitNumber: 5,
					splitLine: {
						show: false
					}
				}],
				grid: [{
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
				}],
				series: [{
						name: $translate.instant('sharepage.1dhs') +'(T)',
						type: 'line',
						smooth: true,
						showSymbol: false,
						data: valueList
					},
					{
						name: $translate.instant('sharepage.1drj') +'(%)',
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

		$scope.init = function () {
			$scope.getSubRuntimeInfo();
			$scope.getpoolNode();
			$scope.getUser24H(); //获取用户24小时 算力
			$scope.getWorker24Online(); //24小时在线矿工数
			$scope.getUser30Day(); //获取用户30天 算力
		}
		$scope.init();


		//分享
		$scope.postCreateWorkerGroup = function () {
			ngDialog.openConfirm({
				template: 'modalShare',
				controller: 'ShareCtrl',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				var val = value.groupName.value;
				var time = value.data;
				$http.post(mpoolUI_url + '/userManager/share?' + val + "=" + time).success(function (data) {
					if (data.msg == "ok") {
						Notify.alert($translate.instant('sharepage.shsc'), {
							status: 'success'
						});
						$scope.postValue(data.data);
					} else {
						var title1 = data.msg;
						Notify.alert(
							title1, {
								status: 'info'
							}
						)
					}
				})
			})
		}
		//分享 返回的值
		$scope.postValue = function (obj) {
			sessionStorage.setItem("obj", obj); //存储编号
			ngDialog.openConfirm({
				template: 'modalShareValue',
				controller: 'ShareCtrlValue',
				className: 'ngdialog-theme-default'
			}).then(function (value) {

			})
		}

		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }

	}
]);

//分享
App.controller('ShareCtrl', ['$http', '$scope', 'ngDialog', '$cookieStore',
	function ($http, $scope, ngDialog, $cookieStore) {

		$scope.itemsModal = [{
				name: '天',
				value: 'd'
			},
			{
				name: '时',
				value: 'h'
			},
			{
				name: '分',
				value: 'm'
			}
		]
		$scope.groupName = {
			name: '天',
			value: 'd'
		};
	}
])

//分享
App.controller('ShareCtrlValue', ['$http', '$scope', 'ngDialog', '$translate',
	function ($http, $scope, ngDialog, $translate) {

		$scope.num = sessionStorage.getItem('obj');
		//复制方法
		$scope.butCopy = $translate.instant('subActsMng.copy');
		$scope.clickCopy = function () {
			var Url1 = document.getElementById("copy");
			Url1.select(); //选择对象
			var tag = document.execCommand("Copy"); //执行浏览器复制命令
			if (tag) {
				$scope.butCopy = $translate.instant('subActsMng.copyed');
			}
		}
	}
])