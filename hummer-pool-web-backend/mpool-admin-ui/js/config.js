App.config(function($httpProvider) {
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
	$httpProvider.defaults.headers["put"] = {
        'Content-Type': 'application/json;charset=utf-8'
    };
	//在这里构造拦截器
	var interceptor = function($q, $rootScope,$location,$window,$cookieStore,$timeout,Notify) {
		var i = 1;
		return {
			'request': function(req) {
				req.params = req.params || {};
				if ($cookieStore.get('userName')) {
					req.headers.name = $cookieStore.get('userName');
				}else{
					if(req.method == "POST"){
						
					}else{
						//控制注销后点击浏览器后退按钮跳入到系统中
						console.log($location.$$url)
						if($location.$$url =='/login'){
							$location.path('/login');
						}else if(($location.$$url != '/resetPassword') && ($location.$$url != '/homePage') && ($location.$$url != '/billboard') && ($location.$$url != '/downloadTool') && ($location.$$url != '/statistics')){
							$location.path('/login');
						}
					}
				}
				return req || $q.when(req);
			},
			'response': function (res) {
				//系统超时自动跳入登录界面
				if(i == 1){   //判断执行一次超时提示
					if (res.data.msg == "err" || res.data.code == '-1') {
						i += 1;
						$cookieStore.remove('nickName');
				    	Notify.alert( 
				        	'系统超时，请重新登录..', 
				        	{status: 'success'}
				    	);
					  	$timeout(function () {
							$location.path('/login');
						}, 2000);
					}
				}
				return res;
			}
		}
	};
	var exceptionInterceptor = function($q, $rootScope,$location,$window,$cookieStore,$timeout,Notify) {
		var i = 1;
		return {
			'request': function(req) {
				
				return req || $q.when(req);
			},
			'response': function (res) {
				
				if(!/.*html$/.test(res.config.url)){
					if(res.data.code){
						switch (res.data.code) {
							case '0':
								break;
							case '500':
								Notify.alert( '系统错误', {status: 'info'});
								break;
							case '-1':
								break;
							default:
								Notify.alert( res.data.msg, {status: 'info'})
								break;
						}
					}
					
				}
			
				
				return res;
			}
		}
	};
	$httpProvider.interceptors.push(interceptor);
	$httpProvider.interceptors.push(exceptionInterceptor);
});

