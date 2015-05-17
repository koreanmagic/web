
/*
 *
 * 한컴기획 업무테이블을 만들기 위한 특별 위젯
 * 
 */
define([
        "jquery",
        "jquery-ui",
], function($) {

	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	var widgetOptions =	{
			openTab: 0,		
		},
		defaultTabHandler = {
			create: function( globalData ) {},
			// 처음 접근할때 :: (지연 로딩이 가능하다.)
			init: function( data ) {},
			open: function( data, accessNum ) {},
			close: function( data, accessNum ) {},
			disable: function( data ) {},
			_events: {}
		},

proto = $.extend({}, 
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 기본 위젯 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
	
	version: "1.1",
	options: widgetOptions,
	
	_extend: $.noop,
	
	// Constructor
	_create: function() {
		
		var self = this,
			tabsHandlers = this.options.tabsHandlers,
			tabs = this.tabs = [],
			container = tabs.container = this.element.find("> ul").addClass("ui-simpleTabs-container"),
			obj, tab, anchor; 
			
		$.each(container.find("> li"), function(index, v) {
			var pannel, handlers, data;
			obj = {};
			obj["tab"] = tab = $(v);
			anchor = tab.find("> a").addClass("ui-simpleTabs-anchor");
			panel = obj["panel"] = $(anchor.attr("href")).addClass("ui-helper-hidden");
			
			anchor.attr("tabs-id", tabs.push(obj) - 1);	// 이벤트 등에 사용하기 위해 tabs-id를 어트리뷰트로 등록해둔다.
			
			// 각 탭별 create 작업
			handlers = obj["handlers"] = $.extend( {}, defaultTabHandler, tabsHandlers[index] );
			
			// 각 탭 핸들러에 전달될 고정 변수
			data = obj["globalData"] = {
									'handlers':handlers,
									// 각 탭 패널 안에서 검색
									'find': function( selector ) {
										return panel.find(selector);
									},
									'init': function() {
										handlers["init"].call(self, data);
									}
								};
			// 아래 설정되는 data는 매 핸들러 호출때마다 전달된다.
			handlers["create"].call(self, obj["globalData"]);
			
			// 이벤트 등록
			$.each(handlers["_events"], function( selector, handlers ) {
				self._on(panel.find(selector), handlers);
			});
			
		});
		
		this._on({ 
				"click .ui-simpleTabs-anchor": function(e) {
					e.preventDefault();
					this.open( e.currentTarget.getAttribute("tabs-id") );
				}
			});
	},
	
	_init: function() {
		var self = this;
			options = this.options;
		
		// 각 탭스 현황정보
		$.each( this.tabs, function( index, obj ){
			obj["access"] = 0;
			obj["data"] = $.extend({}, obj["globalData"]);
		});
		
		//this.disableTabs( options.disableTabs );	// 비활성화 탭 확인 (동시에 감겨진 탭도 풀게 된다)
		//options.disableTabs = null;					// 사용뒤 지운다. 지우지 않으면 다음번 init시 disableTabs를 설정하지 않으면 이 값이 계속 쓰이게 된다.
		this.open( options.openTab );			// 기본 탭을 연다.
	},
	
	disable: function() {
		var self = this;
		$.each(this.tabs, function( index, map ){
			map.handlers["disable"].call(self, map["data"]);
		});
	},
	
	// 모두 삭제
	_destroy: function() {
	},
	
	_reflesh: function() {
	
	},
	
}, // widgetPrototype

	
/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 확장메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
{
		
	access: function(key, value) {
		
		var ret;
		
		// case get
		if(value === undefined) {
			ret = this._access(key);
		}
		else {
			$.data(this, key, value);
		}
		return ret; 
	},
	
	// 프로퍼티 검색
	_access: function(key) {
		var ret, len, i=0;
		key = key.split(".");
		ret = $.data(this, key.shift());
		len = key.length;				 	// 프로퍼티 탐색
		for(;i<len;i++) {
			ret = ret[ key[i] ];
		}
		return ret;
	},
	
	size: function() {
		return this.tabs.length;
	},
	
	// 열린 탭을 돌려준다.
	open: function( pos ) {
		if(pos === undefined) return;
		
		var obj = this.getTab(pos), 
			tab = obj["tab"],
			panel = obj["panel"],
			handlers = obj["handlers"],
			data = obj["data"];
			
		
		// 사용하 ㄹ수 없는 탭은 없앤다.
		if( tab.hasClass("ui-simpleTabs-disable") ) return
		
		this.close( this.activeTab );
		
		if( !obj["access"]++ ) handlers["init"].call(this, data);
		handlers["open"].call( this, data, obj["access"] );
		
		this.activeTab = pos; 
		tab.addClass("ui-simpleTabs-active");
		panel.removeClass("ui-helper-hidden");
		
		return this;
	},
	
	// 닫힌 탭을 돌려준다.
	close: function( pos ) {
		if(pos === undefined) return;
		pos = this.getTab(pos);
		
		var tab = pos["tab"],
			panel = pos["panel"],
			handlers = pos["handlers"],
			data = pos["data"];
			
		handlers["close"].call(this, data, pos["access"]);	
		
		tab.removeClass("ui-simpleTabs-active");
		panel.addClass("ui-helper-hidden");
		
		return this;
	},
	
	
	// disable은 해당 배열에 등록된 탭은 비활성화로 변경하고, 등록되지 않은 탭은 활성화로 바꾼다.
	disableTabs: function( tabsIndexArray ) {
		
		// 등록된 탭을 순회하면서 해당 탭 인덱스가 파라미터 배열에 포함되어 있으면 disable 클래스를 추가하고
		// 없으면 지운다.
		$.each( this.tabs, function( index, tab ){
			if( tabsIndexArray == null || tabsIndexArray.indexOf( index ) === -1 )
				tab.removeClass("ui-simpleTabs-disable");
			else tab.addClass("ui-simpleTabs-disable");
		});
	},
	
	// disable과 달리 해당 배열에 등록된 탭만 enable로 바꾼다.
	enableTabs: function( tabsIndexArray ) {
		
		if( tabsIndexArray === undefined || !tabsIndexArray.length ) {
			$.each( this.tabs, function( i, tab ) {
				tab.removeClass("ui-simpleTabs-disable");
			});
		} else {
			$.each( tabsIndexArray, function( i, tabIndex ) {
				this.tabs[tabIndex].removeClass("ui-simpleTabs-disable");
			});
		}
		
		return this;
	},
	
	getHandler: function() {
		return this.getTab( this.activeTab )["handlers"];
	},
	getData: function() {
		return this.getTab( this.activeTab )["data"];
	},
	
	getTab: function( pos ) {
		return this.tabs[ pos > this.size() ? this.size() : pos ];
	},
	getHandlerData: function( pos ) {
		return this.getTab(pos)["data"];
	},
},

/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 이벤트  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
{


}
); 
	
	
	// widget 코드 시작
	var selectBtn = $.widget("ui.simpleTabs", proto);

});


