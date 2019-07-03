App.config(function ($httpProvider) {
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
	$httpProvider.defaults.headers["put"] = {
		'Content-Type': 'application/json;charset=utf-8'
	};
	//在这里构造拦截器
	var interceptor = function ($q, $rootScope, $location, $window, $cookieStore, $timeout, Notify) {
		var i = 1;
		return {
			'request': function (req) {
				var currUrl = $location.$$url
				req.params = req.params || {};
				if ($cookieStore.get('nickName')) {
					req.headers.name = $cookieStore.get('nickName');
					if ($cookieStore.get('masterUserId')) {
						//子账号 不能访问的
						var test = /^\/api\/(dashboard|subaccounts)/.test(currUrl)
						if (test) {
							$location.path('/login');
						}
					} else {
						//主账号 不能访问的
						var test = /^\/api\/(sondashboard|sonSubaccount|payrecords)/.test(currUrl)
						if (test) {
							$location.path('/login');
						}
					}

				} else {
					if (req.method == "POST") {

					} else {
						//控制注销后点击浏览器后退按钮跳入到系统中

						var flage = !/^\/(|login|phoneRegister|register|resetPassword|phoneResetPassword|homePage|billboard|downloadTool|statistics|api\/subaccounts)/.test(currUrl)
						if (flage) {
							$location.path('/login');
						}
					}
				}
				return req || $q.when(req);
			},
			'response': function (res) {
				//系统超时自动跳入登录界面
				if (i == 1) { //判断子执行一次超时提示
					if (res.data.msg == "err" || res.data.code == '-1') {
						i += 1;
						$cookieStore.remove('nickName');
						Notify.alert(
							'系统超时，请重新登录..', {
								status: 'success'
							}
						);
						$timeout(function () {
							$location.path('/login');
						}, 1000);
					}
				}
				return res;
			}
		}
	};
	$httpProvider.interceptors.push(interceptor);
});