App.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', 'RouteHelpersProvider',
	function ($stateProvider, $locationProvider, $urlRouterProvider, helper) {
  		'use strict';
  		$locationProvider.html5Mode(true);
  		$urlRouterProvider.otherwise('/login');

  	$stateProvider
  	
  		//登录  路由
    	.state('login', {
	        url: '/login',
	        title: '登录',
	        templateUrl: __uri('views/user/login.html'),
	        controller: 'loginController',
	        resolve: helper.resolveFor('modernizr', 'icons','loaders.css', 'spinkit')
	    })
	    
//---------------------------系统管理员路由
  		.state('admin', {
        	url: '/admin',
        	abstract: true,
       		templateUrl:  __uri('views/app.html'),
        	controller: 'AppController',
        	resolve: helper.resolveFor('fastclick', 'modernizr', 'icons', 'screenfull', 'animo', 'sparklines', 'slimscroll', 'classyloader', 'toaster', 'whirl')
   		})
		.state('admin.dashboard', {
        	url: '/dashboard',
        	title: '用户面板',
        	templateUrl:  __uri('views/dashboard/dashboard.html'),
			controller: 'dashboardController',
			resolve: helper.resolveFor('loaders.css', 'spinkit','ngDialog')
    	})
  		.state('admin.account_user', {
        	url: '/account_user',
        	title: '用户管理',
        	templateUrl:  __uri('views/userManagement/userManagement.html'),
			controller: 'userManagementController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'xeditable','ngDialog')
    	})
  		.state('admin.account_info', {
        	url: '/account_info',
        	title: '用户资料',
        	templateUrl:  __uri('views/account_info/account_info.html'),
			controller: 'account_infoController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'xeditable','ngDialog')
    	})
  		
  		
  		.state('admin.userAccountDetails', {
        	url: '/userAccountDetails/:id',
        	title: '用户账号详情',
        	templateUrl:  __uri('views/userManagement/userAccountDetails.html'),
			controller: 'userAccountDetailsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'xeditable','ngDialog')
    	})
  		.state('admin.sys_bill', {
        	url: '/sys_bill',
        	title: '支付账单',
        	templateUrl:  __uri('views/paybill/paybill.html'),
			controller: 'paybillController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
  		
  		.state('admin.bill_create', {
        	url: '/bill_create',
        	title: '创建支付单',
        	templateUrl:  __uri('views/billcreate/billcreate.html'),
			controller: 'billcreateController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
  		
   		.state('admin.pool_IncomeSettlement', {
        	url: '/pool_IncomeSettlement',
        	title: '支付',
        	templateUrl:  __uri('views/incomeSettlement/incomeSettlement.html'),
			controller: 'incomeSettlementController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.incomeSettlementDetails', {
        	url: '/incomeSettlementDetails/:id',
        	title: '支付详情',
        	templateUrl:  __uri('views/incomeSettlement/incomeSettlementDetails.html'),
			controller: 'incomeSettlementDetailsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		
   		.state('admin.setting', {
        	url: '/setting',
        	title: '预警',
        	templateUrl:  __uri('views/settings/settingsList.html'),
			controller: 'settingsListController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.settingedit', {
        	url: '/settingedit/:id',
        	title: '预警编辑',
        	templateUrl:  __uri('views/settings/settings.html'),
			controller: 'settingsController'
    	})
   		.state('admin.addSettings', {
        	url: '/addSettings',
        	title: '添加预警',
        	templateUrl:  __uri('views/settings/addSettings.html'),
			controller: 'addSettingsController'
    	})
   		.state('admin.notice', {
        	url: '/notice',
        	title: '通知公告',
        	templateUrl:  __uri('views/notice/notice.html'),
			controller: 'noticeController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.noticeDetails', {
        	url: '/noticeDetails/:id',
        	title: '通知详情',
        	templateUrl:  __uri('views/notice/noticeDetails.html'),
			controller: 'noticeDetailsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.ratio', {
        	url: '/ratio',
        	title: '矿工费设置',
        	templateUrl:  __uri('views/ratio/ratio.html'),
			controller: 'ratioController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.pool_fpps', {
        	url: '/pool_fpps',
        	title: '挖矿费率',
        	templateUrl:  __uri('views/pool_fpps/pool_fpps.html'),
			controller: 'pool_fppsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		
   		.state('admin.pool_log', {
        	url: '/pool_log',
        	title: '矿池日志',
        	templateUrl:  __uri('views/poolLog/poolLog.html'),
			controller: 'poolLogController',
			resolve: helper.resolveFor('loaders.css', 'spinkit')
    	})
   		
   		
   		.state('admin.account_user_list', {
        	url: '/account_user_list',
        	title: '用户列表',
        	templateUrl:  __uri('views/accountuserlist/accountUserList.html'),
			controller: 'accountUserListController',
			resolve: helper.resolveFor('loaders.css', 'spinkit')
    	})
		.state('admin.accountUserListDetails', {
        	url: '/accountUserListDetails/:id',
        	title: '用户列表详情',
        	templateUrl:  __uri('views/accountuserlist/accountUserListDetails.html'),
			controller: 'accountUserListDetailsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit')
    	})
   		
   		.state('admin.sys_role', {
        	url: '/sys_role',
        	title: '角色管理',
        	templateUrl:  __uri('views/sysrole/sysrole.html'),
			controller: 'sysroleController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
   		.state('admin.sys_menu', {
        	url: '/sys_menu',
        	title: '菜单管理',
        	templateUrl:  __uri('views/sysmenu/sysmenu.html'),
			controller: 'sysmenuController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
    	.state('admin.sys_user', {
        	url: '/sys_user',
        	title: '系统用户管理',
        	templateUrl:  __uri('views/sysuser/sysuser.html'),
			controller: 'sysuserController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})
    	.state('admin.sys_user_edit', {
        	url: '/sys_user_edit/:id',
        	title: '系统用户管理 编辑',
        	templateUrl:  __uri('views/sysuser/sysuserEdit.html'),
			controller: 'sysuserEditController'
    	})
    	.state('admin.add_sys_user', {
        	url: '/add_sys_user',
        	title: '添加   系统用户',
        	templateUrl:  __uri('views/sysuser/addSysuser.html'),
			controller: 'addSysuserController'
    	})
		.state('admin.sys_perms', {
        	url: '/sys_perms',
        	title: '权限设置',
        	templateUrl:  __uri('views/sysperms/sysperms.html'),
			controller: 'syspermsController',
			resolve: helper.resolveFor('loaders.css', 'spinkit', 'ngDialog')
    	})

}]).config(['$ocLazyLoadProvider', 'APP_REQUIRES', function ($ocLazyLoadProvider, APP_REQUIRES) {
    'use strict';
    // 延迟加载模块配置
    $ocLazyLoadProvider.config({
      	debug: false,
      	events: true,
      	modules: APP_REQUIRES.modules
    });
}]).config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ( $controllerProvider, $compileProvider, $filterProvider, $provide) {
      'use strict';
      // 引导后注册组件
      App.controller = $controllerProvider.register;
      App.directive  = $compileProvider.directive;
      App.filter     = $filterProvider.register;
      App.factory    = $provide.factory;
      App.service    = $provide.service;
      App.constant   = $provide.constant;
      App.value      = $provide.value;
}]).config(['$translateProvider', function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix : 'app/i18n/',
        suffix : '.json'
    });
    $translateProvider.preferredLanguage('es_AR');
    $translateProvider.useLocalStorage();
    $translateProvider.usePostCompiling(true);
}]).config(['tmhDynamicLocaleProvider', function (tmhDynamicLocaleProvider) {
    tmhDynamicLocaleProvider.localeLocationPattern('vendor/angular-i18n/angular-locale_{{locale}}.js');
}]).config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeBar = true;
    cfpLoadingBarProvider.includeSpinner = false;
    cfpLoadingBarProvider.latencyThreshold = 500;
    cfpLoadingBarProvider.parentSelector = '.wrapper > section';
}]).config(['$tooltipProvider', function ($tooltipProvider) {
    $tooltipProvider.options({appendToBody: true});
}]);


App.controller('NotifyDemoCtrl', ['$scope', 'Notify', '$timeout', 
	function AlertDemoCtrl($scope, Notify, $timeout) {

  		$scope.msgHtml = "<em class='fa fa-check'></em> Message with icon..";
	  	$scope.notifyMsg = "Some messages here..";
	  	$scope.notifyOpts = {
	    	status: 'danger',
	    	pos: 'bottom-center'
	  	};
	  	$timeout(function(){
	   		Notify.alert( 
	        	'This is a custom message from notify..', 
	        	{status: 'success'}
	    	);
	  	}, 500);
	}
]);

function __uri(path){
	return path;
}

function toString(obj){
	return obj+""
}