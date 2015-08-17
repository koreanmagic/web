 <#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />
 <#include "/WEB-INF/view/macro/html.ftl" />
 
<div class="form-wrap">

<table class="form-layout">

	<tbody>
		<tr>
			<th>담당자</th>
			<td>
			
				<@listTable name="manager-table" size=5 headers=["1 $index No", "1 position 직책", "2 name 이름",  "2 tel 전화번호", "2 fax 팩스", "3 email 이메일"] addList="/WEB-INF/view/admin/#include/snippet/scrollTable-add-manager.ftl" />
			</td>
		</tr>
	</tbody>
	
	
	<tbody>
		<tr>
			<th>작업정보</th>
			<td>
			
			
			<form id="command" method="post" data-customer-id="${command.customer.id}" >
				<input id="subcontractor" name="subcontractor" type="hidden">
				<input id="address" name="address" type="hidden">
				<input id="manager" name="manager" type="hidden">
				
				<dl class="form-group">
					<dt>
						<label>거래처</label>
					</dt>
					<dd>
						<span class="customer-name">${command.customer.name}</span>
					</dd>
				</dl>
							
				<dl class="form-group">
					<dt>
						<label>작업종류</label>
					</dt>
					<dd>
						<div class="input-group">
							<input name="workType" type="radio" id="workType1" value="0" checked="checked"><label for="workType1">일반</label>
							<input name="workType" type="radio" id="workType2" value="1"><label for="workType2">당일판</label>
							<input name="workType" type="radio" id="workType3" value="2"><label for="workType3">긴급</label>
						</div>
						<span class="description">작업 중요도</span>
					</dd>
				</dl>		
					
				
				<dl class="form-group">
					<dt>
						<label>품목</label>
					</dt>
					
					<dd>
						<div class="item-category input-group">
							<input id="item" name="item" type="text" size="50" autocomplete="off" data-ui-dropdown="._item-names" readonly>
							<span class="addon" data-check-for="item"></span>
						</div>
						<span class="description"><i class="fa fa-exclamation-triangle"></i> 품목명을 추가해야할 경우, 한컴 관리자에게 문의</span>
						<span class="error" data-error-for="item"></span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>품목상세</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="itemDetail" name="itemDetail" type="text" placeholder="품목상세" autocomplete="off">
							<span class="addon" data-check-for="itemDetail"></span>
						</div>
						<span class="description">아트지, 스타드림 등</span>
						<span class="error" data-error-for="itemDetail"></span>
					</dd>
				</dl>
				
				
				<#--
				<dl class="form-group">
					<dt>
						<label>후가공</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="afterProcess" name="afterProcess" type="text" autocomplete="off">
							<span class="addon" data-check-for="afterProcess"></span>
						</div>
						<span class="description">귀돌이, 접지, 재단 등</span>
						<span class="error" data-error-for="afterProcess"></span>
					</dd>
				</dl>
				-->
				
				<dl class="form-group">
					<dt>
						<label>사이즈</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="size" name="size" type="text" autocomplete="off">
							<span class="addon" data-check-for="size"></span>
						</div>
						<span class="description">ex) 212-299mm (재단선 3mm등)</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>수량</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="count" name="count" type="text" autocomplete="off">
							<span class="addon" data-check-for="count"></span>
						</div>
						<span class="description">필수입력</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>건수</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="num" name="num" type="text" autocomplete="off">
							<span class="addon" data-check-for="num"></span>
						</div>
						<span class="description">작업 건수</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>제작단가</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="cost" name="cost" type="text" autocomplete="off">
							<span class="addon" data-check-for="cost"></span>
						</div>
						<span class="description">제작하는 드는 비용</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>공급단가</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input id="price" name="price" type="text" autocomplete="off">
							<span class="addon" data-check-for="price"></span>
						</div>
						<span class="description">고객 청구 금액</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>메모</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<textarea name="memo" id="memo" style="height:80px" maxlength="245"></textarea>
						</div>
						<span class="description">전달사항</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<label>배송방법</label>
					</dt>
					
					<dd>
						<div class="input-group">
							<input name="delivery" type="radio" id="delivery1" value="0" checked="checked"><label for="delivery1">방문출고</label>
							<input name="delivery" type="radio" id="delivery2" value="1"><label for="delivery2">직접배송</label>
							<input name="delivery" type="radio" id="delivery3" value="2"><label for="delivery3">택배</label>
						</div>
						<span class="description">고객 청구 금액</span>
					</dd>
				</dl>
				
			</form>
			</td>
		</tr>
	</tbody>

	
	<tbody class="ui-helper-hidden address-input">
		<tr>
			<th>배송정보</th>
			<td>
				<@listTable name="address-table" size=4 headers=["1 $index No", "6 text 주소", "3 mobile 연락처", "2 name 받는사람"] addList="/WEB-INF/view/admin/#include/snippet/scrollTable-add-address.ftl" />
			</td>
		</tr>
	</tbody>
	
	<tbody>
		<tr>
			<th>하청업체</th>
			<td>
				<@listTable name="subcontractor-table" size=8 headers=["1 $index No", "4 name 업체명", "3 tel 전화", "4 web 홈페이지"] addList="" />
			</td>
		</tr>
	</tbody>
	
	
	
</table>


<button class="submit">클릭</button>

</div>

