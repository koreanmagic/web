

define([
	'jquery',
], function($){


	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  폼 플러그인  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	/*
	 * 폼 검증 객체 (싱글톤)
	 * 기본적으로 폼 엘리먼트당 하나의 객체가 할당되도록 설계되었다.
	 * FormUnit은 각 인풋 엘리먼트의 분신으로 생각하면 된다.
	 * 인풋값을 검증하는 validate Rule은 각 FormUnit 객체에 저장된다.
	 * FormValidate 객체의 addRules를 통해 변경하거나 추가할 수 있다.
	 * 
	 */
	
	// 각 인풋별 메서드 정의
	// this.input은 해당 inputElement의 jQuery 확장배열이다.
	var markerClass= "validate-target",
		successClass= "validate-success",
		errorClass= "validate-error",
		
		inputType = {
			"text": {
				get: function() {
					return this.val();
				},
				set: function( v ) {
					this.val(v);
				}
			},
			"hidden": "text",
			"radio": {
				get: function() {
					return this.filter(":checked").val();
				},
				set: function( v ) {
					// 무의미한 값이 들어온다는건 코드상에서 초기화시킬때 뿐이다. 따라서 초기화시킨다.
					if( !v ) {
						this.filter(":not(:checked)")
							.each(function(i, input) {
								input.checked = false;
							});
						return;
					}
					this.each( function( i, input ){
						if( input.value === v ) {
							input.checked = true;
							return false;
						}
					});
					return true;
				},
			},
			"checkbox": "redio",
			"textarea": "text",
			"file": {
				init: function() {
					
				},
			},
			"select-one": {
				get: function() {
					return this.find("option:selected").val();
				},
				set: function( v ) {
					
					if( !v ) return;
					
					v = $.isPlainObject(v) ? v.id : v;
					
					this.find("option[value]").each( function( i, input ){
						if( input.value == v ) {
							input.selected = true;
							return false;
						}
					});
					return true;
				},				
			}
			
		};
	
	
	function getHandler( type ) {
		return typeof inputType[type] === 'string' ?
						inputType[ inputType[type] ] :
						inputType[type];
	};
	
	// inputArray :: Dom Element Array
	// InputUnit 자체는 jQuery객체가 된다.
	function InputUnit( inputArray, context ) {
		var self = this;
		
		this.context = context;
		this.name = inputArray[0].name;
		this.type = inputArray[0].type;
		
		// 검증결과
		this.status = null;
		
		this.length = inputArray.length;
		$.each( inputArray, function( index, input ) {
			self[index] = input;
		});
		
		this.actives = [this];
		this.create();
		this.init();
	};
	
	$.extend(InputUnit.prototype, $(), {
		
		create: function() {
			
			// input은 readOnly이고 다른 엘리먼트를 선택해서 값을 입력받을 경우
			var self = this,
				refer = this.attr("data-value-target"); 
				
			if ( refer ) {
				refer = /([^\s]+)(.*)/.exec(refer);
				// 해당 객체를 마우스다운하면 textValue를 value로 입력한다.
				this.context.addEvent(refer[1], refer[2], function(e) {
					self.setValue( e.currentTarget.textContent );
					self.trigger("change");		// 값이 변했을때의 루틴을 실행하기 위한 트리거 
				});
			}
			
			// 각종 등록
			$.each(this._actives, function( prop, fn ) {
				fn.call(self, self.actives);
			});
		},
		
		_actives: {
			// 체크 엘리먼트
			"check": function( actives ) {
				var ele = this.context.find("[data-check-for='"+ this.name +"']");
				ele.length && actives.push( ele.append('<i class="fa fa-check"></i>') );
			},
			// 여기서 error메서드가 만들어진다.
			"error": function( actives ) {
				var ele = this.context.find("[data-error-for='"+ this.name +"']");
				if(ele.length) {
					actives.push( ele );
					
					// 실제 error메서드
					this["error"] = function(msg) {
						if(msg === false) ele.empty();	// msg가 false가 들어오는 경우는 validate결과이거나, 초기화
						typeof msg === 'string' ? ele.text( msg ) : ele.append( msg );
					};
				}
			},
			"inputGroup": function( actives ) {
				actives.push( this.closest(".input-group") );
			},
		},
		
		init: function( v ) {
			this.status = null;				// 현재 검증상황을 초기화시킨다. 
			v = this.origValue = (v || "");	// 초기값 설정. 위젯 재사용시 값을 지우는 역할도 한다. 
			this.setClass().error(false);	// 마커클래스 & 에러메세지 초기화
			
			// 값이 들어오면 validate도 실행한다. 그게 아니라면 값을 초기화시킨다.
			v ? this.setValue(v).validate() : this.setValue("");
			
			(inputType[this.type]["init"] || $.noop).call(this);
			return this;
		},
		
		validate: function() {
			var result,
				valid = this.getRule("valid"),
				msg = false;
			
			// valid 핸들러가 반환하는 것은 1)true이거나 2)false, 혹은 에러메세지다.
			// true가 아니라면 false이거나, 에러메세지다.
			if( (result = valid.call( this, this.getValue())) !== true ) {
				msg = result !== false ? result : "입력값 에러";	// 에러메세지 생성
				result = false;
			};
			
			this.status = result;
			this.error( msg );
			
			this.setClass();
			
			return result;
		},
		
		setClass: function() {
			var flag = this.status;
			$.each( this.actives, function( _, $ele ){
				// this.status가 null이면 초기화
				flag == null ?
						$ele.removeClass( errorClass ).removeClass( successClass ) : 
						flag ? $ele.removeClass( errorClass ).addClass( successClass ) :
								$ele.removeClass( successClass ).addClass( errorClass );
			});
			return this;
		},
		
		error: $.noop,
		
		// 수정이 이루어졌는지?
		isModify: function() {
			return this.status === null ? false :
					this.origValue == this.getValue() ? false : true;
		},
		
		// 값 되돌리기
		restore: function() {
			this.init( this.origValue );
		},
		
		getSerialize: function() {
			return {name: this.name, value: this.getValue()};
		},

		getValue: function() {
			return getHandler( this.type )["get"].call(this);
		},
		
		setValue: function( v ) {
			getHandler( this.type )["set"].call(this, v);
			return this;
		},
		
		// rule은 contextWidget에서 관리한다. 이를 받아온다.
		getRule: function( prop ) {
			return prop ? this.context.getRule( this.name )[prop] : this.context.getRule( this.name );
		},
		
	});
	
	
	// ********** FormValidate *************
	
	var _events = {
			"submit": function(e) {
				if( !this.validate() || !this.options.beforeCommit.call( this ) ) {
					e.preventDefault();
				}
			},
			"reset": function(e) {
				e.preventDefault();
				this.restore();
				this.block(true);
			},
			
			"validate .validate-target": function(e) {
				this.getUnit(e.target).validate();
			},
			"focusout .validate-target": function(e) {
				var unit = this.getUnit(e.target);
				unit.getRule("focusout").call( unit, e.target, unit.getValue() );
				unit.validate();
			},
			
			"keyup .validate-target": function(e) {
				this.getUnit(e.target).validate();
				// 수정된 상태이면
				if(this.isModify()) this.block(false);
				else this.block(true);
			},
			"change .validate-target": function(e) {
				this.getUnit(e.target).validate();
				// 수정된 상태이면
				if(this.isModify()) this.options.modifyTrue.call(this);
				else this.options.modifyFalse.call(this);
			},
		},
	
		// 위젯 옵션
		defaultOptions = {
		
			create: function( ruleTable ){},
			init: $.noop,
			beforeCommit: function() { return true; },
			
			// 수정 체크
			modifyTrue: $.noop,
			modifyFalse: $.noop,
		},
		
		defaultRule = {
			"valid": function( value ) { return true; },
			"focusout": function( input, value ) { return value; },
		},
		uuid = 1,

	
	/* ************************** 폼 객체 ************************** */
	// form은 반드시 넣어줘야 한다.
	FormValidate = function( formElement, options ) {
		
		var alreadyObj;
		formElement = $(formElement);
		
		// 이미 만들어진 객체가 있다면!!
		if( alreadyObj = formElement.data("FormValidate") ) {
			$.extend( true, alreadyObj.option, options);
			
			alreadyObj._init();	// 다시 초기화의 경우 rule없이 옵션만 넣게 된다. rules는 addRule을 통해서 등록한다.
			return alreadyObj;
		}
		
		// 단순 함수 호출에 객체 미생성이라면 new 키워드
		if( !this.validate )
			return new FormValidate( formElement, options );
		
		if(!/form/i.test(formElement[0].tagName)) throw new Error("form 엘리먼트만 사용할 수 있습니다");
		formElement.data("FormValidate", this);
		
		this.form = formElement.addClass( "validate-form" )[0];
		this.options = $.extend( true, {}, defaultOptions, options );
		this.create();
	};
	
	$.extend(FormValidate.prototype, {
		
		// 처음 만들때만 호출
		create: function( options ) {
			
			var self = this,
				options = this.options,
				units = this.units = {},
				name,
				ruleTable = this.ruleTable = {};
				
			// 글로벌
			this.uuid = "FormValidate" + uuid++;
			this.eventNamespaceG = "." + this.uuid + "G";
			this.eventNamespace = "." + this.uuid;
			
			// 이벤트 맵, 이벤트네임스페이스, 이벤트 주체, 핸들러에 적용할 this객체(생략시 이벤트 주체)
			this.addEvent( _events );
			
			// input collection 
			this.find("[name]")
			.not( (options.exclude || []).join(","))
			.addClass( markerClass )
			.each(function( i, input ) {
				name = input.name;
				units[name] = units[name] || [];
				units[name].push( input );
				
				// ruleTable이 비어있을때만 defaultOptions을 적용한다. 재사용할 수도 있따.
				// ruleTable은 각 input에 1:1로 매칭되는 기본 옵션이다.
				// 여러개의 input엘리먼트, 즉 체크박스나 라디오버튼의 경우 이 루틴을 통해 중복 extend를 방지할 수 있다.
				ruleTable[name] = ruleTable[name] || $.extend({}, defaultRule);
			});
			
			// 유닛 생성
			$.each( units, function( name, inputArray ) {
				units[name] = new InputUnit( inputArray, self );
			});
			
			// 룰을 갱신한다
			this._rebuildRuleTable( options.rules );
			this._globalRulesHandler( options.globalRules );
			
			options.create.call(this, ruleTable);
			this._init();	// options이 없는채로.. 디폴트 설정의 validator를 생성할수도 있으므로 빈 객체를 걸어둔다.
			
		},
		

		getForm: function() {
			return this.form;
		},
		
		find: function( selector ) {
			return $(this.form).find(selector);
		},
		
		// 모든 호출의 시작
		// rules을 새로 입력하면 모두 새로 작성되며, 그렇지 않으면 기존의 rules가 유지된다.
		_init: function() {
			var options = this.options;
			
			this.initValue( options.value );
			options.init.call( this );
		},
		
		// 각 인풋 초기값이 들어올 경우 이를 설정한다.
		initValue: function( values ) {
			
			var names, obj;
			
			$.each(this.getUnit(), function(index, unit){
				names = unit.name.split(".");
				obj = values;
				// values값이 없으면 초기화만 진행한다.
				obj && $.each(names, function( _, key ){
							obj = obj[key];
							if(!obj) return;
						});
				unit.init( obj );
			});
			// 인풋 외에 설정할 다른 엘리먼트가 있는지?
			this._setElementText(values);
			this.block(true);
		},
		
		// input폼 외에 정보를 새로 기입해야 하는 Element들을 갱신한다. [data-type='프로퍼티']
		_setElementText: function( data ) {
			$("[data-prop]").each(function( i, ele ) {
				if(ele["type"]) ele.value = data[ ele.getAttribute("data-prop") ];
				else ele.textContent = data[ ele.getAttribute("data-prop") ];
			});
		},
		
		
		addEvent: function( eventType, selector, handler ) {
			var self = this;
			
			// {이벤트}가 들어올 경우
			if( $.isPlainObject( eventType ) ) {
				$.each( eventType, function( event, handler ) {
					var match = event.match( /^([\w:-]*)\s*(.*)$/ ),
						eventName = match[ 1 ],
						_selector = match[ 2 ];
					// 재귀 호출
					self.addEvent( eventName, _selector, handler );
				});
			};
			
			eventType = eventType + "." + this.eventNamespace;
			$(this.form).on( eventType,
							selector,
							// event Dispatch
							function(e) { 
								// 원래 있던 데이터를 빼고 Validate 데이터를 붙였다가, 함수 호출 후 다시 돌려놓는다.
								var data = e.data; 
								e.data = self.options.data;
								handler.call( self, e );
								e.data = data;
							}
						);
		},
		
		_trigger: function( eventType, data ) {
			$(this.form).trigger( eventType, data );
		},
		
		// Rule은 클라이언트가 직접 입력한다.
		_rebuildRuleTable: function( rules ) {
			var self = this,
				targets = this.units,
				ruleTable = this.ruleTable
				;
			// 1) rules의 프로퍼티로 등록된 selector를 순회하면서 input Element를 찾는다.
			// 2) 찾은 input의 name어트리뷰트로 rulesTable객체를 검색한다.
			// 3-1) 찾아낸 rulesTable의 값 객체를 새로운 설정값으로 덮어쓴다.
			// 3-2) vlaid가 정규식일 경우, dispatcher Handler를 만들어 등록한다.
			$.each(rules || {}, function(selector, valid) {
				self.find( selector ).each(function(i, input) {
					// 기본 ruleTable을 선택적으로 덮어쓴다.
					$.extend( true, ruleTable[input.name], valid );
					if(ruleTable[input.name] === undefined)
						throw new Error("rules 선택자인 " + selector + "이 validate-target으로 등록되지 않았습니다.");
					
					// valid가 정규식일 경우
					if(ruleTable[input.name].valid instanceof RegExp) {
						var _valid = ruleTable[input.name].valid;
						ruleTable[input.name].valid = function( v ) { return _valid.test(v); };
					}
				});
			});
			return this;
		},
		
		
		// 공통 Rule 설정
		// options 중 globalRule에 해당한다.
		// 1) FormValidator가 제공하는 키워드를 통해, 값 검증을 좀 더 쉽게한다. 
		_globalRulesHandler: function( globalRules ) {
			if(globalRules === undefined) return;
			
			var self = this,
				ruleTable = this.ruleTable,
				selectors;
			
			
			$.each( this._globalRules, function( keyword, handler ) {
				if ( !(selectors = globalRules[keyword]) ) return;
				
				$(selectors.join(",")).each(function( i, input ) {
					ruleTable[input.name].valid = handler.call( self, ruleTable[input.name].valid );
				});
			});
			
			
		},
		
		// 공통 Rule 세부 구현
		// this는 inputUnit 객체
		_globalRules:  {
			
			"required": function( valid ) {
				return function( v ) {
					if( !v ) return '필수 입력';
					return valid.call( this, v );
				};
			},
			"number": function( valid ) {
				return function( v ) {
					if( isNan(v) ) return "숫자만 입력할 수 있습니다.";
					return valid.call(this, v);
				};
			},
		},
		
		getRule: function( name ) {
			return name === undefined ? this.ruleTable : this.ruleTable[ name ];
		},
		// wrapDiv 엘리먼트
		getUnit: function( input ) {
			return input == undefined ? this.units : this.units[ input.name ? input.name : input ];
		},
		
		commit: function() {
			this._trigger("submit");
		},
		
		isModify: function() {
			var modify = false;
			$.each(this.getUnit(), function(i, unit) {
				if(unit.isModify()) return !(modify = true);
			});
			return modify;
		},
		
		restore: function() {
			$.each(this.getUnit(), function(i, unit) {
				unit.restore();
			});
		},
		
		// 버튼을 막는다.
		block: function( flag ) {
			var btns = this.find("[type='submit'], [type='reset']");
			btns.length && (flag ? btns.attr("disabled", "disabled") : btns.removeAttr("disabled"));
			return this;
		},
		
		// input :: Dom element
		validate: function( input ) {
			var validateResult = true;
			if(input === undefined) {
				$.each(this.getUnit(), function( i, unit ) {
					validateResult = unit.validate() ? validateResult : false;
				});
			}
			else validateResult = this.getUnit( input ).validate();
			return validateResult;
		},
		
		values: function() {
			var self = this,
				values = [];
			
			$.each( this.units, function(index, unit) {
				values = values.concat(  unit.getSerialize()  );
			});
			return values;
		},
		
		serialize: function() {
			return $.param( this.values() ).replace(/\+/g, "%20");
		},
	
	});
	
	return FormValidate;
	
});


