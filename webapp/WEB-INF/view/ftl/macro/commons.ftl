
<!--
	excute : boolean 값
	trueResult : true시 반환값. 없으면 빈문자
	falseReturn : false시 반환값. 없으면 빈문자
 -->
<#macro value execute o x><#compress>
	<#if execute>
		o
	<#else>
		x
	</#if>
	
	
</#compress></#macro>
