<form class="upload modify">
<div class="form">
<table class="form-table small">
	<tbody>
		<tr>
			<th>
				<label>주소</label>
			</th>
			<td colspan="3">
				<div class="form-group small">
					<div class="input-group span-12">
						<input id="text" type="text" name="text" autocomplete="off" >
						<span class="addon" data-check-for="text"></span>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>
				<label>받는사람</label>
			</th>
			<td class="span-6">
				<div class="form-group small">
					<div class="input-group">
						<input id="name" type="text" name="name" autocomplete="off" >
						<span class="addon" data-check-for="name"></span>
					</div>
				</div>
			</td>
			<th>
				<label>연락처</label>
			</th>
			<td class="span-6">
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
			<th>주소</th>
			<td colspan="3" data-type="text"></td>
		</tr>
		<tr>
			<th>받는사람</th>
			<td data-type="name"></td>
			<th>휴대전화</th>
			<td data-type="mobile"></td>
		</tr>
		<tr>
			<th>메모</th>
			<td colspan="3" data-type="memo"></td>
		</tr>
	</tbody>
</table>

</div>
