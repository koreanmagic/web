
<div id="notice" class="grid">
	
	<#assign names = ["시안검토", "시안완료", "제작 중", "입고", "납품", "완료"] />
	
	<#list names as key> 
	<div class="span-2 notice-box">
		<div class="header">${key}</div>
		
		<div class="content _work-num">
			<div class="number _total">${key_index}</div>
			<div class="_today">
				<span class="btn">today</span> 1
			</div>
		</div>
		
	</div>
	</#list>
	
</div>

<div id="notice" class="grid">
	
	<div class="span-6 notice-box">
		<div class="header"></div>
		<div class="content"></div>
	</div>
	
	<div class="span-2 notice-box">
		<div class="header"></div>
		<div class="content"></div>
	</div>
	
	<div class="span-4 notice-box">
		<div class="header"></div>
		<div class="content"></div>
	</div>
	
</div>

<p style="height: 100px;"></p>

<br />


<a href="/resource/customer.csv">꿀렁</a>

<table class="table">
	
	<thead>
		<tr>
			<th>12345asdfasdfasdfasdfafsd6</th>
			<th>1234</th>
			<th>12</th>
			<th>1</th>
		</tr>
	</thead>
	
	<tbody>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
	<tbody>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>
		
	
<br />
<form>

	<div class="form">
	<label>텍스트</label><input type="text" />
	</div>
	<BR /><BR />
	
	
	<div class="form">
	<input type="checkbox" /><label>체크박스</label>
	<input type="checkbox" /><label>체크박스</label>
	<input type="checkbox" /><label>체크박스</label>
	<input type="checkbox" /><label>체크박스</label>
	</div>
	<BR /><BR />
	
	<div class="form">
	<input type="radio" /><label>라디오</label>
	<input type="radio" /><label>라디오</label>
	<input type="radio" /><label>라디오</label>
	<input type="radio" /><label>라디오</label>
	</div>
	<BR /><BR />
	
	<div class="form">
	<label>라디오</label>
		<select type="radio">
			<option>1</option>
			<option>2</option>
			<option>3</option>
		</select>
		</div>
	<BR /><BR />
	
	<div class="form">
	<textarea></textarea>
	</div>
	
</form>




<table class="table">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="8" rowspan="3">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="5" rowspan="2">&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="2" rowspan="2">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>

<br />
<br />
<br />
<br />

<#-- 

<#macro borad

<div class="btn-group">

	<#if paging.hasPrev>
		
	</#if>
	
	<#list paging.units as unit>
		<a href="${Request.servletPath}?page=${unit.num}&list=${Request.list}">${unit.num}</a>
	</#list>
	
	<#if paging.hasNext>
		
	</#if>

</div>


<div class="btn-group">



	<#if paging.hasPrev>
		
	</#if>
	
	<#list paging.units as unit>
		<a href="${Request.servletPath}?page=${unit.num}&list=${Request.list}">${unit.num}</a>
	</#list>
	
	<#if paging.hasNext>
		
	</#if>

</div>

-->