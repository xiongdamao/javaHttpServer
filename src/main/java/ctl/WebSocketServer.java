package ctl;

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 12/7/20 4:21 PM
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 2020-03-30 18:47
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 2020-03-30 18:47
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 2020-03-30 18:47
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 2020-03-30 18:47
 */

/**
 * @author ke.xiong@oracle.com
 * @version 1.0
 * @date 2020-03-30 18:47
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import org.springframework.stereotype.Component;


@ServerEndpoint("/api/imserver/{userId}")
@Component

public class WebSocketServer {

    //   static Log log=LogFactory.get(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private Long userId;
    private String key;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;

        this.userId = userId;
        key = userId + "-" + session.getId();
        if (webSocketMap.containsKey(key)) {
            webSocketMap.remove(key);
            webSocketMap.put(key, this);
            //加入set中
        } else {
            webSocketMap.put(key, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        System.out.println("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("dddd");
        } catch (IOException e) {
            System.out.println("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(key)) {
            webSocketMap.remove(key);
            //从set中删除
            subOnlineCount();
        }
        System.out.println("用户退出:" + key + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("用户消息:" + session.getId() + ",报文:" + message);


        if (message.startsWith("size:")) {
            String[] sizes = message.split(":");

            try {
                Integer size = Integer.valueOf(sizes[1]);
                byte[] bytes = new byte[size];

                for (int i = 0; i < size; i++) {
                    bytes[i] = 86;


                }
                session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes, 0, size));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            session.getAsyncRemote().sendText("xxxx");
        }

//        //可以群发消息
//        //消息保存到数据库、redis
//        if(StringUtils.isNotBlank(message)){
//            try {
//                //解析发送的报文
//                JSONObject jsonObject = JSON.parseObject(message);
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserId",this.userId);
//                String toUserId=jsonObject.getString("toUserId");
//                //传送给对应toUserId用户的websocket
//                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
//                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//                }else{
//                    System.out.println("请求的userId:"+toUserId+"不在该服务器上");
//                    //否则不在这个服务器上，发送到mysql或者redis
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message, @PathParam("userId") Long userId) {

        if (userId != null) {
            webSocketMap.forEach((k, v) -> {
                if (k.startsWith(userId + "-")) {
                    try {
                        v.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            System.out.println("用户" + userId + ",不在线！");
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}