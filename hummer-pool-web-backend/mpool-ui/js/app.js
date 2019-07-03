var App = angular.module('angle', [
  'ngRoute',
  'ngAnimate',
  'ngStorage',
  'ngCookies',
  'pascalprecht.translate',
  'ui.bootstrap',
  'ui.router',
  'oc.lazyLoad',
  'cfp.loadingBar',
  'ngSanitize',
  'ngResource',
  'tmh.dynamicLocale',
  'ui.utils'
]);

App.run(["$rootScope", "$state", "$stateParams", '$window', '$templateCache',
  function ($rootScope, $state, $stateParams, $window, $templateCache) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$storage = $window.localStorage;
    //页面加载特效
    $rootScope.app = {
      name: '蜂鸟矿池',
      layout: {
        isFixed: true,
        isCollapsed: false,
        isBoxed: false,
        isRTL: false,
        horizontal: false,
        isFloat: false,
        asideHover: false,
        theme: null
      },
      useFullLayout: false,
      hiddenFooter: false,
      viewAnimation: 'ng-fadeInUp'
    };
  }
]);

/**=========================================================
 * Module: constants.js
 * 定义跨应用程序注入的常数
 =========================================================*/
App.constant('APP_COLORS', {
    'primary': '#5d9cec',
    'success': '#27c24c',
    'info': '#23b7e5',
    'warning': '#ff902b',
    'danger': '#f05050',
    'inverse': '#131e26',
    'green': '#37bc9b',
    'pink': '#f532e5',
    'purple': '#7266ba',
    'dark': '#3a3f51',
    'yellow': '#fad732',
    'gray-darker': '#232735',
    'gray-dark': '#3a3f51',
    'gray': '#dde6e9',
    'gray-light': '#e4eaec',
    'gray-lighter': '#edf1f2'
  })
  .constant('APP_MEDIAQUERY', {
    'desktopLG': 1200,
    'desktop': 992,
    'tablet': 768,
    'mobile': 480
  })
  .constant('APP_REQUIRES', {
    // 基于JQuery和独立脚本
    scripts: {
      'whirl': ['js/vendor/whirl/dist/whirl.css'],
      'classyloader': ['js/vendor/jquery-classyloader/js/jquery.classyloader.min.js'],
      'animo': ['js/vendor/animo.js/animo.js'],
      'fastclick': ['js/vendor/fastclick/lib/fastclick.js'],
      'modernizr': ['js/vendor/modernizr/modernizr.js'],
      'animate': ['js/vendor/animate.css/animate.min.css'],
      'icons': ['js/vendor/skycons/skycons.js',
        'js/vendor/fontawesome/css/font-awesome.min.css',
        'js/vendor/simple-line-icons/css/simple-line-icons.css',
        'js/vendor/weather-icons/css/weather-icons.min.css'
      ],
      'sparklines': ['js/vendor/sparklines/jquery.sparkline.min.js'],
      'wysiwyg': ['js/vendor/bootstrap-wysiwyg/bootstrap-wysiwyg.js',
        'js/vendor/bootstrap-wysiwyg/external/jquery.hotkeys.js'
      ],
      'slimscroll': ['js/vendor/slimScroll/jquery.slimscroll.min.js'],
      'screenfull': ['js/vendor/screenfull/dist/screenfull.js'],
      'vector-map': ['js/vendor/ika.jvectormap/jquery-jvectormap-1.2.2.min.js',
        'js/vendor/ika.jvectormap/jquery-jvectormap-1.2.2.css'
      ],
      'vector-map-maps': ['js/vendor/ika.jvectormap/jquery-jvectormap-world-mill-en.js',
        'js/vendor/ika.jvectormap/jquery-jvectormap-us-mill-en.js'
      ],
      'loadGoogleMapsJS': ['js/vendor/gmap/load-google-maps.js'],
      'flot-chart': ['js/vendor/Flot/jquery.flot.js'],
      'flot-chart-plugins': ['js/vendor/flot.tooltip/js/jquery.flot.tooltip.min.js',
        'js/vendor/Flot/jquery.flot.resize.js',
        'js/vendor/Flot/jquery.flot.pie.js',
        'js/vendor/Flot/jquery.flot.time.js',
        'js/vendor/Flot/jquery.flot.categories.js',
        'js/vendor/flot-spline/js/jquery.flot.spline.min.js'
      ],
      // jquery core and widgets
      'jquery-ui': ['js/vendor/jquery-ui/ui/core.js',
        'js/vendor/jquery-ui/ui/widget.js'
      ],
      // loads only jquery required modules and touch support
      'jquery-ui-widgets': ['js/vendor/jquery-ui/ui/core.js',
        'js/vendor/jquery-ui/ui/widget.js',
        'js/vendor/jquery-ui/ui/mouse.js',
        'js/vendor/jquery-ui/ui/draggable.js',
        'js/vendor/jquery-ui/ui/droppable.js',
        'js/vendor/jquery-ui/ui/sortable.js',
        'js/vendor/jqueryui-touch-punch/jquery.ui.touch-punch.min.js'
      ],
      'taginput': ['js/vendor/bootstrap-tagsinput/dist/bootstrap-tagsinput.css',
        'js/vendor/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js'
      ],
      'filestyle': ['js/vendor/bootstrap-filestyle/src/bootstrap-filestyle.js'],
      'loaders.css': ['js/vendor/loaders.css/loaders.css'],
      'spinkit': ['js/vendor/spinkit/css/spinkit.css']
    },
    // Angular based script (use the right module name)
    modules: [{
        name: 'toaster',
        files: ['js/vendor/angularjs-toaster/toaster.js',
          'js/vendor/angularjs-toaster/toaster.css'
        ]
      },
      {
        name: 'localytics.directives',
        files: ['js/vendor/chosen_v1.2.0/chosen.jquery.min.js',
          'js/vendor/chosen_v1.2.0/chosen.min.css',
          'js/vendor/angular-chosen-localytics/chosen.js'
        ]
      },
      {
        name: 'ngDialog',
        files: ['js/vendor/ngDialog/js/ngDialog.min.js',
          'js/vendor/ngDialog/css/ngDialog.min.css',
          'js/vendor/ngDialog/css/ngDialog-theme-default.min.css',
          'js/vendor/ngDialog/css/ngDialog-theme-default2.min.css'
        ]
      },
      {
        name: 'ngWig',
        files: ['js/vendor/ngWig/dist/ng-wig.min.js']
      },
      {
        name: 'ngTable',
        files: ['js/vendor/ng-table/dist/ng-table.min.js',
          'js/vendor/ng-table/dist/ng-table.min.css'
        ]
      },
      {
        name: 'ngTableExport',
        files: ['js/vendor/ng-table-export/ng-table-export.js']
      },
      {
        name: 'angularBootstrapNavTree',
        files: ['js/vendor/angular-bootstrap-nav-tree/dist/abn_tree_directive.js',
          'js/vendor/angular-bootstrap-nav-tree/dist/abn_tree.css'
        ]
      },
      {
        name: 'htmlSortable',
        files: ['js/vendor/html.sortable/dist/html.sortable.js',
          'js/vendor/html.sortable/dist/html.sortable.angular.js'
        ]
      },
      {
        name: 'xeditable',
        files: ['js/vendor/angular-xeditable/dist/js/xeditable.js',
          'js/vendor/angular-xeditable/dist/css/xeditable.css'
        ]
      },
      {
        name: 'angularFileUpload',
        files: ['js/vendor/angular-file-upload/angular-file-upload.js']
      },
      {
        name: 'ngImgCrop',
        files: ['js/vendor/ng-img-crop/compile/unminified/ng-img-crop.js',
          'js/vendor/ng-img-crop/compile/unminified/ng-img-crop.css'
        ]
      },
      {
        name: 'ui.select',
        files: ['js/vendor/angular-ui-select/dist/select.js',
          'js/vendor/angular-ui-select/dist/select.css'
        ]
      },
      {
        name: 'ui.codemirror',
        files: ['js/vendor/angular-ui-codemirror/ui-codemirror.js']
      },
      {
        name: 'angular-carousel',
        files: ['js/vendor/angular-carousel/dist/angular-carousel.css',
          'js/vendor/angular-carousel/dist/angular-carousel.js'
        ]
      },
      {
        name: 'ngGrid',
        files: ['js/vendor/ng-grid/build/ng-grid.min.js',
          'js/vendor/ng-grid/ng-grid.css'
        ]
      },
      {
        name: 'infinite-scroll',
        files: ['js/vendor/ngInfiniteScroll/build/ng-infinite-scroll.js']
      },
      {
        name: 'ui.bootstrap-slider',
        files: ['js/vendor/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js',
          'js/vendor/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css',
          'js/vendor/angular-bootstrap-slider/slider.js'
        ]
      },
      {
        name: 'ui.grid',
        files: ['js/vendor/angular-ui-grid/ui-grid.min.css',
          'js/vendor/angular-ui-grid/ui-grid.min.js'
        ]
      },
      {
        name: 'textAngularSetup',
        files: ['js/vendor/textAngular/src/textAngularSetup.js']
      },
      {
        name: 'textAngular',
        files: ['js/vendor/textAngular/dist/textAngular-rangy.min.js',
          'js/vendor/textAngular/src/textAngular.js',
          'js/vendor/textAngular/src/textAngularSetup.js',
          'js/vendor/textAngular/src/textAngular.css'
        ],
        serie: true
      },
      {
        name: 'angular-rickshaw',
        files: ['js/vendor/d3/d3.min.js',
          'js/vendor/rickshaw/rickshaw.js',
          'js/vendor/rickshaw/rickshaw.min.css',
          'js/vendor/angular-rickshaw/rickshaw.js'
        ],
        serie: true
      },
      {
        name: 'datatables',
        files: ['js/vendor/datatables/media/css/jquery.dataTables.css',
          'js/vendor/datatables/media/js/jquery.dataTables.js',
          'js/vendor/angular-datatables/dist/angular-datatables.js'
        ],
        serie: true
      },
      {
        name: 'angular-jqcloud',
        files: ['js/vendor/jqcloud2/dist/jqcloud.css',
          'js/vendor/jqcloud2/dist/jqcloud.js',
          'js/vendor/angular-jqcloud/angular-jqcloud.js'
        ]
      },
      {
        name: 'ng-nestable',
        files: ['js/vendor/ng-nestable/src/angular-nestable.js',
          'js/vendor/nestable/jquery.nestable.js'
        ]
      },
      {
        name: 'akoenig.deckgrid',
        files: ['js/vendor/angular-deckgrid/angular-deckgrid.js']
      }
    ]
  });
