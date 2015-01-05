
/*
 *
 * 한컴기획 업무테이블을 만들기 위한 특별 위젯
 * 
 * 이 js 파일에서는 구조적인 컨트롤만 제어한다.
 *  
 */
(function($) {


	// 전역 변수
	// 현재 활성화된 widget 객체가 여기에 등록된다.
	var activeList = [],
		widgetClassPrefix,
		widgetFullName,
		expando = new Date() * 1
		;
		

	// widget 코드 시작
	var selectBtn = $.widget("ui.itemContainer", {
	
		version: "1.1",
		
		options: {
			
			toggle: false,	// 한번에 하나씩만 켜게 할 것인지
			
			// remove버튼 클릭시, itemContainer가 전부 지워지면서, 지워진 객체들이 들어온다.
			deleteItem: function( removeElement ) {
			},
			
			waitingImg: "/img/waiting.gif",
			
			/* *********** Item Container 관련 *********** */
			// 모든 this는 widget 객체
			
			
			// 극초반 호출되는 함수
			preInit: $.noop,
			// 새로 생성된 아이템이 들어온다.
			initItem: $.noop,
			// 위젯 시작할때
			createWidget: $.noop,
			
			//Item 객체의 변화를 감지하는 이벤트
			itemRemoveBefore: function( removeElement ) { },
			itemRemoveAfter: function( removeElement ) { },
			
			// @Override 추가할 container를 제공한다.
			createItem: function() {
				return $("<div>");
			},
			
			bodyClickHandler: $.noop,
			bodyOverHandler: $.noop,
			
		},
		
		
		// Constructor
		_create: function() {
			
			// 전역 변수 설정
			widgetFullName = this.widgetFullName;
			widgetClassPrefix = this["widgetClassPrefix"] = "." + widgetFullName + "-";

			var self = this,
				element = this.element,		// ui-item-container
				selectors = {
					"body": element,
					"controller": document,
				};
			
			$.each(selectors, function( selector, context ) {
				self[selector] = $( widgetClassPrefix + selector, context );
			});
			
			
			this.option("preInit").call(this);
			
			// 아이템 객체를 생성하기 전에, Item 클래스의 prototype객체를 확장할 포인트를 제공한다.
			Item.fn.extend( this.option("extendOfItem") );
			this.items = new ItemManager( this );	// 아이템매니저 생성 :: Item객체들을 관리하는 객체
			
			
			// 위젯이 제공하는 이벤트를 사용하면 handler의 컨텍스트가 나 자신이 된다.
			this._on({"click": function( event ) {
				
				var target = $(event.target);
				event.stopPropagation();
				
			}});
			
			this.controller.on("click", function() { 
											console.log("asa");
											self._controllerClickHandler.apply(self, arguments);
										});
			
			this.option("createWidget").call( this );
			
		},
		
		
		_controllerClickHandler: function( e ) {
			var target = $(event.target);
			if( target.hasClass( widgetFullName + "-btn-add") ) {
				this._addItem();
			}
		},
		
		_find: function(selector, context) {
			return $(selector, context || this.element);
		},
		
		
		// 아이템 생성 -> 추가
		_addItem: function() {
			var child = new Item(this.option("createItem")()[0]);	// 콜백함수에 새로운 컨테이너 엘리먼트를 요청한다.
			this.items.unshift( child );
			this._reflesh();
		}, 
		
		_itemRemoveHandler: function( removeElement ) {
			this._reflesh();
		},
		
		
		/*
		 * 옵션을 바꾸면 한번씩 호출된다.
		 */
		_init: function() {
			this._reflesh();
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
		
		_reflesh: function() {
		
		},
		
	});




	/* ************************* ITEM MANAGER CLASS ************************* */
	function ItemManager( widget ) {

		var self = this, item,
			items = $( widgetClassPrefix + "item", widget.widget() )		// 복수의 컨테이너 엘리먼트를 찾는다.
			;
		
		$.each({
			"widget" : widget,
			"option" : function() { return widget.option.apply( widget, arguments ); },
			"body" : widget.body,
			"uuid" : 1,
			"length": 1,	// ★★★★ each 메서드는 희한하게 값 부분에 0이 들어가면 에러가 난다. ㅜㅜ
		}, function(n, v){
			self.addProp(n, v);
		});
		
		this.length = 0;
		
		// 각 아이템의 jQuery 객체를 배열로 등록한다.
		for(var i=0, l=items.length; i<l; i++) {
			item = new Item( items[i] );
			item["manager"] = this;				// 매니저를 prop로 입력
			item.access("_item-index", i + 1);	// index 기입
			self.register( item );
		}
		
		this.init();
	};
	
	/*
	 * 1) Array native메소드 오버라이드
	 * 2) 메서드 등록
	 */
	$.each({
		
		push: function( ) { return Array.prototype.push.apply(this, arguments); },
		concat: function( ) { return Array.prototype.concat.apply(this, arguments); },
		join: function( e ) { return Array.prototype.join.apply( this, arguments ); },
		splice: function( ) { return Array.prototype.splice.apply( this, arguments); },
		slice: function( ) { return Array.prototype.slice.apply( this, arguments); },
		unshift: function( item ) {
			this[0].before( item );
			var returnValue = Array.prototype.unshift.apply( this, item);
			console.log(this);
		},
		
		data: function( elem, name, data ) {
			return $.data( elem, name, data );
		},
		
		addProp: function( propName, value ) {
			Object.defineProperty( this, propName, { "value": value, writable: true, configurable: true } );
		},
		
		// 여러가지 상태정보를 저장한다.
		status: function( key, value ) {
			
			var status = this["_status"] = this["_status"] || {};
			if( value !== undefined ) {
				status[key] = value;
				return this;
			}
			
			return key ? status[key] : status;
		},
		
		
		// 모든 아이템에 클래스 일괄 등록/해제
		setClass: function( className, fn ) {
			
			var value;
			if( !fn && $.isFunction( className ) ) {
				fn = className;
				className = false;
			}
			
			$.each( this, function() {
				
				// setClass( 콜백함수 )
				// 첫번쨰 인자로 함수가 들어오면, 콜백함수로 컨트롤한다.
				// 콜백함수가 문자열을 리턴할때만 클래스로 등록한다. 이외는 모두 패스
				if(!className) {
					value = fn.call(this);
					if(typeof value === 'string') this.addClass( value );
					return;
				}
				
				// setClass( 문자열, 콜백함수 or Boolean )
				// 콜백함수의 경우, 정확하게 false를 리턴하는 경우에만 해당 className을 삭제한다.
				if( ($.isFunction(fn) && fn.apply(this) === false) || fn === false) {	// 함수가 있으면 그에 따라 바꾼다.
					 this.removeClass( className );
					 return;
				}
				this.addClass( className );
			});
		},
		
		// Item객체의 access를 통해 접근할 수 있는 value를 기준으로 정렬한다.
		sort: function( valueName ) {
			
			if( this.status("sort") === valueName)	// 상태 확인
				return;
			
			var objArray = this.getItem(), target,
			i=0, len = objArray.length,
			sorts = new Array(len),
			rresult = /\s(\d+)$/;
			
			for(;i<len;i++) {
				sorts[i] = objArray[i].access(valueName) + " " + i;	 // objArray[i]는 Item 객체
			}
		
			sorts.sort();	// 정렬
			
			for(i=0;i<len;i++) {
				this[i] = objArray[ rresult.exec( sorts[i] )[1] ];
				this.body.append( this[i] );	// 바로 붙인다.
				this[i].access("_item-index", i + 1);
			}
			
			this.status("sort", valueName);
			return this;
		},
		
		// 아이템 반환
		getItem: function(pos) {
			return pos !== undefined ? this[ this.indexOf( pos) ] : this.slice(0);
		},
		
		// 새로 등록되는 item은 무조건 이 메서드를 지나게 된다. 여기서 초기화를 시키면 된다.
		register: function( item ) {
			
			this.push(item);
			if( !$.contains( this["body"], item ) )	// 아직 DOM에 적용되지 않았으면, 바로 적용한다. 
				this["body"].append( item );
			
			this.option("initItem").call(this.widget, item);
		},
		
		
		// 엘리먼트 삭제 :: 인덱스 || Dom Element
		remove: function(pos) {
			
			pos = this.indexOf( pos );
			var removeElement = this[ pos ],
				agree = this.option("itemRemoveBefore").call( this,  removeElement );
			
			if( agree !== false ) {
				removeElement = this.splice( pos , 1)[0].remove();
				this.widget._itemRemoveHandler.call( this.widget, removeElement );
				this.option("itemRemoveAfter").call( this,  removeElement );
			}
		},
		
		mode: function(mode, pos) {
			return mode ? this[mode]( pos ) : this["currentMode"];
		},
		
		destroy: function() {
		},
		
		// Dom Node로 해당 아이템 위치 찾기
		indexOf: function(elem) {
			
			if(typeof elem === 'number')
				return elem;
			
			elem = elem.nodeType ? elem : elem[0];
			
			var ret = -1, i = 0;
			$.each(this, function() {
				if(this[0] === elem) {
					ret = i;
					return false;
				}
				i++; 
			});
			return ret;
		},
		
		init: function() {
			
			var self = this;
				// 이벤트 타겟을 통해, Item객체를 찾아내는 함수
				catcher = function( target ) {
					target = $(target);
					target = target.hasClass( widgetFullName + "item" ) ?
							target :
							target.closest( widgetClassPrefix + "item" );
					return self.getItem( target );
				};
			
			
			// EVENT REGISTER
			this["body"]
			.on("click", function(e) {
				
				if( self.body[0] === e.target ) return;				
				// 클릭이벤트가 생긴 아이템 객체를 찾아낸다.
				var item = catcher(e.target);
				// 아이템 삭제
				if(item.hasClass( widgetFullName + "delete")) {
					self.remove(item);
					return;
				}
				
				// 콜백 함수 호출
				if( self.option("bodyClickHandler").call( self.widget, e, item ) === false ||
					item.clickHandler.call( item, e ) === false )
					e.stopPropagation();
				
			})
			.on("mouseover", function(e) {
				if( self.body[0] === e.target ) return;
				
				var item = catcher(e.target);
				
				if( self.option("bodyOverHandler").call( self.widget, e, item ) === false ||
					item.overHandler.call( item, e ) === false)
					e.stopPropagation();
			});
		},
		
	}, function(name, fn) {
		
		Object.defineProperty(ItemManager.prototype, name, {value: fn});
		
	});
	
	
	var defineOption = {
		
	};
	
	/* ************************* ITEM CLASS ************************* */
	/*
	 * Item 객체는 각 작업 게시물 하나를 뜻한다.
	 * 이는 jQuery를 상속한 객체이므로, jQuery를 이용하는 것과 동일하게 작업하면 된다.
	 */
	function Item( element ) {
		this[0] = element.nodeType === 1 ? element : element[0];
		this.length = 1;
		this.context = this[0].parentNode;
	}
	
	Item.fn = Item.prototype = $();		// jQuery 객체를 상속한다.
	
	
	Item.fn.extend({
		
		// 데이터 접근
		// '_'로 시작하는 key는 클래스이름으로 간주하고 해당 엘리먼트의 text값을 불러온다.
		access: function( key, value ) {
			
			// 실제 엘리먼트일 경우
			if(key[0] === this.accessPrefix) {
				key = this.find("." + key);
				return value === undefined
					? $.trim( key.text() )
					: key.text( value );
			};	
			
			key = expando + key;
			return value === undefined ?
				( value = this.data( key )) ? value : key
				: this.data( key, value ) && value;		// 값을 입력한 후, 값을 내보내준다.
		},
		
		accessPrefix: "_",	// Item객체의 access메서드로 읽고 쓸 수 있는 엘리먼트(클래스)
		clickHandler: $.noop,
		overHandler: $.noop,
	});
	
})(jQuery);

