<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: shashwat.ku
  Date: 25/9/16
  Time: 3:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style>
        li:before{
            content: " ";
        }
        li{
          margin: 8px 0;
            padding: 5px;
            border: 1px solid;
            border-radius:2px;
        }
        body{
            font-size: 1.2em;
        }
    </style>
</head>
<body>

<ul>
<c:forEach items="${requestScope.customerList}" var="customer">
    <img width="10" height="10" src="https://www.google.co.in/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwj-ycOE36nPAhUMuI8KHePvCfQQjRwIBw&url=https%3A%2F%2Fwww.iconfinder.com%2Fsearch%2F%3Fq%3Duser&psig=AFQjCNEKoAkS3qkkGDyAeQfz6y2jq48udw&ust=1474866239694336"/>&nbsp;&nbsp;
    <li><a href="#" data-url="${customer}" onclick="callback(this)">User:${customer}</a></li>
</c:forEach>
</ul>


<script src="https://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script src="js/constant.js" type="text/javascript"></script>

<script>
    function callback(obj) {
        jQuery.ajax({
            url: serverUrl + "/startchat",
            type: "POST",
            data: {
                userId: '${param.userId}',
                customerId : jQuery(obj).data('url')
            }
        }).done(function () {
            jQuery(this).addClass("done");
        });
    }
    setTimeout(function(){
        window.location.reload();
    },5000);
</script>
</body>

</html>
