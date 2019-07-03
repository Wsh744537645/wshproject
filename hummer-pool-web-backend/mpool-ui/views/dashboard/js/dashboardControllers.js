//用户面板
App.controller('dashboardController', ['$scope', '$http', '$location', '$translate', '$state', '$timeout', '$window', 'ngDialog', 'Notify', '$interval', '$rootScope',
	function ($scope, $http, $location, $translate, $state, $timeout, $window, ngDialog, Notify, $interval, $rootScope) {
		$scope.page = 1
		$http.get(mpoolUI_url + '/share/getCurrencyList').success(function (r) {
			$scope.currencyData = r.data
			angular.forEach($scope.currencyData,(d)=> {
				d.coinIcon = "../../img/banner/" + d.type + "1.png"
			})
		})
		//主账户进入后默认选择BTC
		$scope.currencyName= 'BTC'
		$scope.currencyCoin= "../../img/banner/BTC1.png"
		$scope.unit = 'TH/s'
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
		$scope.changeCoin =  function(name) {
			$scope.currencyName = name
			angular.forEach($scope.currencyData, (d)=> {
				if(name == d.type) {
					$scope.unit = d.unit
					$scope.currencyCoin = "../../img/banner/" + d.type + "1.png"
				}
			})
			$scope.refresh();
		}
		//合计列表数据
		$scope.getConverge = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getMasterRuntimeInfo/converge?currencyName=' + $scope.currencyName)
				.success(function (data) {
					$scope.items_Data = data.data;
				})
		}
		$interval(function () {
			$scope.getConverge()
		}, 30000, -1);

		$scope.currentSelect = {
			value: '10',
		}
		//公告
		// $scope.shownotice = false
		$scope.getNotice = function() {
			$http.get(mpoolUI_url + '/msg/list?current=1&size=10')
				.success(function(data){
					if(data.msg == "ok"){
						$scope.newNotice = data.data.records[0];
						if(!!data.data.records[0] && !!data.data.records[0].releaseTime){
							$scope.shownotice = true
							$scope.newNotice.time = $scope.newNotice.releaseTime.substr(0,10)
						}
					}
				})	
			}
		$scope.closeNotice = function() {
			$scope.shownotice = false
		}
		$scope.goNdetail = function (id) {
			$state.go('announcementdetail', {id: id});
		}
		$scope.refresh = function() {
			$scope.getConverge()
			$scope.getUserPanelList($scope.p_current, $scope.p_pernum)
			$scope.getUser24H()
			$scope.getWorker24Online()
			$scope.getUser30Day()
			$scope.getpoolNode();
		}

		//用户面板列表
		$scope.p_pernum = 10;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
		$scope.show_items = true;
		$scope.getUserPanelList = function (current, size) {
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/user/dashbaord/getMasterRuntimeInfo?current=' + current + '&size=' + size + '&groupId=' + $scope.group_Id + '&currencyName=' + $scope.currencyName)
				//$http.get(mpoolUI_url + '/user/dashbaord/getSubRuntimeInfo')
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
							$scope.itemsData = []
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
			$scope.getUserPanelList(page, pernum); //用户面板列表
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




		//折线图公共方法
		$scope.common = function (objData) {
			objData.sort(function (a, b) {
				var value1 = a.date;
				var value2 = b.date;
				var d = moment(value1, 'YYYYMMDDHH').valueOf() - moment(value2, 'YYYYMMDDHH').valueOf();
				return d == 0 ? 1 : d;
			});

			objData.forEach(function(item) {
				var start = moment(moment(item.date, 'YYYYMMDDHH').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
				var end = moment(moment(item.date, 'YYYYMMDDHH').add('hour', 1).format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('HH:mm');
				item.date = start + "-" + end;
			})
			return objData;
		}

		//折线图公共方法
		$scope.common30 = function (objData) {
			objData.sort(function (a, b) {
				var value1 = a.date;
				var value2 = b.date;
				var d = moment(value1, 'YYYYMMDD').valueOf() - moment(value2, 'YYYYMMDD').valueOf();
				return d == 0 ? 1 : d;
			});

			objData.forEach(function(item ) {
				var date = moment(moment(item.date, 'YYYYMMDD').format('YYYY-MM-DDTHH:mm:ss.000+0000')).format('YYYY-MM-DD');
				item.date = date;
			})
			return objData;
		}
		
		//---------------------------------------------------获取用户24小时 算力
		$scope.getUser24H = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getMasterShareChartInfo/24' + '?currencyName=' + $scope.currencyName)
				.success(function (data) {
					if (data.msg == "ok") {
						var itemsData = data.data;
						var name_24 = []; //账户名  
						var date_24 = []; //时间
						var series = [];
						var max24 = 0;
						for (var key in itemsData) {
							name_24.push(key); //账户名  
							$scope.common(itemsData[key]); //折线图公共方法
						}

						for (var i in itemsData) {
							var data = []
							var item = itemsData[i]
							for (var j = 0; j < item.length; j++) {
								if (date_24.length != 24) {
									date_24.push(item[j].date); //时间
								}
								data.push(item[j].hashAcceptT.toFixed(3)); //算折线图data值
							}
							max24 = Math.max(data.max(), max24); // 算数组的最大值
							series.push({
								name: i,
								type: 'line',
								smooth: true,
								showSymbol: false,
								data: data
							})
						}
						$scope.get24HLine(name_24, date_24, series, max24);
					}
				}).error(function (data, status, headers, config) {

				});
		}
		//24小时算力折线图
		$scope.get24HLine = function (name24, date24, series, max24) {
			//算Y轴最大值
			if (max24 == 0 || max24 < 5) {
				max24 = 5;
			} else {
				max24 = max24 + max24 * 0.2;
				max24 = parseInt(max24);
			}
			var myChart1 = echarts.init(document.getElementById('chart_power_24h'));
			var option = {
				tooltip: {
					trigger: 'axis',
					backgroundColor: '#fff',
					borderColor: '#DADADA',
					borderWidth: '1',
					textStyle: {
						color: '#4A4A4A'
					}
				},
				color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],

				legend: {
					data: name24,
					x: 'left'
				},

				xAxis: [{
					data: date24,
					axisTick: false,
					boundaryGap: false,
					splitLine: {
						show: false,
					},
					nameTextStyle: {
						fontSize: 10,
						color: '#8F9EA6'
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
					},
				}],
				yAxis: [{
						name: $translate.instant('dashboard.Hashrate') + '(T)',
						nameTextStyle: {
							fontSize: 12,
							color: '#121212',
							align: 'left'
						},
						type: 'value',
						position: 'left',
						splitNumber: 5,
						min: 0,
						max: max24,
						splitLine: {
							show: true,
							lineStyle: {
								type: 'dashed'
							}
						},
						// data: {
						// 	textStyle: {
						// 		fontSize: 10,
						// 		color: '#888888'
						// 	}
						// }
					}

				],
				grid: [{
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
				}],
			};
			option.series = series;
			myChart1.setOption(option);
			myChart1.resize();

			window.addEventListener("resize", function () {
				myChart1.resize();
			});
		}
		//---------------------------------------------------24小时在线矿工数
		$scope.getWorker24Online = function () {
			$scope.hourData = [];
			$scope.on_lineData = [];
			$http.get(mpoolUI_url + '/user/dashbaord/getMasterWorkerInfo/24' + '?currencyName=' + $scope.currencyName)
				.success(function (data) {
					if (data.msg == "ok") {
						var itemsData = data.data;
						var name24 = []; //账户名  
						var date24 = []; //时间
						var series = [];
						var max24 = 0;
						for (var key in itemsData) {
							name24.push(key); //账户名  
							$scope.common(itemsData[key]); //折线图公共方法
						}

						for (var i in itemsData) {
							var data = []
							var item = itemsData[i]
							for (var j = 0; j < item.length; j++) {
								if (date24.length != 24) {
									date24.push(item[j].date); //时间
								}
								data.push(item[j].onLine); //算折线图data值
							}
							max24 = Math.max(data.max(), max24); // 算数组的最大值
							series.push({
								name: i,
								type: 'line',
								smooth: true,
								showSymbol: false,
								data: data
							})
						}
						$scope.chart_worker_24h(name24, date24, series, max24);
					}
				}).error(function (data, status, headers, config) {

				});
		}

		//24小时在线矿工数   折线图
		$scope.chart_worker_24h = function (name24, date24, series, max24) {
			//算Y轴最大值
			if (max24 == 0 || max24 < 5) {
				max24 = 5;
			} else {
				max24 = max24 + max24 * 0.2;
				max24 = parseInt(max24);
			}
			var myChart = echarts.init(document.getElementById('chart_worker_24h'));
			var option = {
				tooltip: {
					trigger: 'axis',
					backgroundColor: '#fff',
					borderColor: '#DADADA',
					borderWidth: '1',
					textStyle: {
						color: '#4A4A4A'
					}
				},
				color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],
				legend: {
					data: name24,
					x: 'left'
				},
				xAxis: [{
					data: date24,
					axisTick: false,
					boundaryGap: false,
					splitLine: {
						show: false,
						// lineStyle: {
						// 	color: '#f3faff'
						// }
					},
					nameTextStyle: {
						fontSize: 10,
						color: '#8F9EA6'
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
						type: 'value',
						position: 'left',
						splitNumber: 5,
						min: 0,
						max: max24,
						nameTextStyle: {
							fontSize: 12,
							color: '#121212',
							align: 'left'
						},
						splitLine: {
							show: true,
							lineStyle: {
								type: 'dashed'
							}
						}
					}

				],
				grid: [{
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
				}],
			};
			option.series = series;
			myChart.setOption(option);
			myChart.resize();

			window.addEventListener("resize", function () {
				myChart.resize();
			});
		}


		//---------------------------------------------------获取用户30天 算力
		$scope.getUser30Day = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getMasterShareChartInfo/30' + '?currencyName=' + $scope.currencyName)
				.success(function (data) {
					if (data.msg == "ok") {
						var itemsData = data.data;
						var name_30 = []; //账户名  
						var date_30 = []; //时间
						var series30 = [];
						var max30 = 0;
						for (var key in itemsData) {
							name_30.push(key); //账户名  
							$scope.common30(itemsData[key]); //折线图公共方法
						}
						for (var i in itemsData) {
							var data = []
							var item = itemsData[i]
							for (var j = 0; j < item.length; j++) {
								if (date_30.length != 30) {
									date_30.push(item[j].date); //时间
								}
								data.push(item[j].hashAcceptT.toFixed(3)); //算折线图data值
							}
							max30 = Math.max(data.max(), max30); // 算数组的最大值
							series30.push({
								name: i,
								type: 'line',
								smooth: true,
								showSymbol: false,
								data: data
							})
						}
						$scope.dashboardItems(name_30, date_30, series30, max30);
					}
				}).error(function (data, status, headers, config) {

				});
		}
		//获取用户30天 算力  折线图
		$scope.dashboardItems = function (name30, date30, series30, max30) {
			//算Y轴最大值
			if (max30 == 0 || max30 < 5) {
				max30 = 5;
			} else {
				max30 = max30 + max30 * 0.2;
				max30 = parseInt(max30);
			}

			var myChart2 = echarts.init(document.getElementById('chart_power_30d'));
			var option = {
				tooltip: {
					trigger: 'axis',
					backgroundColor: '#fff',
					borderColor: '#DADADA',
					borderWidth: '1',
					textStyle: {
						color: '#4A4A4A'
					}
				},
				legend: {
					data:name30,
					x: 'left'
				},
				
				color: [ '#CA8622', '#FFEEB1','#C23531', '#61A0A8', '#6FA861', '#A8619D', '#ca8622', '#bda29a', '#6e7074', '#546570'],
				xAxis: [{
					data: date30,
					axisTick: false,
					boundaryGap: false,
					splitLine: {
						show: false,
						// lineStyle: {
						// 	color: '#f3faff'
						// }
					},
					nameTextStyle: {
						fontSize: 10,
						color: '#8F9EA6'
					},
				}],
				yAxis: [{
						name: $translate.instant('dashboard.Hashrate') + '(T)',
						nameTextStyle: {
							fontSize: 12,
							color: '#121212',
							align: 'left'
						},
						type: 'value',
						position: 'left',
						splitNumber: 5,
						min: 0,
						max: max30,
						splitLine: {
							show: true,
							lineStyle: {
								type: 'dashed'
							}
						}
					}

				],
				grid: [{
					top: '30%',
					bottom: '10%',
					right: '8%',
					left: '8%'
				}],
			};
			option.series = series30;
			myChart2.setOption(option);
			myChart2.resize();

			window.addEventListener("resize", function () {
				myChart2.resize();
			});
		}


		$scope.init = function () {
			$scope.getNotice();
			$scope.getConverge();
			$scope.getUserPanelList($scope.p_current, $scope.p_pernum); //用户面板列表
			$scope.getUser24H(); //获取用户24小时 算力
			$scope.getWorker24Online(); //24小时在线矿工数
			$scope.getUser30Day(); //获取用户30天 算力
			$scope.getpoolNode(); //挖矿节点
		}
		$scope.init();

		//跳转首页界面
		$scope.homepage = function () {
			var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
		}

		//切换子账号
		$scope.getsWitchSubAccount = function (objID, objName) {
			sessionStorage.setItem("obj2", objName);
			ngDialog.openConfirm({
				template: 'modelGroup123',
				controller: 'GroupCtrl123',
				className: 'ngdialog-theme-default'
			}).then(function (value) {
				sessionStorage.setItem('currencyName',$scope.currencyName)
				$http.get(mpoolUI_url + '/userManager/switchSubAccount/' + objID + '?currencyName=' + $scope.currencyName)
					.success(function (data) {
						if (data.msg == "ok") {
							// var master= $cookieStore.get("nickName");
							// var masterObj = {username:master,userId:data.data.masterUserId,id:data.data.userId}
							// $cookieStore.put('nickName', data.data.username);   //存储子账户名称
							// if(data.data.masterUserId){
							// 	$cookieStore.put("masterUserId",data.data.masterUserId);
							// 	$cookieStore.put("master",masterObj);
							// }

							// $timeout(function(){
							// 	Notify.alert('切换成子账号成功！！！', {status: 'success'});
							// },1000);
							$location.path('/switchAccount'); //跳转界面
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

	}
]);

//模态框 切换子账号
App.controller('GroupCtrl123', ['$scope', 'ngDialog', '$cookieStore',
	function ($scope, ngDialog, $cookieStore) {
		$scope.name = sessionStorage.getItem('obj2'); //获取删除的名称
	}
])

//子账号切换住账号控制器
App.controller('switchAccountController', ['$scope', '$location',
	function ($scope, $location) {
		
		$location.path('api/sondashboard'); //跳转界面

	}
])