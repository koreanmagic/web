define(["jquery","component/dom-ui","./list/editor","./list/itemContainer","./context"],function(a){function b(a,b){return'<a href="/resource/'+b.saveName+'">'+a+"</a>"}function c(b,d,e){var f,g,h='<ul data-id="'+d.id+'">',i={},j=c.types;return a.each(d,function(a,c){(e?-1===e.indexOf(a):0)||(g=j[a],f=g?g(b,c,d):c,i[f.index]='<li class="'+a+'" title="'+(f.title||c)+'">'+(f.value||c)+"</li>")}),a.each(i,function(a,b){h+=b}),h+"</ul>"}c.types={orignalName:function(a,c,d){return{title:c,value:b(c,d),index:3}},size:function(a,b,c){return{index:5}},uploadTime:function(a,b,c){return{index:4}},fileType:function(a,c,d){return{title:"img",value:b('<i class="img-icons-file-l '+c+'"></i>',d),index:2}}};var d={eventHandlers:{"click ._work .delete":function(a){var b={success:function(){alert("삭제 완료"),location.reload()},error:function(){alert("삭제 실패\n한컴 관리자에 문의하세요")}};this.getJSON("/admin/work/delete/"+a.item.id,b)},"mouseover ._work .delete":function(a){a.item.find("._work").addClass("delete")},"mouseout ._work .delete":function(a){a.item.find("._work").removeClass("delete")},"click [data-info]":function(b){b.preventDefault();var c=this,d=b.currentTarget,e=d.getAttribute("data-info"),f=d.getAttribute("data-info-key");url="/admin/"+e+"/ajax/get/id/"+f,element=a(".info-"+e),a.ajax(url).success(function(a){c.popInfo(element,a)})},"click [data-editor]":function(a){a.preventDefault(),a.item.editor({openTab:a.currentTarget.getAttribute("data-editor")})},"click ._work-controller":function(b){var c=a(b.currentTarget);b.ctrlKey&&c.toggleClass("editor")},"dropdownon .confirm-upload":function(b){a(b.currentTarget).find("input").val(""),a(b.currentTarget).find("button").addClass("ui-helper-hidden")},"change .confirm-upload > input":function(b){b.currentTarget.files.length?a(b.currentTarget).next().removeClass("ui-helper-hidden"):a(b.currentTarget).next().addClass("ui-helper-hidden")},"click .confirm-upload > button":function(b){b.preventDefault();var c=b.item,d=new FormData;d.append("file",b.currentTarget.previousSibling.files[0]),d.append("work",b.item.id),d.append("serviceType","workConfirmFile"),a.ajax({url:"/admin/work/editor/resource/upload",processData:!1,contentType:!1,data:d,type:"POST"}).success(function(){c.confirmFile()})},"focusout ._work-wrap":"initialize"},initialize:function(a){}};a("._work-container").itemContainer({extendOfItem:d})});
//# sourceMappingURL=list.js.map