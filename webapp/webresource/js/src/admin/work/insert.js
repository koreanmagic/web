

define([
	
	'jquery',
	'component/FormValidate',
	'src/common/listTable',
	
], function($, FormValidate, listTable) {
	
	// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  하청업체 자동완성  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  //
	var	addressHidden = $("#address"),
		customerId = $("#command").attr("data-customer-id"),
		validateEvents = {
				
			// ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ 배송정보 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
			
			// 택배를 선택했을때, 주소 리스트가 보여지게 한다.
			"change [name='delivery']": function(e) {
				if(e.target.value != 0)
					$("tbody.address-input").removeClass("ui-helper-hidden");
				else {
					$("#address").val("null");
					$("tbody.address-input").addClass("ui-helper-hidden");
				}
			}
		},
		
		
		options = {
			
			//validateForm의 init
			init: function( callbackData ) {
				// hidden값 셋팅
				$("#manager").val("null");
				$("#address").val("null");
			},
				
			// 커밋버튼
			// fileTable에서 커밋을 처리하므로 기본 동작만 제어한다.
			beforeCommit: function( callbackData ) {
				return true;
			},
				
			rules: {
				"#cost, #price": {
					valid: function( v ) {
						if( v.length === 0 || /\d+/.test(v) ) return true;
						else return "공란으로 두거나, 숫자만 써야합니다.";
					},
				},
				"#delivery1": {
					valid: function( v ) {
						if( v == 2 )
							if( !addressHidden.val() ) return "택배를 선택했을 경우, 반드시 주소를 선택해야 합니다.";
						return true;
					},
				}
			},
			
			globalRules: {
				required : ["#item", "#itemType", "#size", "#count", "#itemDetail", "#num"],
			},
		},
	
		validate = FormValidate($("#command"), options);
		validate.addEvent(validateEvents);
		
		// 매니저테이블
		listTable("manager", customerId, {
			selectHandler: function(item) {
				$('#manager').val(item.id);
			},
			cancleHandler: function(item) {
				$('#manager').val("null");
			}
		}).fetch();
		
		// 주소테이블
		listTable("address", customerId, {
			selectHandler: function(item) {
				$('#address').val(item.id);
			},
			cancleHandler: function(item) {
				$('#address').val("null");
			}
		}).fetch();
		
		
		listTable.subcontractor({
			selectHandler: function(item) {
				$('#subcontractor').val(item.id);
			},
			cancleHandler: function(item) {
				$('#subcontractor').val("");
			},
			removable: false,
			selectElement: false,
		});
		
		$(".submit").click(function(){
			validate.commit();
		});
		
});

