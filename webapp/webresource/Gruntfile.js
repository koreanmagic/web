
var options = {
		
		jade: {
			
			compile: {
				options: {
					client: false,
					pretty: true,
				},
				files:[
					{
						cwd: 'jade',
						src: '**/*.jade',
						dest: 'html',
						expand: true,
						ext: '.html',
					}
				],
			},
			
		},
		
        //concat 설정
		concat: {
			sass: {
			}
		},
		
		// SASS
		sass: {
			
			dist: {
				options: {
					//style: 'compressed',
				},
			}
		},
		
		
		scss: {
			dist: {
				files: {
					'css/img/files.css': 'css/img/$files.scss'
				}
			}
		},
		
		sprite:[{
				src: 'img/sprite/files/*.png',
				dest: 'img/spritesheet.png',
				destCss: 'css/img/sprites.css',
				cssTemplate: 'img/sprite/files/template.html'
			}],
		
		watch: {
			sass:  {
				files: ['css/**/*.scss', '!css/temp/**/*'],
				tasks: ['copy:sass', 'sass']
			},
			js: {
				files: ['js/**/*.js'],
				tasks: ['uglify:js', 'list']
			},
			jade: {
				files: ['jade/**/*.jade'],
				tasks: ['jade']
			}
		},
		
		uglify: {
			js: {
				files: [{
					expand: true,
					cwd: 'js',
					src: '**/*.js',
					dest: '../js/'
      		}],
      		options: {
		        sourceMap: true,
		        //sourceMapName: '../js/sourcemap.map'
		      },
			},
		},
		
		copy: {
			sass: {
				files: [
					 {expand: true, cwd: 'css/src/', src: ['**/*', '!**/$*.*', '!require.scss'], dest: 'css/temp/'},
				]
			},
			js: {
				files: [
					 {expand: true, cwd: 'js', src: ['**/*'], dest: '../js'},
				]
			}
		},
		
		clean: {
			sass: {
				src: ['css/temp/']
			},
			js: {
				src: ['../js']
			}
		}
	}
	;
	

module.exports = function(grunt) {
 	
 	options['pkg'] = grunt.file.readJSON('package.json');
 	
 	options.sass.dist['files'] = require('./config/scss').getFiles();
 	
	grunt.initConfig(options);
	 
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-sass');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-jade');
	grunt.loadNpmTasks('grunt-spritesmith');
	
	grunt.registerTask('default', ['img', 'sass', 'uglify:js', 'list', 'watch']);
	
	grunt.registerTask('img', ['checkImgs', 'sprite', 'post']);
	
	grunt.registerTask('scss', ['copy:sass', 'sass']);
	
	// requirejs가 참조할 파일 리스트를 만든다.
	grunt.registerTask('list', function() {
		require('./config/js').getList();
	});
	
	// grunt-sprite의 옵션을 생성한다.
	grunt.registerTask('checkImgs', function() {
		options['sprite'] = require('./config/sprite').options();
	});
	grunt.registerTask('post', function() {
		require('./config/sprite').extend();
	});
	
	
	
};


