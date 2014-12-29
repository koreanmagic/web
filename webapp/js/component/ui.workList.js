
(function($) {
	
	
	var s = $("#testtest").remove(),
			expando = new Date() * 1;
	
	// 데이터 열람
	function access( $item, key, value ) {
		key = expando + key;
		return value === undefined ?
				( value = $item.data( key )) ? value : key
				: $item.data( key, value ) && value;		// 값을 입력한 후, 값을 내보내준다.
	};

	// 알림창
	function notice( $item, msg) {
		var data = access( $item, "index");
		// First Call
		if( typeof data === 'string' ) {
			$item.data( data, $item.find("._notice-panel ._notice") );
			return;
		}
		data.text(msg || "");
	};
	
	
	// 에디터
	function panel( $item, mode ) {
		
		var newPanel, panel;
		
		// 메모 입력
		if( mode === 'editor' ) {
			newPanel =  typeof (newPanel = access( $item, mode )) !== 'string'
						? newPanel
						: access( $item, mode, $("<textarea></textarea>").css({
							'width': '100%', 'height': '100%', 'border': 'none'
						}));
			
		}
		
		panel = access( $item, "panel" ).empty();
		if( newPanel )
			panel.append(newPanel);
		
	};
	
	
	$(".ui-item-container").itemContainer({
		
		toggle: false,
		
		initItem: function( $item ) {
			
			notice( $item );
			access( $item, "panel", $item.find("._panel") );
			
			// 버튼 비활성화
			$item.find(".imgfile-upload").click(function(e) {
				e.preventDefault();
			});
		},
		
		
		createItem: function() {
			return s.clone();
		},
		
		itemRemoveBefore: function() {
			var jqXHR = $.ajax("/ajax-wait", {
				async: false,
			}).done(function(success) {
				console.log( success );
			});
		},
		itemRemoveAfter: function() {
			console.log("껄꾹");
		},
		
		itemClickHandler: function( e, $item ) {
			
			var $target = $(e.target),
				hasClass = function( className ) {
					return $target.hasClass( className );
				};
			
			
			/*
			 * 무조건 modify가 먼저 들어오게 된다.
			 * modify버튼을 클릭한 이후에 update, cancle 버튼이 생성되기 때문이다.
			 */
			if( hasClass("modify") ) {
				notice( $item, "수정" );
				btnControl( $item, "modify" );
				panel( $item, "editor" );
			}
			// 전송
			else if( hasClass("update") ) {
				btnControl( $item, "update" );
			}
			// 
			else if( hasClass("cancle") ) {
				notice( $item, "수정 취소" );
				btnControl( $item );
				panel( $item );
			}
			// 이미지 업로드 버튼 ==> ※※ 버튼 바로 앞에 input 엘리먼트가 위치하고 있어야 한다.
			else if ( hasClass("imgfile-upload") ) {
				imgUpload( $item, $target.parent(), $target.prev() );
			}
		},
	});
	
	// 이미지 업로드
	function imgUpload( $item, $form, $input ) {
		var form = new FormData($form[0]);
		
		$.ajax("/ajax-native", {
			processData: false,
			contentType: false, 
			acyns: false,
			type: "POST",
			data: form,
		});
		
		console.log("asdf");
	};
	
	
	// 데이터 엑세스를 위한 클로저 함수
	function data( $item ) {
	
		// aleady
		var fn = $item.data("_-work-data");
		if( fn ) return fn;
		
		// init
		var obj = {};
		obj["cancle"]	= $('<i class="fa fa-undo cancle"></i>');
		obj["update"]	= $('<i class="fa fa-sign-in update"></i>');
		obj["modify"] 	= $item.find(".modify");
		obj["modify"].before( obj["cancle"] ).before( obj["update"] );
		
		// 클로저
		fn = function( key, value ) {
		
			if( value ) {
				obj[key] = value;
				return value;
			}
				
			return key ? obj[key] : obj;
		};
		
		$item.data("_-work-data", fn );
		return fn;
	};
	

	// 컨테이너에 있는 컨트롤 버튼 관리
	function btnControl( $item, command, responseData ) {
	
		var access = data( $item ), form, resultJSON;
	
		var cancle = access( "cancle" ),
			update = access( "update" ),
			modify = access( "modify" )
			;
		
		// 수정모드로 바꿀때
		if( command === 'modify' ) {
			$item.replaceInput().addClass("modify");	// input으로 변환한다.
			
			// .item_data 엘리먼트가 <form>태그로 둘러쌓이게 된다.
			access( "form", $( "._item-data", $item ).wrap("<form>").parent() );
			cancle.show();
			update.show();
			modify.hide();
			return;
		}
		// update라면 아약스를 실행시키고 리턴한다.
		else if ( command == 'update' ) {
			form = access( "form" );
			console.log('update');
			saveAndGetJSON( $item, form );
			return;
		}
		
		/* 아래 코드는 원상복구 시키는 코드 */		
		
		// command가 key, value로 이루어진 객체라면 이것은 ajax의 결과다.
		if( jQuery.isPlainObject( command ) )
			responseData = command;
		
		// 수정모드가 아닌 모든 상황
		form = access( "form" );
		form.replaceWith( form.children() );	// 다시 돌린다.
		delete access()["form"];
		
		$item.restoreInput( responseData );	// input을 복구한다.
		cancle.hide();
		update.hide();
		modify.show();
		
		$item.removeClass("modify");
	};
	
	
	// 서버에 바뀐 값 전송
	function saveAndGetJSON( $item, form ) {
	
		var option = {
			type: "POST",
			dataType: "json",
			data: form.serialize(),
			},
			returnValue;
	
		$.ajax("/ajax", option)
		.success(function( success_data, statusText, jqXHR ) {
			btnControl( $item, success_data );	// 버튼만 끈다.
		})
		.error(function( jqXHR, statusText, error ) {
			console.log( error );
			btnControl( $item );
		});
	
	};
	
	
})(jQuery);





