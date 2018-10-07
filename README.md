# wf-iot

#### 项目介绍
IOT云平台

#### 软件架构
软件架构说明


#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. xxxx
2. xxxx
3. xxxx

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 码云特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [http://git.mydoc.io/](http://git.mydoc.io/)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

#### 开发文档
# **多路智能通信平台**
## **统一名词**
名称|类型|说明
-|-|-
accountId|string|接入我们云平台的企业id
productionId|string|产品id
productionKey|string|产品key
productionKeySecure|string|用于设备登陆认证强校验
deviceId|string|设备id
sessionId|string |设备或者H5终端的会话id
applicationId|string|应用id
userId|string|用户id，企业不同应用下各自用户体系下的用户
serverHost|string|主机地址
serverPort|int|主机端口
deviceIp|string|设备联网分配的ip地址

## **曲速透明传输协议**
### **设备--云端**
#### **登陆认证**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备通过负载均衡节点查询获得主机地址，连接对应的主机和端口，然后去进行登陆验证。
        
##### 基本协议格式
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备向云端进行登陆认证时发送的的协议的格式分两种情况。认证登陆时消息数据的参数要以一个空格字符分割，以换行符作为结束标记。认证登陆后透传的数据以换行符作为一条消息数据的结束标记。消息数据为字符串形式。
##### （1）当设备与云端通信选择不加密时，即MODE 为N（弱校验）

                   MC MODE PK DID\n
##### （2）当设备云端通信选择加密时，即MODE非N（强校验）
                   MC MODE PKSTOKEN\n

说明：
>MC :消息数据的启始标志，指定为@#*WF ,如果云端接收数据后发现数据头部不含有该标志则丢弃。<br>
MODE:登陆认证的加解密方式，可选参数为[N,ECC,RSA] ，N即None，不采用任何加解密；ECC即椭圆加密算法；RSA即非对称加密算法。<br>
PK:productionKey，即产品key。
DID:deviceId即设备id，登陆认证deviceId。<br>
PSKTOKEN:[PK,DID,nowtime].pks；'.'符号的前面部分是加密后的数据，后面部分是采用的加密算法对应的公钥。<br>
nowtime 时间戳是为了被截包暴力验证。

##### 心跳
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;心跳具体用接入的设备自己定义大小格式等信息,发送到前置机，前置机会对其做心跳检测，设备登陆认证后透传的数据也可以当做心跳。

##### 重连机制
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备30秒超时未发送数据(包括心跳)，前置机断开连接，等待设备重新连接;设备断开连接后重新验证登陆。

**登陆认证流程**

![image](https://note.youdao.com/yws/api/personal/file/BA7575B20B654C33A9CD26616F2BBFC4?method=download&shareKey=0092d0e36497a3afbfc966259d194762)


#### 设备鉴权
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当登陆认证过程不采用加密方式时，直接获取设备登陆认证透传的productKey和deviceId参数，调用设备鉴权服务api。当登陆认证过程采用加密方式时，获取PKSTOKEN再调用对应api。鉴权成功则设备认证成功，设备与云端建立通信设备透传数据；鉴权失败则云端断开这个连接。

**设备鉴权API**

不采用加密方式，做弱校验时：
请求地址|请求方式
-|-
https://cloud.warpfuture.com/iot/auth/checkWithKey|Post
请求参数
名称|类型|说明
-|-|-
productionKey|string|产品key
deviceId|string|设备id
返回参数
名称|类型|说明
-|-|-
accountId|string|企业id
productionId|string|产品id
deviceId|string|设备id

采用加密方式，做强校验时：
请求地址|请求方式
-|-
https://cloud.warpfuture.com/iot/auth/checkWithSecure|Post
请求参数
名称|类型|说明
-|-|-
pkstoken|string|含有公钥的加密字符串
productionSecure|string|产品公钥
返回参数
名称|类型|说明
-|-|-
accountId|string|企业id
productionId|string|产品id
deviceId|string|设备id

**设备登陆认证失败或成功记录**

 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 设备登陆认证失败与否都将数据按一定格式丢到kafka，供后面数据分析所用

>{<br>
   "message":  ,<br>
   "messageTime"："" , //时间戳 <br> 
   "data":{<br>
       "accountId":"" ,//企业id <br>
       "deviceId":"", //设备id <br>
       "productionId":"",//产品id <br>
       "deviceIp":""  //设备联网的ip <br>
   }<br>
}

#### **数据上报**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备认证成功后透传的数据以Base64解码后在云端以数据上报的规定好的格式放到kafka里面(kafka以accountId分topic)，给消费者监听。

数据上报的格式如下：
>{<br>
	"accountId":"",<br>
	"source":{<br>
		"deviceId":"",<br>
		"productionId":""，<br>
 	    "deviceIp":""      //为实时定位设备所在位置作分布图所需<br>
	 },<br>
	"messageTime":，      //时间类型long <br>
	"data":""            //设备透传的数据<br>
}

**固件信息上报说明**
>设备固件基本信息通过设备通过https定时检查固件版本升级时将固件详细的信息以json格式传递给云端OTA固件升级服务那边。

固件基本信息参数表
名称|类型|说明
-|-|-
originOtaVersion|string|固件版本
deviceId|string|设备id 


## **Websocket协议**
### **应用--云端**
#### **app登陆认证**
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用户使用已经注册的账号调用云端登陆验证接口登陆app后会返回一个token，app利用返回的token调用建立websocket长连接的接口建立连接通道。若用户已经登陆直接带token到云端，云端校验这个token合法性，若合法建立连接通道。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;token时效为24小时，若token在调用建立连接通道的接口前已经失效，则接口调用失败即app与云端建立连接通道失败。若token在建立连接通道后才失效，则在连接通道断开之前依然可以通信。
        在与云端建立连接通信前，需要调用负载均衡服务获取云端连接地址和端口(或者域名),然后带着token与云端建立通信。

app与云端通信协议的数据格式如下：

>{<br>
	"accountId":"",<br>
	"data":"",<br>
	"messageTime": ,  //long类型<br>
	"source":{<br>
	    "applicationId":"",<br>
		"sessionId":"",<br>
		"deviceId":"",<br>
		"userId":""  <br>
	},<br>
	"target":{<br>
		"productionId":"",<br>
		"deviceId":"" <br>
	}<br>
}

#### **token校验**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;app通过负载均衡节点查询获取到连接地址后建立websocket连接带上token参数，云端获取到token调用token校验接口去校验token的合法性质。若token校验合法，返回userId给云端。

**token校验API**
请求地址|请求方式
-|-

请求参数
名称|类型|说明
-|-|- 
token|string|app请求云端建立websocket长连接时带来的token
返回参数
名称|类型|说明
-|-|-
userId|string|app应用下对应的用户id
applicationId|string|应用id
accountId|string|企业id

返回格式例子

>{<br>
   "code":""  ,<br>
   "message":  ,  //说明消息的内容<br>
   "data":{
       "accountId":"" ,//企业id <br>
       "applicationId":"" ,//应用id <br>
       "userId":"", //用户id <br>
       "deviceId":"" //用户使用设备id <br>
   }<br>
}


**token校验成功与否记录**

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;记录token验证时的信息，给以后进行分析所用。

记录的格式

成功
>{ <br>
   "message":  ,<br>
   "messageTime"："" , //时间戳 <br> 
   "data":{<br>
       "accountId":"" ,//企业id <br>
       "applicationId":"" ,//应用id <br>
       "userId":"", //用户id <br>
       "deviceId":"", //用户使用设备id <br>
   }<br>
}
>
失败<br>
>{ <br>
   "message":  ,<br>
   "messageTime"："" , //时间戳 <br> 
   "data":{<br>
       "userIp":"", //用户id <br>
       "token":"", //用户使用设备id <br>
   }<br>
}




#### **用户鉴权**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端对token成功进行校验之后，将接收到的userId、deviceId，applicationId这些参数传递给用户鉴权服务，检验用户是否具有对设备的权限。若检验成功，则将数据封装成app到云端下发的数据格式放到kafka。若校验不成功，则断开这个连接。

**用户鉴权API**

请求地址|请求方式
-|-
https://cloud.warpfuture.com/iot/auth/userToDev|Post
请求参数
名称| 类型| 说明
---|---|---
applicationId|string|应用id
userId|string|应用下对应用户id
deviceId|string|设备id
返回参数
名称|类型|说明
-|-|-
code|int|状态码
message|string|消息描述
data|string|若有则返回需要的数据，若无则为空

返回格式例子

>{<br>
   "code":""  , <br>
   "message":  ,  //说明该记录的内容<br>
   "data":{<br>
       "applicationId":"" ,//应用id <br>
       "userId":"", //用户id <br>
       "deviceId":"", //设备id <br>
       "productionId":"" //产品id <br>
   }<br>
}

**用户鉴权成功与否记录**
>{<br>
   "message":  ,  //说明该记录的内容<br>
   "messageTime"："" , //时间戳 <br> 
   "data":{
        <br>
       "accountId":"" ,//企业id <br>
       "applicationId":"" ,//应用id <br>
       "userId":"", //用户id <br>
       "deviceId":"" //用户使用设备id <br>
   }<br>
}



####  **设备上报鉴权**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;websocket服务器端监听到kafka消息，分析该接受到的消息是否具有发送到对应用户的权限。调用相应的鉴权接口。

 **设备上报鉴权API**
请求地址|请求方式
   -|-
 https://cloud.warpfuture.com/iot/auth/devToUser|Post

请求参数
名称|类型|说明
-|-|-
deviceId|string|设备id
productionId|string|产品id
返回参数
名称|类型|说明
-|-|-
code|int|状态码
message|string|消息描述
data|string|返回的数据，若不空则data包含userList
返回格式例子

>{<br>
   "code":""  ,<br>
   "message":  ,  //返回内容说明<br>
   "data":{<br>
       "userList":[  
                //多个用户id <br>
            ]      
   }<br>
}

#### 设备异常掉线日志记录
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设备断线，会回调通知服务器断线情况。按照规定好的设备状态存储格式，存放日志记录。

设备断线与否的状态记录格式
{
     "message":"",     //消息说明<br>
     "messageTime":"" ,//时间戳<br>
     "data":{<br>
         "accountId"："" ,<br>
         "deviceId":"" ,<br>
         "productionId":"" ,<br>
         "deviceIp"<br>
     }<br>
}



**该部分总体流程图**

![image](https://note.youdao.com/yws/api/personal/file/17B2E22FF0F345FEAA66E896BC35D6B0?method=download&shareKey=3db2d40db8bcd15ccd16250151c521b3)

## **前置机连接控制**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;另外起一个服务，供企业和内部使用，可以断开某个设备连接或者某个用户的连接。通过该服务，下发控制指令到kafka。netty服务器和websokect服务器监听到对应的控制指令，解析指令并执行指令，踢掉具体的某个设备或者用户的连接。

**控制指令格式**


{<br>
      "statement":"",<br>
      "messageTime":  ,<br>
      "data":{<br>
       "accountId":"",<br>
        "objectList":[<br> 
                //设备id或者用户id数组<br>
          ] ,<br>
        "content":""  //给设备或者用户发送的消息内容
      }<br>
}


### 内部API
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当某些服务器需要升级的时候，需要踢掉设备对服务器的连接，为避免一下子停掉服务器，导致负载均衡时增加对其他服务器的压力。通过访问内部接口，及时关闭设备对服务器的连接，随后服务器端自行批量地踢掉连接。该内部接口可以开启和关闭服务器对设备的屏蔽。

**请求API**
  请求地址|请求方式
  -|-
  https://iot-wtt.warpfuture.com/iot/connection/screen|Get
  请求参数
  名称|类型|说明
  ---|---|---
  flag|boolean|关闭和开启的标志
  返回参数
  名称|类型|说明
  -|-|-
 code|int|状态码
 message|string|消息描述
 data|string|返回的数据，此处成功与否都为空。

 返回格式例子
 >{<br>
   "code":""  ,<br>
   "message":"" ,//成功显示success;失败显示failure <br>
   "data":""  <br>
 }

### 企业api
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;给企业提供踢掉某设备连接或者用户连接的服务。企业调用该接口后，该服务将数据按照控制指令格式放到kafka。

**请求API**

请求地址|请求方式
-|-
https://iot-enterpriseapi.warpfuture.com/control|post

请求参数
名称|类型|说明
-|-|-
accountId|string|企业id
devList|String|设备id的json数组
返回参数
名称|类型|说明
-|-|-
code|int|状态码
message|string|消息描述
data|string|返回的数据，此处成功与否数据为空。

返回格式例子
>{<br>
   "code":""  ,<br>
   "message":"" ,//成功显示success;失败显示failure <br>
   "data":""  <br>
 }


## **全球负载均衡**
### **全球负载均衡节点查询**

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;app或者设备在请求连接云端前需要向统一的请求入口去获取连接云端的地址。获取的云端地址可能是一个数组。app或者设备在请求这个统一入口的时候，通过识别其ip地址，解析出ip地址来源于那个地区，根据所在地区去查询服务地址列表，获取当前最佳链接地址。
        
获取服务地址
请求地址|请求方式
-|-
https://iot-glb.warpfuture.come/getLinkedAddress|Get
请求参数
名称|类型|说明
-|-|-
deviceId|string|设备id
productionKey|string|产品密钥
返回参数
名称|类型|说明
-|-|-
code|int|状态码
message|string|消息描述
data|string|返回的数据，若不为空，包含serverHost，serverPort、connectType、encryptType，可能是json数组

返回格式例子

>{<br>
   "code":""  ,<br>
   "message":  ,<br>
   "data":[<br>
           { <br>
            "serverHost":"" ,    //主机地址<br>
            "serverPort":"" ,    //主机端口<br>
            "connectType":""  ,  //连接类型 websocket、TCP<br>
            "encryptType":""     //加密类型<br>
          }<br>
       ]<br>
}

## **OAuth认证**
### 认证相关数据结构
名称|类型|是否为空|说明
-|-|-|-
accoutId|string|否|企业id
applicationId|string|否|应用id
userId|string|否|用户id
phone|string|否|用户的手机号码
email|string|可以为空|用户邮箱
password|string|可以为空|用户密码
OAuthType|string|可以为空|第三方认证类型
qqData|string|可以为空|QQ认证相关数据
wxData|string|可以为空|微信认证相关数据
wbData|string|可以为空|微博认证相关数据
expand|string|不能为空|拓展字段

存在mongoDB的格式如下：

{<br>
  "accoutId":"",<br>
   "applicationId":"",<br>
   "userId":"",<br>
   "phone":"" ,<br>
   "email":"" ,<br>
   "password":"" ,<br>
   "OAuthType":"" , //微信 qq 微博<br>
   "qqData:":{<br>
      
   }<br>
   "wxData":"" ,<br>
   "wbData":"" ,<br>
   "expand":" <br>
}











##  **kafka管理**
### **名称规范约定**
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;kafka服务器那边消息以topic区分，一个企业分一个topic，topic命名以企业id命名。其他例如登陆信息的数据专门存放到一个topic，日志信息也存放到一个专门的topic。
### **问题解决**    
#### 动态建topic
&nbsp;&nbsp;&nbsp;&nbsp;kafka服务器那边跑一个程序，该程序对内部开放一个接口，当console有新的企业注册则主动调用该API去kafka那边增加一个企业topic。该程序通过java API运行kafka-topics.sh来增加topic。
#### netty服务端和websocket端监听kafka
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一个consumer订阅一个topic。
    
​    

## **状态码设计**
### **用户登陆认证相关**

状态码|提示信息|中文说明
-|-|-
1000|success|调用成功
1001|error| 未知错误，需要单独处理
1002|json parse error |json格式解析错误
1003| JWT parse error|jwt token解析错误
1004|deviceId repeat login|设备重复登陆
1005|Frame parse error|透传数据格式错误或者内容错误
1006|token can not verifivation|token校验错误
1007|App repeat login|App重复登陆
1008|device not online|设备不在线
1009|Internal error|服务器内部错误

### **设备登陆认证相关**  
状态码|提示信息|中文说明
-|-|-
2000|success|调用成功
2001|data not valid,please check your protocol template|透传的数据不合法，请参照协议说明
2002|productionKey not available|不是有效的PK

### **负载均衡相关**
状态码|提示信息|中文说明
-|-|-
3000|success|调用成功
3001|error|未知错误
3002|time out|时间超时
3003|method not support|方法不支持
3004|server error|服务错误
3005|server response error|服务应答错误

  



  



[1]:https://thumbnail0.baidupcs.com/thumbnail/c0de3b2ea9b208c5481e7400adbac343?fid=2360090255-250528-504853889458277&amp;time=1523041200&amp;rt=sh&amp;sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-oom%2FaRhJQ9228%2BBrxSc44QcNnOQ%3D&amp;expires=8h&amp;chkv=0&amp;chkbd=0&amp;chkpc=&amp;dp-logid=9146941436977920599&amp;dp-callid=0&amp;size=c710_u400&amp;quality=100&amp;vuk=-&amp;ft=video
[2]: https://thumbnail0.baidupcs.com/thumbnail/a0d3c6cd2f0d973bb5ae64beb336b449?fid=2360090255-250528-1035751636332577&amp;time=1523077200&amp;rt=sh&amp;sign=FDTAER-DCb740ccc5511e5e8fedcff06b081203-WqJga7Vb1NXDIJe48fYDqmeG2Gg=&amp;expires=8h&amp;chkv=0&amp;chkbd=0&amp;chkpc=&amp;dp-logid=9157532366320749052&amp;dp-callid=0&amp;size=c710_u400&amp;quality=100&amp;vuk=-&amp;ft=video
