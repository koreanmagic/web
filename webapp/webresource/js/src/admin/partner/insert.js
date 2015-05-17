
define([
	
	'jquery',
	'component/FormValidate',
	'src/common/listTable',
	
], function($, FormValidate, listTable) {


	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  하청업체 자동완성  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  //
	
	var	uploading = false,
		validateEvents = {
				
			// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 배송정보 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
			"change [name='delivery']": function(e) {
				var validate = this;
				if( e.target.value != 0 ) {
					$.getJSON("/admin/address/list/customer/" + this.getForm().getAttribute("data-customer-id"))
						.success(function(data) {
							addressTable._init().addList(data).enable();
						});
				}
				else {
					addressHidden.val("");
					addressTable.disable();
				}
			},
			"click #extend": function(e) {
				if( e.target.checked ) {
					
				}
			},
		},
		
		options = {
			
			//validateForm의 init
			init: function( callbackData ) {
				
			},
				
				
			// 커밋버튼
			// fileTable에서 커밋을 처리하므로 기본 동작만 제어한다.
			beforeCommit: function( callbackData ) {
				// address가 비어있으면, 값을 찾을 수 없기때문에 스프링 콘트롤러단에서 에러가 난다.
				// 때문에 값이 없다면 아예 지워버린다.
				return true;
			},
				
			
			rules: {
			},
			
			globalRules: {
				required : ["#name"],
			},
		},
	
		validate = FormValidate($("#command"), options);
		validate.addEvent(validateEvents);
});

