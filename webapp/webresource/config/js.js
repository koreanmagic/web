
var fs = require('fs'),
	path = require('path'),
	
	rootPath = 'js',
	stat,
	result
	;
	

function tour( target/* 디렉터리명 */, array ) {
	
	var realPath = fs.realpathSync( path.join(rootPath, target) );
	
	fs.readdirSync(realPath).forEach(function(file) {
	
		stat = fs.statSync( path.join(realPath, file) );
		
		if( stat.isDirectory() ) {
			tour( path.join(target, file),  array );
		} 
		else if( stat.isFile() && /\.js$/.test(file) ) {
			result = path.join(target, file.substring(0, file.length - 3 ));
			array.push( result.replace(/\\/g, '/') );
		}
	});
	
	return array;

};


exports.getList = function() {
	var result = tour('src', [] );
	fs.writeFileSync( '../js/files.json', JSON.stringify(result) );
	return result;
};


