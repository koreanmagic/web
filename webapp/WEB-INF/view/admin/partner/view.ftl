<#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />
<#include "/WEB-INF/view/macro/html.ftl" />

<#macro btns><#compress>
<div class="btn-group modify-btn small">
	<a href="#" class="modify">수정</a>
	<a href="#" class="cancle">취소</a>
	<a href="#" class="confirm">확인</a>
</div>
</#compress></#macro>


<div class="form-wrap" data-type="${requestInfo.command}" data-id="${partner}">

<#-- ********************************************************************* -->
<#-- ********************************  기본정보  ******************************** -->
<#-- ********************************************************************* -->
<form id="businessInfo">
<span class="_img-partner-view-text-default"></span>
<@btns />
<table class="table bordered outline _modify-table">
	<tbody>
	
		<tr>
			<th>거래처명</th>
			<td colspan="3">
				<div class="form-group small  hidden">
					<div class="input-group">
						<input name="name" type="text" size="55" id="name" >
						<span class="addon" data-check-for="name"></span>
					</div>
				</div>
				<span class="value" data-type="name"></span>
			</td>
		</tr>
		
		<tr>
			<th>다른이름</th>
			<td colspan="3">
				<div class="form-group small  hidden">
					<div class="input-group">
						<input name="otherName" type="text" size="55" id="otherName" >
					</div>
				</div>
				<span class="value" data-type="otherName"></span>
			</td>
		</tr>
		
		<tr>
			<th>대표자 성명</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group">
						<input name="ceoName" type="text" size="25" id="ceoName" >
					</div>
				</div>
				<span class="value" data-type="ceoName"></span>
			</td>
			<th>사업자번호</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="bizNum.num1" type="text" maxlength="3" size="3" id="bizNum.num1" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="bizNum.num2" type="text" maxlength="4" size="4" id="bizNum.num2" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="bizNum.num3" type="text" maxlength="4" size="4" id="bizNum.num3" >
					</div>
				</div>
				<span class="value" data-type="bizNum.num1"></span>
				<span class="value" data-type="bizNum.num2"></span>
				<span class="value" data-type="bizNum.num3"></span>
			</td>
		</tr>
		<tr>
			<th>업태</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group">
						<input name="bizCondition" type="text" size="25" id="bizCondition" >
					</div>
				</div>
				<span class="value" data-type="bizCondition"></span>
			</td>
			
			<th>종목</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group">
						<input name="bizTypes" type="text" size="25" id="bizTypes" >
					</div>
				</div>
				<span class="value" data-type="bizTypes"></span>
			</td>
		</tr>
	</tbody>
</table>
</form>

<BR />
<BR />
<BR />
<#-- ********************************************************************* -->
<#-- ********************************  연락정보  ******************************** -->
<#-- ********************************************************************* -->
<form id="numberInfo">
<span class="_img-partner-view-text-tel"></span>
<@btns />
<table class="table bordered outline _modify-table">
	<tbody>
		<tr>
			<th>휴대전화</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="mobile.num1" type="text" maxlength="3" size="3" id="mobile.num1" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="mobile.num2" type="text" maxlength="4" size="4" id="mobile.num2" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="mobile.num3" type="text" maxlength="4" size="4" id="mobile.num3" >
					</div>
				</div>
				<span class="value" data-type="mobile.num1"></span>
				<span class="value" data-type="mobile.num2"></span>
				<span class="value" data-type="mobile.num3"></span>
			</td>
			
			<th>이메일</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="email.id" type="text" size="13" id="email.id" >
					</div>
					<span class="separator">@</span>
					<div class="input-group inline">
						<input name="email.host" type="text" size="15" id="email.host" >
					</div>
				</div>
				<span class="value" data-type="email.id"></span>
				<span class="value" data-type="email.host"></span>
			</td>
		</tr>
		<tr>
			<th>대표전화</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="tel.num1" type="text" maxlength="3" size="3" id="tel.num1" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="tel.num2" type="text" maxlength="4" size="4" id="tel.num2" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="tel.num3" type="text" maxlength="4" size="4" id="tel.num3" >
					</div>
				</div>
				<span class="value" data-type="tel.num1"></span>
				<span class="value" data-type="tel.num2"></span>
				<span class="value" data-type="tel.num3"></span>
			</td>
			
			<th>대표팩스</th>
			<td>
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="fax.num1" type="text" maxlength="3" size="3" id="fax.num1" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="fax.num2" type="text" maxlength="4" size="4" id="fax.num2" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="fax.num3" type="text" maxlength="4" size="4" id="fax.num3" >
					</div>
				</div>
				<span class="value" data-type="fax.num1"></span>
				<span class="value" data-type="fax.num2"></span>
				<span class="value" data-type="fax.num3"></span>
			</td>
		</tr>
		
		<tr>
			<th>회사홈페이지</th>
			<td colspan="3">
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="web.url" type="text" maxlength="3" size="3" id="web.url" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="web.id" type="text" maxlength="4" size="4" id="web.id" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input name="web.password" type="text" maxlength="4" size="4" id="web.password" >
					</div>
				</div>
				<span class="value" data-type="web"></span>
			</td>
		</tr>
		
		<tr>
			<th>웹하드</th>
			<td colspan="3">
				<div class="form-group small  hidden">
					<div class="input-group inline">
						<input name="webhard.id" type="text" maxlength="3" size="3" id="webhard.id" >
					</div>
					<span class="separator"> / </span>
					<div class="input-group inline">
						<input name="webhard.password" type="text" maxlength="4" size="4" id="webhard.password" >
					</div>
				</div>
				<span class="value" data-type="webhard"></span>
			</td>
		</tr>
	</tbody>
</table>
</form>

<BR />
<BR />
<BR />
<#-- ********************************************************************* -->
<#-- ********************************  담당자  ******************************** -->
<#-- ********************************************************************* -->
<@listTable name="manager-table" size=5 headers=["1 $index No", "1 position 직책", "2 name 이름",  "2 tel 전화번호", "2 fax 팩스", "2 email 이메일", "1 $delete 삭제"] addList="/WEB-INF/view/admin/#include/snippet/scrollTable-add-manager.ftl" />


<BR />
<BR />
<BR />
<#-- ********************************************************************* -->
<#-- ********************************  주소  ******************************** -->
<#-- ********************************************************************* -->
<@listTable name="address-table" size=5 headers=["1 $index No", "5 text 주소", "3 mobile 연락처", "2 name 받는사람", "1 $delete 삭제"] addList="/WEB-INF/view/admin/#include/snippet/scrollTable-add-address.ftl" />



<BR />
<BR />
<BR />
<#-- ********************************************************************* -->
<#-- ********************************  계좌  ******************************** -->
<#-- ********************************************************************* -->
<@listTable name="bank-table" size=3 headers=["1 $index No", "2 bankName 은행", "5 accountNum 계좌번호", "3 holder 예금주", "1 $delete 삭제"] addList="/WEB-INF/view/admin/#include/snippet/scrollTable-add-bank.ftl" />

</div>

