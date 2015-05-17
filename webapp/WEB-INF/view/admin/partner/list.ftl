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
		
	${boardList.pageTag}
	
</div> <!-- ._partner-insert -->

<script>

</script>