App.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
	function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
		'use strict';
		$locationProvider.html5Mode(true);
		$urlRouterProvider.otherwise('/login');

		$stateProvider

			//登录  注册  路由
			.state('login', {
				url: '/login',
				title: '登录',
				templateUrl: __uri('views/user/login.html'),
				controller: 'loginController',
				resolve: helper.resolveFor('modernizr', 'icons', 'loaders.css', 'spinkit')
			})
			.state('register', {
				url: '/register',
				title: '邮箱注册',
				templateUrl: __uri('views/user/emailRegister.html'),
				controller: 'registerController',
				resolve: helper.resolveFor('modernizr', 'icons', 'ngDialog')
			})
			.state('phoneRegister', {
				url: '/phoneRegister',
				title: '手机注册',
				templateUrl: __uri('views/user/phoneRegister.html'),
				controller: 'phoneRegisterController',
				resolve: helper.resolveFor('modernizr', 'icons', 'ngDialog')
			})
			.state('resetPassword', {
				url: '/resetPassword',
				title: '忘记密码',
				templateUrl: __uri('views/user/emailResetPassword.html'),
				controller: 'emailResetPasswordController'
			})
			.state('phoneResetPassword', {
				url: '/phoneResetPassword',
				title: '手机找回密码',
				templateUrl: __uri('views/user/phoneResetPassword.html'),
				controller: 'phoneResetPasswordController'
			})
			.state('statistics', {
		        url: '/statistics',
		        title: '统计',
		        templateUrl: __uri('views/statistics/statistics.html'),
		        controller: 'statisticsController',
		        resolve: helper.resolveFor('loaders.css', 'spinkit', 'icons')
	    	})
			.state('announcement', {
		        url: '/announcement',
		        title: '公告',
		        templateUrl: __uri('views/announcement/announcement.html'),
		        controller: 'announcementController',
		        resolve: helper.resolveFor('loaders.css', 'spinkit', 'icons')
	    	})
			.state('announcementdetail', {
		        url: '/announcementdetail/:id',
		        title: '公告详情',
		        templateUrl: __uri('views/announcementdetail/announcementdetail.html'),
		        controller: 'announcementdetailController',
		        resolve: helper.resolveFor('loaders.css', 'spinkit', 'icons')
	    	})



			//	    .state('downloadTool', {
			//	        url: '/downloadTool',
			//	        title: '下载工具',
			//	        templateUrl: __uri('views/downloadTool/downloadTool.html'),
			//	        controller: 'downloadToolController',
			//	        resolve: helper.resolveFor('icons')
			//	    })
			//	    .state('billboard', {
			//	        url: '/billboard',
			//	        title: '公告',
			//	        templateUrl: __uri('views/billboard/billboard.html'),
			//	        controller: 'billboardController',
			//	        resolve: helper.resolveFor('icons')
			//	    })


			.state('api', {
				url: '/api',
				abstract: true,
				templateUrl: __uri('views/app.html'),
				controller: 'AppController',
				resolve: helper.resolveFor('fastclick', 'modernizr', 'icons', 'screenfull', 'animo', 'sparklines', 'slimscroll', 'classyloader', 'toaster', 'whirl', 'ngDialog')
			})
			.state('api.dashboard', {
				url: '/dashboard',
				title: '主用户面板',
				templateUrl: __uri('views/dashboard/dashboard.html'),
				controller: 'dashboardController',
				resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
			})

			.state('api.sondashboard', {
				url: '/sondashboard',
				title: '子用户面板',
				templateUrl: __uri('views/dashboard/sondashboard.html'),
				controller: 'sondashboardController',
				resolve: helper.resolveFor('ngDialog')
			})

			.state('api.worker', {
				url: '/workers',
				title: '矿机管理',
				templateUrl: __uri('views/workers/workers.html'),
				controller: 'workersController',
				resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
			})
			.state('api.payrecord', {
				url: '/payrecords',
				title: '支付记录',
				templateUrl: __uri('views/payrecords/payrecords.html'),
				controller: 'payrecordsController',
				resolve: helper.resolveFor('loaders.css', 'spinkit')
			})
			.state('api.newAccounts', {
				url: '/newAccounts',
				title: '用户中心',
				templateUrl: __uri('views/accounts/newAccounts.html'),
				controller: 'newAccountsController'
			})
			.state('api.bindingEmail', {
				url: '/bindingEmail/:userPhone',
				title: '用户中心--绑定邮箱',
				templateUrl: __uri('views/accounts/bindingEmail.html'),
				controller: 'bindingEmailController'
			})
			.state('api.modifyEmail', {
				url: '/modifyEmail/:modifyuserEmail',
				title: '用户中心--修改邮箱',
				templateUrl: __uri('views/accounts/modifyEmail.html'),
				controller: 'modifyEmailController'
			})
			.state('api.bindingPhone', {
				url: '/bindingPhone/:userEmail',
				title: '用户中心--绑定手机',
				templateUrl: __uri('views/accounts/bindingPhone.html'),
				controller: 'bindingPhoneController'
			})
			.state('api.modifyPhone', {
				url: '/modifyPhone/:modifyuserPhone',
				title: '用户中心--修改手机',
				templateUrl: __uri('views/accounts/modifyPhone.html'),
				controller: 'modifyPhoneController'
			})
			.state('api.subaccount', {
				url: '/subaccounts',
				title: '子账号管理(主)',
				templateUrl: __uri('views/subaccounts/subaccounts.html'),
				controller: 'subaccountsController',
				resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
			})

			.state('api.sonSubaccount', {
				url: '/sonSubaccount',
				title: '账号管理(子)',
				templateUrl: __uri('views/subaccounts/sonSubaccounts.html'),
				controller: 'sonSubaccountsController',
				resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
			})
			.state('api.addsubaccount', {
				url: '/addsubaccount',
				title: '创建子账户',
				templateUrl: __uri('views/subaccounts/addsubaccount.html'),
				controller: 'addsubaccountsController'
			})
			.state('api.newcoin', {
				url: '/newcoin',
				title: '新增币种',
				templateUrl: __uri('views/subaccounts/newcoin.html'),
				controller: 'newcoinController',
				params: {data: null},
			})
			.state('switchAccount', {
				url: '/switchAccount',
				title: '主账号切子账号提示界面',
				templateUrl: __uri('views/subaccounts/switchAccount.html'),
				controller: 'switchAccountController'
			})
			.state('transfer', {
				url: '/transfer',
				title: '子账号切主账号提示界面',
				templateUrl: __uri('views/user/transfer.html'),
				controller: 'transferController'
			})
			.state('api.commonProblem', {
				url: '/commonProblem',
				title: '常见问题',
				templateUrl: __uri('views/commonProblem/commonProblem.html'), //loaders.css  'spinkit' 加载图标样式
				controller: 'commonProblemController',
				resolve: helper.resolveFor('loaders.css', 'spinkit')
			})
			.state('api.LoginAfter_Statistics', {
				url: '/LoginAfter_Statistics',
				title: '登录后统计',
				templateUrl: __uri('views/statistics/LoginAfter_Statistics.html'),
				controller: 'statisticsController',
				resolve: helper.resolveFor('loaders.css', 'spinkit', 'icons')
			})

			.state('share', {
				url: '/share/:comId',
				title: '分享',
				templateUrl: __uri('views/share/share.html'),
				controller: 'shareController',
				resolve: helper.resolveFor('icons')
			})
			.state('shareAccountData', {
				url: '/shareAccountData/:comId',
				title: '分享数据',
				templateUrl: __uri('views/share/shareAccountData.html'),
				controller: 'shareAccountDataController'
			})
			.state('shareWorker', {
				url: '/share/:comId/worker',
				title: '分享矿机数据',
				templateUrl: __uri('views/share/shareWorker.html'),
				controller: 'shareWorkerController'
			})
			.state('sharePay', {
				url: '/share/:comId/payrecords',
				title: '分享支付数据',
				templateUrl: __uri('views/share/sharePay.html'),
				controller: 'sharePayController'
			})
			.state('shareStastic', {
				url: '/share/:comId/Statistics',
				title: '分享统计数据',
				templateUrl: __uri('views/share/shareStastic.html'),
				controller: 'shareStasticController'
			})
			.state('shareCommonproblem', {
				url: '/share/:comId/commonproblem',
				title: '分享常见问题',
				templateUrl: __uri('views/share/shareCommonproblem.html'),
				controller: 'shareCommonproblemController'
			})





	}
]).config(['$ocLazyLoadProvider', 'APP_REQUIRES', function ($ocLazyLoadProvider, APP_REQUIRES) {
	'use strict';
	// 延迟加载模块配置
	$ocLazyLoadProvider.config({
		debug: false,
		events: true,
		modules: APP_REQUIRES.modules
	});
}]).config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
	function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
		'use strict';
		// 引导后注册组件
		App.controller = $controllerProvider.register;
		App.directive = $compileProvider.directive;
		App.filter = $filterProvider.register;
		App.factory = $provide.factory;
		App.service = $provide.service;
		App.constant = $provide.constant;
		App.value = $provide.value;
	}
]).config(['$translateProvider', function ($translateProvider) {
	$translateProvider.useStaticFilesLoader({
		prefix: 'app/i18n/',
		suffix: '.json'
	});
	$translateProvider.preferredLanguage('es_AR');
	$translateProvider.useLocalStorage();
	$translateProvider.usePostCompiling(true);
}]).config(['tmhDynamicLocaleProvider', function (tmhDynamicLocaleProvider) {
	tmhDynamicLocaleProvider.localeLocationPattern('vendor/angular-i18n/angular-locale_{{locale}}.js');
}]).config(['cfpLoadingBarProvider', function (cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeBar = true;
	cfpLoadingBarProvider.includeSpinner = false;
	cfpLoadingBarProvider.latencyThreshold = 500;
	cfpLoadingBarProvider.parentSelector = '.wrapper > section';
}]).config(['$tooltipProvider', function ($tooltipProvider) {
	$tooltipProvider.options({
		appendToBody: true
	});
}]);


