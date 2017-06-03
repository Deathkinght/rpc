<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.4.5/jquery.easyui.min.js"></script>

<script type="text/javascript">

$(function(){
	$("input[name='invokeTest']").click(function(){
		var invokeId = $(this).attr("id");
		var id = invokeId.substring(11);
		var serviceName_ = $("#bean"+id).val();
		var methodName_ = $("#method"+id).val();
		var params= $("input[name='param"+id+"']");
		var args ="";
		for(var i=0;i<params.length;i++)
		{
			args+=$(params[i]).val()+",";
		}
		var arg = args.substring(0,args.length-1);
		$.post('invoke',{interfaceName:serviceName_,methodName:methodName_,arg:arg},function(data){
				$("#text"+id).val(data);
		});
	});
})

</script>
</head>
<body>
<input type="button" value="aaa"  id="aa">
	<div>
		<table width="90%" background="silver" border="1">
			<tr align="center">
				<td>Service</td>
				<td>Methods</td>
				<td>参数名</td>
				<td>参数类型</td>
				<td>输入值</td>
				<td>操作</td>
				<td>结果</td>
			</tr>

			<c:set var="cnt" value="0" ></c:set>
			<c:forEach items="${beans}" var="bean">
				<c:forEach items="${bean.value.methods}" var="method">
				<c:set var="cnt" value="${cnt+1}" ></c:set>
					<c:choose>
						<c:when test="${method.value.parameters.size()==0}">
								<tr align="center">
								<td>
								${bean.key}
								</td>
								<td>${method.key}</td>
								<td>No</td>
								<td>No</td>
								<td>No</td>
								<td>
								<input type="hidden" id="bean${cnt}" value="${bean.key}">
								<input type="hidden" id="method${cnt}" value="${method.key}">
								<input type="button" value="接口测试" name="invokeTest" id="invokeTest+${cnt}">
								</td>
								<td><div><textarea  id="text${cnt}" rows="4" cols="30"></textarea></div></td>
								</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${method.value.parameters}" varStatus="var"  var="param_">
							<tr align="center">
							 <c:if test="${var.index==0}">
								 	<td rowspan="${method.value.parameters.size()}">${bean.key}</td>
									<td rowspan="${method.value.parameters.size()}">${method.key}</td>
								 </c:if> 
									<td>${param_.paramName}</td>
									<td>${param_.paramType}</td>
									<td><input type="text" name="param${cnt}" id="${param_.paramName}"></td>
								
								<c:if test="${var.index==0}">
									<td rowspan="${method.value.parameters.size()}">
										<input type="hidden" id="bean${cnt}" value="${bean.key}">
										<input type="hidden" id="method${cnt}" value="${method.key}">
										<input type="button" value="接口测试" name="invokeTest" id="invokeTest+${cnt}">
									</td>
									<td rowspan="${method.value.parameters.size()}">
										<div><textarea id="text${cnt}" rows="4" cols="30"></textarea></div>
									</td>
									</c:if>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:forEach>
		</table>
	</div>

</body>
</html>