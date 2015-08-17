
<#assign listType = RequestParameters.listType?default("card") />

<#-- 링크 -->
<#macro url page><#compress>
	${boardList.path}?page=${page}&list=${boardList.size}&order=${boardList.order}
</#compress></#macro>


<#if searchResult?has_content>
<div id="searchResult">
<span class="title">검색어: </span>
<span class="keyword">거래처</span>
</div>
</#if>


<#-- 드랍다운 어트리뷰트 -->
<#macro dropdown class position><#compress>
data-ui-dropdown="${selector} ${class}" data-ui-option="${position}"
</#compress></#macro>


<#include "#include/header/" + "card" + ".ftl" />

<div class="_work-container grid-10">
	<#include "#include/body/" + listType + ".ftl" />
</div> <#-- _work-container -->



<#include "#include/component/editor.ftl" />
<#include "#include/component/info-table.ftl" />
<#include "/WEB-INF/view/admin/#include/component/gallery.ftl" />

