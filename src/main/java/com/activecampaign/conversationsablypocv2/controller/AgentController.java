package com.activecampaign.conversationsablypocv2.controller;

import com.activecampaign.conversationsablypocv2.AblySubscriberService;
import com.activecampaign.conversationsablypocv2.ChatMessage;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class AgentController {

    @Autowired
    private AblyRealtime ablyRealtime;

    ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    @GetMapping("/agent")
    public String greeting(Model model) throws AblyException {
        model.addAttribute("chatMessages", chatMessages);
        Channel channel = ablyRealtime.channels.get("site-channel");
        channel.subscribe(new Channel.MessageListener() {
            @Override
            public void onMessage(Message messages) {
                System.out.println("Message received: " + messages.data);
                ChatMessage incomingMessage = new ChatMessage();
                incomingMessage.setContent(messages.data.toString());
                chatMessages.add(incomingMessage);
            }
        });
        return "agent-template";
    }

}