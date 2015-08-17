
<form class="upload modify">
<div class="form">
<table class="form-table small">
	<tbody>
		<tr>
			<th>
				<label>이름</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group">
						<input id="name" type="text" name="name" autocomplete="off" >
						<span class="addon" data-check-for="name"></span>
					</div>
					<span class="error" data-error-for="name"></span>
				</div>
			</td>
			<th>
				<label>직책</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group">
						<input id="position" type="text" name="position" autocomplete="off" >
						<span class="addon" data-check-for="position"></span>
					</div>
					<span class="error" data-error-for="position"></span>
				</div>
			</td>
		</tr>
		
		
		<tr>
			<th>
				<label>휴대전화</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group inline num">
						<input id="mobile1" type="text" name="mobile.num1" maxlength="3" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="mobile2" type="text" name="mobile.num2" maxlength="4" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="mobile3" type="text" name="mobile.num3" maxlength="4" autocomplete="off" >
					</div>
				</div>
			</td>
			
			<th>
				<label>이메일</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group inline email">
						<input id="email.id" type="text" name="email.id" autocomplete="off" >
					</div>
					<span class="separator">@</span>
					<div class="input-group inline email">
						<input id="email.host" type="text" name="email.host" autocomplete="off" >
					</div>
				</div>
			</td>
		</tr>
		
		<tr>
			<th>
				<label>유선전화</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group inline num">
						<input id="tel1" type="text" name="tel.num1" maxlength="3" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="tel2" type="text" name="tel.num2" maxlength="4" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="tel3" type="text" name="tel.num3" maxlength="4" autocomplete="off" >
					</div>
				</div>
			</td>
			<th>
				<label>팩스</label>
			</th>
			<td>
				<div class="form-group small">
					<div class="input-group inline num">
						<input id="fax1" type="text" name="fax.num1" maxlength="3" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="fax2" type="text" name="fax.num2" maxlength="4" autocomplete="off" >
					</div>
					<span class="separator">-</span>
					<div class="input-group inline">
						<input id="fax3" type="text" name="fax.num3" maxlength="4" autocomplete="off" >
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
<table class="table bordered outline">
	<tbody>
		<tr>
			<th>이름</th>
			<td colspan="3">
				<span data-type="name"></span>
				<span data-type="position"></span>
			</td>
		</tr>
		<tr>
			<th>휴대전화</th>
			<td data-type="mobile"></td>
			<th>이메일</th>
			<td data-type="email"></td>
		</tr>
		<tr>
			<th>전화</th>
			<td data-type="tel"></td>
			<th>팩스</th>
			<td data-type="fax"></td>
		</tr>
		<tr>
			<th>메모</th>
			<td colspan="3" data-type="memo"></td>
		</tr>
	</tbody>
</table>

</div>