/*messages 数据请求返回 正确 错误提示*/
App.service('Notify', ["$timeout", function ($timeout) {
  this.alert = alert;

  function alert(msg, opts) {
    if (msg) {
      $timeout(function () {
        $.notify(msg, opts || {});
      });
    }
  }
}]);
(function ($, window, document) {
  var containers = {},
    messages = {},
    notify = function (options) {
      if ($.type(options) == 'string') {
        options = {
          message: options
        };
      }
      if (arguments[1]) {
        options = $.extend(options, $.type(arguments[1]) == 'string' ? {
          status: arguments[1]
        } : arguments[1]);
      }
      return (new Message(options)).show();
    },
    closeAll = function (group, instantly) {
      if (group) {
        for (var id in messages) {
          if (group === messages[id].group) messages[id].close(instantly);
        }
      } else {
        for (var id in messages) {
          messages[id].close(instantly);
        }
      }
    };
  var Message = function (options) {
    var $this = this;
    this.options = $.extend({}, Message.defaults, options);
    this.uuid = "ID" + (new Date().getTime()) + "RAND" + (Math.ceil(Math.random() * 100000));
    this.element = $([
      '<div class="uk-notify-message alert-dismissable">',
      '<a class="close">&times;</a>',
      '<div>' + this.options.message + '</div>',
      '</div>'
    ].join('')).data("notifyMessage", this);

    if (this.options.status) {
      this.element.addClass('alert alert-' + this.options.status);
      this.currentstatus = this.options.status;
    }
    this.group = this.options.group;
    messages[this.uuid] = this;
    if (!containers[this.options.pos]) {
      containers[this.options.pos] = $('<div class="uk-notify uk-notify-' + this.options.pos + '"></div>').appendTo('body').on("click", ".uk-notify-message", function () {
        $(this).data("notifyMessage").close();
      });
    }
  };
  $.extend(Message.prototype, {
    uuid: false,
    element: false,
    timout: false,
    currentstatus: "",
    group: false,
    show: function () {
      if (this.element.is(":visible")) return;
      var $this = this;
      containers[this.options.pos].show().prepend(this.element);
      var marginbottom = parseInt(this.element.css("margin-bottom"), 10);
      this.element.css({
        "opacity": 0,
        "margin-top": -1 * this.element.outerHeight(),
        "margin-bottom": 0
      }).animate({
        "opacity": 1,
        "margin-top": 0,
        "margin-bottom": marginbottom
      }, function () {
        if ($this.options.timeout) {
          var closefn = function () {
            $this.close();
          };
          $this.timeout = setTimeout(closefn, $this.options.timeout);
          $this.element.hover(
            function () {
              clearTimeout($this.timeout);
            },
            function () {
              $this.timeout = setTimeout(closefn, $this.options.timeout);
            }
          );
        }
      });
      return this;
    },
    close: function (instantly) {
      var $this = this,
        finalize = function () {
          $this.element.remove();
          if (!containers[$this.options.pos].children().length) {
            containers[$this.options.pos].hide();
          }
          delete messages[$this.uuid];
        };
      if (this.timeout) clearTimeout(this.timeout);
      if (instantly) {
        finalize();
      } else {
        this.element.animate({
          "opacity": 0,
          "margin-top": -1 * this.element.outerHeight(),
          "margin-bottom": 0
        }, function () {
          finalize();
        });
      }
    },
    content: function (html) {
      var container = this.element.find(">div");
      if (!html) {
        return container.html();
      }
      container.html(html);
      return this;
    },
    status: function (status) {
      if (!status) {
        return this.currentstatus;
      }
      this.element.removeClass('alert alert-' + this.currentstatus).addClass('alert alert-' + status);
      this.currentstatus = status;
      return this;
    }
  });
  Message.defaults = {
    message: "",
    status: "normal",
    timeout: 1000,
    group: null,
    pos: 'top-center'
  };
  $["notify"] = notify;
  $["notify"].message = Message;
  $["notify"].closeAll = closeAll;
  return notify;
}(jQuery, window, document));

