//分享
App.controller('sharePayController', ['$scope', '$http', '$location', '$cookieStore', '$state', '$timeout', '$translate', '$stateParams',
	function ($scope, $http, $location, $cookieStore, $state, $timeout, $translate, $stateParams) {
		$scope.href = function(url){
			if(url == '') {
				$location.path('share/' + $stateParams.comId)
			}else{
				$location.path('share/' + $stateParams.comId + '/'+url)
			}
		} 
		$scope.cointype = $stateParams.comId.split('-')[1]
		
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


		$http.get(mpoolUI_url + '/share/rest/' + $stateParams.comId).success(
			function (data) {
				if (data.msg == "ok") {
					if (data.data) {
						$scope.userStatus = data.data.userStatus;
					}
				}
			}
		)
		
		$scope.getSubRuntimeInfo = function () {
			$http.get(mpoolUI_url + '/user/dashbaord/getSubRuntimeInfo')
				.success(function (data) {
					if (data.msg == "ok") {
						$scope.items_Data = data.data;
					}
				}).error(function (data, status, headers, config) {

				});
		}
		//初始化方法
		$scope.init = function(){
			$scope.selectedState = 1;
			$scope.getSubRuntimeInfo(); 
			$scope.payparameterQuery(); //付款记录查询
		}

		//跳转首页界面
		$scope.homepage = function () {
	        var url = $state.href("homePage.html", {}, {
				reload: true
			});
			window.open("/home.html", '_self')
	    }

		//列表排序		
		var vm = $scope.vm = {};
		vm.sort = {
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
		vm.columns = [
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
				label: $translate.instant('payrecord.Amount'),
				name: 'fppsAfterFee',
				type: 'string'
			}
		];
		$scope.filter = function () {
			$scope.items_num = $filter('filter')($scope.itemsData, vm.filter.$).length;
		};
		//付款记录查询列表方法
		$scope.p_pernum = 10;
		$scope.p_current = 1;
		$scope.p_all_page = 0;
		$scope.pages = [];
	    $scope.page = 1;
		$scope.show_items = true;
		if($cookieStore.get("shareTableSize")) {
			$scope.p_pernum = $cookieStore.get("shareTableSize"); 
		}
		$scope.getPayList = function (current, size) {
			$cookieStore.put("shareTableSize",size);
			$scope.show_items = false;
			$http.get(mpoolUI_url + '/share/pay/record?current=' + current + '&size=' + size+'&id='+id)
				.success(function (data) {
					if (data.msg == "ok") {
						$timeout(function () {
							$scope.show_items = true;
						}, 800);
						if (data.data != null) {
							$scope.itemsData = data.data.records;
							// $scope.itemsData.forEach(function(item){
								
							// })
							$scope.items_num = $scope.itemsData.length;

							$scope.count = data.data.total; //总页数
							$scope.totalPage = data.data.pages; //页数
							$scope.p_current = current;
							$scope.p_all_page = Math.ceil($scope.count / size);
							reloadPno(); //初始化页码  
						} else {
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
			$scope.getPayList(page, pernum);
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

		//付款记录查询
		$scope.payparameterQuery = function () {
			$scope.currentSelect= {
				value: $scope.p_pernum,
			}
			$scope.getPayList(1, $scope.p_pernum); //付款记录查询列表方法
		}

		
		$scope.init();
	}
	

	
])