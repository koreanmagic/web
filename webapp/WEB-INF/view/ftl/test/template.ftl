<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>koreanmagic.co.kr Ver.2</title>
        
        
        <!--  <link rel="stylesheet" href="/css/test.css" type="text/css" media="screen" title="no title" charset="utf-8"/> -->
        <link rel="stylesheet" href="/css/bootstrap.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
   		<!-- [if IE 7]>
   			<link rel="stylesheet" href="/css/font-awesome-ie7.min.css" />
    	<!--[if lt IE 9]>
    	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    	<![endif]-->
    	
    </head>
	
	<body>
 		<div id="header"><@tiles.insertAttribute name="header"/></div>
 		<div id="body"><div><@tiles.insertAttribute name="body"/></div></div>
 		<div id="footer"><@tiles.insertAttribute name="footer"/></div>
    </body>
</html>