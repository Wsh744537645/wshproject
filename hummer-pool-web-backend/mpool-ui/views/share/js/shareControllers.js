//分享
App.controller('shareController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', '$stateParams',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, $stateParams) {
		$scope.href = function(url){
			if(url == '') {
				$location.path('share/' + $stateParams.comId)
			}else{
				$location.path('share/' + $stateParams.comId + '/'+url)
			}
		}
		$scope.cointype = $stateParams.comId.split('-')[1]
		
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			angular.forEach(r.data, (d)=> {
				if($scope.cointype == d.type) {
					$scope.unit = d.unit
				}
			})
		})
		
		$scope.getpoolNode = function() {
			$scope.northNode = []
			$scope.southNode = []
			$http.get(mpoolUI_url + '/user/dashbaord/getPoolNodes?currencyName=' + $scope.cointype).success(function (r) {
				angular.forEach(r.data.north, (adr)=> {
					$scope.northNode.push({adr: adr.address, order: adr.order})
				})
				angular.forEach(r.data.south, (adr)=> {
					$scope.southNode.push({adr: adr.address, order: adr.order})
				})
				$scope.discr = r.data.north[0].discrible
			})
		}
		//如果浏览器支持requestAnimFrame则使用requestAnimFrame否则使用setTimeout  
		window.requestAnimFrame = (function() {
		  return window.requestAnimationFrame ||
			window.webkitRequestAnimationFrame ||
			window.mozRequestAnimationFrame ||
			function(callback) {
			  window.setTimeout(callback, 1000 / 60);
			};
		})();

		var id = $stateParams.comId;

		$http.get(mpoolUI_url + '/share/rest/' + id).success(function (data) {
			if (data.msg == "ok") {
				if (data.data) {
					$scope.userStatus = data.data.userStatus;
					var itemsData = data.data.share24h;

					itemsData.sort(function (a, b) {
						var value1 = a.hour;
						var value2 = b.hour;
						var d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
						return d == 0 ? 1 : d;
					});

					$scope.result = itemsData.map(function(item){
						var hashAcceptT = item.hashAcceptT || 0;
						var hashRejectT = item.hashRejectT || 0;
						var start = moment(moment(item.hour, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
						var end = moment(moment(item.hour, 'YYYYMMDDHH').add('hour', 1).format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
						var date = start + "-" + end;
						var rejectRate = (hashRejectT / (hashAcceptT + hashRejectT) *100) || 0
						return {
							date: date,
							hashAcceptT: hashAcceptT,
							rejectRate: rejectRate
						};
					})

					var hashAcceptTList = [];
					var hashRejectTList = [];
					var dateList = [];
					$scope.result.forEach(function(item){
						hashAcceptTList.push(item.hashAcceptT.toFixed(3))
						hashRejectTList.push(item.rejectRate.toFixed(3))
						dateList.push(item.date)
					})
					$scope.get24HLine(dateList, hashRejectTList, hashAcceptTList);
				}
			}
		})

		//24小时算力折线图
		$scope.get24HLine = function (dateList1, rateList1, valueList1) {
			var maxs = eval("Math.max(" + valueList1.toString() + ")"); //求Y轴最大值
			var max = parseInt(maxs);
			if (max == 0 || max < 5) {
				$scope.minValue = 0;
				$scope.maxValue = 5;
			} else {
				$scope.minValue = 0;
				$scope.maxValue = max + 10;
			}

			var myChart1 = echarts.init(document.getElementById('linChart'));
			var option = {
				color: ['#3ACA60', '#EB8C01'],
				visualMap: [{
					show: false,
					type: 'continuous',
					seriesIndex: 1,
					dimension: 0,
					min: 0,
					max: dateList1.length + 1
				}],
				title: [{
					left: 'center'
				}],
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: [$translate.instant('sharepage.1hhs') +'(T)', $translate.instant('sharepage.1hrj') +'(%)'],
					x: 'left'
				},
				xAxis: [{
					data: dateList1,
					axisTick: false,
					boundaryGap: false,
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
					},
				}],
				yAxis: [{
					name:  $translate.instant('dashboard.Hashrate') +'(T)',
					type: 'value',
					position: 'left',
					splitNumber: 5,
					min: $scope.minValue,
					max: $scope.maxValue,
					splitLine: {
						show: true,
						lineStyle: {
							color: '#dedede',type:'dashed'
						}
					}
				}, {
					name: $translate.instant('sharepage.rj') +'(%)',
					max: 10,
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
						data: valueList1
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
						data: rateList1
					}
				]
			};
			myChart1.setOption(option);
			myChart1.resize();

			window.addEventListener("resize", function () {
				myChart1.resize();
			});
		}
		//获取用户30天 算力
		$scope.getUser30Day = function () {
			$http.get(mpoolUI_url + '/share/rest/' + id)
				.success(function (data) {
					var itemsData = data.data.user30Share;
					//排序
					itemsData.sort(function (a, b) {
						var value1 = a.date;
						var value2 = b.date;
						var d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
						return d == 0 ? 1 : d;
					});
					$scope.result30 = itemsData.map(function(item) {
						var hashAcceptT = item.hashAcceptT || 0;
						var rejectRate = item.rejectRate || 0;
						var date = moment(moment(item.date, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('YYYY-MM-DD');
						return {
							date: date,
							hashAcceptT: hashAcceptT,
							rejectRate: rejectRate
						};
					})
					var hashAcceptTList = [];
					var hashRejectTList = [];
					var dateList = [];
					$scope.result30.forEach(function(item) {
						hashAcceptTList.push(item.hashAcceptT.toFixed(3))
						hashRejectTList.push(item.rejectRate.toFixed(3))
						dateList.push(item.date)
					})
					$scope.dashboardItems(dateList, hashRejectTList, hashAcceptTList); //获取用户30天 算力  折线图

				}).error(function (data, status, headers, config) {
					var dateList = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '00']; //时间
					var rateList = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //小时拒绝率
					var valueList = ['0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0']; //算力
					$scope.dashboardItems(dateList, rateList, valueList); //24小时算力折线图
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
					// splitLine: {
					// 	show: true,
					// 	lineStyle: {
					// 		color: '#f3faff'
					// 	}
					// }
				}],
				yAxis: [{
					name: $translate.instant('dashboard.Hashrate') +'(T)',
					type: 'value',
					position: 'left',
					splitNumber: 5,
					min: $scope.minValue2,
					max: $scope.maxValue2,
					splitLine: {
						show: true,
						lineStyle: {
							color: '#dedede',type:'dashed'
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

		//24小时在线矿工数
		$scope.getWorker24Online = function () {
			$scope.hourData = [];
			$scope.on_lineData = [];
			$http.get(mpoolUI_url + '/share/rest/' + id).success(function (data) {
					var itemsData = data.data.worker24hOnline;
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

		$scope.init = function(){
			$scope.getUser30Day()
			$scope.getWorker24Online()
			$scope.getpoolNode()
		}
		

		$scope.init();
	}
	

	
])