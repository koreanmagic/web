
define([
        "jquery",
        
        "ui/scrollTable-ajax"
], function($) {
	
	var validateOptions;
	
	function tableOption( name, customerId, validateOption, options ) {
		return $.extend({
			prefix: "/admin/"+ name + "/ajax",
					
			// 리스트 채우기
			fetch: {
				options: {
					url: function() {
						return "/admin/"+ name + "/ajax/list/" +  customerId();
					}
				}
			},
			upload: {
				options: {
					url: function( item, prefix ) {
						return prefix + "/add/"+ customerId();
					},
				},
				validate: $.extend({}, validateOption)
			},
			modify: {
				validate: $.extend({}, validateOption)
			},
			
		}, options);
	};
	
	
	validateOptions = {
		manager: {
			globalRules: {
				"required": ["#name", "#position"],
			}
		},
		address: {
			globalRules: {
			}
		},
		bank: {
			globalRules: {
			}
		}
	};
	
	// OUTPUT FUNCTION returnOptions의 경우 옵션만 전달받는다.
	function listTable( name, customerId, options ) {
		// workEditor의 경우 customerId가 계속 변경된다. 따라서 프록시 핸들러를 등록한다.
		var proxy = $.isFunction(customerId) ? customerId : function() { return customerId; };
		return $("." + name + "-table")
			.scrollTableAjax( tableOption( name, proxy, validateOptions[name], options ) )
			.scrollTableAjax("instance");
	};
	
	
	listTable.subcontractor = function ( options ) {
		
		var c =  $(".subcontractor-table")
				.scrollTable(options)
				.scrollTable("instance");
				
		$.ajax("/admin/subcontractor/list/all")
		.success(function(data) {
			c.addList(data);
		});
	};
	
	
	return listTable;
	
});

		