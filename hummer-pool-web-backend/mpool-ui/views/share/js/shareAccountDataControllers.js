//分享
App.controller('shareAccountDataController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', '$stateParams',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, $stateParams) {

		var canvas = document.getElementById('canvas');
		var ctx = canvas.getContext('2d');
		canvas.width = canvas.parentNode.offsetWidth;
		canvas.height = canvas.parentNode.offsetHeight;
		
		//如果浏览器支持requestAnimFrame则使用requestAnimFrame否则使用setTimeout  
		window.requestAnimFrame = (function() {
		  return window.requestAnimationFrame ||
			window.webkitRequestAnimationFrame ||
			window.mozRequestAnimationFrame ||
			function(callback) {
			  window.setTimeout(callback, 1000 / 60);
			};
		})();
		
		window.onresize = function(){
		  canvas.width = canvas.parentNode.offsetWidth;
		  canvas.height = canvas.parentNode.offsetHeight;
		}
		//画布背景
		//初始角度为0  
		var step = 0;
		//定义三条不同波浪的颜色  
		var lines = ["rgba(0,222,255, 0.3)",
		//   "rgba(157,192,249, 0.3)",
		  "#708de2"
		];
		
		function loop() {
		  ctx.clearRect(0, 0, canvas.width, canvas.height);
		  step++;
		  //画3个不同颜色的矩形  
		  for (var j = lines.length - 1; j >= 0; j--) {
			ctx.fillStyle = lines[j];
			//每个矩形的角度都不同，每个之间相差45度  
			var angle = (step + j * 45) * Math.PI / 180;
			var deltaHeight = Math.sin(angle) * 50;
			var deltaHeightRight = Math.cos(angle) * 50;
			ctx.beginPath();
			ctx.moveTo(0, canvas.height / 6 + deltaHeight);
			ctx.bezierCurveTo(canvas.width / 2, canvas.height / 2 + deltaHeight - 50, canvas.width / 2, canvas.height / 2 + deltaHeightRight - 50, canvas.width, canvas.height / 2 + deltaHeightRight);
			ctx.lineTo(canvas.width, canvas.height);
			ctx.lineTo(0, canvas.height);
			ctx.lineTo(0, canvas.height / 2 + deltaHeight);
			ctx.closePath();
			ctx.fill();
		  }
		  requestAnimFrame(loop);
		}
		loop();

		var id = $stateParams.comId;

		$http.get(mpoolUI_url + '/share/rest/' + id).success(
			function (data) {
				if (data.msg == "ok") {
					if (data.data) {
						$scope.userStatus = data.data.userStatus;
						var itemsData = data.data.workerList;

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
					data: [$translate.instant('sharepage.1hhs') +'(T)', $translate.instant('sharepage.1hrj') +'(%)']
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
					top: '15%',
					bottom: '5%',
					right: '10%',
					left: '10%'
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
				title: [{
					left: 'center'
				}],
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: [$translate.instant('sharepage.1dhs') +'(T)', $translate.instant('sharepage.1drj') +'(%)']
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
					top: '20%',
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
		//矿工记录table
		var vm = $scope.vm = {};
        vm.sort = {
            column: 'workerName',
            direction: 1,
            toggle: function (column) {
            	if(column.name != 'checkbox' && column.name != 'operation'){
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
        	// {label: '',			    name: 'checkbox',				type: 'string'},
        	{label: $translate.instant('worker.Miner Name'),			name: 'workerName',				type: 'string'},
        	// {label: '5分钟算力',		name: 'hashAccept5mT',		type: 'int'},
        	{label: $translate.instant('worker.15mHs'),		name: 'hashAccept15mT',		type: 'string'},
            {label: $translate.instant('worker.15mRj'), name: 'accept15mRe',			type: 'string'},
            {label: $translate.instant('worker.1hHs'),		name: 'hashAccept1hT',	    type: 'string',class:'hidden-xs'},
            {label: $translate.instant('worker.1hRj'),	name: 'accept1hRe',				type: 'string',class:'hidden-xs'},
            {label: $translate.instant('worker.Last update'),		name: 'lastShareTime',			type: 'string', class:'hidden-xs'}
		];
		$scope.p_pernum = 10;  
	    $scope.p_current = 1;  
	    $scope.p_all_page = 0;  
	    $scope.pages = [];
		$scope.show_items = true;
		if($cookieStore.get("shareTableSize")) {
			$scope.p_pernum = $cookieStore.get("shareTableSize"); 
		}
		$scope.getWorkerList = function(current,size){
			$cookieStore.put("shareTableSize",size); 
			var json = "";
			$scope.allchecked=false
			$scope.stepCheckedValues =[]
			if($scope.workerName){
				json = '/share/shareWorkerList?current='+current+'&size='+size+'&status='+$scope.statusValue+'&workerName='+$scope.workerName+'&id='+id;                        
			}else{
				json = '/share/shareWorkerList?current='+current+'&size='+size+'&status='+$scope.statusValue+'&id='+id;
			}
			if($scope.group_Id){
				json = json+'&groupId='+$scope.group_Id
			}
			$scope.show_items = false;
			$http.get(mpoolUI_url + json).success(function(data){
				if(data.msg == "ok"){
					$timeout(function(){
					   $scope.show_items = true;
          		 	},800);
          		 	if(data.data != null){
						$scope.itemsData = data.data.records;
						$scope.items_num = $scope.itemsData.length;
						if($scope.statusValue == "offline"){
							for(var i=0;i<$scope.itemsData.length;i++){
								var offline = 'offline';
								$scope.itemsData[i].offline = offline;
							}
						}
						for(var i in $scope.itemsData){
							$scope.itemsData[i].hashAccept1hT=parseFloat($scope.itemsData[i].hashAccept1hT);
							$scope.itemsData[i].hashAccept1mT=parseFloat($scope.itemsData[i].hashAccept1mT);
							$scope.itemsData[i].hashAccept5mT=parseFloat($scope.itemsData[i].hashAccept5mT);
							$scope.itemsData[i].hashAccept15mT=parseFloat($scope.itemsData[i].hashAccept15mT);
							$scope.itemsData[i].hashReject1hT=parseFloat($scope.itemsData[i].hashReject1hT);
							$scope.itemsData[i].hashReject15mT=parseFloat($scope.itemsData[i].hashReject15mT);
							var accept15mRe = ($scope.itemsData[i].hashReject15mT/ ($scope.itemsData[i].hashReject15mT+$scope.itemsData[i].hashAccept15mT))*100;
							if(!accept15mRe){
								accept15mRe=0.0;
							}
							var accept1hRe= ($scope.itemsData[i].hashReject1hT/($scope.itemsData[i].hashReject1hT+$scope.itemsData[i].hashAccept1hT))*100;
							if(!accept1hRe){
								accept1hRe=0.0;
							}
							$scope.itemsData[i].accept15mRe=accept15mRe
							$scope.itemsData[i].accept1hRe=accept1hRe
						}
						$scope.count = data.data.total;     //总页数
		                $scope.totalPage = data.data.pages;   //页数
		                $scope.p_current = current;  
		                $scope.p_all_page = Math.ceil($scope.count/size);  
		                reloadPno();  //初始化页码  
	                }else{
          		 		$scope.items_num = 0;
          		 	}
				}
			}).error(function(data, status, headers, config) {
				$timeout(function(){
				   $scope.show_items = true;
      		 	},800);
				$scope.itemsData = [];
				$scope.items_num = 0;
			});
		}
		//自动刷新
		
		setInterval(function() {
			$scope.load_page(1,$scope.currentSelect.value);
		}, 120000);

		//首页  
	    $scope.p_index = function(){  
	        $scope.load_page(1,$scope.currentSelect.value);  
	    }  
	    //尾页  
	    $scope.p_last = function(){  
	        $scope.load_page($scope.p_all_page,$scope.currentSelect.value);  
	    }  
	    //加载某一页  
	    $scope.load_page = function(page,pernum){  
	        $scope.getWorkerList(page,pernum); 
	    };  
	    //初始化页码  
	    var reloadPno = function(){  
	      	$scope.pages = calculateIndexes($scope.p_current,$scope.p_all_page,8);  
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
		
		$scope.folders = [
			{name: $translate.instant('worker.All'),  id: '-1', title: '查询全部矿机'},
			{name: $translate.instant('worker.No Group'),  id: '0', title: '查询未分组矿机'}
		];
			
		//分组查询
		$scope.parameterQuery = function(obj, index, name){
			if(obj == '0' || obj =='-1'){
				$scope.selected = -2;
				if(obj == "-1"){
					$scope.selectedState = 0;
					$scope.statusValue = "all";
				}
			}else{
				$scope.selected = index;
			}
			$scope.group2 = false;      //点击确认组操作按钮后禁用 该按钮
			$scope.minerName = name;
			$scope.currentSelect = {
		    	value: $scope.p_pernum,
		 	}
			$scope.group_Id = obj;   //分组查询矿机列表参数
			$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
		}
		
		$scope.statusData = [
  			{name: $translate.instant('worker.No Group'),  	id: '0',value:"all"},
			{name: $translate.instant('dashboard.active'),  	id: '1',value:"active"},
			{name: $translate.instant('dashboard.Inactive'),    id: '2',value:"inactive"},
    		{name: $translate.instant('dashboard.Offline'),  	id: '3',value:"offline"}
    		
  		];
		//状态查询
		$scope.getState = function(index2, status2,to){
			$scope.selectedState = parseInt(index2);
			$scope.statusValue = status2;    //状态参数
			if(!to){
				$scope.getWorkerList(1,$scope.p_pernum);//我的矿机列表
			}
			
		}
		
		//矿工组列表
		// $scope.getWorkerGroupList = function(){
			// $http.get(mpoolUI_url + '/worker/workerGroupList').success(function(data){
				// if(data.msg == "ok"){
				// 	$scope.itemsData2 = data.data;
				// }
			// })
		// }		
		//初始化方法
		$scope.init = function(){
			$scope.selectedState = 1;
			$scope.statusValue = "active";
			// $scope.getWorkerGroupList();    			//矿工组列表
			$scope.parameterQuery(0, -2,'未分组');      	//矿工分组查询
			// $scope.getSubRuntimeInfo();
			$scope.payparameterQuery(); //付款记录查询
			$scope.getUser30Day()
		}
		
//.............................支付记录

		

		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }

		//列表排序		
		var paidvm = $scope.paidvm = {};
		paidvm.sort = {
			column: 'day',
			direction: 1,
			toggle: function (column) {
				if (column.name != 'txid') {
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
		paidvm.columns = [
			{
				label: $translate.instant('payrecord.txid'),
				name: 'txid',
				type: 'string',
				className:'hidden-sm hidden-md hidden-lg',
			},
			{
				label: $translate.instant('payrecord.txid'),
				name: 'txid',
				type: 'string',
				className:"hidden-xs"
			},
			{
				label: $translate.instant('payrecord.Time'),
				name: 'payAt',
				type: 'string'
			},
			{
				label: $translate.instant('payrecord.Amount') +'（BTC）',
				name: 'fppsAfterFee',
				type: 'string'
			}
		];
		$scope.filter2 = function () {
			$scope.items_num2 = $filter('filter')($scope.itemsData2, paidvm.filter.$).length;
		};
		//付款记录查询列表方法
		$scope.p_pernum2 = 10;
		$scope.p_current2 = 1;
		$scope.p_all_page2 = 0;
		$scope.pages2 = [];
		$scope.show_items2 = true;
		if($cookieStore.get("shareTableSize2")) {
			$scope.p_pernum2 = $cookieStore.get("shareTableSize2"); 
		}
		$scope.getPayList = function (current, size) {
			$cookieStore.put("shareTableSize2",size);
			$scope.show_items2 = false;
			$http.get(mpoolUI_url + '/share/pay/record?current=' + current + '&size=' + size+'&id='+id)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items2 = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData2 = data.data.records;
							// $scope.itemsData2.forEach(function(item){
								
							// })
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
			$scope.getPayList(page, pernum);
		};
		//初始化页码  
		var reloadPno2 = function () {
			$scope.pages2 = calculateIndexes($scope.p_current2, $scope.p_all_page2, 8);
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

		//付款记录查询
		$scope.payparameterQuery = function () {
			$scope.currentSelect2 = {
				value: $scope.p_pernum2,
			}
			$scope.getPayList(1, $scope.p_pernum2); //付款记录查询列表方法
		}

		
		$scope.init();
	}
	

	
])