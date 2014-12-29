
/*
 *
 * 한컴기획 업무테이블을 만들기 위한 특별 위젯
 * 
 * 1) <UL class="ui-item-container">가 전체박스가 된다.
 * 2) 그 하위에는 <LI class="ui-item-header">가 컨트롤 박스이자, 기본 인터페이스가 된다. (이를 통해 열기/닫기 구현)
 * 3) 그리고 그 다음 <LI class="ui-item-body">
 * 
 *  
 * btn-toggle-open	 	:: 컨테이너 열기 버튼
 * btn-toggle-close		:: 컨데이터 닫기 버튼
 * btn-delete			:: 리스트 아예 삭제
 * btn-add				:: 컨테이터 추가
 * header 				:: 컨트롤 키 있는 엘리먼트
 * body					:: 컨테이너 엘리먼트들의 직속 부모
 * length				:: 컨테이너 갯수를 적어두는 엘리먼트
 */
(function($) {


	// 전역 변수
	// 현재 활성화된 widget 객체가 여기에 등록된다.
	var activeList = [],
		prefix = ".ui-item-";
		
	// 해당 엘리먼트가 열려있는지 확인
	// activeList는 클로저 전역변수이며, 열려있는 엘리먼트만을 원소로 가진다.
	function isOpen( instance ) {
		return !!(activeList.indexOf(instance) != -1);
	};

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
			
			// 새로 생성된 아이템이 들어온다.
			initItem: function( $item ) { },
			
			//Item 객체의 변화를 감지하는 이벤트
			itemRemoveBefore: function( removeElement ) { },
			itemRemoveAfter: function( removeElement ) { },
			
			// @Override 추가할 container를 제공한다.
			createItem: function() {
				return $("<div>");
			},
			
			itemClickHandler: function( e, $item ) { },
			
		},
		
		
		// Constructor
		_create: function() {
			
			var self = this,
				element = this.element,		// ui-item-container
				selects = ["btn-toggle-open", "btn-toggle-close", "btn-delete", "btn-add",
							"header", "body", "length"
						  ],
				firstHide = ["body", "btn-toggle-close"];
			
			
			// Register
			$.each(selects ,function(i, v) {
					self[ v ] = self._find(prefix +  v);
					
					// 아이템 목록이 있는 li나 클로즈 버튼은 바로 감추어 놓는다.
					if( firstHide.indexOf( v ) != -1 ) {
						self[ v ].hide();
					} 
				});
			
			
			this.items = new Item( this );	// 아이템박스 수집
			
			
			// 위젯이 제공하는 이벤트를 사용하면 handler의 컨텍스트가 나 자신이 된다.
			this._on({"click": function( event ) {
				
				var target = $(event.target);
				event.stopPropagation();
				
				// 컨테이너 박스 추가
				if ( target.hasClass("ui-item-btn-add") ) {
					self._addItem();
				}
				// 리스트 삭제
				else if ( target.hasClass("ui-item-btn-delete") ) {
					self.destroy();
				}
				// 컨테이너 BODY 열기 닫기 ::: ㅡheadline이나 오픈/닫기 버튼을 클릭했을때
				else if( (jQuery.contains(this["header"][0], event.target) && !isOpen(self))
						|| target.is("[class*=ui-item-btn-toggle-]")) {
					self.toggle();
				} 
			}});
			
		},
		
		
		_find: function(selector, context) {
			return $(selector, context || this.element);
		},
		
		// body 엘리먼트의 토글 버튼
		toggle: function() {
			if(isOpen(this)) {
				this.close();
			} else {
				if(this.toggleType()) // 토글타입이고 현재 열려진 리스트가 있으면
					this.closeAll();
				this.open();
			}
		},
		
		// toggle = true 이면, A가 열린 상태라 할때 B를 클릭하면 A가 자동으로 닫힌다.
		// false 이면, 제한없이 모두 열린다.  
		toggleType: function() {
			return this.options.toggle;
		},
		
		open: function() {
			if(isOpen(this))
				return;
			
			this["body"].show();
			this["btn-toggle-open"].hide();		// 열기 아이콘을 비활성화시키고
			this["btn-toggle-close"].show();		// 닫기 아이콘을 활성화시킨다.
			this.element.addClass("active");
			
			activeList.push(this);						// 전역 배열에 추가한다.
		},
		
		close: function() {
			if(!isOpen(this))
				return;
				
			this["body"].hide();
			this["btn-toggle-close"].hide();
			this["btn-toggle-open"].show();
			this.element.removeClass("active");
			
			if(activeList.length)
				activeList.splice(activeList.indexOf(this), 1);
		},
		
		closeAll: function() {
			if(activeList.length) {
				$.each(activeList, function() { this.close(); } );
			}
		},
		
		// item container 관련
		_addItem: function() {
			var child = this.option("createItem")();	// 콜백함수에 새로운 컨테이너 엘리먼트를 요청한다.
			this.items.add( child );
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
			var items = this.items.get(), item = null,
				len = items.length;
			
			while( item = items.pop() ) {
				this.option( "deleteItem" ).call( this,  item.remove() );
				this._reflesh();
			};
			this.items.destroy();
			this.element.remove();
		},
		
		_reflesh: function() {
			this._workCount();
		},
		
		// 작업 갯수 산정
		_workCount: function() {
			var boxies = this._find(".ui-item-item", this.element);	// 모든 아이템 컨테이너 찾기
			
			var i=0, done=0;
			while(boxies[i++])	// 다 끝난 작업물 확인
				if(boxies.hasClass("_state-confirm")) done++;
			
			this["length"].text(boxies.length);
		},
	});


	/*
	 * 아이템 컨테이너 관리 객체.
	 * 내부 배열에 아이템 컨테이너들을 담고 있다.
	 * 기본적으로 삭제할 수 있는 버튼을 제공한다.
	 * 
	 */
	var propClass = ["index"];
	
	// context :: widget 객체
	function Item( widget ) {

		var self = this,
			items = $( prefix + "item", widget.widget() )		// 복수의 컨테이너 엘리먼트를 찾는다. 
			;
		
		this["widget"] = widget;
		// 위젯의 옵션은 Item에서도 그대로 사용하게 된다.
		this["option"] = function() { return widget.option.apply( widget, arguments ); };
		this["body"] = widget.body;
		this["items"] = [];
		this["uuid"] = 1;
		
		var item;
		// 각 아이템의 jQuery 객체를 배열로 등록한다.
		for(var i=0, l=items.length; i<l; i++) {
			item = items[i];
			self.add( item );
		}
		
		this.init();
	};
	
	// prototype에 등록되는 메서드들을 감추기 위한 Object.defineProperty에 쓰일 옵션값들
	var defineOption = {
		writable: false,
		enumerable: false,
		configurable: false
	};
	
	/*
	 * 1) Array native메소드 오버라이드
	 * 2) 메서드 등록
	 */
	$.each({
		
		// prefix : &- :: 각 아이템 원소 jQuery 객체가 컨텍스트가 되는 함수 
		$push: function() {
			console.log(this);
		},
		
		notice: function() {
			
		},
		
		// 아이템 반환
		get: function(pos) {
			return pos !== undefined ? this.items[pos] : this.items;
		},
		
		// 들어오는건 
		add: function( ele ) {
			
			var self = this,
				length = self.length(),
				item = this.items[ length ] = $( ele )
				;
			
			// 관리해야할 엘리먼트 등록
			$.each( propClass, function(i, v) {
				item[ v ] = $( prefix + v, item ).text( self.uuid++ );
			});
			
			if( !$.contains( this["body"], item ) )	// 없다면
				this["body"].append( item );
			
			this.option("initItem").call(this.widget, item);
		},
		
		// 엘리먼트 삭제 :: 인덱스 || Dom Element
		remove: function(pos) {
			pos = this.index(pos);
			var removeElement = this.items[ pos ],
				agree = this.option("itemRemoveBefore").call( this,  removeElement );
			
			if( agree !== false ) {
				removeElement = this.items.splice( pos , 1)[0].remove();
				this.widget._itemRemoveHandler.call( this.widget, removeElement );
				this.option("itemRemoveAfter").call( this,  removeElement );
			}
			
		},
		
		mode: function(mode, pos) {
			return mode ? this[mode]( pos ) : this["currentMode"];
		},
		
		destroy: function() {
			delete this.items;
		},
		
		// Dom Node로 해당 아이템 위치 찾기
		index: function(elem) {
			
			if(typeof elem === 'number')
				return elem;
			
			elem = elem.nodeType ? elem : elem[0];
			
			var ret = -1, i = 0;
			$.each(this.items, function() {
				if(this[0] === elem) {
					ret = i;
					return false;
				}
				i++; 
			});
			return ret;
		},
		
		// 엘리먼트 갯수
		length: function() {
			return this.items.length;
		},
		
		init: function() {
			
			var self = this;
			
			this["body"].on("click", function(e) {
				
				if( self.body[0] === e.target ) return;				
				
				var $target = $(e.target),
					item = $target.hasClass("ui-item-item") ?
									$target :
									$target.closest(".ui-item-item");
				
				if($target.hasClass("ui-item-delete")) {
					self.remove(item);
					return;
				}
				
				// 콜백 함수 호출
				if( self.option("itemClickHandler").call( self.widget, e, item ) === false )
					e.stopPropagation();
					
			});
		},
		
	}, function(name, fn) {
		
		// 접두어 $로 시작하는건 jQuery.each의 프록시가 되야 하므로 판정한다. 또한 메서드 이름은 접두사를 빼고 등록된다. 
		var childs = /^\$.+/.test(name);
		name = childs ? name.slice(1) : name;	// 접두사를 뺀다.
		
		var proxyFn = function() {
			var args = arguments;
			
			if(childs)
				return $.each(this.items, function() { fn.apply(this, args); });
			else
				return fn.apply(this, args);
		};
		
		Object.defineProperty(Item.prototype, name,
								$.extend({}, defineOption, {value: proxyFn})
							);
	});
	
	
})(jQuery);

