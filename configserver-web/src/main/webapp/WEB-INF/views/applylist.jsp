<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表列表</title>
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
			<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
				<meta http-equiv="description" content="This is my page">
					<link href="<c:url value="/resources/css/common.css" />"
						rel="stylesheet" type="text/css" />
</head>

<body>
	<form action="" name="form1" method="post" target="_self">
		<table class="add_table">
			<tr>
				<td width="10%" class="addtable_left">报表名称：</td>
				<td align="left" width="40%"><input type="text" class="text"
					value="" title="报表名称" /></td>

				<td class="addtable_left">统计时间：</td>
				<td class="tdright"><input class="Wdate text" type="text"
					readonly="readonly" value="" title="统计开始时间"
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true,maxDate:'#F{$dp.$D(\'dragEndTimestr\')}'})" />
					至 <input class="Wdate text" type="text" id="dragEndTimestr"
					title="统计结束时间" name="dragEndTimestr" readonly="readonly" value=""
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true,minDate:'#F{$dp.$D(\'dragStartTimestr\')}'})" />
				</td>
			</tr>

			<tr>
				<td class="addtable_left">用户名称：</td>
				<td align="left" class="tdright"><input type="text"
					class="text" value="" title="用户名称" /></td>
				<td class="addtable_left">用户帐号：</td>
				<td align="left" class="tdright"><input type="text"
					class="text" value="" title="用户帐号" /></td>
			</tr>

			<tr>
				<td class="addtable_left">用户名称：</td>
				<td align="left" class="tdright"><input type="text"
					class="text" value="" title="用户名称" /></td>
				<td class="addtable_left">用户帐号：</td>
				<td align="left" class="tdright"><input type="text"
					class="text" value="" title="用户帐号" /></td>
			</tr>

			<tr>
				<td colspan="4"><input type="submit" value="查 询"
					class="right-button02" title="查询" /> <input class="right-button02"
					type="reset" value="清 空" title="清空" /></td>
			</tr>

		</table>

		<table border="0" cellpadding="4" cellspacing="1" class="edit_table">
			<thead>
				<tr style="background: #eee;">
					<th width="5%" align="center">序号</th>
					<th width="15%">周开始日期</th>
					<th width="5%">周结束日期</th>
					<th width="6%">确认订单金额</th>
					<th width="8%">客单价</th>
					<th width="6%">操作</th>
				</tr>
			</thead>
			<tbody>
				<tr style="background: #fff;">
					<td>0</td>
					<td>2011-10-13</td>
					<td>2011-10-13</td>
					<td>4652</td>
					<td>300</td>
					<td><a href="#">修改</a> | <a href="#">删除</a></td>
				</tr>
				<tr style="background: #fff;">
					<td>1</td>
					<td>2011-10-13</td>
					<td>2011-10-13</td>
					<td>4652</td>
					<td>300</td>
					<td><a href="#">修改</a> | <a href="#">删除</a></td>
				</tr>
				<tr style="background: #fff;">
					<td>2</td>
					<td>2011-10-13</td>
					<td>2011-10-13</td>
					<td>4652</td>
					<td>300</td>
					<td><a href="#">修改</a> | <a href="#">删除</a></td>
				</tr>
				<tr style="background: #fff;">
					<td>3</td>
					<td>2011-10-13</td>
					<td>2011-10-13</td>
					<td>4652</td>
					<td>300</td>
					<td><a href="#">修改</a> | <a href="#">删除</a></td>
				</tr>
			</tbody>
		</table>
		<div class="page">
			<div class="pgleft do">
				<input type="button" value="删 除" class="right-button02"
					onclick="DeleteAll()" title="删除">
			</div>
			<div class="pgright"></div>
		</div>
	</form>
</body>
</html>
