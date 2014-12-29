


<br />
<br />
<#assign values = ["new", "update", "emergency"]>
<#list 1..3 as i>
<ul class="ui-item-container bordered radius">
	
	<!-- *CSS* abstract-stretch-line TEMPLATE -->
	<li class="ui-item-header fluid-line">
		<div class="_command-btns">
			<i class="fa fa-chevron-circle-down ui-item-btn-toggle-open cursor-pointer"></i>
			<i class="fa fa-chevron-circle-up ui-item-btn-toggle-close cursor-pointer"></i>
			<i class="fa fa-floppy-o ui-item-btn-add cursor-pointer"></i>
			<i class="fa fa-times ui-item-btn-delete cursor-pointer"></i>
		</div>
		
		
		<!-- 리스트 왼쪽 공간 -->
		<div class="left">
		
			<!-- 번호 DB #primary -->
			<div class="_no">
				no.<span class="_id text-num text-bold h3">3320</span>
			</div>
			
			<div class="_work-state">
				<div class="select-btn">
					<button class="btn">작업중</button>
					<button class="toggle"></button>
					<ul class="option">
                  		<li><a href="#">대기</a></li>
                  		<li><a href="#">제작대기</a></li>
                  		<li><a href="#">인쇄완료</a></li>
                  		<li class="divider"></li>
                  		<li><a href="#">납품</a></li>
                  		<li><a href="#">완료</a></li>
                	</ul>
				</div>
			</div>
			
			<div class="_insert-date">
				<span class="text-num text-bold h5">2014-11-09</span>
				<span>(월)</span><BR />
				<span class="_time text-num">(11:32:89)</span>
			</div>
			
		</div>
		
		<!-- 리스트 오른쪽 공간 -->
		<div class="right">
		
			<div class="_work-count">
				<span class="_work-done text-num h3 text-bold">1</span>
				<span class="_work-separator">/</span>
				<span class="ui-item-length text-num h1 text-bold">2</span>
			</div>
		
		</div>
			
			
		<!-- 유동적으로 가로사이즈가 조정되는 부분 -->
		<div class="center">
		
			<div class="_customer-icon"><img width="30" height="30" src="/img/customer-who.gif"/></div>
			<div class="_customer">
				<span class="_customer h4 text-bold"><a href="#">무한유통</a></span>
			</div>
		
			
			<div class="_subject">
				<span class="_subject h3 text-bold">엘마트가 없는 금지스티커</span><i class="_text-icon-${values[i-1]}"></i>
			</div>
			
		</div>
			
	</li>
	
	
	<!-- ######## 세부 리스트 아이템 ######## -->
	<li class="ui-item-body grid-10">
			
			<#list 1..4 as i>
			<div class="ui-item-item span-2">
				
				<!-- 작업 정보 컨테이너 -->
				<div class="_list-item-container">
					
					
					<!-- 컨트롤 버튼 -->
					<div class="_item-btns">
						
						<div class="_notice-panel text-num">
							<span class="ui-item-index"></span>
							<span class="_notice"></span>
						</div>
						
						<i class="fa fa-pencil" title="메모"></i>
						<i class="fa fa-folder-open-o" title="파일업로드"></i>
						<i class="fa fa-wrench modify" title="수정"></i>
						<i class="fa fa-trash-o ui-item-delete delete" title="삭제"></i>
					</div>
					
					<!-- 각종 에디터 -->
					<div class="_panel">
						<img src="/img/test.jpg">
					</div>
					
					<!-- 작성시간 / 업데이트 시간 -->
					<div class="_item-time text-num">
						2014-12-19 09:00:00
					</div>
					<!-- <div class="_item-subject">${i}) 이거슨 제목</div>  -->
					
					
					<!-- 작업 정보 -->
					<div class="_item-data">
						
						<div class="_data-cell fluid-line">
							<span class="_label left">품목</span>
							<div data-name="item" class="_value center" data-transform="input">전단지 ${i}</div>
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
							<div data-name="contractor" class="_value center" data-transform="input">중앙에스엠</div>
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
			
	</li>
	
</ul>
<br />
<br />
<br />
</#list>





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
			
			
<script src="/js/component/ui.input-transform.js"></script>
<script src="/js/component/ui.selectBtn.js"></script>
<script src="/js/component/ui.itemContainer.js"></script>
<script src="/js/component/ui.workList.js"></script>
<script>
	$(".select-btn").selectBtn();
</script>