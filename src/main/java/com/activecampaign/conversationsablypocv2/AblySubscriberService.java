package com.activecampaign.conversationsablypocv2;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AblySubscriberService {
    @Autowired
    private AblyRealtime ablyRealtime;

    ArrayList<ChatMessage> chatMessages = new ArrayList<>();

//    public ArrayList<ChatMessage> getChatMessages() {
//        return chatMessages;
//    }
//
//    public AblySubscriberService() throws AblyException {
//        Channel channel = ablyRealtime.channels.get("site-channel");
//        channel.subscribe(new Channel.MessageListener() {
//            @Override
//            public void onMessage(Message messages) {
//                System.out.println("Message received: " + messages.data);
//                ChatMessage incomingMessage = new ChatMessage();
//                incomingMessage.setContent(messages.data.toString());
//                chatMessages.add(incomingMessage);
//            }
//        });
//    }
}
