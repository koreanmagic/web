
define([
	
	'jquery',
	'ui/scrollTable'
	
], function($) {
	
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
			existKeyword;
		
		element.on("popOn", function() {
			table.clear();
			input.val("");
		});
		
		input.on("keyup", function(e) {
			var keyword = input.val();
			// 빠르게 타이핑쳤을떄 같은 글씨가 두번 호출되는 걸 방지하는 코드
			if( existKeyword === (keyword = $(e.target).val())) return;
			if( keyword.length === 0 ) {
				input.val("");
				table.clear();
				return;
			} 
			existKeyword = keyword;
			
			if( /^[가-힣|\w|\s]+$/.test(keyword) ) {
				$.getJSON("/admin/customer/search/name/" + keyword)
					.success( function(data) {
						table.clear();
						table.addList(data);
					});
				}
		});
		
	})();
	
	
});


