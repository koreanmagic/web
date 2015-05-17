
var fs = require('fs'),
	path = require('path'),
	
	sassfiles,		// SASS COMPILE INFO :: 컴파일될 사스파일만 골라내서 등록한다.
 	
 	sourceDir = "./css/src",
	targetDir = "./css/temp",
	
	cssName,
	result,
	src,
	
	// 모든 $파일에 include될 문구
	data = fs.readFileSync('css/src/require.scss', 'utf8'),
	targetScss
	;

// $ 접두사가 들어간 파일만 골라내준다.
function fn(targetPath, matchFileRegex, callback ) {
	var stat,
		realPath = fs.realpathSync( path.join(sourceDir, targetPath) );
	
	if(!fs.statSync(realPath).isDirectory()) return;
	
	fs.readdirSync(realPath).forEach(function(file) {
		
		stat = fs.statSync( path.join(realPath, file) );
		
		if( stat.isDirectory() ) {
			fn(path.join(targetPath, file) , matchFileRegex, callback );
		} 
		
		else if( stat.isFile() && matchFileRegex.test(file) ) {
			callback( targetPath, file );	
		}
		
	});
};


function callback( parentPath, file ) {
	
	// $.scss --> 폴더명.scss  // $파일명 --> 파일명.scss 
	cssName = file.match(/^\$(.*)\./)[1] || parentPath.match(/\w+$/)[0],
 	result = '../css/' + parentPath + "/" + cssName + '.css';							// 변환된 css파일은 곧장 webapp 폴더로 옮긴다.
 	
 	src = path.join(parentPath, file);	// 파일의 상대경로
	
	targetScss = fs.readFileSync( path.join(sourceDir, src), 'utf-8' );
	if (!fs.existsSync( path.join(targetDir, parentPath) )) fs.mkdirSync(path.join(targetDir, parentPath));
	fs.writeFileSync( path.join(targetDir, src), targetScss.replace(/@charset "UTF-8";/, "@charset \"UTF-8\";\n" + data), 'utf-8' );

	sassfiles[result] = path.join(targetDir, src);
	
};


exports.getFiles = function( target ) {
	
	sassfiles = {};
 	concatFiles = {};
 	
	fn(target || "", /^\$\w*.scss$/, callback);
	
	return sassfiles;
};