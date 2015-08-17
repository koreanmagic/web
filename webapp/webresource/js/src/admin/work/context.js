
define([
	
	'jquery',
	'events',
	
	'ui/scrollTable',
	
], function($, Events) {
	
	
	/* ***********************  ▼ 작업리스트 추가 ▼  *********************** */
	var obj = (function() {
		var self = this,
			context = $("#contextLine"),
			options = {
				removable: false,
				selectElement: false,
				selectHandler: function( item ) {
					window.location.href = "/admin/work/insert/" + item.id;
				},
			},
			element = context.find("#searchCustomer"),
			table = context.find(".customer-table").scrollTable(options).scrollTable("instance"),
			input = element.find("input"),
			props = '?props=' + ['id', 'name', 'ceoName', 'tel'].join('&props=');
			;
		
		element.on("popOn", function() {
			table.clear();
			input.val("");
			input.focus();
		});
		
		
		Events.keyup(input, function( word ) {
			if( word ) {
				$.getJSON("/admin/customer/search/byname/" + word + props )
					.success( function(data) {
						table.clear();
						table.addList(data);
					});
			} else {
				input.val("");
				table.clear();
				return;
			}
		});
		
	})();
	
	
});


