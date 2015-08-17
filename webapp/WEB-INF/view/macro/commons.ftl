<#macro import src>
	<#if src?starts_with("/")>
		<#local v = "/WEB-INF/view/admin/#include" + src />
		<#include v />
	<#else>
		<#local v ="#include/" + src />
		<#include v />
	</#if>
</#macro>