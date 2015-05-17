

define([
	'jquery',
], function($){

	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ 오버레이 ▼  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	// overlay 메서드에서 사용할 형제 찾기
	function sibling( ele, prop ) {
		prop = prop ? prop : "nextSibling";
		var ele = ele[prop];
		for(;ele;ele=ele[prop]) {
			if(ele.nodeType === 1)
				return ele;
		}
	};
	
	var overlayUUID = 1,
	
	Popup = (function(){
			
		var body = $(document.body),
			table = $('<table id="KScript-popup"><tbody><tr><td id="KScript-popup-container"></td></tr></tbody></table>')
								.addClass('ui-helper-hidden').appendTo(body),
			td = table.find("td"),
			dummy = $('<div style="display:none;"></div>'),
			parent,
			eleNext,
			oldEle,
			targetElement,
			relateTarget;
			
		table.on("click", function(e) {
			if( e.target.id === "KScript-popup-container" ) {
				Popup( false );	// 직접 호출하면 기존 객체를 돌려받을 수 있다.
			} 
		});
		
		body.on("click", "[data-popup]", function(e) {
			e.preventDefault();
			e.stopPropagation();
			relateTarget = e.currentTarget;
			Popup( relateTarget.getAttribute("data-popup") );
		});
		
		
		return function popup( ele ) {
			if(!ele) {
				table.addClass("ui-helper-hidden");	// 테이블(오버레이)을 가린다.
				
				// 기존에 붙어있던 객체가 없다면 undefined 반환
				if( !targetElement ) return undefined;
				
				dummy.before(targetElement).detach();
				ele = targetElement;
				targetElement = false;
				
				return ele	// 오버레이 객체에서 떼어낸다.
						.trigger("popOff")
						.removeClass("ui-helper-block");
			}
			
			
			oldEle = popup( false );
			
			targetElement = $(ele).before(dummy);
			targetElement.appendTo(td)
						.trigger("popOn", relateTarget)
						.addClass("ui-helper-block");
			
			table.removeClass("ui-helper-hidden");
			
			return oldEle;
		};
		
	})();
	
	return Popup;
	
});