/**=========================================================
 * Module: main.js
 * 主应用控制器
 =========================================================*/
App.controller('AppController',
  ['$rootScope', '$scope', '$state', '$translate', '$window', '$localStorage', '$timeout', 'toggleStateService', 'colors', 'browser', 'cfpLoadingBar',
    function ($rootScope, $scope, $state, $translate, $window, $localStorage, $timeout, toggle, colors, browser, cfpLoadingBar) {
      "use strict";
      $rootScope.app.layout.horizontal = ($rootScope.$stateParams.layout == 'app-h');
      var thBar;
      $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if ($('.wrapper > section').length)
          thBar = $timeout(function () {
            cfpLoadingBar.start();
          }, 0);
      });
      $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
        event.targetScope.$watch("$viewContentLoaded", function () {
          $timeout.cancel(thBar);
          cfpLoadingBar.complete();
        });
      });

      $rootScope.$on('$stateNotFound',
        function (event, unfoundState, fromState, fromParams) {});
      $rootScope.$on('$stateChangeError',
        function (event, toState, toParams, fromState, fromParams, error) {
          console.log(error);
        });
      $rootScope.$on('$stateChangeSuccess',
        function (event, toState, toParams, fromState, fromParams) {
          $window.scrollTo(0, 0);
          $rootScope.currTitle = $state.current.title;
        });

      $rootScope.currTitle = $state.current.title;
      $rootScope.pageTitle = function () {
        var title = $rootScope.app.name + ' - ' + ($rootScope.currTitle || $rootScope.app.description);
        document.title = title;
        return title;
      };

      $rootScope.$watch('app.layout.isCollapsed', function (newValue, oldValue) {
        if (newValue === false)
          $rootScope.$broadcast('closeSidebarMenu');
      });

      if (angular.isDefined($localStorage.layout))
        $scope.app.layout = $localStorage.layout;
      else
        $localStorage.layout = $scope.app.layout;

      $rootScope.$watch("app.layout", function () {
        $localStorage.layout = $scope.app.layout;
      }, true);

      $scope.colorByName = colors.byName;

      $scope.toggleUserBlock = function () {
        $scope.$broadcast('toggleUserBlock');
      };

      $scope.language = {
        listIsOpen: false,
        available: {
          'es_AR': '中文',
          'en': 'English'
          
        },
        init: function () {
          var proposedLanguage = $translate.proposedLanguage() || $translate.use();
          var preferredLanguage = $translate.preferredLanguage();
          $scope.language.selected = $scope.language.available[(proposedLanguage || preferredLanguage)];
        },
        set: function (localeId, ev) {
          $translate.use(localeId);
          $scope.language.selected = $scope.language.available[localeId];
          $scope.language.listIsOpen = !$scope.language.listIsOpen;
        }
      };
      $scope.language.init();
      toggle.restoreState($(document.body));
      $rootScope.cancel = function ($event) {
        $event.stopPropagation();
      };
      //
      $scope.topNavbar = __uri('views/partials/top-navbar.html');
    }
  ]);

