 <#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />
 
<div class="_default-formwrap">

<table class="table bordered outline _partner-list">
	
	<thead>
		<tr>
			<th>No.</th>
			<th>거래처명</th>
			<th>대표자</th>
			<th>대표전화</th>
			<th>대표팩스</th>
			<th>웹하드</th>
			<th>웹사이트</th>
		</tr>
	</thead>
	
	<tbody>
		<#list boardList.list as partner>
		<tr>
			<td width="4%">${partner_index + 1}</td>
			<td width="25%"><a href="/admin/${requestInfo.command}/view/${partner.id}">${partner.name?default('')}</a></td>
			<td width="6%">${partner.ceoName?default('')}</td>
			<td width="8%">${partner.tel?default('')}</td>
			<td width="8%">${partner.fax?default('')}</td>
			<td width="8%">${partner.webhard?default('')}</td>
			<td width="10%">${partner.web?default('')}</td>
		</tr>
		</#list>
	</tbody>
	
	
</table>


<#-- 링크 -->
<#macro url page><#compress>
	${boardList.path}?page=${page}&list=${boardList.size}&order=${boardList.order}
</#compress></#macro>

<#compress>
<div class="page-nums">
	
	<#if boardList.prev != -1>
		<a class="prev" href="<@url boardList.prev />"><i class="fa fa-chevron-left"></i></a>
	</#if>
	
	<#list boardList.before as num>
		<a href="<@url num />">${num}</a>
	</#list>
	
	<a class="current" href="#" onClick="return false;">${boardList.this}</a>
	
	<#list boardList.after as num>
		<a href="<@url num />">${num}</a>
	</#list>
	
	<#if  boardList.next != -1>
		<a class="next" href="<@url boardList.next />"><i class="fa fa-chevron-right"></i></a>
	</#if>
</div>
</#compress>
	
</div> <#-- ._partner-insert -->

<script>

</script>