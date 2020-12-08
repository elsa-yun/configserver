<%@ include file="common.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title></title>
<script type="text/javascript"
	src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
<link href="<c:url value="/resources/css/left.css" />" rel="stylesheet"
	type="text/css" />
</head>

<body style="margin: 0; padding: 0; overflow: hidden;">
	<c:if
		test="${user_role=='tl' || user_role=='opm' || user_role=='admin'}">
		<div class="left">
			<div class="welcome">
				<div id="ninhao">${userName}</div>
			</div>
			<div class="left_center">
				<div class="left_body">

					<c:if test="${user_role=='admin'}">
						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img3" id="img3"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="用户管理"><a
												href="${wwwDomain}users/list" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">用户管理</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>

						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img4" id="img4"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="APP应用管理"><a
												href="${wwwDomain}appName/list" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">APP应用管理</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:if>

					<c:if test="${user_role=='tl'}">
						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img5" id="img5"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="APP配置文件名管理"><a
												href="${wwwDomain}config/tl_list" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">APP配置文件名管理</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:if>

					<c:if test="${user_role=='opm'}">
						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img6" id="img6"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="配置审核管理"><a
												href="${wwwDomain}opm/opm_list" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">配置审核管理</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:if>

					<c:if
						test="${user_role=='tl' || user_role=='opm' || user_role=='admin'}">
						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img7" id="img7"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="退出系统"><a
												href="${wwwDomain}users/edit_pwd" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">修改密码</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table border="0" cellpadding="0" cellspacing="0"
							class="left-table03">
							<tr>
								<td height="29">
									<table width="94%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="8%"><img name="img7" id="img7"
												src="images/img4.gif" width="16" height="15" /></td>
											<td width="92%" title="退出系统"><a
												href="${wwwDomain}login_out" target="mainFrame"
												class="wdleft" hidefocus="true"><font
													class="left-font03">退出系统</font></a></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:if>

				</div>
				<div class="left_bodybot"></div>
			</div>
		</div>
		<script>
			//Hoogle20111014
			$(function() {
				$(".left-table03:last").addClass("marginbot0");//动态让最后一个左侧栏目margin-bottom为0，否则有些浏览器会出现兼容性问题

				//1 左侧底部背景自动在底部，且不影响上面面板
				var windowheight = $(window).height();//窗口的高度
				var leftheight = windowheight - 80;//窗口的高度减去欢迎块的高度
				$(".left_center").css("height", leftheight);//动态设置left_center的高度			

				//2 右边frame高度动态设置为刚好满屏
				//console.log(window.parent.frames["centerFrame"].document);
				var leftheight = $(".left_center").height();
				//console.log(leftheight);
				var maindoc = window.parent.frames["centerFrame"].document;
				//console.log($(maindoc).find("#mainFrame"));
				$(maindoc).find("#mainFrame").css("height", leftheight + 44);
				//console.log($(maindoc).find("#mainFrame").height())

				//当窗口缩放时以上两种情况自适应
				window.onresize = function() {
					//1 情况
					var windowheight = $(window).height();//窗口的高度
					var leftheight = windowheight - 80;//窗口的高度减去欢迎块的高度
					$(".left_center").css("height", leftheight);//动态设置left_center的高度

					//2 情况
					//var leftheight=$(".left_center").height();
					var maindoc = window.parent.frames["centerFrame"].document;
					$(maindoc).find("#mainFrame")
							.css("height", leftheight + 44);
					console.log($(maindoc).find("#mainFrame").height())
				}

				$('.wdleft').click(function() {
					location.reload(true);
				});

			});

			function list(obj) {
				var innerval = $(obj).find("font").html();
				var mapstr = "统一配置管理系统" + " >> <span style='color:#002D61;'>"
						+ innerval + "</span>";
				//console.log(mapstr);
				var mainhtml = window.parent.frames["centerFrame"]
				//console.log(mainhtml);
				mainhtml.document.getElementById("guide").innerHTML = mapstr;
			}
		</script>
	</c:if>
</body>
</html>
