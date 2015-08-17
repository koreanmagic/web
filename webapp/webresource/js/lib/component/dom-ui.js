

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
	
	var active,			// 이벤트 주체
		target,			// 팝업 박스
		existsClass,	// 원래 active클래스가 붙어있다면 그냥 둔다.
		closeBtn,		// 버튼으로 조작할지
		isInput,			// active가 input객체인지? (data-value) 입력시 참고.
		block;			// [type='file'] 클릭시 포커스가 날아가버리는 것을 방지하기 위한 플래그

	function close() {
		if(!active) return; // 중복 클릭에 의해 이미 지워졌을 수 있다.
		
		var obj = active;
		target.removeClass('ui-dropdown-target')
				.addClass('ui-helper-hidden')
				.trigger('dropdownoff');
		active = null;
		
		if(!existsClass) obj.removeClass('active');
		
		obj.trigger('dropdownoff' );
		
		return obj;
	};

	function open( currentTarget ) {
		
		close();
		
		if(active && active.is(currentTarget)) return;
		
		var targetWidth;
		
		active = $( currentTarget );
		target = $( active.data('uiDropdown') );
		targetWidth = target.attr('data-ui-extend-width');
		targetWidth = targetWidth == null ? false : ( parseInt(targetWidth) || 0 ); // targetWidth은 NaN일 수 있다.
		closeBtn = target.attr('data-ui-close');
		isInput = /input/i.test(currentTarget.tagName);
		
		
		if(!target.length) throw new Error('[' + active.data('uiDropdown') + ']에 해당하는 객체가 없습니다.');
		
		// 클로즈버튼
		if(closeBtn) {
			$(closeBtn).one('mousedown', function() {
				close();
			});
		}
		
		// 타겟 엘리먼트 가로사이즈 
		if( targetWidth !== false )
			target.css('width', active.width() + targetWidth);
		
		target[0].tabIndex = target[0].tabIndex; 
		
		// active 클래스
		if(active.hasClass('active')) existsClass = true;
		else active.addClass('active');	// 트리거;
		
		active.trigger('dropdownon');
		
		target.addClass('ui-dropdown-target')
				.removeClass('ui-helper-hidden')
				.position ( _pos( active.data('uiOption') || 'lb', { of: active } ) )
				.trigger('dropdownon');
				
		active[0].focus();
	};
	
	function init() {
		
	}
	
	return {
		// disble이 있으면 안쓴다.
		'[data-ui-dropdown]:not(.disable)': {
			
			'mousedown': function(e) {
				// tagindex어트리뷰트를 등록한다. :: tabIndex프로퍼티를 설정하면 자동으로 tabindex어트리뷰트가 생성된다.
				e.currentTarget.tabIndex = e.currentTarget.tabIndex;
			},
			
			
			'focusin': function(e) {
				open(e.currentTarget);
				
				block = true;
			}, // click
			
			// active가 없을때는 그냥 둔다.
			'click': function(e) {
				//e.currentTarget.tabIndex = e.currentTarget.tabIndex;
				if(block) block = false;
				// 현재 
				else active ? close() : open(e.currentTarget);
			},
			
			'ui-dropdown-on': function(e) {
				e.currentTarget.tabIndex = e.currentTarget.tabIndex;
				e.currentTarget.focus();
			},
			
		},
		
		// 현재 실행중인 드랍다운
		'[data-ui-dropdown].active': {
			
			'ui-disable': function(e) {
				close();
			},
		},
		
		'[data-ui-dropdown].active, .ui-dropdown-target': {
			
			// 포커스가 이동되면 드랍다운이 꺼진다.
			// 하지만 몇가지 상황에서는 드랍다운이 유지된다.
			'focusout': function(e) {
				var relateTarget = e.relatedTarget;
				if(closeBtn ||
						(e.target.type == 'file' && document.activeElement == e.target) ||
						$(relateTarget).closest('[data-ui-dropdown].active, .ui-dropdown-target').length) return;
				
				close();
			},
			
		},
		
		'.ui-dropdown-target [data-value]': {
			'mousedown': function(e) {
				
				e.preventDefault();
				
				var name = e.currentTarget.textContent,
					value = e.currentTarget.getAttribute('data-value');
					
				isInput ? active.val(name) : active.text(name);
				active.trigger('ui-dropdown-value', [value || name]);
				close();
			},
		}
	};
}; // exports.dropdown


exports.autoComplete = function($) {
	
	var r = /^[가-힣|\w|\s]+$/, 
			existKeyword, 	// keyup 중복 방지
			uuid = 0,
			inputs = {},
			lists = {},
			container = function(uuid) {
				return '<div class="ui-autocomplete-container drop-box" data-autocomplete-uuid="' 
							+ uuid 
							+ '"><div class="result-list"></div></div>';	
			},
			
			getContainer = function(element) {
				var list;
				
				if( list = lists[ element['__autocomplete__'] ] )
					return list;
					
				element['__autocomplete__'] = uuid;
				
				inputs[uuid] = element;
				
				return lists[uuid] = $( container(uuid++) )
												.appendTo(document.body)
												.css('width', $(element).outerWidth())
												.position({
													of: element,
													my: 'left top',
													at: 'left bottom'
												});
			}
			;
			
	
	
	return {
		'[data-ui-autocomplete]:not(.disable)': {
			
			'keyup': function(e) {
				
				var target = e.currentTarget,
					$target = $(target),
					url = target.getAttribute('data-ui-autocomplete'),
					keyword = target.value,
					container = getContainer(target),
					list = container.find('.result-list'),
					values = '';
				
				// 중복방지
				if( existKeyword == keyword ) return;
				
				existKeyword = keyword;
				
				// input이 비워지면 리스트도 지운다.				
				if( keyword.length === 0 ) {
					container.addClass('ui-helper-hidden');
					return;
				}
				// 완성형 문자가 아니면 리턴
				if(!r.test(keyword)) return;
				
				
				$.getJSON(url + keyword)
				.success(function(json) {
					
					$.each(json, function( name, v ){
						values += '<span data-id="' + v.id + '">' + v.name.replace(keyword, '<b>' + keyword + '</b>') +'</span>';
					});
					
					list.empty().append(values);
					container.removeClass('ui-helper-hidden');
				});
				
			},
		}, // '[data-ui-autocomplete]:not(.disable)'
		
		'.ui-autocomplete-container [data-id]': {
			'click': function(e) {
				var container = $(e.currentTarget).closest('.ui-autocomplete-container'),
					input = inputs[container.data('autocompleteUuid')];
				input.value = e.currentTarget.textContent;
				container.addClass('ui-helper-hidden');
				$(input).trigger('change');
			},
		},
	};
	
	
}; // exports.autoComplete



define([
	'jquery',
	
	'jquery-ui'
], function($){

	var body =$(document.body);
	
	$.each(exports, function( name, handler ){
		
		$.each( /* export 반환객체 */ handler($), function( selector, handlers ) {
			
			$.each(handlers, function( eventType, fn ) {
				body.on(eventType, selector, fn);
			});
		});
	});

});

