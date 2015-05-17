define(["jquery","component/FormValidate","src/common/listTable","ui/simple-tabs"],function(a,b,c){function d(b,c,d){var e=this;d=this.options=a.extend({},f,d),this.name=b,this.element=c.find(".choice-list"),this.selected=c.find(".selected"),this.current=c.find(".current"),this.confirm=c.find(".confirm").on("click",function(a){a.preventDefault(),d.confirmHandler.call(e,c,e.getSelected())}),this.reset=c.find(".reset"),d.enableReset?this.reset.on("click",function(d){d.preventDefault(),a.ajax(h+b+"_id/null").success(function(){alert("취소완료"),c.init()})}):this.reset.addClass("ui-helper-hidden")}function e(b,e,f){var i,j=this,k=new d(b,e,f.selectOptions);return f=a.extend({},g,f),i=c(b,f.proxy,{selectHandler:function(a){k.setSelected(a,f.displayHandler.call(j.setSelected,a))},cancleHandler:function(a){k.clear()},disableHandler:function(){k.disable()},enableHandler:function(){k.enable()},fetchAfterHandler:function(){var b=this;a.ajax(f.fetchURL).success(function(a){var c=i.searchItem({id:f.getId(a)});k.setCurrent(a,c&&f.displayHandler.call(b.setCurrent,c.item)),f.afterHandler.call(b,a)})},removeHandler:function(c,d){var f=!0;return k.isAleady(c)&&(f=confirm("현재 선택된 정보입니다.\n삭제하면 선택도 취소됩니다.\n 삭제하시겠습니까?"),a.ajax(h+b+"_id/null"),f={done:function(){e.init()}}),f},modify:{afterHandler:function(){e.init()}}})}var f={enableReset:!0,confirmHandler:function(b,c){a.ajax(h+this.name+"_id/"+c.id).success(function(){alert("변경 완료!"),b.init()})},equals:function(a,b){return a==b.id}},g={getId:function(a){return a.id},afterHandler:a.noop};d.prototype={getCurrent:function(){return this.current.data("#value")},setCurrent:function(a,b){this.options.enableReset&&(b?this.reset.removeClass("ui-helper-hidden"):this.reset.addClass("ui-helper-hidden")),this.current.text(b||"선택 없음").data("#value",a||null),this.clear()},setSelected:function(a,b){return this.isAleady(a)?(this.clear(),this):(this.selected.text(b).data("#value",a),this.ready(!0),this)},getSelected:function(){return this.selected.data("#value")},isAleady:function(a){return this.options.equals(this.getCurrent(),a)},clear:function(){this.selected.text("").data("#value",null),this.ready()},ready:function(a){return a=!!a,this._ready!==a&&(a?this.confirm.removeClass("ui-helper-hidden"):this.confirm.addClass("ui-helper-hidden")),this._ready=a,this},disable:function(){this.element.addClass("ui-helper-hidden")},enable:function(){this.element.removeClass("ui-helper-hidden")}};{var h="/admin/work/editor/",i={tabsHandlers:{0:{create:function(c){var d={beforeCommit:function(b){var c={url:h+"data/",type:"POST",data:this.serialize()};return a.ajax(c).success(function(a){alert("저장 완료")}).fail(function(){}),!1},modifyTrue:function(){},modifyFalse:function(){},rules:{"#cost, #price":{valid:function(a){return 0===a.length||/\d+/.test(a)?!0:"공란으로 두거나, 숫자만 써야합니다."}}},globalRules:{required:["#item","#itemDetail","#count","#size"]}};c.validate=b(c.find("#modifyForm")[0],d)},init:function(b){a.getJSON(h+"data/").success(function(a){b.validate.initValue(a)})},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{}},1:{create:function(b){var c={proxy:a.proxy(function(){return this.getCustomerId()},this),displayHandler:function(a){return a.name+" "+a.position},fetchURL:h+"values?keys=manager.id%20id"};b.table=e("manager",b,c)},init:function(a){a.table.fetch()},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{".change":{click:function(a){}}}},2:{create:function(a){var b={enableReset:!1},c=a.selectTable=new d("subcontractor",a,b),e={selectHandler:function(a){c.setSelected(a,a.name)},selectLen:1,removable:!1,selectElement:!1};a.scrollTable=a.find(".subcontractor-table").scrollTable(e).scrollTable("instance")},init:function(b){var c=b.selectTable,d=b.scrollTable;a.ajax("/admin/subcontractor/list/all").success(function(a){b.scrollTable.clear(),b.scrollTable.addList(a)}).success(function(){a.ajax(h+"/values?keys=subcontractor.id%20id").success(function(a){var b=a.id;b?(value=d.searchItem({id:b}).item,value||new Error("반드시 해당 데이터가 리스트에 존재해야합니다."),c.setCurrent(value,value.name)):c.setCurrent()})})},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{}},3:{create:function(b){var c,d=!1,e={selectElement:!1},f={removeHandler:function(b,c){var e=this,f=d;return f?d=!1:a.ajax(h+"resource/delete/"+b.id).success(function(){d=!0,e.remove(c),alert(b.originalName+" 삭제")}),f}},g={changeHandler:function(){this.size()?a(".btn-upload").removeClass("ui-helper-hidden"):a(".btn-upload").addClass("ui-helper-hidden")}},i={"#file":{change:function(a){this.addList(a.target.files)}},".btn-upload":{click:function(d){var e=c.getItems(),f=new FormData;a.each(e,function(a,b){f.append("file",b)}),a.ajax({url:h+"resource/upload",processData:!1,contentType:!1,data:f,type:"POST",success:function(a){b.init()}})}}};b.list=b.find(".resource-table").scrollTable(a.extend(f,e)).scrollTable("instance"),c=b.upload=b.find(".resource-upload-table").scrollTable(a.extend(g,e)).scrollTable("instance"),a.each(i,function(a,d){c._on(b.find(a),d)})},init:function(b){a(".btn-upload").addClass("ui-helper-hidden"),b.upload.clear(),b.list.clear(),a.getJSON(h+"resource/list").success(function(a){b.list.addList(a)})},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{}},4:{create:function(b){var c=b.changeBtn=b.find(".change-btn"),d=b.radioBtns=b.find("[name='delivery']"),f={proxy:a.proxy(function(){return this.getCustomerId()},this),displayHandler:function(a){return"["+d.filter(":checked").next().text()+"] "+a.text},selectOptions:{confirmHandler:function(b,c){var e=a.param({addressId:c.id,deliveryId:d.filter(":checked").val()});a.post(h+"delivery/",e).success(function(){b.init()}).fail(function(a){console.log(a.responseText)})},equals:function(a,b){return a.delivery.ordinal==d.filter(":checked").val()&&a.address==b.id},enableReset:!1},fetchURL:h+"values?keys=delivery%20delivery&keys=address.id%20address",getId:function(c){var e=c.delivery.ordinal;return b.currentDelivery=c.delivery,a.each(d,function(b,c){c.value==e&&(c.checked=!0,a(c).trigger("change"))}),c.address}};b.table=e("address",b,f),b.check=function(a){"0"!=a?(b.table.enable().refresh(),c.addClass("ui-helper-hidden")):(b.table.disable(),0!=b.currentDelivery.ordinal&&c.removeClass("ui-helper-hidden"))}},init:function(a){a.table.fetch()},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{"[name='delivery']":{change:function(a){this.getData().check(a.target.value)}},".change-btn":{click:function(b){b.preventDefault();var c=this.getData(),d=(this.getHandler(),a.param({addressId:"null",deliveryId:c.radioBtns.filter(":checked").val()}));a.post(h+"delivery/",d).success(function(){c.init()})}}}}}},j=a.extend({},widgetPrototype={version:"1.1",options:i},{_create:function(){this._super()},_createClass:function(a){return this.widgetFullName+"-"+a},_init:function(){this._super()},find:function(a){return this.element.find(a)},getItem:function(){return this.options.item},getWorkId:function(){return this.getItem().id},getCustomerId:function(){return this.getItem().customer},enable:function(){},disable:function(){this._super(),this.element.addClass("ui-helper-hidden")}},{});a.widget("ui.editorTabs",a.ui.simpleTabs,j)}});
//# sourceMappingURL=editor.js.map