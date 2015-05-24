

define([
        "jquery",
        "utils",
        
        "jquery-ui",
], function($, Utils) {


	
	function ItemList() {
		this.values = {};
		this.length = 0;
	};
	
	// { uuid: { "ul" : ulElement, "item" : item } }, 
	ItemList.prototype = {
		"add": function( obj, pos ) {
			this.values[pos] = obj;
			this.length++;
		},
		"remove": function( pos ) {
			var value = this.values[pos];
			this.length--;
			delete this.values[pos];
			return value;
		},
		"get": function( pos ) {
			return this.values[pos];
		},
		"size": function() {
			return this.length;
		},
		"list": function() {
			return this.values;
		}
		
	};
	
	
	function equals( own, target ) {
		var _own, _target;
		
		if( own == null || target == null ) return false;
		
		for( var prop in own ) {
			if( (_target = target[prop]) === undefined ) return false;
			if($.isPlainObject(_own =own[prop])) return equals(_own, _target);
			if( _target != _own ) return false;
		}
		return true;
	}
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	var 
	
		widgetOptions =	{
		
		// 리스트가 생성될때 ul 엘리먼트를 받는다.
		listHandler: function( $ul, item, uuid ) {},
		
		// 리스트에 변동이 있을때마다 호출 
		changeHandler: function() {},
		
		
		overlapHandler: function( item ) {
			return true;
		},
		
		// 리스트 직접 클릭해서 선택했을때
		selected: true,
		selectHandler: $.noop,
		cancleHandler: $.noop,
		selectLen: 1,
		
		selectElement: true,	// 셀렉트 된 아이템이 상단에 노출된다.
		
		// 삭제 직전에 호출되며, false 리턴시 삭제가 되지 않는다.
		removeHandler: function( item, uuid ) { return true; },
		
		removable: true,	// 삭제버튼
		
		init: $.noop,
		
		disableHandler: $.noop,
		enableHandler: $.noop,
		
		events: {
				// 삭제 이벤트
				"click .btn-delete": function(e) {
					e.preventDefault();
					e.stopPropagation();	// 셀렉트 리스트로 전파되는걸 막는다.
					
					this.remove( $(e.currentTarget).closest("ul").attr("data-uuid") );
					
					return false;
				},
				// 삭제 이벤트
				"click .btn-select": function(e) {
					e.preventDefault();
					e.stopPropagation();	// 셀렉트 리스트로 전파되는걸 막는다.
					
					var uuid = $(e.currentTarget).closest("ul").attr("data-uuid");
					
					return false;
				},
				
				// 클릭해서 선택하는 체크리스트의 경우, 이벤트
				"click .select-list": function(e) {
					this._selectedOn( $(e.currentTarget).attr("data-uuid") );
				},
			},
	};
	

proto = $.extend({}, 
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 기본 위젯 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
	
	version: "1.1",
	options: widgetOptions,
	
	_extend: $.noop,
	
	// Constructor
	_create: function() {
		var self = this,
			options = this.options,
			elements = this.elements = {};
		
		$.each(this._elements, function(name, fn) {
			elements[name] = fn.call(self); 
		});
		
		options.removable && this.element.addClass("removable");
		this._on(options.events);
		
	},
	
	_elements: {
		"list": function() {
			return this.element.find("div.list");
		},
		"overlay": function() {
			return this.element.find("div.ui-overlay");
		},
		"select": function() {
			var element = this.element.find("div.select");
			if(!this.options.selectElement) element.addClass("ui-helper-hidden");
			return element;
		}
	},
	
	
	_init: function() {
		var options = this.options;
		
		
		
		this.clear();
	},
	
	clear: function() {
		this.getEle("list").empty();
		this.itemList = new ItemList();
		this.uuid = 0;
		
		this.appendSelect();
		this.selectArray = [];
		return this;
	},
	
	
	getEle: function( name ) {
		return this.elements[name];
	},
	
	access: function( key, value ) {
		return this.element.data(key, value);
	},

	// 모두 삭제
	_destroy: function() {
	},
	
	
	disable: function() {
		if( this.options.disableHandler.call(this) === false ) return this;
		this.element.addClass("ui-helper-hidden");
		return this;
	},
	enable: function() {
		if( this.options.enableHandler.call(this) === false ) return this;
		this.element.removeClass("ui-helper-hidden");
		return this;
	},
	
	trigger: function( eventName ) {
		this.element.trigger( eventName );
	},
	
}, // widgetPrototype

	
/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 확장메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
{
	
	// ******************  아이템 리스트 조회  ****************** //
	find: function( selector ) {
		return this.element.find( selector );
	},
	
	list: function() {
		return this.itemList.list();
	},
	
	remove: function( uuid ) {
		var options = this.options,
			removeValue = this.get( uuid );
			
		if( !options.removeHandler.call( this, this.getItem(uuid), uuid ) ) return;
		
		// 현재 선택되어 있으면 선택을 해제한다. 안그러면 에러가 난다.
		if(this.selectArray.indexOf(uuid) !== -1)
			this._selectedOff(uuid);
		
		removeValue = this.itemList.remove( uuid );
		removeValue["ul"].remove();
		options.changeHandler.call(this);
		return this;
	},	
	
	add: function( obj, uuid ) {
		this.itemList.add(obj, uuid);
	},
	get: function( pos ) {
		if(pos === undefined) return pos;
		return this.itemList.get(pos);
	},
	getItem: function( pos ) {
		var values = this.get( pos );
		return values ? values["item"] : pos;
	},
	size: function() {
		return this.itemList.size();
	},
	
	"addList": function( items ) {
		for(var i=0,l=items.length; i<l; i++) {
			this.addItem(items[i]);
		}
		return this;
	},
	
	"addItem": function( item ) {
		if( this.isExists(item) ) {
			if( !this.options.overlapHandler.call(this, item) )
				return;
		}
						
		this.add( { "ul" : this.append(item, ++this.uuid), "item" : item }, this.uuid );
		this.options.changeHandler.call(this);
	},
	
	"append": function( item, uuid ) {
		var options = this.options,
			ul = this._createUL(item, uuid);
		
		options.removable && ul.prepend('<div class="delete"><button class="btn-delete small">Del</button></div>');
		
		// 체크리스트 :: 클릭시 마킹이 되고, 콜백함수가 호출된다.
		return options["selected"] ? ul.addClass("select-list") : ul;
	},
	
	"appendSelect": function( uuid ) {
		if( uuid === undefined ) return this.getEle("select").empty(); 
		return this.getEle("select").append( this._createUL( this.getItem(uuid), uuid ) );
	},
	
	dataConvertor: Utils.dataConvert({
		"$index": function( value, uuid, item ) {
			return uuid;
		},
		"fileType": function( value, uuid, item ) {
			return '<a href="/resource/' + item['parentPath'] + '/' + item['saveName'] 
							+'" class="icon-static-file-s-' + value 
							+ '" style="display:inline-block; vertical-align: middle;"></a>';
		},
	}),
	
	_createUL: function( item, uuid ) {
		var self = this,
			types = this.options["types"],
			ul = $('<ul data-uuid="' + uuid + '">'),
			html = "",
			value, type,
			typeHandler;
			
		// header의 정보를 추출해서 리스트 HTML을 작성한다.
		// 1) data-type 어트리뷰트는 options의 types 프로퍼티와 매치되어 데이터를 파싱할 핸들러를 결정한다.
		// 2) 핸들러가 없을 경우, data-type 어트리뷰트값은 item의 프로퍼티로 간주된다.
		this.find(".header ul li[data-type]").each(function( i, li ) {
			
			type = li.getAttribute("data-type");
			value = self.dataConvertor(item, type);
			/*
			typeHandler = types[type];
			value = item[type];
			// 값이 있거나 $로 시작하는 특수타입일 경우에만 타입핸들러를 쓴다.
			if( value || /^\$/.test(type) ) {
				value = typeHandler ?
				
						typeof typeHandler === 'string' ?
							types[typeHandler].call(self, item[type], uuid, item) :
							typeHandler.call(self, item[type], uuid, item)
							
						: value || "";
			}
			else value = "";
			*/
			
			html += '<li class="' + li.className.match(/(span-\d)/)[1] + " " + type + '">' + value + '</li>';
			
		});
		
		return ul.append( html ).appendTo( this.getEle("list") );
	},
	
	
	"isExists": function( newItem ) {
		var exists = false;
		$.each(this.list(), function( i, item ) {
			item = item.item;
			if( equals( item, newItem ) )
				return !( exists = true );
		});
		return exists;
	},
		
	"getItems": function() {
			
		if( !this.size() ) return null;
		
		var returnValue = [];
		$.each(this.list(), function( i, values ) {
			returnValue.push( values.item );
		});
		return returnValue;
	},	
	
	
	block: function( $ele ) {
		var overlay = this.getEle("overlay");
		if(!$ele) {
			overlay.empty().addClass("ui-helper-hidden");
			return;
		} 
		
		var positionTop = (overlay.height() / 2) - ( $ele.height() / 2 );
		
		overlay.append($ele.css("top", positionTop))
				.removeClass("ui-helper-hidden");
	},
	
	// ****************** ▲ 아이템 리스트 조회 ▲ ****************** //
	
	
	
	select: function( fn ) {
		var self = this,
			selectId;
		wrap = $.isFunction(fn) ?
			fn :
			function( uuid, item ) {
				var returnValue = null;
				$.each(fn, function(name, value) {
					if(item[name] == value) {
						returnValue = uuid;
					}
				});
				return returnValue;
			};
		
		$.each(this.list(), function( uuid, item ) {
			if( (selectId = wrap.call(this, uuid, item["item"])) != null ) {
				self._selectedOn(selectId);
				return false;
			}
		});
		
	},
	
	// 아이템 찾기
	searchItem: function( search ) {
		var result;
		$.each(this.list(), function( uuid, item ) {
			item = item["item"];
			result = equals(search, item);
			return result ? !(result = { "uuid": uuid, "item": item }) : true;
		});
		return result;
	},
	
	_selectedOn: function( uuid ) {
		var options = this.options,
			ul = this.get(uuid).ul;
			
		if(!options.selected) return;
		
		if(ul.hasClass("selected"))	{// 이미 등록된 엘리먼트이면 지운다. (토글방식)
			this._selectedOff( uuid );
			return false;	// 자식 위젯이 있을 경우를 대비해, 루틴을 중지하라는 시그널인 false를 날린다.
		}
		
		if(this._isFull()) this._selectedOff(this.selectArray[0]);
		
		ul.addClass("selected");
		this.selectArray.push(uuid);
		options.selectHandler.call(this, this.getItem(uuid));
		options.selectElement && this.appendSelect(uuid);
		
		return true;
	},
	
	_selectedOff: function( uuid ) {
		var options = this.options;
		
		this.get(uuid).ul.removeClass("selected");
		options.cancleHandler.call(this, this.getItem(uuid));
		
		this.selectArray.splice( this.selectArray.indexOf(uuid), 1 ); // 목록삭제
		
		options.selectElement && this.getEle("select").find("[data-uuid=" + uuid + "]").remove();
	},
	
	_isFull: function() {
		return this.selectArray.length == this.options.selectLen;
	},
	
	_getSelected: function( index ) {
		if(index === undefined) return this.selectArray;
		return this.selectArray[index];
	},
}

); // proto = $.extend
	
	// widget 코드 시작
	var scrollTable = $.widget("ui.scrollTable", proto);

});


