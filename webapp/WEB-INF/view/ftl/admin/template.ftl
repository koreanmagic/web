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
	
	<body class="admin">
	
	<div id="top">
		<div id="top-line">
			<@tiles.insertAttribute name="top"/>
		</div>
		
		<div id="context-line">
			<@tiles.insertAttribute name="context"/>
		</div>
	</div>
	
	<div id="left"><div id="left-container">
		<@tiles.insertAttribute name="left"/>
	</div></div>
 	
 	<div id="body">
 		<div id="body-container">
 			<@tiles.insertAttribute name="body"/>
 		</div>
 	</div>
 		
    </body>
</html>