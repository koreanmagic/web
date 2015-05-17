
/*
 *
 * 리스트를 관리하는 테이블
 * 원격 READ, WRITE, DELETE를 지원한다.
 * @import ui.scrollTable;
 * 
 */
define([
        "jquery",
        'component/FormValidate',
        "utils",
        
        "./scrollTable"
], function($, FormValidate, Utils) {



	function equals( own, target ) {
		var v;
		
		if( own == null || target == null ) return false;
		
		for( var prop in own ) {
			if( (v = target[prop]) === undefined ) return false;
			if( v !== own[prop] ) return false;
		}
	}
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	var events = {
			
			"click [data-mode]": function(e) {
				e.preventDefault();
				e.stopPropagation();
				this._mode(e.currentTarget.getAttribute("data-mode"));
			},
			
			
			"click .btn-confirm": function(e) {
				e.preventDefault();
				e.stopPropagation();
				// 현재 등록중인 deferred를 실행한다.
				this._done(true);
			},
			"click .btn-cancle": function(e) {
				e.preventDefault();
				e.stopPropagation();
				// 현재 등록중인 deferred를 실행한다.
				this._done(false);
			},
			
			
		},
		
		// 위젯 옵션
		widgetOptions =	{

		prefix: "",
		
		selectElement: false,
		selectLen: 1,
		// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  AJAX OPTIONS  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 
		
		
		// 리스트 항목 수정
		modify: {
			options: {
				url: function( item, prefix ) {
					return prefix + "/modify";
				},
				data: true,
				method: "POST",
				processData: false,
			},
			validate: null,
		},
		
		// WRITE
		upload: {
			options: {
				url: function( item, prefix ) {
					return prefix + "/add";
				},
				data: true,
				method: "POST",
				processData: false,
			},
			validate: null,
		},
		
		// DELETE
		"delete": {
			options: {
				url: function( item, prefix ) {
					return prefix + "/delete/" + item.id;
				},
				method: "GET",
			},
		},
		
		init: $.noop,
		
	};
	

proto = $.extend({}, 
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 기본 위젯 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
	
	version: "1.1",
	options: widgetOptions,
	
	_extend: $.noop,
	
	// Constructor
	_create: function() {
		this._super();
		var self = this,
			options = this.options,
			modeData = this.modeData = {},
			elements = this.modeElements = {};
		
		$.each( ["view", "upload", "modify"], function( i, mode) {
			elements[mode] = self.find("."+mode).addClass("ui-scrollTable-" + mode);
		});
		
		this.loadingImg = $('<img src="/img/loading.gif" width="30" height="30" />');
		this.element.prepend('<div class="controller"><a href="#" data-mode="upload"><i class="fa fa-plus-square"></i>추가</a></div>');
		
		// mode Handler를 초기화시킨다.
		$.each(this._modes, function( modeName, modeObj ) {
			// parameter 1) 모드로직을 담고잇는 객체, mode에 해당하는 옵션, mode가 가지는 elements들, mode전용 데이터
			modeObj.create && modeObj.create.call(self, modeObj, options[modeName], elements[modeName], (modeData[modeName] = {}) );
		});
		
		this._on(events);
		this._mode();
		
	},
	
	
	_init: function() {
		this._done(false);
		this._super();
	},
	
	// 리스트는 유지한 상태에서 모든 설정을 되돌림
	refresh: function() {
		this._done(false);
	},
	
	// fetch가 끝난 후에 이루어질 루틴을 등록할 수 있게, ajax의 promise를 돌려준다.
	fetch: function() {
		var promise;
		this._mode("fetch");
		promise = this.deferred["ajaxPromise"].done( this.options.fetchAfterHandler );
		this._done( true );
		return promise;
	},
	
	// 리스트 삭제후 다시 패치하기때문에 상위 메서드를 오버라이딩 할 필요가 없다.
	remove: function( uuid ) {
		var promise,
			// 객체를 내보낼 경우 ajax의 success혹은 fail 함수로 등록된다.
			handlerReturnValue = this.options.removeHandler.call( this, this.getItem(uuid), uuid );
		
		if( handlerReturnValue === false ) return;
		this.currentId = uuid;
		this._mode("delete");
		promise = this.deferred["ajaxPromise"];
		this._done( true );
		
		if( $.isPlainObject(handlerReturnValue) ) {
			$.each(["done", "fail"], function( _, prop ) {
				promise[prop](handlerReturnValue[prop] || $.noop);
			});
		}
		return promise;
	},
		
	
	// 리스트 클릭했을때 :: 여기서는 해당 레코드의 상세정보가 나타나는 루틴을 호출한다.
	_selectedOn: function( uuid ) {
		if( !this._superApply(arguments) ) return;
		this.currentId = uuid;
		this._mode("view");
	},
	
	// 리스트 껐을때
	_selectedOff: function( uuid ) {
		this._superApply(arguments);
		
		/*
		 * this.currentId를 null로 바꾸면 안된다.
		 * 만약 view상황에서 modify로 넘어갈 경우, this.currentId는 modify에서 사용할 주요 데이터가 된다.
		 * currentId는 셀렉팅시에만 사용하는 변수가 아니라, 현재 작업 흐름을 나타내는 주체로 봐야 한다.
		 * 따라서 currentId는 다시 설정하기 직전까지 현재 작업 주체가 무엇인지 나타내는 null일 수 없는 변수다.  
		 */
		//this.currentId = null;	
		this._mode();
	},
	
	// 비동기 작업이 이루어질때 클릭방지를 위한 루틴
	block: function( flag ) {
		if( flag ) this._super( this.loadingImg );
		else this._super();
		return this;
	},
	
}, // widgetPrototype

	
/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 확장메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
{
	
	// ******************  MODE  ****************** //
	// mode에 따라 group에 등록된 1)엘리먼트들을 block처리하고, 2) modeElement를 설정한다. 
	_mode: function( mode ) {
		var self = this,
			modeFns = this._modes[mode],
			panel;
		
		this._done(false);	// 현재 등록된 deffered의 reject를 호출해서 예약된 취소함수를 실행시킨다.
		
		this.status = mode;
		
		panel = this._viewControll(mode);
		
		if(modeFns) {
			modeFns.init && modeFns.init.call( this, this.currentId, panel, this.modeData[mode] );
			this.deferred = this._deferred();
		}
		
		return this;
	},
	
	// false나 undefined가 들어오면 모든 것을 감춘다.
	_viewControll: function( mode ) {
		$.each( this.modeElements, function( name, elem ){
			 elem.hasClass("ui-scrollTable-" + mode) ?
				elem.removeClass("ui-helper-hidden") :
				elem.addClass("ui-helper-hidden");
		});
		return this.modeElements[mode];
	},
	
	
	// 각 모드에 해당하는 루틴이 담겨있다.	
	_modes: {
		
		// 리스트 가지고 오기
		"fetch": {
			done: function( data ) {
				this.clear();
				this.addList(data);
			},
		},
		
		"delete": {
			done: function( data ) {
				this._mode(); 
				this._init();
				this.fetch();
			},
		},
		
		"view": {
			init: function( uuid, element ) {
				var item = this.getItem(uuid),
					convert = Utils.dataConvert();
				
				element.find("[data-type]").each(function( _, span ) {
					span.textContent = convert(item, [span.getAttribute("data-type")]);
				});		
			},
			cancle: function( uuid, element ) {
				/*
				 * view의 cancle은 1)셀렉터를 끄거나 , 2) 다른 모드로 넘어갔을때 호출된다.
				 * 정상의 경우라면, 사용자가 반복클릭하여 셀렉터를 끄는 루틴을 진행하게 된다.
				 * 그러나 곧바로 다른 모드로 넘어갈 경우, 셀렉터를 끄는 루틴을 누군가는 진행시켜야 한다.
				 * 그 역할을 이 메서드가 실행한다.
				 */
				if(this._isFull()) {
					this._selectedOff(this.currentId);
				}
			}
			
		},
		
		"modify": {
			create: function( selfObj, options, element, data ) {
				var self = this, option = {
					beforeCommit: function() {
						self._done(true);
						return false;
					}
				};
				data["validate"] = FormValidate( element, $.extend({}, options["validate"], option) );
			},
			init: function( uuid, element, data ) {
				var item = this.getItem(uuid),
					url = this.options.prefix + "/modify/" + item.id;
				this._remote( { "url" : url })
					.success(function( values ) {
						data["validate"].initValue( values );
					});
			},
			
			// ajax 송출전에 폼을 검증하기 위한 인터셉트. 여기서 true가 반환되어야 deferred의 다음단계가 이루어진다.
			preAjax: function( uuid, element, data ) {
				var validate = data["validate"];
				return validate.validate() ? validate.serialize() : false;
			},
			
			done: function() {
				this._mode(); 
				this._init();
				this.fetch();
			},
			cancle: function( uuid, element, data ) {
				this._viewControll(false);
			}
		},
		
		"upload": {
			create: function( selfObj, options, element, data ) {
				var self = this, option = {
					beforeCommit: function() {
						self._done(true);
						return false;
					}
				};
				data["validate"] = FormValidate( element, $.extend({}, options["validate"], option) );
			},
			init: function( uuid, element, data ) {
				data["validate"]._init();
			},
			// ajax 송출전에 폼을 검증하기 위한 인터셉트. 여기서 true가 반환되어야 deferred의 다음단계가 이루어진다.
			preAjax: function( uuid, element, data ) {
				var validate = data["validate"];
				return validate.validate() ? validate.serialize() : false;
			},			
			done: function() {
				this._mode(); 
				this._init();
				this.fetch();
			},
			cancle: function() {
				this._viewControll(false);
			}
		},
		
	},
	
},

{
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  AJAX  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ //

	_done: function( flag ) {
		var self = this,
			deferred,
			returnValue;
		
		if( flag === undefined )
			return this.deferred = undefined;
		
		// intercept가 등록되어 있을 경우에는 그 함수가 true를 호출하는 경우에만 resolve를 실행한다.
		// false를 리턴하면 deferred는 유지된다.
		if( deferred = this.deferred ) {
			
			this.deferred = undefined;	// 자기참조 방지
			
			if(flag) {
				if(deferred["intercept"] && !(returnValue = deferred["intercept"].call(self))) {
					this.deferred = deferred;
					return;
				} 
				else deferred.resolve(returnValue);
			}
			
			else deferred.reject();
		}
		return this;
		 
	},
	
	// 존나 개복잡한 메서드!! 짧은 코드에 함축된 루틴이 어마어마하게 많다. ㅠㅠ
	_deferred: function() {
		var self = this,
			// 클로저를 위한 변수
			mode = this.status,
			options = this.options[mode],
			data = this.modeData[ mode ],
			args = [ this.currentId, this.modeElements[mode], data],
			fns = this._modes[ mode ],
			deferred = $.Deferred().fail( function() { fns.cancle.apply(self, args); } );
			
			
			deferred["name"] = mode;
			
			if(fns["preAjax"]) deferred["intercept"] = function() { return fns["preAjax"].apply(self, args); };
			
			deferred["ajaxPromise"] = deferred.then( function( data ) {
				var option = $.extend({}, self.options[mode]["options"]);
				
				// url 연산
				option.url = $.isFunction(option.url) ?
								// fetch일 경우 리스트가 등록되기 이전이므로
								option.url.call(self, self.getItem(args[0]), self.options.prefix) :
								option.url;
				
				option.data = data;
				return self._remote.call( self, option, self.options[mode]["handlers"]);
			}).done( fns.done ).done(options && options.afterHandler || $.noop);
			
			
		return deferred;
	},
	
	// 클로저!!
	// ajax를 대신 해준다. // preventRepeat 계속 클릭시 중복해서 데이터가 적용되는걸 방지
	_remote: function( options, handlers /* 기 등록된 옵션이 아니라 커스텀 옵션일 경우 */ ) {
		var self = this,
			jqXHR,
			option;
		
		this.block(true);
		
		option = $.extend(
					{
						context: self
					}
				, options /* 옵션이 undefined면 무시된다. */);
		
		jqXHR = $.ajax(option);
		$.each( handlers || [], function( prop, fn ){
			jqXHR[prop]( function() { fn.apply(this, arguments); }  );
		});
		
		jqXHR.promise().done( function() { self.block(false); } );
		return jqXHR;
	},
	
}
); 
	
	
	// widget 코드 시작
	var scrollTable = $.widget("ui.scrollTableAjax", $.ui.scrollTable, proto);

});


