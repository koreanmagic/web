

<#assign values = ["new", "update", "emergency"]>
<div class="ui-item-container">
	
	<!-- *CSS* abstract-stretch-line TEMPLATE -->
	<div class="ui-item-header fluid-line">
		
		<!-- 리스트 왼쪽 공간 -->
		<div class="left">
		
			<select class="_highlight-customer">
				<option value="reset">거래처</option>
			</select>
			
			<select class="_sort">
				<option value="reset">정렬기준</option>
			</select>
			
			
		</div>
		
		<!-- 리스트 오른쪽 공간 -->
		<div class="right">

		<span class="ui-item-btn-add">추가</span>
		</div>
			
			
		<!-- 유동적으로 가로사이즈가 조정되는 부분 -->
		<div class="center">
		
		</div>
			
	</div>
	<!-- 
	<div style="
    width: 300px;
    height: 300px;
    position: absolute;
    top: 30px;
    left: 250px;
    background: white;
    border: 2px solid red;
    z-index: 1001;
    box-shadow: 5px 5px 5px rgb(170, 170, 170);
	"></div>
	<div class="test" style="
    width: 50px;
    height: 50px;
    position: absolute;
    top: 300px;
    left: 400px;
    background: white;
    border: 2px solid red;
    z-index: 1000;
    box-shadow: 5px 5px 5px rgb(170, 170, 170);
	"></div>
	<div class="test" style="
    width: 50px;
    height: 50px;
    position: absolute;
    top: 302px;
    left: 402px;
    background: white;
    z-index: 1002;
	"></div> -->
	<!-- ######## 세부 리스트 아이템 ######## -->
	<div class="ui-item-body grid-10">
	
			
			<#list data as v>
			<div class="ui-item-item span-2">
				
				<!-- 작업 정보 컨테이너 -->
				<div class="_list-item-container">
					
					
					<!-- 컨트롤 버튼 -->
					<div class="_item-btns">
						
						<div class="_notice-panel text-num">
							<span class="_item-index text-num text-bold h5"></span>
							<span class="_work_state">완료</span>
						</div>
						
						<i class="fa fa-bus" title="배송"></i>
						<i class="fa fa-pencil" title="메모"></i>
						<i class="fa fa-folder-open-o" title="파일업로드"></i>
						<i class="fa fa-trash-o ui-item-delete delete" title="삭제"></i>
					</div>
					
					<!-- 각종 에디터 -->
					<div class="_panel">
						<div class="_screen"><img src="/img/test.jpg"></div>
						<div class="_control fluid-line">
							<div class="right">
								<i class="fa fa-chevron-circle-left"></i> <i class="fa fa-chevron-circle-right"></i>
								<i class="fa fa-minus-square"></i> <i class="fa fa-plus-square"></i>
							</div>
						</div>
					</div>
					
					
					<!-- 작성시간 / 업데이트 시간 -->
					<div class="_item-label fluid-line">
						<img src="/img/customer-who.gif">
						<span class="_customer h4 text-bold">${v.customer}</span>
					</div>
					
					<div class="_item-date">
						<i class="fa fa-wrench modify" title="수정"></i>
						<span class="_date text-num">
							${v.date}
						</span>
					</div>
					
					
					<!-- 작업 정보 -->
					<div class="_item-data">
						
						<div class="_data-cell fluid-line">
							<span class="_label left">품목</span>
							<div data-name="item" class="_value center _item" data-transform="input">${v.item}</div>
						</div>
						
						<div class="_data-cell fluid-line">
							<span class="_label left">상세</span>
							<div data-name="memo" class="_value center _memo" data-transform="input">${v.memo}</div>
						</div>

						<div class="_data-cell fluid-line">
							<span class="_label left">사이즈</span>
							<div data-name="bleed" class="_value center _size" data-transform="input">${v.size}</div>
						</div>
						
						<div class="_data-cell fluid-line">
							<span class="_label left">수량</span>
							<div data-name="count" class="_value center _count" data-transform="input">${v.count}</div>
						</div>
						
						<div class="_data-cell fluid-line _half">
								<span class="_label left">제작단가</span>
								<div data-name="contractor" class="_value center _cost" data-transform="input">${v.cost}</div>
						</div>
						
						<div class="_data-cell fluid-line _half float-right">
							<span class="_label left">공급단가</span>
							<div data-name="contractor" class="_value center _price" data-transform="input">${v.price}</div>
						</div>
						
						<div class="_data-cell fluid-line">
							<span class="_label left">하청</span>
							<div data-name="contractor" class="_value center _constructor" data-transform="input">${v.constructor}</div>
						</div>
					</div>
					
					
					<!-- 작업 부가 정보 -->
					<div class="_item-resource">
					
						<a><i class="fa fa-comments"></i></a>
						<i class="separator"></i>
						
						<!-- 인쇄파일 올리기 -->
						<a><i class="fa fa-file-text-o"></i></a>
						<i class="separator"></i>
						
					</div>
				</div>
				
			</div>
			</#list>
			
	</div>
	