/**=========================================================
 * Module: sidebar-menu.js
 * 处理边栏折叠元素
 =========================================================*/
App.controller('SidebarController', ['$rootScope', '$scope', '$state', '$http', '$timeout', 'Utils',
  function ($rootScope, $scope, $state, $http, $timeout, Utils) {
    var collapseList = [];
    $rootScope.$watch('app.layout.asideHover', function (oldVal, newVal) {
      if (newVal === false && oldVal === true) {
        closeAllBut(-1);
      }
    });

    var isActive = function (item) {
      if (!item) return;
      if (!item.sref || item.sref == '#') {
        var foundActive = false;
        angular.forEach(item.submenu, function (value, key) {
          if (isActive(value)) foundActive = true;
        });
        return foundActive;
      } else
        return $state.is(item.sref) || $state.includes(item.sref);
    };
    $scope.getMenuItemPropClasses = function (item) {
      return (item.heading ? 'nav-heading' : '') +
        (isActive(item) ? ' active' : '');
    };
    //动态请求左边栏数据
    $scope.loadSidebarMenu = function () {
      //var menuJson = 'server/sidebar-menu.json',
      //menuURL  = menuJson + '?v=' + (new Date().getTime());
      $http.get(mpoolUI_url + '/user/menus' + '?v=' + (new Date().getTime()))
        .success(function (items) {
          $scope.menuItems = items.data.menu;
        })
        .error(function (data, status, headers, config) {
          alert('Failure loading menu');
        });
    };
    $scope.loadSidebarMenu();
    $scope.addCollapse = function ($index, item) {
      collapseList[$index] = $rootScope.app.layout.asideHover ? true : !isActive(item);
    };
    $scope.isCollapse = function ($index) {
      return (collapseList[$index]);
    };
    $scope.toggleCollapse = function ($index, isParentItem) {
      if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) return true;
      if (angular.isDefined(collapseList[$index])) {
        if (!$scope.lastEventFromChild) {
          collapseList[$index] = !collapseList[$index];
          closeAllBut($index);
        }
      } else if (isParentItem) {
        closeAllBut(-1);
      }
      $scope.lastEventFromChild = isChild($index);
      return true;
    };

    function closeAllBut(index) {
      index += '';
      for (var i in collapseList) {
        if (index < 0 || index.indexOf(i) < 0)
          collapseList[i] = true;
      }
    }

    function isChild($index) {
      return (typeof $index === 'string') && !($index.indexOf('-') < 0);
    }
  }
]);

App.controller('UserBlockController', ['$scope', function ($scope) {
  $scope.userBlockVisible = true;
  $scope.$on('toggleUserBlock', function (event, args) {
    $scope.userBlockVisible = !$scope.userBlockVisible;
  });
}]);
App.controller('TranslateController',['$translate','$scope', function($translate, $scope) {
  if($translate.instant('nav.homepage') == '首页') {
      $scope.langKeyCur = '中'
  }else{
      $scope.langKeyCur = 'EN'
  }
	$scope.changeLanguage = function(langKey) {
    $translate.use(langKey);
    if(langKey == 'en') {
      $scope.langKeyCur = 'EN'
    }else if(langKey == 'es_AR'){
      $scope.langKeyCur = '中'
    }
  }
}]);
//下拉框指令
App.directive('href', function () {
  return {
    restrict: 'A',
    compile: function (element, attr) {
      return function (scope, element) {
        if (attr.ngClick || attr.href === '' || attr.href === '#') {
          if (!element.hasClass('dropdown-toggle'))
            element.on('click', function (e) {
              e.preventDefault();
              e.stopPropagation();
            });
        }
      };
    }
  };
});

/**=========================================================
 * Module: fullscreen.js
 * 切换全屏模式打开/关闭
 =========================================================*/
App.directive('toggleFullscreen', function () {
  'use strict';
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.on('click', function (e) {
        e.preventDefault();
        if (screenfull.enabled) {
          screenfull.toggle();
          if (screenfull.isFullscreen)
            $(this).children('em').removeClass('fa-expand').addClass('fa-compress');
          else
            $(this).children('em').removeClass('fa-compress').addClass('fa-expand');
        } else {
          $.error('Fullscreen not enabled');
        }
      });
    }
  };
});

/**=========================================================
 * Module panel-tools.js
 * Directive tools to control panels. 
 * Allows collapse, refresh and dismiss (remove)
 * Saves panel state in browser storage
 =========================================================*/
