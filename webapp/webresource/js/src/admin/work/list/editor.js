

define([
	
	'jquery',
	'component/FormValidate',
	'src/common/listTable',
	
	'ui/simple-tabs',
	
], function($, FormValidate, listTable) {
	
	
	var selectDefaultOptions = {
			enableReset: true,
			confirmHandler: function( data, value ){
				$.ajax( prefix + this.name + "_id/" + value.id )
					.success(function() {
						alert("변경 완료!");
						data.init();
					});	
			},
			equals: function( customerValue, item ) {
				return customerValue == item.id;
			}
		},
		listTableDefaultOptions = {
			getId: function(values) {
				return values.id;
			},
			afterHandler: $.noop,
		};
		
	
	function SelectTable( name, data, options ) {
		var self = this;
		
		options = this.options = $.extend({}, selectDefaultOptions, options);
		this.name = name;
		this.element = data.find(".choice-list");
		this.selected = data.find(".selected");		// 선택한
		this.current = data.find(".current");		// 현재
		
		this.confirm = data.find(".confirm")		// 버튼
		.on("click", function(e) {
			e.preventDefault();
			options.confirmHandler.call(self, data, self.getSelected());
		});
		
		this.reset = data.find(".reset");
		if(!options.enableReset)
			this.reset.addClass("ui-helper-hidden");
		else {
			this.reset
			.on("click", function(e) {
			e.preventDefault();
			$.ajax( prefix + name + "_id/null" )
				.success(function() {
					alert("취소완료");
					data.init();
				});
			});
		}
		
	};
	
	SelectTable.prototype = {
		getCurrent: function() {
			return this.current.data("#value");
		},
		
		setCurrent: function( value, displayValue ) {
			if(this.options.enableReset) displayValue ? this.reset.removeClass("ui-helper-hidden") : this.reset.addClass("ui-helper-hidden");
			this.current.text(displayValue || "선택 없음").data("#value", value || null);
			this.clear();
		},
		
		setSelected: function( value, displayValue ) {
			if(this.isAleady(value)) {
				this.clear();
				return this;
			} 
			this.selected.text(displayValue).data("#value", value);
			this.ready(true);
			return this;
		},
		
		getSelected: function() {
			return this.selected.data("#value");
		},
		
		isAleady: function( value ) {
			return this.options.equals(this.getCurrent(), value);
		},
		
		clear: function() {
			this.selected.text("").data("#value", null);
			this.ready();
		},
		
		ready: function( flag ) {
			flag = !!flag;
			if( this._ready !== flag )
				flag ? this.confirm.removeClass("ui-helper-hidden") : this.confirm.addClass("ui-helper-hidden");
			this._ready = flag;
			return this;
		},
		disable: function() {
			this.element.addClass("ui-helper-hidden");
		},
		enable: function() {
			this.element.removeClass("ui-helper-hidden");
		}
	};
	
	// 매니저, 주소, 하청업체 등을 변경하기 위한 셀렉팅 객체
	function fetchData( name, data, options ) {
		
		var self = this,
			table,
			selectTable = new SelectTable( name, data, options["selectOptions"]);
		
		options = $.extend({}, listTableDefaultOptions, options);
		
		table = listTable( name, options["proxy"], 
							{
								// selectedHandler
								selectHandler: function( item ) {
									selectTable.setSelected(item, options["displayHandler"].call(self.setSelected, item));
								},
								// cancleHandler
								cancleHandler: function( item ) {
									selectTable.clear();
								},
								
								disableHandler: function() {
									selectTable.disable();
								},
								enableHandler: function() {
									selectTable.enable();
								},
								fetchAfterHandler: function() {
									var self = this;
									$.ajax( options.fetchURL )
										.success(function(json) {
											
											var value = table.searchItem({"id": options.getId( json )});
											selectTable.setCurrent(json, value && options["displayHandler"].call(self.setCurrent, value["item"]));
											
											options.afterHandler.call(self, json);
									});
									
								},
								removeHandler: function( item, uuid ) {
									var returnValue = true;
									if(selectTable.isAleady(item)) {
										returnValue = confirm('현재 선택된 정보입니다.\n삭제하면 선택도 취소됩니다.\n 삭제하시겠습니까?');
										$.ajax( prefix + name + "_id/null" );
										returnValue = {
											done: function() {
												data.init();
											}
										};
									}
									return returnValue;
								},
								// 수정 후 핸들러
								modify: {
									afterHandler: function() {
										data.init();
									}
								}
							}
						);
		return table;
	};
	
	
	
	// 클라이언트에게서 받은 옵션과 이 옵션을 extends해서 기본값을 유지시킨다.
	var prefix = "/admin/work/editor/",
	
	events = {
			
	},
		
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 위젯 옵션  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	 widgetOptions =	{
	 	
	 	// 각 탭별 핸들러
		tabsHandlers: {
			"0": {
				// 탭 위젯이 만들어질때
				create: function( data ) {
					var validateOptions = {
						
						beforeCommit: function( data ) {
							var self = this,
								option = {
									url: prefix + "data/",
									type: "POST",
									data: this.serialize()
								};
								
							$.ajax(option)
							.success(function( v ) {
								alert("저장 완료");
							})
							.fail( function() {
							});
							return false;
						},
						
						modifyTrue: function() {
						},
						modifyFalse: function() {
						},
						
						rules: {
							"#cost, #price": {
								valid: function( v ) {
									if( v.length === 0 || /\d+/.test(v) ) return true;
									else return "공란으로 두거나, 숫자만 써야합니다.";
								},
							},
						},
						
						globalRules: {
							"required": ["#item", "#itemDetail", "#count", "#size"],
						}
					};
					
					data.validate = FormValidate(data.find("#modifyForm")[0], validateOptions);
				},
				
				// 처음 접근할때 :: (지연 로딩이 가능하다.)
				init: function( data ) {
					
					$.getJSON( prefix + "data/" )
					.success(function(json) {
						data.validate.initValue( json );
					});
				},
				
				open: function( data, accessNum ) {
					
				},
				
				close: function( data, accessNum ) {
				},
				
				disable: function( data ) {
				},
				_events: {
				},
			}, // 0
			
			"1": {
				// 탭 위젯이 만들어질때
				create: function( data ) {
					var options = {
							proxy: $.proxy(function() { return this.getCustomerId(); }, this),
							displayHandler: function( item ) {
								return item.name + " " + item.position;
							},
							fetchURL: prefix + "values?keys=manager.id%20id",
						};
					// 매니저테이블
					data["table"] = fetchData( "manager", data, options);
				},
				
				// 처음 접근할때 :: (지연 로딩이 가능하다.)
				init: function( data ) {
					data["table"].fetch();
				},
				
				open: function( data, accessNum ) {
					
				},
				
				close: function( data, accessNum ) {
				},
				
				disable: function( data ) {
				},
				_events: {
					".change": {
						"click": function(e) {
							
						}
					}
				},
			},
			"2": {
				// 탭 위젯이 만들어질때
				create: function( data ) {
					
					var selectOptions = {
							enableReset: false
						},
						selectTable = data["selectTable"] = new SelectTable("subcontractor", data, selectOptions),
						options = {
							// 리스트 클릭할때마다
							selectHandler : function( item ) {
								selectTable.setSelected( item, item.name );
							},
							selectLen: 1,
							removable: false,
							selectElement: false,
					};
					data["scrollTable"] = data.find(".subcontractor-table")
											.scrollTable(options).scrollTable("instance");
				},
				
				// 처음 접근할때 :: (지연 로딩이 가능하다.)
				init: function( data ) {
					var selectTable = data["selectTable"],
						table = data["scrollTable"];
						
					$.ajax("/admin/subcontractor/list/all")
						.success(function(json) {
							data["scrollTable"].clear();
							data["scrollTable"].addList(json);
						})
						.success(function() {
							$.ajax(prefix + "/values?keys=subcontractor.id%20id")
										.success(function(json) {
											var id = json.id;
											if(id) {
												value = table.searchItem({"id": id})["item"];
												if(!value) new Error("반드시 해당 데이터가 리스트에 존재해야합니다.");
												selectTable.setCurrent(value, value["name"]);
											} else {
												selectTable.setCurrent();
											}
									});
						});
						
				},
				
				open: function( data, accessNum ) {
					
				},
				
				close: function( data, accessNum ) {
				},
				
				disable: function( data ) {
				},
				_events: {
				},
			}, // 2
			"3": {
				// 탭 위젯이 만들어질때
				create: function( data ) {
					var removeFlag = false, 
					common =  {
						selectElement: false,
					},
					
					listOptions = {
						removeHandler: function( item, uuid ) {
							var self = this,
								flag = removeFlag;
							if(!flag) {
								$.get( prefix + "resource/delete/" + item.id, { "serviceType": 'workResourceFile' } )
								.success(function() {
									removeFlag = true;
									self.remove(uuid);
									alert(item.originalName + " 삭제");
								});
							}
							else removeFlag = false;
							
							return flag;
						},
					},
					uploadOptions = {
						changeHandler: function() {
							if( this.size() ) $(".btn-upload").removeClass("ui-helper-hidden");
							else $(".btn-upload").addClass("ui-helper-hidden");
						},
					},
					upload,
					events = {
						"#file": {
							"change": function(e) {
								this.addList(e.target.files);
							}
						},
						".btn-upload": {
							"click": function(e) {
								var items = upload.getItems(),
									formData = new FormData();
								$.each(items, function( i, file ) {
									formData.append("file", file);
								});
								 $.ajax({
					                url: prefix + 'resource/upload',
					                processData: false,
					                contentType: false,
					                data: formData,
					                type: 'POST',
					                success: function(result){
					                   data.init();
					                }
					            });
							}
						}
					};
					
					data["list"] = data.find(".resource-table")
												.scrollTable( $.extend(listOptions, common) ).scrollTable("instance");
					upload = data["upload"] = data.find(".resource-upload-table")
												.scrollTable( $.extend(uploadOptions, common) ).scrollTable("instance");
					
					$.each(events, function( selector, handler) {
						upload._on(data.find( selector ), handler);
					});
				},
				
				// 처음 접근할때 :: (지연 로딩이 가능하다.)
				init: function( data ) {
					// 업로드 버튼은 감춘다.
					$(".btn-upload").addClass("ui-helper-hidden");
					
					data["upload"].clear();
					data["list"].clear();
					
					$.getJSON(prefix + "resource/list", { "work": this.getWorkId(), "serviceType": 'workResourceFile' })
					.success(function(json) {
						data["list"].addList(json);
					});
				},
				
				
				open: function( data, accessNum ) {
					
				},
				
				close: function( data, accessNum ) {
				},
				
				disable: function( data ) {
				},
				_events: {
				},
			}, // 3
			"4": {
				// 탭 위젯이 만들어질때
				create: function( data ) {
					var changeBtn = data["changeBtn"] = data.find(".change-btn"),
						radioBtns = data["radioBtns"] = data.find("[name='delivery']"),
						options = {
							"proxy": $.proxy(function() { return this.getCustomerId(); }, this),
							"displayHandler": function( item ) {
								return "[" + radioBtns.filter(":checked").next().text() + "] " + item.text;
							},
							selectOptions: {
								confirmHandler: function( data, item ) {
									var param = $.param({
										"addressId": item.id,
										"deliveryId": radioBtns.filter(":checked").val()
									});
									$.post(prefix + "delivery/", param)
									.success(function(){
										data.init();
									})
									.fail(function(d){
										console.log(d.responseText);
									});
								},
								equals: function(orig, item) {
									return orig.delivery.ordinal == radioBtns.filter(":checked").val() &&
											 orig.address == item.id;
								},
								enableReset: false,
							},
							"fetchURL": prefix + "values?keys=delivery%20delivery&keys=address.id%20address",
							// 아이디를 반환하는 메서드지만 초기화를 함께 진행한다.
							"getId": function(value) {
								var ordinal = value.delivery["ordinal"]; 
								data["currentDelivery"] = value.delivery; // 현재 Delivery 저장
								 
								$.each(radioBtns, function( _, ele ) {
									if(ele.value == ordinal) {
										ele.checked = true;
										$(ele).trigger("change");
									}
								});
								return value.address;
							},
						};
					// 테이블
					data["table"] = fetchData( "address", data, options);
					
					// 공용 함수
					data["check"] = function( value ) {
						if( value != '0' ) {
							data["table"].enable().refresh();
							changeBtn.addClass("ui-helper-hidden");
						} else {
							data["table"].disable();
							data["currentDelivery"].ordinal != 0 && changeBtn.removeClass("ui-helper-hidden");
						}
					};
				},
				
				// 처음 접근할때 :: (지연 로딩이 가능하다.)
				init: function( data ) {
					data["table"].fetch();
				},
				
				open: function( data, accessNum ) {
					
				},
				
				close: function( data, accessNum ) {
					
				},
				
				disable: function( data ) {
					
				},
				_events: {
					"[name='delivery']": {
						"change": function(e) {
							this.getData().check(e.target.value);
						}
					},
					".change-btn": {
						"click": function(e) {
							e.preventDefault();
							var data = this.getData(),
								handler = this.getHandler(),
								param = $.param({
									"addressId": 'null',
									"deliveryId": data["radioBtns"].filter(":checked").val()
								});
							$.post(prefix + "delivery/", param)
							.success(function(){
								data.init();
							});
						}
					}
				}, // _events
			},
		}
	},
	
	
proto = $.extend({}, 
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 기본 위젯 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	widgetPrototype = {
	
		version: "1.1",
		options: widgetOptions,
	}, // widgetPrototype
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ Create  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
		
		_create: function() {
			this._super();
		},
		
		_createClass: function( name ) {
			return this.widgetFullName + "-" + name;
		},
		
		_init: function() {
			this._super();
		},
		
		find: function( selector ) {
			return this.element.find(selector);
		},
		
		getItem: function() {
			return this.options.item;
		},
		
		// 현재 수정중인 workId
		getWorkId: function() {
			return this.getItem().id;
		},
		// 현재 수정중인 work의 customerId
		getCustomerId: function() {
			return this.getItem().customer;
		},
		
		enable: function() {
			
		},
		
		// 끌때
		disable: function() {
			this._super();
			this.element.addClass("ui-helper-hidden");
		},
	},
		
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  Item관련 메서드  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	{
		
	}
	
); // prototype extend
	
	
	// widget 코드 시작
	var selectBtn = $.widget("ui.editorTabs", $.ui.simpleTabs, proto);

});

