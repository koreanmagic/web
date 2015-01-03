
(function($) {
	
	
	var VALUE = {"거래처": "_customer",
				"날짜": "_date",	
				"품목": "_item",
				"상세": "_memo",
				"사이즈": "_size",
				"수량": "_count",
				"제작단가": "_cost",
				"공급단가": "_price",
				"하청업체": "_constructor"
				},
		highlightList = ["_customer", "_constructor"],
		highlightLock = false
		;
		
		
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
	
	// 특정 조건에 맞는 엘리먼트에 클래스네임 부여
	// this :: ItemManager객체	
	function highlight( className, value ) {
		
		// 하이라이트 기능이 잠겨있을 경우 바로 리턴
		// 콘트롤 센터의 셀렉터그룹이 이를 잠글 수 있다.
		if(highlightLock) return;
		
		var manager = this, old;
		
		// highlight( false ) => All Reset!!
		if( className === false ) {
			manager.setClass( "ui-invisible", false );
			manager.status( "highlight", false );
			return;
		}
		
		if( old = manager.status( "highlight" ) ) {
			if( old.className === className && old.value === value ) return;
			else manager.setClass( "ui-invisible", false );	// 같은 이름이 아니면 전부 리셋한다.
		}
			
		manager.setClass( function(){
			if( this.access( className ) !== value )		// 값이 다른 것만 감춘다.
				return "ui-invisible";
		});
		
		manager.status( "highlight", {"className": className, "value": value} );	// 현재 하이라트 중인 것 표시

	};
	
	
	// 정렬, 하이라이트 등을 선택하는 셀렉터의 OPTION 엘리먼트를 채운다.
	// 함수객체 자체에 프로퍼티가 있다.
	function setSelect( type, value, extend ) {
		
		var obj = setSelect[type],
			text = extend ? 
					extend.text ? extend.text : value
					: value; 
		
		if( obj["list"].indexOf( value ) == -1 ) {	// 없는 객체면
			obj["list"].push( value );
			obj["select"].append( $("<OPTION>", $.extend({ "value": value, "text": text }, extend)) );
		}
				
	};
	
	
	/* ************* Item.prototype 확장 ************* */ 
	var extend = {
		
		// 가리기
		blind: function( flag ) {
			if( flag )
				this.overlay.show();
			else
				this.overlay.hide();
			
			return this;
		},
		
		// 마우스 오버시
		overHandler: function( e ) {
			
			var i = highlightList.length, target = $(e.target), value;
			
			while(i--) {
			
				if( target.hasClass( highlightList[i] ) ) {
					value = this.access( highlightList[i] );
					highlight.call( this.manager, highlightList[i], value );
					break;
				}
			}
			
			if(!value) highlight.call( this.manager, false );
			
		},
	};
	
	
	/* ************** Widget create ************** */
	$(".ui-item-container").itemContainer({
		
		// 극초반에 호출되는 함수. 미리 해두어야할 로직을 한다.
		preInit: function() {
			var obj;
			// customerSelect :: 회사명 이름을 기준으로 하이라이트 설정하는 셀렉터 엘리먼트 
			obj = setSelect[VALUE["거래처"]] = { "list": [] };
			obj["select"] = this._find("._highlight-customer");
			
			
			// 정렬기준 설정
			obj = setSelect["sort"] = { "list": [], "select": this._find("._sort") };
			$.each(VALUE, function(i, v) {
				setSelect( "sort", i, { "value": v } );
			});
		},
		
		
		// 위젯의 _create의 로직을 빠져나가기 직전 메서드 끝에서 호출
		createWidget: function() {
			var self = this, val, customerSelect;
			
			// ::EVENT REGISTER:: 셀렉터 엘리먼트 거래처 하이라이트 이벤트 설정
			setSelect[ VALUE["거래처"] ]["select"].on("change", function(e) {
				
				val = $(e.target).val();
				if( val === 'reset') {
					highlightLock = false;
					highlight.call( self.items, false );
					return;
				}
				highlightLock = false;
				highlight.call( self.items, VALUE[ "거래처" ], val );
				highlightLock = true;
			});
			
			
			// 정렬
			setSelect[ "sort" ]["select"].on("change", function(e) {
				
				val = $(e.target).val();
				
				if( val !== 'reset') {
					self.items.sort(val);
				}
			});
			
						
		},
		
		initItem: function( item ) {
			
			setSelect( VALUE["거래처"], item.access( VALUE["거래처"] ) );	// 셀렉터 연결
			
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
		
		// 아이템 객체 확장
		extendOfItem: extend,
		
		// ::Event:: MouseOver
		bodyOverHandler: function( e, item ) {
			
		},
		
		// ::Event:: MouseClick
		bodyClickHandler: function( e, item ) {
			
			var $target = $(e.target),
				hasClass = function( className ) {
					return $target.hasClass( className );
				};
			
			/*
			 * 무조건 modify가 먼저 들어오게 된다.
			 * modify버튼을 클릭한 이후에 update, cancle 버튼이 생성되기 때문이다.
			 */
			if( hasClass("modify") ) {
				btnControl( item, "modify" );
				panel( item, "editor" );
			}
			// 전송
			else if( hasClass("update") ) {
				btnControl( item, "update" );
			}
			// 
			else if( hasClass("cancle") ) {
				btnControl( item );
				panel( item );
			}
			// 이미지 업로드 버튼 ==> ※※ 버튼 바로 앞에 input 엘리먼트가 위치하고 있어야 한다.
			else if ( hasClass("imgfile-upload") ) {
				imgUpload( item, $target.parent(), $target.prev() );
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
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  모든 버튼 이벤트 관리   ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	function btnControl( $item, command, responseData ) {
	
		var access = data( $item ), form, resultJSON;
	
		var cancle = access( "cancle" ),
			update = access( "update" ),
			modify = access( "modify" )
			;
		
		// 수정모드로 바꿀때
		if( command === 'modify' ) {
			$item.replaceInput().addClass("modify");	// input으로 변환한다.
			
			// .item 엘리먼트가 <form>태그로 둘러쌓이게 된다.
			access( "form", $item.find("._list-item-container").wrap("<form>").parent() );
			cancle.show();
			update.show();
			modify.hide();
			return;
		}
		// update라면 아약스를 실행시키고 리턴한다.
		else if ( command == 'update' ) {
			form = access( "form" );
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






