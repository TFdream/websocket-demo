<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Java后端WebSocket的Tomcat实现</title>
    <script type="text/javascript" src="http://ajax.microsoft.com/ajax/jquery/jquery-1.4.min.js"></script>
</head>

<body>
<div id="main" style="width: 1200px;height:800px;"></div>
Welcome<br/><input id="text" type="text" />
<button onclick="send()">发送消息</button>
<hr/>
<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<hr/>
<div id="message"></div>
</body>
<script type="text/javascript">
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window) {
        //改成你的地址
        websocket = new WebSocket("ws://127.0.0.1:8080/endpoint?token="+${token});
    } else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function() {
        setMessageInnerHTML("WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function() {
        setMessageInnerHTML("WebSocket连接成功");
    }
    var U01data, Uidata, Usdata
    //接收到消息的回调方法
    websocket.onmessage = function(event) {
        console.log(event);
        setMessageInnerHTML(event);
        setechart()
    }

    //连接关闭的回调方法
    websocket.onclose = function() {
        setMessageInnerHTML("WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function() {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send('{"msg":"' + message + '"}');
        setMessageInnerHTML(message + "&#13;");
    }
</script>

</html>