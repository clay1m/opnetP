<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<c:url value='/static/css/bootstrap.css' />"
		rel="stylesheet"></link>
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
	<link href="<c:url value='/static/css/jsPlumbToolkit-defaults.css' />"
		rel="stylesheet"></link>
	<link href="<c:url value='/static/css/jsPlumbToolkit-demo.css' />"
		rel="stylesheet"></link>
	<script src="<c:url value='/static/js/jquery-2.1.4.js' />"></script>
	<script src="<c:url value='/static/js/jquery-ui.js' />"></script>
	<script src="<c:url value='/static/js/jsPlumb-2.0.4.js' />"></script>
	<script src="<c:url value='/static/js/dagre.js' />"></script>
	<script src="<c:url value='/static/js/angular.js' />"></script>
	<script src="<c:url value='/static/js/jsPlumbToolkit-1.0.js' />"></script>
	<script src="<c:url value='/static/js/jsPlumbToolkit-angular-1.0.13.js' />"></script>
	<script src="<c:url value='/static/js/app.js' />"></script>
	
	
	<style>
		table {
			position: absolute;
			border: 1px solid black;
			border-radius: 10px;
			padding: 0px;
			width: 100px;
			height: 75px;
		}
		
		th, td {
			border: 0.2px solid black;
			text-align: center;
			background-color: LightSteelBlue;
		}
		
		td.midRow {
			font-weight: bold;
		}
	</style>
	
	<title>${projectName} graph</title>
</head>

<body>
	<input type="hidden" id="project" value='${project}'>  
	<input type="hidden" id="projectId" value='${projectId}'> 
	<input type="hidden" id="userId" value='${userId}'> 
</body>

</html>