App.directive('paneltool', ["$compile", "$timeout", function ($compile, $timeout) {
    var templates = {
      collapse: "<a href='#' panel-collapse='' tooltip='Collapse Panel' ng-click='{{panelId}} = !{{panelId}}'> \
                <em ng-show='{{panelId}}' class='fa fa-plus'></em> \
                <em ng-show='!{{panelId}}' class='fa fa-minus'></em> \
              </a>",
      dismiss: "<a href='#' panel-dismiss='' tooltip='Close Panel'>\
               <em class='fa fa-times'></em>\
             </a>",
      refresh: "<a href='#' panel-refresh='' data-spinner='{{spinner}}' tooltip='Refresh Panel'>\
               <em class='fa fa-refresh'></em>\
             </a>"
    };

    function getTemplate(elem, attrs) {
      var temp = '';
      attrs = attrs || {};
      if (attrs.toolCollapse)
        temp += templates.collapse.replace(/{{panelId}}/g, (elem.parent().parent().attr('id')));
      if (attrs.toolDismiss)
        temp += templates.dismiss;
      if (attrs.toolRefresh)
        temp += templates.refresh.replace(/{{spinner}}/g, attrs.toolRefresh);
      return temp;
    }
    return {
      restrict: 'E',
      scope: false,
      link: function (scope, element, attrs) {
        var tools = scope.panelTools || attrs;
        $timeout(function () {
          element.html(getTemplate(element, tools)).show();
          $compile(element.contents())(scope);
          element.addClass('pull-right');
        });
      }
    };
  }])
  /**=========================================================
   * Dismiss panels * [panel-dismiss]
   =========================================================*/
  .directive('panelDismiss', ["$q", "Utils", function ($q, Utils) {
    'use strict';
    return {
      restrict: 'A',
      controller: ["$scope", "$element", function ($scope, $element) {
        var removeEvent = 'panel-remove',
          removedEvent = 'panel-removed';
        $element.on('click', function () {
          var parent = $(this).closest('.panel');
          removeElement();

          function removeElement() {
            var deferred = $q.defer();
            var promise = deferred.promise;
            $scope.$emit(removeEvent, parent.attr('id'), deferred);
            promise.then(destroyMiddleware);
          }

          function destroyMiddleware() {
            if (Utils.support.animation) {
              parent.animo({
                animation: 'bounceOut'
              }, destroyPanel);
            } else destroyPanel();
          }

          function destroyPanel() {
            var col = parent.parent();
            parent.remove();
            col
              .filter(function () {
                var el = $(this);
                return (el.is('[class*="col-"]:not(.sortable)') && el.children('*').length === 0);
              }).remove();

            $scope.$emit(removedEvent, parent.attr('id'));
          }
        });
      }]
    };
  }])
  /**=========================================================
   * Collapse panels * [panel-collapse]
   =========================================================*/
  .directive('panelCollapse', ['$timeout', function ($timeout) {
    'use strict';

    var storageKeyName = 'panelState',
      storage;
    return {
      restrict: 'A',
      scope: false,
      controller: ["$scope", "$element", function ($scope, $element) {
        var $elem = $($element),
          parent = $elem.closest('.panel'), // find the first parent panel
          panelId = parent.attr('id');

        storage = $scope.$storage;
        var currentState = loadPanelState(panelId);
        if (typeof currentState !== 'undefined') {
          $timeout(function () {
              $scope[panelId] = currentState;
            },
            10);
        }
        $element.bind('click', function () {

          savePanelState(panelId, !$scope[panelId]);

        });
      }]
    };

    function savePanelState(id, state) {
      if (!id) return false;
      var data = angular.fromJson(storage[storageKeyName]);
      if (!data) {
        data = {};
      }
      data[id] = state;
      storage[storageKeyName] = angular.toJson(data);
    }

    function loadPanelState(id) {
      if (!id) return false;
      var data = angular.fromJson(storage[storageKeyName]);
      if (data) {
        return data[id];
      }
    }
  }])
  /**=========================================================
   * Refresh panels
   * [panel-refresh] * [data-spinner="standard"]
   =========================================================*/
  .directive('panelRefresh', ["$q", function ($q) {
    'use strict';
    return {
      restrict: 'A',
      scope: false,
      controller: ["$scope", "$element", function ($scope, $element) {
        var refreshEvent = 'panel-refresh',
          whirlClass = 'whirl',
          defaultSpinner = 'standard';
        $element.on('click', function () {
          var $this = $(this),
            panel = $this.parents('.panel').eq(0),
            spinner = $this.data('spinner') || defaultSpinner;

          panel.addClass(whirlClass + ' ' + spinner);

          $scope.$emit(refreshEvent, panel.attr('id'));

        });

        $scope.$on('removeSpinner', removeSpinner);

        function removeSpinner(ev, id) {
          if (!id) return;
          var newid = id.charAt(0) == '#' ? id : ('#' + id);
          angular
            .element(newid)
            .removeClass(whirlClass);
        }
      }]
    };
  }]);

/**=========================================================
 * Module: play-animation.js
 * Provides a simple way to run animation with a trigger
 * Requires animo.js
 =========================================================*/
App.directive('animate', ["$window", "Utils", function ($window, Utils) {
  'use strict';
  var $scroller = $(window).add('body, .wrapper');
  return {
    restrict: 'A',
    link: function (scope, elem, attrs) {
      var $elem = $(elem),
        offset = $elem.data('offset'),
        delay = $elem.data('delay') || 100, // milliseconds
        animation = $elem.data('play') || 'bounce';

      if (typeof offset !== 'undefined') {
        testAnimation($elem);
        // test on scroll
        $scroller.scroll(function () {
          testAnimation($elem);
        });
      }

      function testAnimation(element) {
        if (!element.hasClass('anim-running') &&
          Utils.isInView(element, {
            topoffset: offset
          })) {
          element
            .addClass('anim-running');

          setTimeout(function () {
            element
              .addClass('anim-done')
              .animo({
                animation: animation,
                duration: 0.7
              });
          }, delay);

        }
      }
      $elem.on('click', function () {

        var $elem = $(this),
          targetSel = $elem.data('target'),
          animation = $elem.data('play') || 'bounce',
          target = $(targetSel);

        if (target && target.length) {
          target.animo({
            animation: animation
          });
        }

      });
    }
  };

}]);

