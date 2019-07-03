var homePage = angular.module('homePage', ['ngCookies','pascalprecht.translate']);
// homePage.controller('myCtrl',[ '$cookieStore',function($cookieStore,$scope) {
    
// }]);
homePage.config(['$translateProvider', function ($translateProvider) {
	$translateProvider.useStaticFilesLoader({
		prefix: '/app/i18n/',
		suffix: '.json'
	});
	$translateProvider.preferredLanguage('es_AR');
	$translateProvider.useLocalStorage();
	$translateProvider.usePostCompiling(true);
}])
homePage.controller('myCtrl', ['$translate','$scope','$cookieStore','$http',function($translate,$scope,$cookieStore,$http){
    $http.get(mpoolUI_url + '/userManager/current/getUserInfo').success(function (resp) {
        $scope.username = resp.data.username
        if (resp.data.masterUserId) {
            //子账号管理
            $scope.master = $cookieStore.get("master");
            //子账号
            $scope.userType = 1
            $scope.dashboardUrl="/api/sondashboard"
        } else {
            //主账号
            $scope.userType = 0
            $scope.dashboardUrl="/api/dashboard"
        }
    })
    console.log($translate.instant('nav.homepage'))
    if($translate.instant('nav.homepage') == 'HOME') {
        $scope.cur = 'EN'
    }else{
        $scope.cur = '中'
    }
	$scope.changeLanguage = function(langKey, name) {
        $translate.use(langKey);
        $scope.cur = name
    }
}]);