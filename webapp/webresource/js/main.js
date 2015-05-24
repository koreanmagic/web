

var reqURL = location.pathname,
	urlMatcher = reqURL.match(/\/(.+?\/.+?\/)([^/]+)/),
	url = urlMatcher[1].replace(/customer|subcontractor/, 'partner'),
	model = urlMatcher[2],
	moduleId = 'src/' + url + model;
	
	
	
require.config({
	paths: {
        // 이 설정으로 모듈 이름을 호출하면 값의 위치를 요청한다.
        // ".js"는 자동 추가
        'jquery': 'http://code.jquery.com/jquery-2.1.4.min',
        //'jquery': 'lib/jquery-2.1.4',
        'jquery-ui': 'http://code.jquery.com/ui/1.11.4/jquery-ui.min',
        
        'utils': 'lib/Utils',
        'ui': 'lib/ui',
        'component': 'lib/component',
        'json': 'src/json',
        
        'text': 'https://cdnjs.cloudflare.com/ajax/libs/require-text/2.0.12/text.min',
        'css': 'https://cdnjs.cloudflare.com/ajax/libs/require-css/0.1.5/css.min',
        '#json': 'https://cdnjs.cloudflare.com/ajax/libs/requirejs-plugins/1.0.3/json.min',
    },
    urlArgs : 'ts=' + (new Date()).getTime(),
});



// 1) jquery를 미리 로드한다.
require([
	'jquery',
	'#json!files.json',
],
function($, files) {
	
	if(files.indexOf(moduleId) !== -1) {
		// 1) 각 path에 해당하는 메인을 호출한다.
		require([moduleId], function() {
			$('#LodingBlock').hide();
		});
	} else {
		$('#LodingBlock').hide();
	}
	
	
	//디버그 작업
	$('#topLine').on('click', function(e) {
		$(document.body).toggleClass('outline-all');
	});
	
});




