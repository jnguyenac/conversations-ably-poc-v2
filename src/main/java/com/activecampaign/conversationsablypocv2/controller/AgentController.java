package com.activecampaign.conversationsablypocv2.controller;

import com.activecampaign.conversationsablypocv2.AblySubscriberService;
import com.activecampaign.conversationsablypocv2.ChatMessage;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.ErrorInfo;
import io.ably.lib.types.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Controller
public class AgentController {

    @Autowired
    private AblyRealtime ablyRealtime;

//    @Autowired
//    private AblySubscriberService ablySubscriberService;

    ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    @GetMapping("/agent")
    public String greeting(Model model) throws AblyException {
        model.addAttribute("chatMessage", new ChatMessage());

        model.addAttribute("chatMessages", chatMessages);


//        chatMessages = ablySubscriberService.getChatMessages();
        return "agent-template";
    }

    @PostMapping("/sendAgentMessage")
    public ModelAndView sendMessage(@ModelAttribute ChatMessage chatMessage) throws AblyException {
        // TODO: put message in channel
        System.out.println("Please send the message = " + chatMessage.getContent());
        RestTemplate restTemplate = new RestTemplate();

//        String fooResourceUrl
//                = "http://localhost:8092/publish";
//        AblyMessage ablyMessage = new AblyMessage();
//        ablyMessage.setContent(chatMessage.getContent());
//        HttpEntity<AblyMessage> request = new HttpEntity<>(ablyMessage);
//        restTemplate.postForObject(fooResourceUrl, request, AblyMessage.class);
        Channel channel = ablyRealtime.channels.get("site-channel");
//        try {
//            channel.publish("send", message.getContent());
//            return ResponseEntity.ok().build();
//        } catch (AblyException e) {
//            System.out.println("uh oh");
//        }

        channel.publish("update", "{ \"chatMessage\": \""+ chatMessage.getContent() +"\" }", new CompletionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Message sent");
            }

            @Override
            public void onError(ErrorInfo reason) {
                System.out.println("Message not sent, error occurred: "
                        + reason.message);
            }
        });

//        return "agent-template";
        return new ModelAndView("redirect:/agent");

    }

    @PostConstruct
    public void initialize() throws AblyException {
        //do your stuff
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
    }

}