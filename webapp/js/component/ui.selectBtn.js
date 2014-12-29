
/*
 * selectBtn
 */
(function($) {


	// 전역 변수
	// 현재 활성화된 widget 객체가 여기에 등록된다.
	var activeElement = null;
	
	// 이 위젯이 이벤트의 target이 되는 경우는 body까지 이벤트가 전달되지 않는다.
	// 따라서 body에 클릭이벤트가 전달된다는 것은 다른 곳을 클릭했다는 것이다.			
	$(document.body).click(function() {
		!activeElement || activeElement.close();
		activeElement = null;
	});


	// widget 코드 시작
	var selectBtn = $.widget("ui.selectBtn", {
	
		version: "1.1",
		defaultElement: "<button>",
		
		options: {
			// 옵션 리스트가 선택됐을때 호출
			// this :: widget 객체
			selected: function(e) {
				this._btn.text($(e.target).text());
				this._active();
			},
			btnClick: function(e) {
				
			},
			// false를 리턴하면 기본 동작이 실행되지 않는다.
			toggleClick: function(e) {
				return true;
			},
			
			url: null,
			data: null,
			success: $.noop,
		},
		
		
		_create: function() {
			var self = this;
			
			// this._btn ...  각 버튼의 엘리먼트 jQuery객체를 위젯객체의 프로퍼티로 등록해둔다.
			$.each(['.btn', '.toggle', '.option'], function() {
				self[this.replace(".", "_")] = self.element.find(arguments[1]);
			});
			
			// "eventType .select" <-- 이벤트 타입과 공백을 두고 입력하면 select로 인식한다.
			this._on({
				"click": function(e) {
					e.stopPropagation();
					
					// selected
					if(self._option[0].contains(e.target))
						self._selecting(e);
						
					// btn click
					else if(self._btn[0].contains(e.target))
						self._btnClick.call(self, e);
						
					// toggle click
					else {
						if(self.option("toggleClick").call(self, e)) {
							self._active(self);
						}
					}
				},
			});
			
		},
		
		open: function() {
			this._option.show();
			return this;
		},
		
		close: function() {
			this._option.hide();
			return this;
		},
		
		// 옵션창에서 특정 항목을 선택했을때
		_selecting: function(e) {
			this.option("selected").call(this, e);
		},
		
		_btnClick: function(e) {
			if(this.option("url"))
				$.getJSON(this.option("url"), this.option("data"), this.option("success"));
			this.option("btnClick").call(this, e);
		},
		
		
		// 일종의 토글버튼
		// 이미 activeElement에 등록되어 있으면 close되고
		// 처음이면 다른 element를 close하고 자신은 open된다.
		_active: function(element) {
			
			// _toggle(undefinded)
			if(element == null) {
				!activeElement || activeElement.close();
				activeElement = null;
				return;
			}
				
			// First
			if(activeElement == null) {
				activeElement = element.open();
			}
			// 또 다시 반복해서 들어올때는 닫는다.
			else if(activeElement === element) {
				activeElement.close();
				activeElement = null;
			}
			// 다른 버튼이 켜져 있을 경우는 그걸 먼저 close한다.
			else {
				activeElement.close();
				activeElement = element.open();
			}
		},
		
		/*
		 * 옵션을 바꾸면 한번씩 호출된다.
		 */
		_init: function() {
			
		},
	});


})(jQuery);


