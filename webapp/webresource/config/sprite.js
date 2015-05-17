
var fs = require('fs'),
	path = require('path'),
	result,
	globalScss,
	list
	;

function tour( parentPath, currentDir, result ) {
	
	var stat,
		currentPath = parentPath + (currentDir ? '/' + currentDir : ''), 
		realPath = fs.realpathSync( currentPath );
	
	fs.readdirSync(realPath).forEach(function(file) {
		
		stat = fs.statSync( currentPath + '/' + file );
		
		if( stat.isDirectory() ) {
			tour( currentPath, file, result );
		} 
		else if(file == 'extend.scss') {
			result.push({
				src: currentPath + '/*.png',
				dest: '../img/sprite/' + currentDir + ".png",
				destCss: 'img/' + currentDir + ".scss",
				imgPath: '/img/sprite/' + currentDir + ".png",
				//cssTemplate: currentPath + '/template.html',
			});
			
			// sprite파일을 확장하기 위한 리스트
			list.push({
				name: currentDir,
				data: fs.readFileSync( path.join(realPath, file) ),
			});
		}
		
		return result;
	});
	
};


exports.options = function( target ) {
	result = [];
	list = [];
	tour('img', '', result);
	return result;
};

// 만들어진 scss파일을 확장한다.
exports.extend = function() {
	var name,
		file;
	globalScss = fs.readFileSync('img/global.scss', 'utf8');
	
	list.forEach(function(value, index, array) {
		name = value.name;
		file = name + '.scss';
		
		fs.writeFileSync('css/src/img/$' + file,
									fs.readFileSync('img/' + file, 'utf-8') + value.data + globalScss.replace(/{{dir}}/, name),
									'utf-8');
	});
		

};
