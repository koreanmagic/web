
define([
	
	'jquery',
	'component/Popup',
	'utils',
	
	'jquery-ui',
	'ui/gallery',
	
], function($, Popup, Utils) {
	
	
	// 전역 변수
	// 현재 활성화된 widget 객체가 여기에 등록된다.
	var widgetSelectorPrefix,
		widgetFullName,
		
		convertor = Utils.dataConvert(),	// 기본적인 데이터 컨버터
		
		// 기본적으로 data-name="{bane}" 엘리먼트의 textContent를 변경해준다.
		// 이보다 복잡한 작업이 필요할 경우는 직접 커스터마이징한다.
		defaultTypeHandlers = {
			"default": function( v, name, values, ele ) {
				var ele;
				v = v ? v : "";
				ele.length && ele.text( convertor(values, name) );   
			},
			"memo": function(v, name, values, ele) {
				ele.length && ele.html( convertor(values, name) );
			},
			"fileType": function( v, name, values, ele ) {
				var url = '/resource/' + values['parentPath'] + '/' + values['saveName'] + '.' + values['fileType'];
					className = 'icon-static-file-' + v;
				
				url += '?filename=' + values['filename'];
				
				ele[0].className = '';
				ele.addClass(className).attr('href', url);
			},
		}
		;
	
	// data-name 어트리뷰트가 핵심 마킹이다.  실제 값을 입력해주는 공통 메서드
	function setValues( values, handlers ) {
		var self = this;
		handlers = handlers || defaultTypeHandlers;
		$.each(values, function( name, value ) {
			getHandler(name, handlers).call( self, value, name, values, getEle.call(self, name));
		});
	}
	
	
	// data-name 어트리뷰트가 핵심 마킹이다.  실제 값을 입력해주는 공통 메서드
	function setValuesFactory( handlers ) {
		handlers = $.extend({}, defaultTypeHandlers, handlers);
		
		return function( values ) {
			setValues.call(this, values, handlers);
		};
		
	}
	
	function getHandler( name, handlers ) {
		var handler = handlers[name];
			
		if(!handler) return handlers["default"];
		return typeof handler === 'string' ? handlers[handler] : handler;
	};
	
	function getEle( name ) {
		return this.find("[data-name='"+ name + "']");
	};
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	var widgetOptions =	{
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
			//this[2].gallery();
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
	var selectBtn = $.widget("ui.itemContainer", widgetPrototype),
		
		gallery = $('#ui-gallery')
							.gallery()
							.on('popOff', function( e ) {
								$(this).data('itemObj').draftFile();
							})
							.gallery( 'instance' );
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ITEM CLASS ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * Item 객체는 각 작업 게시물 하나를 뜻한다.
	 * 이는 jQuery를 상속한 객체이므로, jQuery를 이용하는 것과 동일하게 작업하면 된다.
	 */
	function Item( element, widget ) {
		
		var split, i, obj, self = this;
		this[0] = this.element = element.nodeType === 1 ? element : element[0];
		this.length = 1;
		this.widget = widget;
		
		this.data("instance", this);


		this.setValue = function() {
			Item.prototype.setValue.apply(self, arguments);
		};
		
		this.create();
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
			this.state = this.access("workState");
			
			this.resourceFile();
			this.confirmFile();
			this.draftFile();
		},
		
		
		// 삭제될때 호출
		destroy: function() {
			return this;
		},
		
		
		// 모든 정보 새로고침
		refresh: function() {
			var self = this;
			
			$.getJSON('/admin/work/editor/get/' + this.id)
			.success(function( json ) {
				self.setValue(json);
				self.resourceFile();
				self.confirmFile();
				self.draftFile();
			});
		},
		
		
		/********************* ▼ RESOURCE FILE ▼ *********************/
		// [UPLOAD] 리소스파일 업로드하기
		resourceUpload: function( type, files, handler ) {
			var self = this, 
				formData = new FormData();
			
			$.each(files, function( i, file ) {
				formData.append("file", file);
			});
			formData.append("work", this.id);
			formData.append("serviceType", type);
				
			$.ajax({
				url: '/admin/work/editor/resource/upload',
				processData: false,
				contentType: false,
				data: formData,
				type: 'POST',
			}).success(function(){
				handler.call(self);
			});
				
			return false;
		},
		
		// [GET] 리소스파일 리스트 가지고 오기
		getResource: function( type, handler ) {
			var self = this;
			
			$.getJSON( '/admin/work/editor/resource/list',
				{"work": this.id, 'serviceType': type}
			).success(function( json ) {
				handler.call(self, json);
			});
		},
		
		// [REMOVE] 리소스파일 지우기
		removeResource: function( type, resourceId, handler ) {
			var self = this;
			$.ajax({
				url: '/admin/work/editor/resource/delete/' + resourceId,
				type: 'GET',
				data: {"work": item.id, 'serviceType': type},
			}).success(function(data) {
				handler.call(self);
			});
		},
		
		/********************* ▲ RESOURCE FILE ▲ *********************/
		
		
		// 시안 갤러리
		gallery: function() {
			
			this.getResource( 'workDraftFile', function( json ) {
				var self = this, 
					data = [];		
				
				gallery._init();
				
				// 갤러리 이미지 삭제 콜백
				gallery.options.removeHandler = function( index, imgObj ) {
					var returnValue = false;
					$.ajaxSetup({ async: false });	// 동기식으로
					
					self.removeResource( 'workDraftFile', imgObj.id, function(e) {
						returnValue = true;
					});
					
					$.ajaxSetup({ async: true });
					return returnValue;
				};
				
				// 이미지 추가시, 업로드 후 gallery를 재부트한다.
				gallery.options.addHandler = function(files) {
					self.resourceUpload( 'workDraftFile', files, function() { this.gallery(); });
				};
				
				
				$.each(json, function( i, values ) {
					// 데이터 컨버팅
					data.push({
						id: values.id,
						src: '/resource/' + values['parentPath'] + '/' + values['saveName'] + '.' + values['fileType'],
						thumbnail: '/resource/' + values['parentPath'] + '/' + values['saveName'] + '-thumbnail.' + values['fileType'],
						filename: values['originalName']  + '.' + values['fileType'],
						length: values['size'],
						datetime: values['uploadTime'],
					});
					
				});	
				
				gallery.put(data);
				if( data.length ) gallery.view(0);
				
				// 팝오프시 재호출을 위한 아이템을 저장한다.
				this.widget.popup( gallery.element.data('itemObj', this) );
			});
		},
		
		// 참고파일이 있는지 확인하고 버튼 생성
		resourceFile: function() {
			var self = this,
				div = this.find("._pop-file-refer");
			
			$.getJSON("/admin/work/get/resource/size/" + this.id)
				.success(function( data ) {
					if( data.size > 0 ) {
						div.removeClass("disable");
						div.find(".size").text(data.size);
					} 
					else div.addClass("disable");
				});
		},
		
		// 인쇄파일이 있는지 확인하고 있으면 버튼 생성
		confirmFile: function() {
			var self = this,
				btn = self.find('.btn-work-file'),
				target = $( btn.data('uiDropdown') )
				;
				
			this.getResource( 'workConfirmFile', function( data ) {
				data = data[0];
				if( data ) {
					btn.removeClass('disable');
					// 실제 다운받게 될 파일명
					data['filename'] = self.id + '_'
												+ self.getValue(['customer', 'item']).join('_') 
												+ '.' + data['fileType'];  
					
					setValues.call(target, data);
				} else {
					btn.addClass('disable');
				}
			});
				
		},
		
		draftFile: function() {
			this.getResource( 'workDraftFile', function( data ) {
				var ele = this.find('._work-img').empty();
				data = data[0];
				
				if(!data) {
					ele.addClass('empty');
					return this;
				};
				
				ele.append(
					'<img src="/resource/' + data['parentPath'] + '/' + data['saveName'] + '-thumbnail.' + data['fileType'] + '">'
				).removeClass('empty');
			});
		},
		
		// 에디터 열기
		// 1) 먼저 work 하이버네이트 프록시를 세션에 저장한다.
		editor: function( option ) {
			var item = this,
				widget = this.widget;
				
			// 세션에 작업 아이디 저장
			$.ajax("/admin/work/editor/init/" + this.id)
				.success(function() {
					option = $.extend( option, {"item": item} );
					widget.editor(option, item);
				});
		},
		
		
		setValue: setValuesFactory({
				"afterProcess": function( v, name, values, ele ) {
					v ? 
						getEle.call(this, 'itemDetail').next().removeClass('ui-helper-hidden') :
						getEle.call(this, 'itemDetail').next().addClass('ui-helper-hidden');
				}, // afterProcess
				
				"memo": function( v, name, values, ele ) {
					if(v) {
						this.find('.btn-memo').removeClass('disable');
						defaultTypeHandlers.memo.apply(this, arguments);
					} else {
						this.find('.btn-memo').addClass('disable');
					}
				}, // memo
				
				"delivery": function( v, name, values, ele ) {
					var className = ['fa-cubes', 'fa-male', 'fa-truck'],
						ordinal = v.ordinal;
					
					$.each(className, function(i, cName) {
						if( i == ordinal ) {
							ele.attr('alt', v.value);
							ele.addClass(cName);
						}
						else ele.removeClass(cName);
					});
				}, // delivery
				
				'count': function( v, name, values, ele ) {
					var num = values.num ? '(' + values.num + '건)' : '';
					
					ele.text( v + ' ' + num );
				}, // count
				
				"subcontractor": function( v, name, values, ele ) {
					this.find("[data-info='subcontractor']").attr("data-info-key", v.id);
					ele.text( v.name );
				},
				"manager": function( v, name, values, ele ) {
					var manager = ele,
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
				
				'insertTime': function( v, name, values, ele ) {
					return;
				},
			}
		),
		
		// 이름리스트를 배열로 입력하며, 배열로 값을 받고, 객체로 입력하면 객체로 받는다.
		getValue: function( names, context ) {
			var self = context || this;
			
			$.each(names, function( i, name ) {
				names[i] = getEle.call(self, name).text();
			});
			
			return names;
		},
		
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
	
});