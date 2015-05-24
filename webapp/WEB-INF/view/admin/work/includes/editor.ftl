<#include "/WEB-INF/view/macro/html.ftl" />
<div id="editor" class="ui-helper-hidden">
 	
	<ul>
		<li><a href="#tabs1">입력데이터</a></li>
		<li><a href="#tabs2">담당자</a></li>
		<li><a href="#tabs3">하청업체</a></li>
		<li><a href="#tabs4">파일</a></li>
		<li><a href="#tabs5">배송정보</a></li>
	</ul>
	
	<!-- 수정 -->
	<div id="tabs1">
	
	
		<form id="modifyForm">
		
		<div class="box">
			<table class="form-table editor-form">
				<tbody>
					<tr>
						<th><label>품목</label></th>
						<td colspan="3">
							<div class="form-group">
							<div class="tooltip-focus input-group">
								<input id="item" name="item" type="text" class="tooltip-target" data-name-target="mousedown ._item-names ul li" autocomplete="off" readonly>
								<div class="tooltip-content _item-names _tooltip-pop">
									<div class="_item-category">일반출력</div>
									<ul>
										<li>명함</li>
										<li>스티커</li>
										<li>전단</li>
										<li>봉투</li>
										<li>상업인쇄</li>
										<li>경인쇄</li>
										<li>기타</li>
									</ul>
									<div class="_item-category">대형인쇄</div>
									<ul>
										<li>간판</li>
										<li>현수막</li>
										<li>실사</li>
										<li>시트컷팅</li>
										<li>스카시</li>
										<li>기타</li>
									</ul>
								</div>
								<span class="addon" data-check-for="item"></span>
							</div>
							<span class="description">귀돌이, 접지, 재단 등</span>
						</div>
						</td>
						
						<th></th><td></td>
					</tr>
				
					<tr>
					
						<th><label>상세</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="itemDetail" name="itemDetail">
									<span class="addon" data-check-for="itemDetail"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
							
						</td>
						
						<th><label>후가공</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="afterProcess" name="afterProcess">
									<span class="addon" data-check-for="afterProcess"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
						</td>
						
					</tr>
					
					<tr>
						<th><label>수량</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="count" name="count">
									<span class="addon" data-check-for="count"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
						</td>
						
						<th><label>사이즈</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="size" name="size">
									<span class="addon" data-check-for="size"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<th><label>건수</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="num" name="num">
									<span class="addon" data-check-for="num"></span>
								</div>
								<span class="description">작업건수</span>
							</div>
						</td>
						<th></th><td></td>
					</tr>
					
					<tr>
						<th><label>제작단가</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="cost" name="cost">
									<span class="addon" data-check-for="cost"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
						</td>
						
						<th><label>공급단가</label></th>
						<td>
							<div class="form-group">
								<div class="input-group">
									<input type="text" id="price" name="price">
									<span class="addon" data-check-for="price"></span>
								</div>
								<span class="description">귀돌이, 접지, 재단 등</span>
							</div>
						</td>
					</tr>
					
					<tr>
						<th><label>메모</label></th>
						<td colspan="3">
							<div class="form-group">
								<div class="input-group span-12">
									<textarea id="memo" name="memo"></textarea>
								</div>
							</div>
						</td>
					</tr>					
				</tbody>
			</table>
		</div>
		
		<div class="btns">
			<input type="submit" value="수정">
			<input type="reset" value="취소">
		</div>
						
		</form>
	
	</div> <!-- tabs1 -->
	
	<div id="tabs2">
		
		<dl class="choice-list">
			<dt>
				<span class="title">현재 :</span>
				<span class="current"></span>
				<button class="reset">취소</button>
			</dt>
			<dt>
				<span class="title">선택 :</span>
				<span class="selected"></span>
				<button class="confirm">변경</button>
			</dt>
		</dl>
		
		<@listTable name="manager-table" size=5 headers=["1 $index No", "1 position 직책", "2 name 이름",  "2 tel 전화번호", "2 fax 팩스", "3 email 이메일"] addList="/WEB-INF/view/admin/includes/scrollTable-add-manager.ftl" />
		
	</div>
	
	<div id="tabs3">
		<dl class="choice-list">
			<dt>
				<span class="title">현재 :</span>
				<span class="current"></span>
				<button class="reset">취소</button>
			</dt>
			<dt>
				<span class="title">선택 :</span>
				<span class="selected"></span>
				<button class="confirm">변경</button>
			</dt>
		</dl>
		<br />
		<@listTable name="subcontractor-table" size=9 headers=["1 $index No", "4 name 업체명", "2 tel 전화번호", "2 fax 팩스", "3 email 이메일"] addList="" />
	</div>
	
	
	
	<!-- ▒▒▒▒▒▒▒▒▒▒▒▒▒▒  참고파일 ▒▒▒▒▒▒▒▒▒▒▒▒▒▒ -->
	<div id="tabs4">
		<@listTable name="resource-table" size=4 headers=["1 $index No", "5 originalName 파일명", "2 size 사이즈", "2 fileType 다운로드"] addList="" />
		
		<ul class="select-file">
			<li class="input-group">
				<input id="file" type="file" name="file" multiple>
				<label for="file">파일 등록</label>
			</li>
			<li class="length">0</li>
			<li class="commit">
				<button class="btn-upload">전송</button>
			</li>
		</ul>
		<@listTable name="resource-upload-table" size=4 headers=["1 $index No", "4 name 파일명", "2 size 사이즈", "2 icon 타입"] addList="" />
	

	</div> <!-- tab2 -->
	
	
	<div id="tabs5">
		<div class="delivery input-group">
			<input name="delivery" type="radio" id="delivery1" value="0" checked="checked"><label for="delivery1">방문출고</label>
			<input name="delivery" type="radio" id="delivery2" value="1"><label for="delivery2">직접배송</label>
			<input name="delivery" type="radio" id="delivery3" value="2"><label for="delivery3">택배</label>
			<button class="change-btn">변경</button>
		</div>
		<dl class="choice-list">
			<dt>
				<span class="title">현재 :</span>
				<span class="current"></span>
				<button class="reset">취소</button>
			</dt>
			<dt>
				<span class="title">선택 :</span>
				<span class="selected"></span>
				<button class="confirm">변경</button>
			</dt>
		</dl>
		<@listTable name="address-table" size=4 headers=["1 $index No", "6 text 주소", "3 mobile 연락처", "2 name 받는사람"] addList="/WEB-INF/view/admin/includes/scrollTable-add-address.ftl" />
	</div> <!-- tab3 -->
	
		
</div> <!-- editor -->

		