/**=========================================================
 * Module: scroll.js
 * Make a content box scrollable
 =========================================================*/
App.directive('scrollable', function () {
  return {
    restrict: 'EA',
    link: function (scope, elem, attrs) {
      var defaultHeight = 250;
      elem.slimScroll({
        height: (attrs.height || defaultHeight)
      });
    }
  };
});
/**=========================================================
 * Module: sidebar.js
 * Wraps the sidebar and handles collapsed state
 =========================================================*/
App.directive('sidebar', ['$rootScope', '$window', 'Utils', function ($rootScope, $window, Utils) {
  var $win = $($window);
  var $body = $('body');
  var $scope;
  var $sidebar;
  var currentState = $rootScope.$state.current.name;
  return {
    restrict: 'EA',
    template: '<nav class="sidebar" ng-transclude></nav>',
    transclude: true,
    replace: true,
    link: function (scope, element, attrs) {
      $scope = scope;
      $sidebar = element;
      var eventName = Utils.isTouch() ? 'click' : 'mouseenter';
      var subNav = $();
      $sidebar.on(eventName, '.nav > li', function () {
        if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) {
          subNav.trigger('mouseleave');
          subNav = toggleMenuItem($(this));
          sidebarAddBackdrop();
        }
      });

      scope.$on('closeSidebarMenu', function () {
        removeFloatingNav();
      });
      $win.on('resize', function () {
        if (!Utils.isMobile())
          $body.removeClass('aside-toggled');
      });

      $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        currentState = toState.name;
        $('body.aside-toggled').removeClass('aside-toggled');
        $rootScope.$broadcast('closeSidebarMenu');
      });
      if (angular.isDefined(attrs.sidebarAnyclickClose)) {
        $('.wrapper').on('click.sidebar', function (e) {
          if (!$body.hasClass('aside-toggled')) return;
          if (!$(e.target).parents('.aside').length) {
            $body.removeClass('aside-toggled');
          }

        });
      }

    }
  };

  function sidebarAddBackdrop() {
    var $backdrop = $('<div/>', {
      'class': 'dropdown-backdrop'
    });
    $backdrop.insertAfter('.aside-inner').on("click mouseenter", function () {
      removeFloatingNav();
    });
  }

  function toggleTouchItem($element) {
    $element
      .siblings('li')
      .removeClass('open')
      .end()
      .toggleClass('open');
  }

  function toggleMenuItem($listItem) {
    removeFloatingNav();
    var ul = $listItem.children('ul');
    if (!ul.length) return $();
    if ($listItem.hasClass('open')) {
      toggleTouchItem($listItem);
      return $();
    }
    var $aside = $('.aside');
    var $asideInner = $('.aside-inner');
    var mar = parseInt($asideInner.css('padding-top'), 0) + parseInt($aside.css('padding-top'), 0);
    var subNav = ul.clone().appendTo($aside);
    toggleTouchItem($listItem);
    var itemTop = ($listItem.position().top + mar) - $sidebar.scrollTop();
    var vwHeight = $win.height();
    subNav
      .addClass('nav-floating')
      .css({
        position: $scope.app.layout.isFixed ? 'fixed' : 'absolute',
        top: itemTop,
        bottom: (subNav.outerHeight(true) + itemTop > vwHeight) ? 0 : 'auto'
      });
    subNav.on('mouseleave', function () {
      toggleTouchItem($listItem);
      subNav.remove();
    });
    return subNav;
  }

  function removeFloatingNav() {
    $('.dropdown-backdrop').remove();
    $('.sidebar-subnav.nav-floating').remove();
    $('.sidebar li.open').removeClass('open');
  }
}]);
/**=========================================================
 * Module: skycons.js
 * Include any animated weather icon from Skycons
 =========================================================*/
App.directive('skycon', function () {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var skycons = new Skycons({
        'color': (attrs.color || 'white')
      });
      element.html('<canvas width="' + attrs.width + '" height="' + attrs.height + '"></canvas>');
      skycons.add(element.children()[0], attrs.skycon);
      skycons.play();
    }
  };
});
/**=========================================================
 * Module: sparkline.js
 * SparkLines Mini Charts
 =========================================================*/
App.directive('sparkline', ['$timeout', '$window', function ($timeout, $window) {
  'use strict';
  return {
    restrict: 'EA',
    controller: ["$scope", "$element", function ($scope, $element) {
      var runSL = function () {
        initSparLine($element);
      };
      $timeout(runSL);
    }]
  };

  function initSparLine($element) {
    var options = $element.data();
    options.type = options.type || 'bar'; // default chart is bar
    options.disableHiddenCheck = true;
    $element.sparkline('html', options);
    if (options.resize) {
      $(window).resize(function () {
        $element.sparkline('html', options);
      });
    }
  }
}]);

/**=========================================================
 * Module: table-checkall.js
 * Tables check all checkbox
 =========================================================*/
App.directive('checkAll', function () {
  'use strict';
  return {
    restrict: 'A',
    controller: ["$scope", "$element", function ($scope, $element) {

      $element.on('change', function () {
        var $this = $(this),
          index = $this.index() + 1,
          checkbox = $this.find('input[type="checkbox"]'),
          table = $this.parents('table');
        table.find('tbody > tr > td:nth-child(' + index + ') input[type="checkbox"]')
          .prop('checked', checkbox[0].checked);

      });
    }]
  };

});
/**=========================================================
 * Module: tags-input.js
 * Initializes the tag inputs plugin
 =========================================================*/
