<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>koreanmagic.co.kr Ver.2</title>
        
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
        <link rel="stylesheet" href="/css/admin/admin-layout.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
        <link rel="stylesheet" href="/css/admin/work/<@tiles.insertAttribute name="css"/>.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
        
        <!-- jQuery Style Sheet -->
        
   		<!-- [if IE 7]>
   			<link rel="stylesheet" href="/css/font-awesome-ie7.min.css" />
    	<!--[if lt IE 9]>
    	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    	<![endif]-->
    	
    	<!-- jQuery JavaScript Source -->
    	<!-- <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>  -->
    	<script src="/js/component/jquery-1.11.1.js"></script>
    	<script src="/js/component/jquery-ui.js"></script>
    	<!-- <script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>  -->
    </head>
	
	<body>
	
	<div id="header">
		
		<!-- 최상위 컨트롤러 :: 전 사이트에 걸쳐 필요한 전역 설정 -->
		<div id="topLine"><div class="content">
			<@tiles.insertAttribute name="top"/>
		</div></div>
		
		<!-- 각 페이지별로 변하는 컨텍스트 메뉴 -->
		<div id="contextLine"><div class="content">
			<@tiles.insertAttribute name="context"/>
		</div></div>
	</div>
 	
 	<div id="body">
 		<div id="bodyContainer">
 			
 			<div id="bodyContent">
 				<@tiles.insertAttribute name="body"/>
 			</div>
 			
 		</div>
 	</div>
 		
    </body>
</html>