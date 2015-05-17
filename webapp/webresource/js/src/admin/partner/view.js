
define([
	
	'jquery',
	'component/FormValidate',
	'src/common/listTable',
	'utils',
	
], function( $, FormValidate, listTable, Utils ) {
	
	
	var formWrap = $(".form-wrap"),
		id = formWrap.attr("data-id"),
		type = formWrap.attr("data-type"),
		staticURL = "/admin/" + type + "/ajax";
	
	function Inputs( selector ) {
		
		var self = this,
			validateOptions = {
				beforeCommit: function(e) {
					alert(e);
					return false;
				}
			},
			container = this.element = $(selector),
			validator = this.validator = FormValidate(selector, validateOptions),
			btns = container.find(".modify-btn >a"),
			inputs = this.inputs = {},
			texts = this.texts = {};
		
		this.groups = container.find(".form-group");
		
		this.btns = {
			modify: ["confirm cancle", btns.filter(".modify")],
			cancle: ["modify", btns.filter(".cancle").addClass("disable")],
			confirm: ["modify", btns.filter(".confirm").addClass("disable")],
		};
		
		container.find("[name]").each(function( v, input) {
			v = input.name;
			inputs[ v ] = input;
		});
		container.find("span[data-type]").each(function( v, span) {
			v = span.getAttribute("data-type");
			texts[ v ] = span;
		});
		
		$.each(self.btns, function( prop, arr ) {
			arr[1].on("click", function(e) {
									e.preventDefault();
									if(arr[1].hasClass("disable")) return;
									self.mode(prop);
								});
		});
		
		this._init();
	};
	
	Inputs.prototype = {
		
		_init: function() {
			var self = this;
			$.ajax( staticURL + "/get/id/" + id)
				.success(function( data ) {
					self.setTexts( Utils.jsonToDataMap(data) );
				});
		},
		
		setTexts: function( data ) {
			$.each( this.texts, function( prop, span ) {
				span.textContent = data[prop] || "";
			});
		},
		setInputs: function( data ) {
			$.each( this.inputs, function( prop, input ) {
				input.value = data[prop] || "";
			});
		},
		
		serialize: function() {
			var result = [];
			$.each( this.inputs, function( prop, input ) {
				result.push( { name: prop, value: input.value } );
			});
			return result;
		},
		
		_mode: {
			"modify": function(self) {
				$.getJSON( staticURL + "/modify/" + id)
					.success(function(data) {
						self.setInputs( Utils.jsonToDataMap(data) );
						self.groups.removeClass("hidden");
					}
				);
			}, // modify
			
			"cancle": function(self) {
				$.ajax( staticURL + "/modify/done")
					.success(function() {
						self.groups.addClass("hidden");
					}
				);
			}, // cancle 
			"confirm": function(self) {
				option = {
					url:  staticURL + "/modify",
					data: $.param(this.serialize()).replace(/\+/g, "%20"), 
					type: "POST",
					processData: false,
				};
			
				$.ajax(option)
					.success(function(data) {
						self._init();
						self.groups.addClass("hidden");
					});
			} // confirm
		},
		
		mode: function( mode ) {
			$.each(this.btns, function(name, arr) {
				if(arr[0].indexOf(mode) != -1) arr[1].removeClass("disable");
				else arr[1].addClass("disable");
			});
			this._mode[mode].call(this, this);
		},
	};
	
	var business = new Inputs("#businessInfo"),
		number = new Inputs("#numberInfo");
		
		// 매니저테이블
		listTable("manager", id).fetch();
		
		// 주소테이블
		listTable("address", id).fetch();
		
		// 주소테이블
		listTable("bank", id).fetch();
		
		
});

