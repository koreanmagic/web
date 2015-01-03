

function Ko() {
	
};

Ko.prototype = {
	
	// (obj, "prop.prop2.prop3") 을 입력하면 obj에서 prop3값을 돌려준다. 
	getter: function( obj, propArray ) {
		propArray = Array.isArray(propArray) ? propArray : propArray.split('.');
 
		var i=0, l = propArray.length;
		for(;i<l;i++) {
			obj = obj[ propArray[i] ];
		}
		return obj;
	},
	
	// 오브젝트 배열을 오브젝트의 특정 property 값을 기준으로 정렬한다.
	objectSort: function ( objArray, compareKey ) {
		var objArray = Array.isArray(objArray) ? objArray : [objArray],		// 배열로 만든다 
			i=0, len = objArray.length,
			sorts = new Array(len),
			keys = compareKey.split("."),
			rresult = /\s(\d+)$/;
		
		for(;i<len;i++) {
			sorts[i] = getter(objArray[i], keys) + " " + i;
		}
		
		sorts.sort();	// 정렬
		
		while(i--)
			sorts[i] = objArray[ rresult.exec( sorts[i] )[1] ];
		
		return sorts;
	},
	
};


