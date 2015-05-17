

function setVal( target, value ) {
	target = target.jquery ? target[0] : target;
	/input/i.test(target.tagName) ? target.value = value : target.textContent = value;
};


function r( str, distance, revert ) {
	
	if(str.length == 2) {
		return str[1] == 'c' ?
					r(str[0], distance, revert) + ' ' + r(str[1]) :
					r(str[0]) + ' ' + r(str[1], distance, revert);
	}
	
	// 거리가 있을 경우 반전되는 값에 더해주면 된다.
	switch(str) {
		case 'c' : return 'center';
		case 'r' : return revert ? 'left' + (distance || '') : 'right';
		case 'l' : return revert ? 'right' + (distance || '') : 'left';
		case 'b' : return revert ? 'top' + (distance || '') : 'bottom';
		case 't' : return revert ? 'bottom' + (distance || '') : 'top';
	}
};

function position( e, o ) {
	
	e = e.match(/(.{2})(.*)/);
	
	o.at = r(e[1]);
	o.my = r(e[1], e[2], true);
	return o;
};

define([
	'jquery',
	
	'jquery-ui'
], function($){


	var  tooltipBox = $('<div class="tooltip-box ui-helper-hidden"></div>')
							.appendTo($(document.body)),
	
	
	events = {
		
		// data-value 어트리뷰트가 설정된 엘리먼트들을 보여주고, 클릭하면 이후 행동을 정의한다. 
		'[data-ui-dropdown]': function( $target ) {
			
			var selected,
				showPanel,
				my,
				at,
				posOption = {of: $target},
				immutable = $target.data('immutable') == undefined ? false : true;	// 버튼에 data-immutable만 써넣으면 바뀔 수 있다. 없으면 안됨
			
			// 포지션
			my = $target.attr('tabindex', -1)
			 					.data('uiOption');
			 					
			position(my || 'lb', posOption);
			
			showPanel = $( $target.data('uiDropdown') )
									.addClass('ui-helper-hidden')
									.attr('tabindex', -1)
									
									.focusin(function(e) {
										showPanel.position( posOption );
										$target.addClass('active');
									})
									
									// 상자 바깥을 클릭했을때
									.focusout(function(e) {
										if( $target.is(e.relatedTarget) ||	// 버튼을 다시 클릭할 경우는 버튼 이벤트로 주도권을 넘긴다.
												(!!e.relatedTarget && !!$(e.relatedTarget).closest(showPanel).length) )
											return;
										$target.removeClass('active');
										showPanel.addClass('ui-helper-hidden');
									})
									;
			
			// 버튼 클릭시
			$target.click(function(e) {
				// 현재 클릭중이 아니라면
				if(!$target.hasClass('active')) {
					showPanel.removeClass('ui-helper-hidden').focus();
				}
				else {
					showPanel.blur();
				}
				
			});
			
			
			showPanel.on('click', '[data-value]', function(e) {
				
				if(selected) selected.removeAttribute('selected');
				
				selected = e.target;
				selected.setAttribute('selected', '');
				
				var value = selected.getAttribute('data-value');
				value = value ? value : selected.textContent;
				
				immutable || setVal($target, value);
				
				// 선택시 드롭다운 주체에 selected이벤트를 트리거한다.
				$target.trigger({
					type: 'selected',
					'selected': {
						value: value
					}
				});
				
				showPanel.blur();
				
			});
			
			// INIT selected된 객체가 있는지 검사한다.
			showPanel.find('[data-value][selected]').trigger('click');
			
		}, //--> '[data-ui-dropdown]' 드롭다운 이벤트
		
		
		// 툴팁
		'[data-ui-tooltip]': function( $target ) {
			
			var posOption;
			
			posOption = position( $target.data('uiTooltip'), {of:$target} );
			
			$target
			.on('mouseenter', function(e) {
				
				if($target.hasClass('ui-tooltip-on')) return;
				
				tooltipBox.removeClass('ui-helper-hidden');
				tooltipBox.text( $target.attr('alt') )
							.position( posOption );
				$target.addClass('ui-tooltip-on');
			})
			.on('mouseleave', function(e) {
				tooltipBox.addClass('ui-helper-hidden');
				$target.removeClass('ui-tooltip-on');
			});
			
			
		}, //--> '[data-ui-tooltip]' 드롭다운 이벤트
		
		
	};
	
	
	$.each(events, function( target, fn ){
		
		target = $(target);
		
		if(target.length) {
			
			target.each(function( index, target ) {
				fn.call(target, $(target));
			});
		}
		
	});
	


});