App.directive('tagsinput', ["$timeout", function ($timeout) {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function (scope, element, attrs, ngModel) {

      element.on('itemAdded itemRemoved', function () {
        if (ngModel.$viewValue && ngModel.$viewValue.split) {
          ngModel.$setViewValue(ngModel.$viewValue.split(','));
          ngModel.$render();
        }
      });
      $timeout(function () {
        element.tagsinput();
      });
    }
  };
}]);

/**=========================================================
 * Module: toggle-state.js
 * Toggle a classname from the BODY Useful to change a state that 
 * affects globally the entire layout or more than one item 
 * Targeted elements must have [toggle-state="CLASS-NAME-TO-TOGGLE"]
 * User no-persist to avoid saving the sate in browser storage
 =========================================================*/
App.directive('toggleState', ['toggleStateService', function (toggle) {
  'use strict';
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var $body = $('body');
      $(element).on('click', function (e) {
        e.preventDefault();
        var classname = attrs.toggleState;
        if (classname) {
          if ($body.hasClass(classname)) {
            $body.removeClass(classname);
            if (!attrs.noPersist)
              toggle.removeState(classname);
          } else {
            $body.addClass(classname);
            if (!attrs.noPersist)
              toggle.addState(classname);
          }
        }
      });
    }
  };
}]);

/**=========================================================
 * Module: trigger-resize.js
 * 触发任何元素的窗口调整大小事件
 =========================================================*/
App.directive("triggerResize", ['$window', '$timeout', function ($window, $timeout) {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      element.on('click', function () {
        $timeout(function () {
          $window.dispatchEvent(new Event('resize'))
        });
      });
    }
  };
}]);

/**=========================================================
 * Module: validate-form.js
 * 初始化验证插件欧芹
 =========================================================*/
App.directive('validateForm', function () {
  return {
    restrict: 'A',
    controller: ["$scope", "$element", function ($scope, $element) {
      var $elem = $($element);
      if ($.fn.parsley)
        $elem.parsley();
    }]
  };
});

/**=========================================================
 * Module: browser.js
 * 浏览器检测
 =========================================================*/
App.service('browser', function () {
  "use strict";
  var matched, browser;
  var uaMatch = function (ua) {
    ua = ua.toLowerCase();
    var match = /(opr)[\/]([\w.]+)/.exec(ua) ||
      /(chrome)[ \/]([\w.]+)/.exec(ua) ||
      /(version)[ \/]([\w.]+).*(safari)[ \/]([\w.]+)/.exec(ua) ||
      /(webkit)[ \/]([\w.]+)/.exec(ua) ||
      /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
      /(msie) ([\w.]+)/.exec(ua) ||
      ua.indexOf("trident") >= 0 && /(rv)(?::| )([\w.]+)/.exec(ua) ||
      ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) || [];
    var platform_match = /(ipad)/.exec(ua) ||
      /(iphone)/.exec(ua) ||
      /(android)/.exec(ua) ||
      /(windows phone)/.exec(ua) ||
      /(win)/.exec(ua) ||
      /(mac)/.exec(ua) ||
      /(linux)/.exec(ua) ||
      /(cros)/i.exec(ua) || [];
    return {
      browser: match[3] || match[1] || "",
      version: match[2] || "0",
      platform: platform_match[0] || ""
    };
  };
  matched = uaMatch(window.navigator.userAgent);
  browser = {};
  if (matched.browser) {
    browser[matched.browser] = true;
    browser.version = matched.version;
    browser.versionNumber = parseInt(matched.version);
  }
  if (matched.platform) {
    browser[matched.platform] = true;
  }
  if (browser.android || browser.ipad || browser.iphone || browser["windows phone"]) {
    browser.mobile = true;
  }
  if (browser.cros || browser.mac || browser.linux || browser.win) {
    browser.desktop = true;
  }
  if (browser.chrome || browser.opr || browser.safari) {
    browser.webkit = true;
  }
  if (browser.rv) {
    var ie = "msie";
    matched.browser = ie;
    browser[ie] = true;
  }
  if (browser.opr) {
    var opera = "opera";
    matched.browser = opera;
    browser[opera] = true;
  }
  if (browser.safari && browser.android) {
    var android = "android";
    matched.browser = android;
    browser[android] = true;
  }
  browser.name = matched.browser;
  browser.platform = matched.platform;
  return browser;
});
/**=========================================================
 * Module: colors.js
 * 检索全局颜色的服务
 =========================================================*/
App.factory('colors', ['APP_COLORS', function (colors) {
  return {
    byName: function (name) {
      return (colors[name] || '#fff');
    }
  };
}]);

/**=========================================================
 * Module: helpers.js
 * Provides helper functions for routes definition
 =========================================================*/

App.provider('RouteHelpers', ['APP_REQUIRES', function (appRequires) {
  "use strict";
  this.basepath = function (uri) {
    return 'app/views/' + uri;
  };
  this.resolveFor = function () {
    var _args = arguments;
    return {
      deps: ['$ocLazyLoad', '$q', function ($ocLL, $q) {
        var promise = $q.when(1); // empty promise
        for (var i = 0, len = _args.length; i < len; i++) {
          promise = andThen(_args[i]);
        }
        return promise;

        function andThen(_arg) {
          if (typeof _arg == 'function')
            return promise.then(_arg);
          else
            return promise.then(function () {
              // if is a module, pass the name. If not, pass the array
              var whatToLoad = getRequired(_arg);
              // simple error check
              if (!whatToLoad) return $.error('Route resolve: Bad resource name [' + _arg + ']');
              // finally, return a promise
              return $ocLL.load(whatToLoad);
            });
        }

        function getRequired(name) {
          if (appRequires.modules)
            for (var m in appRequires.modules)
              if (appRequires.modules[m].name && appRequires.modules[m].name === name)
                return appRequires.modules[m];
          return appRequires.scripts && appRequires.scripts[name];
        }
      }]
    };
  };
  this.$get = function () {
    return {
      basepath: this.basepath
    }
  };
}]);

