 <#assign form=JspTaglibs["http://www.springframework.org/tags/form"] />
 
 
<div class="form-wrap">
<@form.form enctype="multipart/form-data" method="post" class="input-form"> 

<table class="form-layout partner-insert">

	<tbody>
		<tr>
			<th>회사 정보</th>
			<td>
				<dl class="form-group">
					<dt>
						<@form.label path="bizNum">사업자번호</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="bizNum.num1" maxlength="3" size="3" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="bizNum.num2" maxlength="2" size="3" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="bizNum.num3" maxlength="5" size="5" autocomplete="off" />
						</div>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<@form.label path="name">거래처명</@form.label>
					</dt>
					<dd>
						<div class="input-group">
							<@form.input path="name" size="50" autocomplete="off" />
							<span class="addon" data-check-for="name"></span>
						</div>
						<span class="description">사업자등록증에 기재된 상호명</span>
					</dd>
				</dl>
					
				<dl class="form-group">
					<dt>
						<@form.label path="otherName">다른이름</@form.label>
					</dt>
					<dd>
						<div class="input-group">
							<@form.input path="otherName" size="50" autocomplete="off" />
							<span class="addon" data-check-for="otherName"></span>
						</div>
						<span class="description">별칭이 있거나, 관례적인 다른 이름이 있을 경우 (쉼표로 구분해서 기재)명</span>
					</dd>
				</dl>
				
				
				<dl class="form-group">
					<dt>
						<@form.label path="ceoName">대표자</@form.label>
					</dt>
					<dd>
						<div class="input-group">
							<@form.input path="ceoName" size="20" autocomplete="off" />
							<span class="addon" data-check-for="ceoName"></span>
						</div>
						<span class="description">사업자등록증에 기재된 대표자성명</span>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<@form.label path="name">업태</@form.label>
					</dt>
					<dd>
						<div class="input-group">
							<@form.input path="bizCondition" size="20" autocomplete="off" />
							<span class="addon" data-check-for="bizCondition"></span>
						</div>
						<span class="description">서비스, 도소매업 등</span>
					</dd>
				</dl>
				<dl class="form-group">
					<dt>
						<@form.label path="name">종목</@form.label>
					</dt>
					<dd>
						<div class="input-group">
							<@form.input path="bizTypes" size="20" autocomplete="off" />
							<span class="addon" data-check-for="bizTypes"></span>
						</div>
						<span class="description">광고대행, 광고물작성, 기타광고 등</span>
					</dd>
				</dl>
				
			</td>
		</tr>
	</tbody>

	<tbody>
		<tr>
			<th>연락 정보</th>
			<td>

				<dl class="form-group">
					<dt>
						<@form.label path="mobile">휴대전화</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="mobile.num1" maxlength="3" size="3" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="mobile.num2" maxlength="4" size="5" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="mobile.num3" maxlength="4" size="5" autocomplete="off" />
						</div>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<@form.label path="tel">대표전화</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="tel.num1" maxlength="3" size="3" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="tel.num2" maxlength="4" size="5" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="tel.num3" maxlength="4" size="5" autocomplete="off" />
						</div>
					</dd>
				</dl>
				
				<dl class="form-group">
					<dt>
						<@form.label path="fax">대표팩스</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="fax.num1" maxlength="3" size="3" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="fax.num2" maxlength="4" size="5" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="fax.num3" maxlength="4" size="5" autocomplete="off" />
						</div>
					</dd>
				</dl>	
						
				<dl class="form-group">
					<dt>
						<@form.label path="email">이메일</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="email.id" size="21" autocomplete="off" />
						</div>
						<span class="separator">@</span>
						<div class="input-group inline">
							<@form.input path="email.host" size="30" autocomplete="off" />
						</div>
						<span class="separator">-</span>
						<div class="input-group inline">
							<@form.input path="fax.num3" maxlength="4" size="5" autocomplete="off" />
						</div>
						<span class="description">대표자 이메일, 혹은 회사에서 주로 사용하는 이메일</span>
					</dd>
				</dl>	
						
				<dl class="form-group">
					<dt>
						<@form.label path="web">홈페이지</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="web.url" size="30" autocomplete="off" />
						</div>
						<span class="separator">//</span>
						<div class="input-group inline">
							<@form.input path="web.id" size="10" autocomplete="off" />
						</div>
						<span class="separator">/</span>
						<div class="input-group inline">
							<@form.input path="web.password" size="10" autocomplete="off" />
						</div>
						<span class="description">대표자 이메일, 혹은 회사에서 주로 사용하는 이메일</span>
					</dd>
				</dl>			
				
				
				<dl class="form-group">
					<dt>
						<@form.label path="webhard">웹하드</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.input path="webhard.userId" size="13" autocomplete="off" />
						</div>
						<span class="separator">//</span>
						<div class="input-group inline">
							<@form.input path="webhard.password" size="13" autocomplete="off" />
						</div>
						<span class="description">대표자 이메일, 혹은 회사에서 주로 사용하는 이메일</span>
					</dd>
				</dl>			
				
			</td>
		</tr>
	</tbody>	
	
	
	<tbody>
		<tr>
		
			<th>메모</th>
			
			<td>
				<dl class="form-group">
					<dt>
						<@form.label path="memo">메모</@form.label>
					</dt>
					<dd>
						<div class="input-group inline">
							<@form.textarea path="memo" />
						</div>
						<span class="description">대표자 이메일, 혹은 회사에서 주로 사용하는 이메일</span>
					</dd>
				</dl>
			</td>
		</tr>
	</tbody>
	
</table>

<button>전송</button>
</@form.form>
</div>

