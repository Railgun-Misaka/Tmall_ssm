<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<html>

<head>
	<script src="js/jquery/2.0.0/jquery.min.js"></script>
	<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
	<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
	<link href="css/fore/style.css" rel="stylesheet">
	<script>
		function checkEmpty(id, name){
			var value = $("#"+id).val();
			if(value.length==0){
				alert(name+ "不能为空");
				$("#"+id)[0].focus();
				return false;
			}
			return true;
		}
		function checkNumber(id, name){
			var value = $("#"+id).val();
			if(value.length==0){
				alert(name+ "不能为空");
				$("#"+id)[0].focus();
				return false;
			}
			if(isNaN(value)){
				alert(name+ "必须是数字");
				$("#"+id)[0].focus();
				return false;
			}
			
			
			return true;
		}
        function formatMoney(num){
            var val = parseFloat(num);
            if(val != num)
            	return;
            var z = parseInt(val);
            var f = val - z;
            z = z.toString();
            var head = z.length%3;
            if(head == 0)
            	head += 3;
            var money = "";
            for(var i = 0; head <= z.length; head += 3){
            	money = money + z.substring (i,head) + ",";
            	i = head;
            }
            money = money.substring (0, money.length - 1) + "." + parseInt(f*100);
            return money;
        }

        $(function(){


            $("a.productDetailTopReviewLink").click(function(){
                $("div.productReviewDiv").show();
                $("div.productDetailDiv").hide();
            });
            $("a.productReviewTopPartSelectedLink").click(function(){
                $("div.productReviewDiv").hide();
                $("div.productDetailDiv").show();
            });

            $("span.leaveMessageTextareaSpan").hide();
            $("img.leaveMessageImg").click(function(){

                $(this).hide();
                $("span.leaveMessageTextareaSpan").show();
                $("div.orderItemSumDiv").css("height","100px");
            });

            $("div#footer a[href$=#nowhere]").click(function(){
                alert("模仿天猫的连接，并没有跳转到实际的页面");
            });


            $("a.wangwanglink").click(function(){
                alert("模仿旺旺的图标，并不会打开旺旺");
            });
            $("a.notImplementLink").click(function(){
                alert("这个功能没做，蛤蛤~");
            });


        });

	</script>
</head>

<body>

