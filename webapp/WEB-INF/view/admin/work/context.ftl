<#include "/WEB-INF/view/macro/html.ftl" />
<dl class="btn" data-popup="#searchCustomer">
	<dt>
		<a href="#"><i class="fa fa-list"></i></a>
	</dt>
	<dd>작업등록</dd>
</dl>

<div id="searchCustomer">
	
	<div class="_search">
		<input type="text" name="keyword" size="50">
	</div>
	<br />
	<br />
	<@listTable name="customer-table" size=10 headers=["1 $index No", "5 name 거래처명", "2 ceoName 대표자", "2 tel 전화번호"] addList="" />
</div>