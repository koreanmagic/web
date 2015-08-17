
<#macro listTable name size headers addList><#compress>
<#assign spliteText = '' />
<div class="scroll-table ${name?default('')}">
	
	<#-- 블락처리할때 사용한다. -->
	<div class="ui-overlay ui-helper-hidden"></div>
	
	<div class="header">
		<span class="removable"><i class="fa fa-scissors"></i></span>
		<ul>
			<#list headers as header>
				<#assign spliteText = header?split(' ') />
				<li class="span-${spliteText[0]}" data-type="${spliteText[1]}">${spliteText[2]}</li>
			</#list> 
		</ul>
	</div>
	
	<div class="select">
	</div>
	
	<div class="list-wrap">
		<div class="list line-${size?default('5')}">
		</div> <!-- list -->
	</div>
	
	<#if (addList?length > 0)>
		<#include addList />
	</#if>	
</div>
</#compress></#macro>



<#macro inputText discription size name label><#compress>
<div class="input-group ${size?default('')}">
	<label>${label}</label>
	<div class="inputs">
		<input id="${name}" name="${name}" type="text" size="${size}" autocomplete="off">
		<#if (discription?length > 0)><span class="discription">${discription}</span></#if>
	</div>
	<span class="error" data-for="${name}"></span>
</div>
</#compress></#macro>