
define([
	
	'jquery',
	'component/Popup',
	'utils',
	
	'jquery-ui',
	
], function($, Popup, Utils) {
	
	
	// 전역 변수
	// 현재 활성화된 widget 객체가 여기에 등록된다.
	var activeList = [],
		widgetSelectorPrefix,
		widgetFullName,
		expando = new Date() * 1,
		uuid = 1,
		rsortnum = /\s(\d+)$/,
		customerSelectKeyword,
		
		convertor = Utils.dataConvert(),
		
		defaultTypeHandlers = {
			"default": function( name, v ) {
				var ele;
				v = v ? v : "";
				ele = getEle.call(this, name);
				ele.length && ele.text( convertor(v, name) );   
			},
			"memo": function(name, v) {
				ele = getEle.call(this, name);
				ele.length && ele.html( convertor(v, name) );
			},
			"subcontractor": function( name, v ) {
				this.find("[data-info='subcontractor']").attr("data-info-key", v.id);
				getEle.call(this, name).text( v.name );
			},
			"manager": function( name, v ) {
				var manager = getEle.call(this, "manager"),
					position = getEle.call(this, "manager.position");
					
				if( v != null ) {
					manager.text(v.name).attr("data-info-key", v.id).attr("data-info", "manager");
					position.text(v.position);
				}
				else {
					manager.text("담당자 없음").attr("data-info-key", "").removeAttr("data-info");
					position.text("");
				}
			},
		}
		;
	
	// data-key 어트리뷰트가 핵심 마킹이다.  실제 값을 입력해주는 공통 메서드
	function setValues( values, handlers ) {
		var self = this;
		handlers = handlers || defaultTypeHandlers;
		$.each(values, function( name, value ) {
			getHandler(name, handlers).call(self, name, value);
		});
	}
	
	function getHandler( name, handlers ) {
		var handler = handlers[name];
			
		if(!handler) return handlers["default"];
		return typeof handler === 'string' ? handlers[handler] : handler;
	};
	
	function getEle( name ) {
		return this.find("[data-key='"+ name + "']");
	};
	
	
	
	function ajax( url, data, handlers, method ) {
		var widget = this;
		
		if(handlers === undefined) {
			handlers = data;
			data = undefined;
		}
		handlers = (typeof handlers === "string") ?
					this.constructor.prototype[handlers] :
					handlers;
		// one Handler == success
		if($.isFunction(handlers)) {
			handlers = { success: handlers };
		}
		return jQuery.ajax( jQuery.extend({
				url: url,
				type: method,		// GET, POST
				dataType: "json",
				//processData: false,
				//traditional: true,
				data: data,
			}, jQuery.isPlainObject( url ) && url ) )
			.success(function(e) { (handlers.success || $.noop).apply(widget, arguments); })
			.fail(function(e) { (handlers.error || $.noop).apply(widget, arguments); })
			.done(function(e) { (handlers.complete || $.noop).apply(widget, arguments); });
	}	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	var widgetOptions =	{
		defaultSort: "_customer",
	},
	
	// -------------------- ★★★ 위젯 확장 ★★★  --------------------
	widgetPrototype = $.extend({}, 
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 기본 위젯 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
	
		version: "1.1",
		options: widgetOptions,
		
		// Constructor
		_create: function() {
			
			// 전역 변수 설정
			widgetFullName = this.widgetFullName;
			widgetSelectorPrefix = this["widgetSelectorPrefix"] = widgetFullName + "-";

			var self = this,
				options = this.options;
				
			// 아이템 확장 프로토타입
			Item.fn.extend( options["extendOfItem"] );
			
			// 거래처 등록
			this.access("customer-selector", $("._customer-select").detach() );
			
			// 각종 정보
			this.access("status", {
				"editor": null,
				"popup": false,	// 오버레이 중인지
			});
			
			/* ***********************  아이템 객체 설정  *********************** */
			this.length = 0;
			
			this.find( "._work-wrap").each(function( _, workElement) {
				self[self.length++] = new Item( workElement, self ).addClass( self.widgetFullName + "-item" );
			});
			
			/* ***********************  초기화 메서드 호출  *********************** */
			this._initUI();
			this._registerEvent();
		},
		
		_init: function() {
		},
		
		// 모두 삭제
		_destroy: function() {
			var items = this.items.getItem(), item = null,
				len = items.length;
			
			while( item = items.pop() ) {
				this.option( "deleteItem" ).call( this,  item.remove() );
				this._reflesh();
			};
			this.items.destroy();
			this.element.remove();
		},
		

		find: function(selector, context) {
			return $(selector, context || this.element);
		},
		
		access: Utils.access,
		
		// 여러가지 상태정보를 저장한다.
		status: function( key, value ) {
			if(key === undefined)
				return this.access( "status" );
			key = "status." + key;
			return value === undefined ? 
					this.access( key ) :
					this.access( key, value );
		},
		
	}, // widgetPrototype
	
		
		
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 아이템 관리 확장메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
		
		getJSON: function( url, data, handlers ) {
			return ajax.call(this, url, data, handlers, "GET" );
		},
		
		postJSON: function( url, data, handlers ) {
			return ajax.call(this, url, data, handlers, "POST" );
		},
		
	}, // advancedPrototype
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ UI  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
		
		_initUI: function() {
			
			var self = this; 
			
			// 에디터탭 미리 만들어놓는다.
			this.editorElement = $("#editor")
								.on("popOn", function(e) {
									self.status("popup", true);
				
								}).on("popOff", function(e) {
									self.status("editor").refresh();
									self.status("editor", null);
									self.status("popup", false);
								});
			
		},
		
		popup: Popup,
		
		// tabs-editor 호출
		// tabs-editor는 Editor엘리먼트에 붙어있으며, 그 엘리먼트는 editorElement 프로퍼티로 관리된다.
		// handlers는 오버레이가 동작할때 사용될 콜백함수다.
		editor: function( options, item ) {
			if( options === false )
				this.editorElement.editorTabs("disable");
			
			// 위젯은 jQuery 루트 객체를 반환하다. 
			// 오버레이는 기본적으로 등록되는 온오프 핸들러 외에, 오버레이에 올려지는 객체마다 설정할 수 있는 온오프 핸들러도 인자로 입력받는다.
			else {
				this.status("editor", item); 
				this.popup( this.editorElement.editorTabs( options ) );
			}
			
			return this;
		},
		
		popInfo: function( $elements, jsonData ) {
			//var self = this;
			setValues.call($elements, jsonData);
			this.popup($elements);
		},
		
		convertor: Utils.dataConvert(),
		
	},
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 이벤트 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
		
		// 프로퍼티 이름은 셀렉터
		// 값으로 함수를 등록하게 되면 widget 이벤트로 등록된다. 프로퍼티는 {이벤트명 셀렉터}가 된다.
		_globalEvent: {
			
		},
		
		
		// 아이템 이벤트 설정
		_registerEvent: function() {
			
			// 각 아이템의 이벤트 객체 (각 아이템의 유일한 식별자인 id별로 배열을 제공하고, 해당 이벤트가 발생하면 등록된 핸들러를 모두 실행해준다.)
			var widget = this,
				itemFn = Item.prototype,
				eventObj = {}
				;
			
			// 아이템 이벤트 등록
			$.each(itemFn.eventHandlers, function(event, handler) {
				handler = typeof handler === 'string' ? itemFn[handler] : handler;
				
				eventObj[event] = function(e) {
					target = $(e.target);
					e.item = target.closest( "." + widgetSelectorPrefix + "item" ).data("instance");
					handler.apply( widget, arguments );
				};
			});
			this._on( eventObj );
			
			
			eventObj = {};
			$.each(this._globalEvent, function(selector, handler) {
				// 오브젝트의 value가 함수라면 위젯 이벤트로 등록
				if($.isFunction(handler)) eventObj[selector] = handler;
				// 그게 아니라면 전역 이벤트로 등록
				else {
					widget._on(selector, handler);
				} 
				
			});
			
			this._on( eventObj );
		},
	}	
	);
	
	// widget 코드 시작
	var selectBtn = $.widget("ui.itemContainer", widgetPrototype);
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ITEM CLASS ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * Item 객체는 각 작업 게시물 하나를 뜻한다.
	 * 이는 jQuery를 상속한 객체이므로, jQuery를 이용하는 것과 동일하게 작업하면 된다.
	 */
	function Item( element, parent ) {
		
		var split, i, obj;
		this[0] = this.element = element.nodeType === 1 ? element : element[0];
		this.length = 1;
		this.parent = parent;
		
		this.data("instance", this);
		
		this.create();
		this.init();
	}
	
	Item.fn = Item.prototype = $();		// jQuery 객체를 상속한다.
	
	
	Item.fn.extend({
		
		// 데이터 접근 
		// 총 3개의 저장소가 있다.
		// 1) Item객체 자체적으로 관리하는 레퍼런스 트리  2) DOM Element의 텍스트 노드  3) jQuert Data객체
		
		// '_'로 시작하는 key는 엘리먼트의 클래스 이름으로 간주한다.
		// 이 경우, Item.refers(레퍼런스 저장소)를 먼저 탐색한 후, 값이 없으면 엘리먼트의 텍스트 노드를 가져온다.
		// 값 입력이라면, value값으로 구분하는데 문자열이라면 엘리먼트의 텍스트로 설정하고, 그 외에는 refers에 등록한다.
		access: access,
		
		create: function() {
			this.id = this.access("workId");		// DB primary Key
			this.customer = this.access("customerId");
			
		},
		
		getValueEle: function() {
			var elems = {};
			$.each(this.find("[data-key]"), function( index, ele ){
				Utils.createObjByName(elems, ele.getAttribute("data-key"), ele);
			});
			return elems;
		},
		
		// 삭제될때 호출
		destroy: function() {
			return this;
		},
		
		// 모든 정보 새로고침
		refresh: $.noop,
		
		editor: function( option ) {
			var item = this,
				widget = this.parent;
				
			// 세션에 작업 아이디 저장
			$.ajax("/admin/work/editor/init/" + this.id)
				.success(function() {
					option = $.extend( option, {"item": item} );
					widget.editor(option, item);
				});
			
		},
		
		URL: function( url ) {
			return url + this.id;
		},
		
		typeHandlers: $.extend( {}, defaultTypeHandlers,
			{
				"afterProcess": function( name, v ) {
					v ? 
						getEle.call(this, 'itemDetail').next().removeClass('ui-helper-hidden') :
						getEle.call(this, 'itemDetail').next().addClass('ui-helper-hidden');
				}, // afterProcess
				
				"memo": function(name, v) {
					v ? this.find('.btn-memo').removeClass('disable') : this.find('.btn-memo').addClass('disable');
					defaultTypeHandlers.memo.call(this, name, v);
				}, // memo
				
				"delivery": function(name, v) {
					var className = ['fa-male', 'fa-cubes', 'fa-truck'],
						ordinal = v.ordinal,
						ele = getEle.call(this, name);
					
					$.each(className, function(i, cName) {
						if( i == ordinal ) {
							ele.attr('alt', v.value);
							ele.addClass(cName);
						}
						else ele.removeClass(cName);
					});
				}, // delivery
			}
		),
		
		values: function( values ) {
			var self = this,
				getter = function( ele ) {
					return ele.textContent;
				};
				
			// 값 입력
			if( values ) setValues.call(this, values, this.typeHandlers);
			else
				Utils.mapCopy( this.getValueEle(), getter );
			
			return values;
		},
		
		// 위젯의 registerHandler 메서드의 편의함수
		setClass: function( classNames, add ) {
			var l = classNames.length;
			
			if( add !== false)
				while(l--)
					this.addClass( classNames[l] );
					
			else
				while(l--)
					this.removeClass( classNames[l] );
		},
		
		getJSON: widgetPrototype.getJSON,
		postJSON: widgetPrototype.postJSON,
		
		init: $.noop,				// 초기화 메서드. 
	});
	
	
	function access( key, value ) {
		var dom = /^[\.#]/.test(key);
		// Get
		if( value === undefined ) {
			if( dom ) return this.find(key).text();
			else return this.data( key );
		}
		
		// Set
		if(dom) this.find(key).text( value );
		else this.data( key, value);
			
		return value;
	};
	
	function log( msg ) {
		console.log( msg );
		return msg;
	}
	
});