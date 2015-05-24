
var fs = require('fs'),
	path = require('path'),
	result,
	globalScss,
	list
	;

function tour( parentPath, currentDir ) {
	
	var stat,
		currentPath = parentPath + (currentDir ? '/' + currentDir : ''), 
		realPath = fs.realpathSync( 'img/' + currentPath );
	
	fs.readdirSync(realPath).forEach(function(file) {
		
		stat = fs.statSync( currentPath + '/' + file );
		
		if( stat.isDirectory() ) {
			tour( currentPath, file );
		} 
		else if(file == 'extend.scss') {
			
			var fileName = currentPath.replace(/\//g, '-');
			
			result.push({
				src: currentPath + '/*.png',
				dest: '../img/icon/' + fileName + ".png",
				destCss: 'img/' + fileName + ".scss",
				imgPath: '/img/icon/' + fileName + ".png",
				//cssTemplate: currentPath + '/template.html',
			});
			
			// sprite파일을 확장하기 위한 리스트
			list.push({
				name: fileName,
				data: fs.readFileSync( path.join(realPath, file) ),
			});
		}
		
		return result;
	});
	
};


function tour( dir ) {
	
	var stat,
		realPath = 'img/' + dir;
	
	fs.readdirSync( fs.realpathSync( realPath ) ).forEach(function(file) {
		
		stat = fs.statSync( path.join( realPath, file ) );
		
		if( stat.isDirectory() ) {
			return tour( dir + '/' + file);
		} 
		else if(file == 'extend.scss') {
			
			var fileName = dir.replace(/\//g, '-');
			
			result.push({
				src: realPath + '/*.png',
				dest: '../img/icon/' + fileName + ".png",
				destCss: 'css/src/img/' + fileName + ".scss",
				imgPath: '/img/icon/' + fileName + ".png",
				//cssTemplate: currentPath + '/template.html',
			});
			
			// sprite파일을 확장하기 위한 리스트
			list.push({
				name: fileName,
				data: fs.readFileSync( path.join(realPath, file) ),
			});
		}
		
		return result;
	});
	
};

exports.options = function( target ) {
	result = [];
	list = [];
	tour('icon');
	return result;
};

// 만들어진 scss파일을 확장한다.
exports.extend = function() {
	var name,
		file,
		groupName,
		group = {},
		globalScss = fs.readFileSync('img/global.scss', 'utf8'); // 모든 scss파일이 기본적으로 포함해야 하는 구문
	
	// 만들어진 sprite.scss 파일을 돌면서 내용을 덧붙인다.
	list.forEach(function(value, index, array) {
		name = value.name;
		file = name + '.scss';
		
		groupName = name.match(/icon\-([^-]+)/)[1];
		// 임포트 구문 생성
		if( !group[groupName] ) group[groupName] = '';
		group[groupName] += '@import "' + name + '";\n';
		
		fs.writeFileSync('css/src/img/' + file,
									fs.readFileSync('css/src/img/' + file, 'utf-8') + (value.data + globalScss).replace(/{{dir}}/g, name),
									'utf-8');
	});
	
	// import구문을 가진 해당.scss를 만든다.
	for(var name in group) {
		fs.writeFileSync('css/src/img/$' + name + '.scss', group[name], 'utf-8');
	}

};
