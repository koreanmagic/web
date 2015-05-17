
define([
	'jquery',
	'component/dom-ui',
	
	'./list/editor',
	'./list/itemContainer',
	'./context',
], function($, a) {
	
	function link( inner, obj ) {
		return '<a href="/resource/' + obj.saveName + '">' + inner + '</a>'; 
	};
	
	
	
	function resourceHtml( index, obj, filter ) {
		var html = '<ul data-id="' + obj.id + '">',
			htmlArray = {},	// li 순서를 맞추기 위한 hashMap
			typeHandlers = resourceHtml.types,
			cValue,
			handler;
		
		$.each(obj, function(prop, value) {
			if( filter ? filter.indexOf(prop) !== -1 : true) {
				handler = typeHandlers[prop];
				cValue = handler ? handler(index, value, obj) : value;
				
				htmlArray[ cValue.index ] = '<li class="' + prop +
											'" title="' + (cValue["title"] || value) + '">' +
											(cValue["value"] || value) + '</li>';
			}
		});
		
		$.each(htmlArray, function(i, li) {
			html += li;
		});
			
		return html + "</ul>";
	};
	
	// 각 정보별 처리 핸들러 (v는 각 프로퍼티에 해당하는 값이다)
	resourceHtml.types = {
		"orignalName": function(index, v, obj) {
			return {
					title: v, 
					value: link(v, obj),
					index: 3
			};
		},
		"size": function(index, v, obj) {
			return {
					index: 5
					};
		},
		"uploadTime": function(index, v, obj) {
			return {
					index: 4
					};
		},
		"fileType": function(index, v, obj) {
			return {
					title: "img",
					value: link('<i class="img-icons-file-l ' + v +'"></i>', obj),
					index: 2
					} ;
		},
	};
	
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ Item.prototype 확장 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */ 
	var itemExtend = {
		
		// Item객체는 jQuery를 상속한다. jQuery의 init메서드와 중복되지 않게 _를 접두사로 한다.
		// Item객체 생성시 호출된다.
		init: function( ) {
		
			this.resourceRefresh();
		
		}, // init
		
		
		// 여기에 등록하면 위젯 이벤트로 등록된다. halder <this:: widget>
		// string이면 prototype에 등록된 핸들러 이름으로 사용한다.
		eventHandlers: {
			
			// 아이템 삭제
			"click ._work .delete": function(e) {
				var handlers =  {
					success: function () {
						alert("삭제 완료");
						location.reload(); 
					},
					error: function() {
						alert("삭제 실패\n한컴 관리자에 문의하세요");
					}
				};
				
				this.getJSON("/admin/work/delete/" + e.item.id,  handlers);
			},
			
			"mouseover ._work .delete": function(e) {
				e.item.find("._work").addClass("delete");
			},
			"mouseout ._work .delete": function(e) {
				e.item.find("._work").removeClass("delete");
			},
			
			"click [data-info]": function(e) {
				e.preventDefault();
				var self = this,
					target = e.currentTarget,
					name = target.getAttribute("data-info"),
					id = target.getAttribute("data-info-key");
					url = "/admin/" + name + "/ajax/get/id/" + id,
					element = $(".info-"+name);
				$.ajax(url)
				.success(function(data) {
					self.popInfo( element, data );
				});
			},
			
			"click [data-editor]": function(e) {
				e.preventDefault();
				e.item.editor({ openTab: e.currentTarget.getAttribute("data-editor") });
			},
			
			// 아이템 박스에서 포커스 아웃되면 모든 이벤트를 초기화시킨다.
			"focusout ._work-wrap": "initialize",
		},
		
		
		refresh: function() {
			this.refreshItemData();
			this.resourceRefresh();
		},
		
		// 데이터 갱신
		refreshItemData: function() {
			var self = this;
			this.getJSON(this.URL("/admin/work/editor/get/"),
				function(data) {
					self.values(data);
				});
		},
		
		
		// 참고파일 등 확인
		resourceRefresh: function() {
			var self = this,
				div = this.find("._pop-file-refer");
			
			$.getJSON("/admin/work/get/resource/size/" + this.id)
				.success(function( data ) {
					if( data.size > 0 ) {
						div.removeClass("disable");
						div.find(".size").text(data.size);
					} 
					else div.addClass("disable");
					
				});
			
		},
		
		// 모든 이벤츠 초기화
		// 아이템 박스에서 포커스가 아웃되었을때 지울만한 것들을 써넣으면 된다.
		initialize: function(e) {
		},
		
	};
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒  WIDGET create  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	$("._work-container").itemContainer({
		// 아이템 객체 확장
		extendOfItem: itemExtend,
	});
	
	
});