/**=========================================================
 * Module: toggle-state.js
 * Services to share toggle state functionality
 =========================================================*/
App.service('toggleStateService', ['$rootScope', function ($rootScope) {
  var storageKeyName = 'toggleState';
  var WordChecker = {
    hasWord: function (phrase, word) {
      return new RegExp('(^|\\s)' + word + '(\\s|$)').test(phrase);
    },
    addWord: function (phrase, word) {
      if (!this.hasWord(phrase, word)) {
        return (phrase + (phrase ? ' ' : '') + word);
      }
    },
    removeWord: function (phrase, word) {
      if (this.hasWord(phrase, word)) {
        return phrase.replace(new RegExp('(^|\\s)*' + word + '(\\s|$)*', 'g'), '');
      }
    }
  };
  return {
    addState: function (classname) {
      var data = angular.fromJson($rootScope.$storage[storageKeyName]);

      if (!data) {
        data = classname;
      } else {
        data = WordChecker.addWord(data, classname);
      }
      $rootScope.$storage[storageKeyName] = angular.toJson(data);
    },
    removeState: function (classname) {
      var data = $rootScope.$storage[storageKeyName];
      if (!data) return;
      data = WordChecker.removeWord(data, classname);
      $rootScope.$storage[storageKeyName] = angular.toJson(data);
    },
    restoreState: function ($elem) {
      var data = angular.fromJson($rootScope.$storage[storageKeyName]);
      if (!data) return;
      $elem.addClass(data);
    }
  };
}]);
/**=========================================================
 * Module: utils.js
 * 实用库跨主题使用
 =========================================================*/

App.service('Utils', ["$window", "APP_MEDIAQUERY", function ($window, APP_MEDIAQUERY) {
  'use strict';
  var $html = angular.element("html"),
    $win = angular.element($window),
    $body = angular.element('body');
  return {
    // DETECTION
    support: {
      transition: (function () {
        var transitionEnd = (function () {
          var element = document.body || document.documentElement,
            transEndEventNames = {
              WebkitTransition: 'webkitTransitionEnd',
              MozTransition: 'transitionend',
              OTransition: 'oTransitionEnd otransitionend',
              transition: 'transitionend'
            },
            name;
          for (name in transEndEventNames) {
            if (element.style[name] !== undefined) return transEndEventNames[name];
          }
        }());

        return transitionEnd && {
          end: transitionEnd
        };
      })(),
      animation: (function () {
        var animationEnd = (function () {
          var element = document.body || document.documentElement,
            animEndEventNames = {
              WebkitAnimation: 'webkitAnimationEnd',
              MozAnimation: 'animationend',
              OAnimation: 'oAnimationEnd oanimationend',
              animation: 'animationend'
            },
            name;

          for (name in animEndEventNames) {
            if (element.style[name] !== undefined) return animEndEventNames[name];
          }
        }());
        return animationEnd && {
          end: animationEnd
        };
      })(),
      requestAnimationFrame: window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        window.msRequestAnimationFrame ||
        window.oRequestAnimationFrame ||
        function (callback) {
          window.setTimeout(callback, 1000 / 60);
        },
      touch: (
        ('ontouchstart' in window && navigator.userAgent.toLowerCase().match(/mobile|tablet/)) ||
        (window.DocumentTouch && document instanceof window.DocumentTouch) ||
        (window.navigator['msPointerEnabled'] && window.navigator['msMaxTouchPoints'] > 0) || //IE 10
        (window.navigator['pointerEnabled'] && window.navigator['maxTouchPoints'] > 0) || //IE >=11
        false
      ),
      mutationobserver: (window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver || null)
    },
    // 公用事业
    isInView: function (element, options) {

      var $element = $(element);

      if (!$element.is(':visible')) {
        return false;
      }

      var window_left = $win.scrollLeft(),
        window_top = $win.scrollTop(),
        offset = $element.offset(),
        left = offset.left,
        top = offset.top;

      options = $.extend({
        topoffset: 0,
        leftoffset: 0
      }, options);

      if (top + $element.height() >= window_top && top - options.topoffset <= window_top + $win.height() &&
        left + $element.width() >= window_left && left - options.leftoffset <= window_left + $win.width()) {
        return true;
      } else {
        return false;
      }
    },
    langdirection: $html.attr("dir") == "rtl" ? "right" : "left",
    isTouch: function () {
      return $html.hasClass('touch');
    },
    isSidebarCollapsed: function () {
      return $body.hasClass('aside-collapsed');
    },
    isSidebarToggled: function () {
      return $body.hasClass('aside-toggled');
    },
    isMobile: function () {
      return $win.width() < APP_MEDIAQUERY.tablet;
    }
  };
}]);

App.filter('cut', function () { //可以注入依赖
  return function (value, wordwise, max, tail) {
    if (!value) return '';

    max = parseInt(max, 10);
    if (!max) return value;
    if (value.length <= max) return value;

    value = value.substr(0, max);
    if (wordwise) {
      var lastspace = value.lastIndexOf(' ');
      if (lastspace != -1) {
        value = value.substr(0, lastspace);
      }
    }

    return value + (tail || ' …');
  };
});
/**
 * 判断一个变量是不是 无效值 无效值返回true 有效值 false
 * @param {*} arg1 
 */
function isNull(arg1) {
  if (typeof arg1 === "string") {
    return arg1 == "null" || arg1 == "undefined" || arg1 == ""
  }
  return !arg1 && arg1 !== 0 && typeof arg1 !== "boolean" ? true : false;
}