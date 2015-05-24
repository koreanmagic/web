
define([
	'jquery',
	'component/dom-ui',
	
	'./list/editor',
	'./list/itemContainer',
	'./context',
], function($) {
	
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
			
			// 에디터 열기
			"click [data-editor]": function(e) {
				e.preventDefault();
				e.item.editor({ openTab: e.currentTarget.getAttribute("data-editor") });
			},
			
			// 컨트롤키를 누르고 클릭하면 인쇄파일 업로드 창이 열린다.
			"click ._work-controller": function(e) {
				var ct = $(e.currentTarget);
				
				if(e.ctrlKey) ct.toggleClass('editor');
			},
			
			
			// ****** ▼ 파일 업로드 ▼ ******
			// 드랍다운 열릴시 초기화
			"dropdownon .confirm-upload": function(e) {
				$(e.currentTarget).find('input').val('');
				$(e.currentTarget).find('button').addClass('ui-helper-hidden');
			},
			
			// 인풋 등록시 버튼 나타나게..
			"change .confirm-upload > input": function(e) {
				if(e.currentTarget.files.length) $(e.currentTarget).next().removeClass('ui-helper-hidden');
				else $(e.currentTarget).next().addClass('ui-helper-hidden');
			},
			
			// 업로드
			"click .confirm-upload > button": function(e) {
				e.preventDefault();
				
				var item = e.item,
					formData = new FormData();
				
				formData.append("file", e.currentTarget.previousSibling.files[0]);
				formData.append("work", e.item.id);
				formData.append("serviceType", 'workConfirmFile');
				
				$.ajax({
					url: '/admin/work/editor/resource/upload',
					processData: false,
					contentType: false,
					data: formData,
					type: 'POST',
				}).success(function(){
					item.confirmFile();
				});
			},
			// ****** ▲ 파일 업로드 ▲ ******
			
			
			// 아이템 박스에서 포커스 아웃되면 모든 이벤트를 초기화시킨다.
			"focusout ._work-wrap": "initialize",
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






