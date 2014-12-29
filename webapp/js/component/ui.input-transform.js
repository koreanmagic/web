
/*
 * 인풋 인터페이스를 바꿔주는
 */
(function($) {


	function matcher( self ) {
	
		var targets = []; 
		// this 자체가 input변환 엘리먼트가 아니라면 그 하위 엘리먼트들에서 찾아본다.
		if( !self.data("transform") ) {
			targets = $.map(self.find("[data-transform]"), function(v) {
				return $(v);
			});
		} else
			targets.push(self);
			
		return targets;
	};
	
	
	

	$.fn.extend({
		
		/*
		 * 사용법은 간단한다.
		 * jQuery 객체에 replaceInput()를 호출하면 ① 객체 자신이나, ② 그 하위 엘리먼트 중에서 
		 * data-transform 어트리뷰트를 가진 엘리먼트의 text값을 취해 input 엘리먼트로 변환한다.
		 * text값을 제외한 다른 하위엘리먼트는 모두 detach 된다. 
		 * 
		 * restoreInput()를 호출하면 본래값을 찾을 수 있다.
		 * 
		 */
		replaceInput: function (  ) {

			this.each(function() {
				
				var self = $(this), text, inputName,
					i=0, length, targets = matcher( self );
				
				if( !(length = targets.length) ) return;
				
				
				/*
				 * 찾아낸 엘리먼트들로 변환 작업을 실시한다.
				 */
				for(; i<length; i++) {
				
					self = targets[i];
					
					// 이미 변환이 적용됐다면 넘긴다.
					if(self.data($.expando+"_transform"))
						continue;
					
					// 나중에 복구할걸 대비해서
					self.data($.expando+"_transform", self.clone(true));
					
					// 하위 자식들 모두 삭제. 실제 텍스트만 뽑아내기 위한 것
					if( self.children() )
						self.children().remove();
					
					text = $.trim(self.text());
					inputName = self.attr("id");
					
					self.empty().append(
						$("<input>", {
							type: self.data("transform"),
							value: text,
							name: inputName ? inputName : self.data("name"),
						})
					);
				}
				
			});
			
			return this;
			
		}, // -- replaceForm
		
		
		/*
		 * replaceInput된 엘리먼트를 복구한다.
		 * updateValues는 객체이며,
		 * input로 변환된 엘리먼트의 id나s data-name값이 일치하면 해당 value를 텍스트값으로 대치한다.
		 * 인자를 제공하지 않으면, 본래 저장되어있던 값을 복구한다.
		 */
		restoreInput: function( updateValues ) {
			
			var self, restoreData, targets, name;
				
			this.each(function() {
				
				targets = matcher( $(this) );
				
				for( var i=0, l=targets.length; i<l; i++ ) {
					self = targets[i];
					
					name = self.attr("id") || self.data("name");
					
					// updateValues에 해당값이 있으면?
					
					restoreData = self.data($.expando+"_transform");	// 복구데이터
					self.replaceWith( restoreData );
					self.removeData($.expando+"_transform");
					
					// 업데이트 값이 있으면, text값을 업데이트 해준다.
					if( updateValues && name in updateValues)
						restoreData.text( updateValues[name] );
					
				}
			});
		}, // -- restoreInput
	});
	
})(jQuery);