App.controller('NotifyDemoCtrl', ['$scope', 'Notify', '$timeout',
	function AlertDemoCtrl($scope, Notify, $timeout) {

		$scope.msgHtml = "<em class='fa fa-check'></em> Message with icon..";
		$scope.notifyMsg = "Some messages here..";
		$scope.notifyOpts = {
			status: 'danger',
			pos: 'bottom-center'
		};
		$timeout(function () {
			Notify.alert(
				'This is a custom message from notify..', {
					status: 'success'
				}
			);
		}, 500);
	}
]);

function __uri(path) {
	return path;
}

//最小值
Array.prototype.min = function () {
	var min = this[0];
	var len = this.length;
	for (var i = 1; i < len; i++) {
		if (this[i] < min) {
			min = this[i];
		}
	}
	return min;
}
//最大值
Array.prototype.max = function () {
	var max = this[0];
	var len = this.length;
	for (var i = 1; i < len; i++) {
		if (this[i] > max) {
			max = this[i];
		}
	}
	return max;
}
//判断数组是否存在对应的value
Array.prototype.contains = function (value) {
	var len=this.length
	for (var i = 0; i < len; i++) {
		if (this[i] == value) {
			return true;
		}
	}
	return false;
}

Array.prototype.removeValue = function (value) {
	for (var i = 0, n = 0;i < this.length; i++) {
		if (this[i] != value) {
			this[n++] = this[i]
		}
	}
	this.length -= 1
	
}

Array.prototype.remove = function (dx) {
	if (isNaN(dx) || dx > this.length) { return false; }
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i]
		}
	}
	this.length -= 1
} 