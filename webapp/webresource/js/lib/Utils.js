

define([
	'jquery'
], function($){



	var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	
	Date.prototype.format = function(f) {
	    if (!this.valueOf()) return " ";
	 
	    var d = this;
	     
	    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
	        switch ($1) {
	            case "yyyy": return d.getFullYear();
	            case "yy": return (d.getFullYear() % 1000).zf(2);
	            case "MM": return (d.getMonth() + 1).zf(2);
	            case "dd": return d.getDate().zf(2);
	            case "E": return weekName[d.getDay()];
	            case "HH": return d.getHours().zf(2);
	            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
	            case "mm": return d.getMinutes().zf(2);
	            case "ss": return d.getSeconds().zf(2);
	            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
	            default: return $1;
	        }
	    });
	};
	String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};
	
	
	
		
	var Utils = {},
	
		convertTypes = {
		"tel": function( item ) {
				var v = item.num1 + "-" + item.num2 + "-" + item.num3;
				return v.length > 2 ? v : "";
			},
		"fax": "tel",
		"mobile": "tel",
		"bizNum": "tel",
		"email": function( item ) {
			var v = item.id + "@" + item.host;
			return v.length > 1 ? v : "";
		},
		"bankName": function( item ) {
			return item.name;
		},
		"web": function( item ) {
			return item.url;
		},
		"webhard": function( item ) {
			return item.userId || " / " + item.password || "" ;
		},
		"insertTime": function( item ) {
			return new Date(item).format("yyyy-MM-dd HH:mm");
		},
		"updateTime": "insertTime",
		"price": function( item ) {
			return item.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
		},
		"cost": "price",
		"workType": function( item ) {
			return item.value;
		},
		"delivery": "workType",
		"memo": function( item ) {
			return item.replace(/\n/g, '<BR />');
		},
		
	};
	
	
	Utils = {
		
		access: function(key, value, force) {
			var i, keys, data, key, length, getter = (value === undefined);
		
			// Key 1개
			if(key.indexOf(".") === -1)
				return getter ? $.data(this, key) : $.data(this, key, value) && value;
			
			keys = key.split(".");
			data = $.data(this, key = keys.shift());
			
			if(data === undefined) {
				if(getter) return undefined;
				else if(force) $.data(this, key, data = {});
				else throw new Error("(access SET Error) '" + key + "'로 저장된 데이터가 없습니다.");
			}
			
			key = keys.pop();
			length = keys.length;
			
			// key 3개 이상
			if(length) {
				for(i=0; i<length; i++) {
					if(data[keys[i]] === undefined) {
						if(getter) return undefined;
						else if(force) data[keys[i]] = {};
						else throw new Error("(access SET Error) '" + keys.splice(0, i + 1).join(".") + "'로 저장된 데이터가 없습니다.");
					}
					data = data[keys[i]];
				}
			};
			return getter ? data[key] : (data[key] = value) && value;
		},
		
		// 중첩오브젝트로 이루어진 json데이터를 name.name으로 읽어준다.
		jsonParser: function jsonParser( data, fn, prefix ) {
		
			prefix = prefix || "";
			
			$.each(data, function( key, value ) {
				key = prefix ? prefix + "." + key : key;
				if($.isPlainObject(value))
					jsonParser( value, fn, key );
				else fn.call(this, key, value);
			});
		},
		
		jsonToDataMap: function(data) {
			var result = {};
			Utils.jsonParser.call(this, data, function( k,v ) { result[k] = v; });
			return result;
		},
		
		// obj Value를 받아서 지정된 타입의 문자열로 바꿔준다.
		dataConvert: function( options ) {
			if( options === null ) return $.extend( {}, convertTypes ); 
			
			var types = options ? $.extend( {}, convertTypes, options ) : convertTypes,
				handler, value;
			return function( item, type ) {
				// item은 객체이거나 리터럴값
				value = item; //$.isPlainObject(item) ? item[type] : item;
				if( !value ) return "";
				if( handler = types[type] ) {
					value = (typeof handler === 'string') ? types[handler].call(null, value) : handler.call(null, value);
				}
				return value;
			};
		},
		
		// 객체구조를 복제하면서, 값만 변경한다.
		mapCopy: function copy(values, fn) {
			var self = this,
				f = function re( obj, name, vs ) {
					 
			if($.isPlainObject(vs)) {
				obj = name ? obj[name] : obj;
				$.each(vs, function(n, v) {
					obj[n] = {};
					re(obj, n, v);
				});
			} else obj[name] = fn.call(self, vs);	// 값 변경
				return obj;
			};
			// 객체가 들어왔을때만 동작한다.
			return $.isPlainObject(values) ? f({}, null, values) : null;
		},
		
		// 값이 나타날때까지 객체를 순회하면서, 해당 깊이까지의 네임 배열을 전달한다.
		eachName: function f( values, fn ) {
			var self = this,
				f = function re( array, name, vs ) {
					name && array.push(name);
				
					if($.isPlainObject(vs)) {
						$.each(vs, function(n, v) {
							re(array.slice(), n, v);
						});
					} else fn.call(self, array, vs);
				};
			
			f([], null, values);
		},
		
		// create(values, "prop1.prop2", "v") ==> { prop1: { prop2: "v" } }
		// fn은 중복시 처리 핸들러
		createObjByName: function create(src, name, value, fn) {
			var length, obj = src, prop, temp;
			
			name = name.split(".");
			length = name.length;
			
			while(--length) {
				prop = name.shift();
				obj = obj[prop] = $.isPlainObject(obj[prop]) ? obj[prop] : {};
			}
			temp = name.shift();
			obj[temp] = obj[temp] === undefined ? value :
						fn ? fn.call(this, obj[temp]) : value;
		},
		
		// valueMap인 lead를 순회하면서, target에도 같은 값이 있다면 fn를 호출해준다.
		withRoop: function tour( lead, target, fn ) {
			var self = this;
			$.each(lead, function( name, value ) {
				// 객체면 재귀함수
				if( $.isPlainObject(value) ) {
					if(fn.call(self, name, value, target[name]))
						tour.call(self, value, target[name], fn);
				}
				// 1) 프로퍼티 이름  2) lead객체의 값  3) target객체의 값 
				else fn.call(self, name, value, target[name]);
			});
		},
		
	};
	
	return Utils;

});

