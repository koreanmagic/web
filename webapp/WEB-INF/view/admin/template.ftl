<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<#assign resourceName = requestInfo.command />
<#if resourceName == 'customer' || resourceName == 'subcontractor'>
	<#assign resourceName = 'partner' />
</#if> 
<#assign temp = .now?time?string("yyyyMMddHHmmss") />
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>koreanmagic.co.kr Ver.2</title>
        
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
        
        <link rel="stylesheet" href="/css/base.css?ts=${temp}" type="text/css" media="screen" title="no title" charset="utf-8"/>
        <link rel="stylesheet" href="/css/admin/default.css?ts=${temp}" type="text/css" media="screen" title="no title" charset="utf-8"/>
        <link rel="stylesheet" href="/css/admin/${resourceName}/${requestInfo.view}.css?ts=${temp}" type="text/css" media="screen" title="no title" charset="utf-8"/>
        
        
   		<!-- [if IE 7]>
   			<link rel="stylesheet" href="/css/font-awesome-ie7.min.css" />
    	<!--[if lt IE 9]>
    	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    	<![endif]-->
    	
    </head>
	
	<body>




<table id="LodingBlock" style="position: fixed; z-index: 100; height: 100%; text-align: center; background:white"><tbody><tr><td><img src="/img/page-loading.gif" /></td></tr></tbody></table>

<div id="header">
		
	<!-- 최상위 컨트롤러 :: 전 사이트에 걸쳐 필요한 전역 설정 -->
	<div id="topLine"><div class="content">
		<@tiles.insertAttribute name="top"/>
	</div></div>
	
	<!-- 각 페이지별로 변하는 컨텍스트 메뉴 -->
	<div id="contextLine">
		<div class="content">
			<div class="fluid-line">
	
				<!-- 검색바 -->
				<div class="left">
				<form name="search" id="search" action="/admin/${requestInfo.command}/search" method="GET">
					
						<div class="command-img _img-static-${requestInfo.command}"></div>
						
						<div id="SearchBar">
						
							<button class="select-btn" data-ui-dropdown=".search-options" onClick="return false;">선택</button>
							<ul class="drop-box search-options ui-helper-hidden">
								<li data-value>아이템</li>
								<li data-value>디자인</li>
								<li><input ></li>
							</ul>
							
							<button class="submit" onClick="return false;"><i class="fa fa-search"></i></button>
							
							<div class="input">
								<input type="hidden" name="type">
								<input id="searchInput" type="text" name="search" autocomplete="off">
							</div>
						</div>
						
					</form>
				</div> <!-- left -->
					
					
				<div class="right">
					<!-- 각 페이지별 컨텍스트 -->
					<@tiles.insertAttribute name="context"/>
				</div> <!-- right -->
				
			</div> <!-- fluid-line -->
		</div> <!-- content -->
	</div> <!-- #contextLine -->
</div> <!-- header -->
 	
 <div id="body">
 	
	<div id="bodyContainer">
		<div id="bodyContent">
			<@tiles.insertAttribute name="body"/>
		</div>
	</div>
</div> <!-- #body -->
 	
 	
<div id="footer">
	<div id="footerContent">
	</div>
</div> <!-- #footer -->
<script data-main="/js/main" src="http://requirejs.org/docs/release/2.1.17/comments/require.js"></script>
</body>

</html>