</div>
<br />
<br />
<br />





<div id="testtest" class="ui-item-item span-2 _list-box">
				
				<!-- 작업 정보 컨테이너 -->
				<div class="_list-item-container _state-confirm">
					
					
					<div class="_item-btns">
					
						<div class="ui-item-container-index _index text-num"></div>
						
						<!-- 이미지 올리기 -->
						<i class="fa fa-file-image-o"></i>
						
						<i class="fa fa-cogs modify"></i>
						
						<i class="fa fa-times ui-item-delete remove"></i>
					</div>
					
					
					<div class="_panel">
						<!-- <img src="/img/test.jpg">  -->
						<div class="_img-data">
						</div>
					</div>
					
					
					<div class="_item-time text-num">
						2014-12-19 09:00:00
					</div>
					
					
					<div class="js-formTransform _item-data">
						
						
						<div class="_data-cell fluid-line">
							<span class="_label left">품목</span>
							<div data-name="item" class="_value center" data-transform="input">전단지</div>
						</div>
						
						<div class="_data-cell fluid-line">
							<span class="_label left">상세</span>
							<div data-name="memo" class="_value center" data-transform="input">아트</div>
						</div>

						<div class="_data-cell _half fluid-line">
							<span class="_label left">재단</span>
							<div data-name="bleed" class="_value center" data-transform="input">210-297</div>
						</div>
						
						<div class="_data-cell _half float-right fluid-line">
							<span class="_label left">작업</span>
							<div data-name="size" class="_value center" data-transform="input">212-299</div>
						</div>
					
						<div class="_data-cell _half fluid-line">
							<span class="_label left">수량</span>
							<div data-name="count" class="_value center" data-transform="input">4000</div>
						</div>
						
						<div class="_data-cell _half float-right fluid-line">
							<span class="_label left">하청</span>
							<div data-name="subcontractor" class="_value center" data-transform="input">중앙에스엠</div>
						</div>
					</div>
					
					
					<div class="_item-resource">
					
						<a><i class="fa fa-comments"></i></a>
						<i class="separator"></i>
						
						<!-- 인쇄파일 올리기 -->
						<a><i class="fa fa-file-text-o"></i></a>
						<i class="separator"></i>
						
					</div>
				</div>
				
			</div>
			

<div id="new2015">
	<div class="div-1">꿍따</div>
	<div class="div-2">리</div>
	<div class="div-3">샤바</div>
	<div class="div-4">라</div>
	빠빠빠
</div>

<script src="/js/component/ui.input-transform.js"></script>
<script src="/js/component/ui.selectBtn.js"></script>

<script src="/js/component/ui.itemContainer.js"></script>
<script src="/js/component/ui.itemContainerDetail.js"></script>
<script>
	$(".select-btn").selectBtn();
</script>