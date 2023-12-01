var ws_protocol = 'ws'; // ws 或 wss
var ip = '127.0.0.1'
var port = 9326

var heartbeatTimeout = 5000; // 心跳超时时间，单位：毫秒
var reconnInterval = 1000; // 重连间隔时间，单位：毫秒

var binaryType = 'blob'; // 'blob' or 'arraybuffer';//arraybuffer是字节
var handler = new IMHandler()
var myself=0;
var mtime="";
var myname="";


var tiows
//生成从minNum到maxNum的随机数
function randomNum(minNum,maxNum){ 
    switch(arguments.length){ 
        case 1: 
            return parseInt(Math.random()*minNum+1,10); 
        break; 
        case 2: 
            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); 
        break; 
            default: 
                return 0; 
            break; 
    } 
}
function initWs () {
 myname=getRandomName();
  var queryString = 'name='+myname;
   
  var param = "";
  tiows = new tio.ws(ws_protocol, ip, port, queryString, param, handler, heartbeatTimeout, reconnInterval, binaryType)
  tiows.connect()
}

//回车事件绑定
document.onkeydown = function(e){
    var ev = document.all ? window.event : e;
    if(ev.keyCode==13) {
        sendMsg();
    }
}

function showTime()
{
	var myDate = new Date();
	var m=myDate.getMinutes();
	if (m<10) m="0"+m;
	var s=myDate.getSeconds();
	if (s<10) s="0"+s;
	mtime=myDate.getHours()+":"+m+":"+s;
	
	document.getElementById("mtime").innerText=mtime;
}

function sendMsg () {
  var msg = document.getElementById('messageText');  
  tiows.send(msg.value);
  msg.value="";
}

function clearMsg () {
  document.getElementById('contentId').innerHTML = ''
}

initWs();
setInterval(showTime,1000);