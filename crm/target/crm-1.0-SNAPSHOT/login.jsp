<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

	<script type="text/javascript">
		$(function (){

			//如果当前窗口不是顶层窗口，把当前窗口设为顶层窗口
			if (window.top != window){
				window.top.location = window.location;
			}

			//页面加载完毕后，让用户的文本框自动获得焦点
			$("#loginAct").focus();
			//为登录按钮绑定事件
			$("#submitBtn").click(function (){
				login()
			});

			//为当前登录窗口绑定敲键盘事件  event可以取得我们敲得是哪个键
			$(window).keydown(function (event){
				if (event.keyCode == 13){
					login();
				}
			});
		})

		function login(){
			//验证账号密码不能为空
			var loginAct = $.trim($("#loginAct").val())
			var loginPwd = $.trim($("#loginPwd").val())
			if (loginAct == "" || loginPwd == ""){
				$("#msg").html("账号密码不能为空")
				return false;
			}

			//去后台验证相关登录操作
			$.ajax({
				url : "settings/user/login.do",
				data : {
					"loginAct":loginAct,
					"loginPwd" : loginPwd
				},
				type : "post",
				dataType : "json",
				success : function (data){
					//如果登录成功，后端返回的数据为success
					if (data.success){
						//跳转到工作台初始页面
						window.location.href = "workbench/index.jsp";
					}else if (data == "sessionInvalid"){
						alert("测试")
						window.location.href = "login.jsp";
					}else{
						//如果登录失败
						$("#msg").html(data.msg)
					}
				}
			})

		}

	</script>
</head>

<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2021&nbsp;客户关系管理系统</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form  class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct" name="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" name="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: #c12e2a"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>