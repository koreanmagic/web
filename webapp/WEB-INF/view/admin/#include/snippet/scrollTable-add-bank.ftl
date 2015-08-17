<form class="upload modify">
<div class="form">
<table class="form-table small">
	<tbody>
		<tr>
			<th>
				<label>계좌번호</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group inline">
						<select id="bankName" name="bankName">
						<#list bankNames as b>
							<option value="${b.id}" <#if b_index == 3>selected="selected"</#if>>${b.name}</option>
						</#list>
						</select>
					</div>
					<div class="input-group inline span-4">
						<span class="addon">계좌번호</span>
						<input id="accountNum" type="text" name="accountNum" autocomplete="off" >
					</div>
					<div class="input-group inline span-2">
						<span class="addon">예금주</span>
						<input id="holder" type="text" name="holder" autocomplete="off" >
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>
				<label>메모</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group span-12">
						<textarea id="memo" rows="4" name="memo"></textarea>
					</div>
				</div>
			</td>
		</tr>
	</tbody>
</table>
</div>

<div class="btns">
	<input type="submit" value="확인">
	<input type="reset" value="취소">
</div>
</form>

<div class="view">
<div class="controller">
	<a href="#" data-mode="modify"><i class="fa fa-plus-square"></i>수정</a>
</div>
<span data-type="bankName"></span>
<span data-type="accountNum"></span>
<span data-type="holder"></span>

	
</div>
