

<ul class="nav-col">
	<li><a>현황판</a></li>
	<li><a>스케쥴</a></li>
	<li><a class="active">매출열람</a></li>
	<li><a>미수내역</a></li>
</ul>



<#list nav.iterator() as n>
	
	<ul class="nav-row ${n.on?string('active', '')}">
	<li class="header"><a href="${n.context.url}">${n.context.name}</a></li>
	<#list n.iterator() as i>
		<li ${i.on?string('class="active"', '')}><a href="${i.context.url}">${i.context.name}</a></li>
	</#list>
	</ul>
	
</#list>
