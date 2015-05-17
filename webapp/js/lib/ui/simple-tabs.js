define(["jquery","jquery-ui"],function(a){{var b={openTab:0},c={create:function(a){},init:function(a){},open:function(a,b){},close:function(a,b){},disable:function(a){},_events:{}},d=a.extend({},{version:"1.1",options:b,_extend:a.noop,_create:function(){var b,d,e,f=this,g=this.options.tabsHandlers,h=this.tabs=[],i=h.container=this.element.find("> ul").addClass("ui-simpleTabs-container");a.each(i.find("> li"),function(i,j){var k,l;b={},b.tab=d=a(j),e=d.find("> a").addClass("ui-simpleTabs-anchor"),panel=b.panel=a(e.attr("href")).addClass("ui-helper-hidden"),e.attr("tabs-id",h.push(b)-1),k=b.handlers=a.extend({},c,g[i]),l=b.globalData={handlers:k,find:function(a){return panel.find(a)},init:function(){k.init.call(f,l)}},k.create.call(f,b.globalData),a.each(k._events,function(a,b){f._on(panel.find(a),b)})}),this._on({"click .ui-simpleTabs-anchor":function(a){a.preventDefault(),this.open(a.currentTarget.getAttribute("tabs-id"))}})},_init:function(){options=this.options,a.each(this.tabs,function(b,c){c.access=0,c.data=a.extend({},c.globalData)}),this.open(options.openTab)},disable:function(){var b=this;a.each(this.tabs,function(a,c){c.handlers.disable.call(b,c.data)})},_destroy:function(){},_reflesh:function(){}},{access:function(b,c){var d;return void 0===c?d=this._access(b):a.data(this,b,c),d},_access:function(b){var c,d,e=0;for(b=b.split("."),c=a.data(this,b.shift()),d=b.length;d>e;e++)c=c[b[e]];return c},size:function(){return this.tabs.length},open:function(a){if(void 0!==a){var b=this.getTab(a),c=b.tab,d=b.panel,e=b.handlers,f=b.data;if(!c.hasClass("ui-simpleTabs-disable"))return this.close(this.activeTab),b.access++||e.init.call(this,f),e.open.call(this,f,b.access),this.activeTab=a,c.addClass("ui-simpleTabs-active"),d.removeClass("ui-helper-hidden"),this}},close:function(a){if(void 0!==a){a=this.getTab(a);var b=a.tab,c=a.panel,d=a.handlers,e=a.data;return d.close.call(this,e,a.access),b.removeClass("ui-simpleTabs-active"),c.addClass("ui-helper-hidden"),this}},disableTabs:function(b){a.each(this.tabs,function(a,c){null==b||-1===b.indexOf(a)?c.removeClass("ui-simpleTabs-disable"):c.addClass("ui-simpleTabs-disable")})},enableTabs:function(b){return void 0!==b&&b.length?a.each(b,function(a,b){this.tabs[b].removeClass("ui-simpleTabs-disable")}):a.each(this.tabs,function(a,b){b.removeClass("ui-simpleTabs-disable")}),this},getHandler:function(){return this.getTab(this.activeTab).handlers},getData:function(){return this.getTab(this.activeTab).data},getTab:function(a){return this.tabs[a>this.size()?this.size():a]},getHandlerData:function(a){return this.getTab(a).data}},{});a.widget("ui.simpleTabs",d)}});
//# sourceMappingURL=simple-tabs.js.map