

var uuid = new Date(),
	exports = {};

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

function _pos( e, o ) {
	
	e = e.match(/(.{2})(.*)/);
	
	o.at = r(e[1]);
	o.my = r(e[1], e[2], true);
	return o;
};


exports.tooltip = function($) {
	
	var body = $(document.body),
		tooltipBox = $('<div class="tooltip-box ui-helper-hidden"></div>')	// 툴팁박스는 공유된다.
							.appendTo($(document.body));
	
	return {
		'[data-ui-tooltip]':  {
			
			'mouseenter': function( e ) {
				var target = $(this);
				if(target.hasClass('ui-tooltip-on')) return;
							
				tooltipBox.removeClass('ui-helper-hidden');
				tooltipBox.text( target.attr('alt') )
								.position( _pos( target.data('uiTooltip'), {of:target} ) );
				target.addClass('ui-tooltip-on');
			},
			'mouseleave': function( e ) {
				var target = $(this);
				if(!target.hasClass('ui-tooltip-on')) return;
			
				tooltipBox.addClass('ui-helper-hidden');
				target.removeClass('ui-tooltip-on');
			},
		}, // 툴팁
	};
};


exports.dropdown = function($) {
	
	var active,
		isInput,
		block;	// [type='file'] 클릭시 포커스가 날아가버리는 것을 방지하기 위한 플래그

	function close() {
		
		if(!active) return; // 중복 클릭에 의해 이미 지워졌을 수 있다.
		
		var target = $( active.data('uiDropdown') );
		active.removeClass('active');
		target.removeClass('ui-dropdown-target')
				.addClass('ui-helper-hidden')
				.trigger('dropdownoff');	// 트리거
		active = null;
	};

	function open( ) {
		var target = $( active.data('uiDropdown') );
		
		active.addClass('active');
		target.addClass('ui-dropdown-target')
				.removeClass('ui-helper-hidden')
				.position ( _pos( active.data('uiOption') || 'lb', { of: active } ) )
				.trigger('dropdownon');	// 트리거
	};
	
	return {
		'[data-ui-dropdown]': {
			
			'mousedown': function(e) {
				// tagindex어트리뷰트를 등록한다. :: tabIndex프로퍼티를 설정하면 자동으로 tabindex어트리뷰트가 생성된다.
				e.currentTarget.tabIndex = e.currentTarget.tabIndex;
			},
			
			'click': function(e) {
				if(!active) {
					active = $(e.currentTarget);
					isInput = /input/i.test(e.currentTarget.tagName);
					open();
				} else {
					close();
				}
				//e.currentTarget.focus();
			}, // click
			
		},
		
		'[data-ui-dropdown].active, .ui-dropdown-target': {
			'focusout': function(e) {
				if(block) return block = false;
				if($(e.relatedTarget).is(active)) return;
				if(!$(e.relatedTarget).closest('.ui-dropdown-target').length) {
					close();
				}
			},
		},
		
		// 파일 인풋을 클릭시 포커스가 날아가버린다. 이때도 여전히 드랍다운이 유효함을 알려주기 위해 플래그를 설정한다.
		".ui-dropdown-target [type='file']": {
			'click': function(e) {
				block = true;
			}
		},
		
		'.ui-dropdown-target [data-value]': {
			'mousedown': function(e) {
				var value = e.currentTarget.getAttribute('data-value') || e.currentTarget.textContent;
				isInput ? active.val(value) : active.text(value);
				close();
			}
		}
	};
}; // exports.dropdown


define([
	'jquery',
	
	'jquery-ui'
], function($){

	var body =$(document.body);
	
	$.each(exports, function( name, handler ){
		
		$.each( handler($), function( selector, handlers ) {
			
			$.each(handlers, function( eventType, fn ) {
				body.on(eventType, selector, fn);
			});
		});
	});

});

