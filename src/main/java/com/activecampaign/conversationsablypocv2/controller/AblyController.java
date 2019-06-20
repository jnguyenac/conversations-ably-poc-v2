package com.activecampaign.conversationsablypocv2.controller;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.rest.Auth;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Capability;
import io.ably.lib.types.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AblyController {
    @Autowired
    private AblyRest ablyRest;

    @Autowired
    private AblyRealtime ablyRealtime;

    @GetMapping("/publish")
    public String publishForm(Model model) {
        model.addAttribute("message", new AblyMessage());
        return "publish";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishSubmit(AblyMessage message) throws AblyException {
        System.out.println("send this to ably: " + message);
        Channel channel = ablyRealtime.channels.get("site-channel");
        try {
            channel.publish("send", message.getContent());
            return ResponseEntity.ok().build();
        } catch (AblyException e) {
            System.out.println("uh oh");
        }

//        channel.publish("update", "{ \"chatMessage\": \""+ message.getContent() +"\" }", new CompletionListener() {
//            @Override
//            public void onSuccess() {
//                System.out.println("Message sent");
//            }
//
//            @Override
//            public void onError(ErrorInfo reason) {
//                System.out.println("Message not sent, error occurred: "
//                        + reason.message);
//            }
//        });

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/auth", method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
    public ResponseEntity<Auth.TokenRequest> auth(HttpServletRequest request, HttpServletResponse response) throws AblyException {
        String username = null;
        Auth.TokenParams tokenParams = new Auth.TokenParams();
        tokenParams.ttl = 36000000;
        Auth.TokenRequest tokenRequest;

//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equalsIgnoreCase("username")) {
//                    username = cookie.getValue();
//                    break;
//                }
//            }
//        }

        if (username == null) {
            tokenParams.capability = Capability.c14n("{ 'notifications': ['subscribe'] }");
        } else {
            tokenParams.capability = Capability.c14n("{ '*': ['publish', 'subscribe'] }");
            tokenParams.clientId = username;
        }

        try {
            tokenRequest = ablyRest.auth.createTokenRequest(tokenParams, null);
            return ResponseEntity.ok(tokenRequest);
        } catch (AblyException e) {
            response.setStatus(500);
            return null;
        }
    }

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(@RequestParam(name = "username", defaultValue = "anonymous")
//                                String username, HttpServletResponse response) throws IOException {
//        response.addCookie(new Cookie("username", username));
//        response.sendRedirect("/");
//        return "redirect:/";
//    }
//
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        for (Cookie cookie : request.getCookies()) {
//            if (cookie.getName().equalsIgnoreCase("username")) {
//                cookie.setValue(null);
//                cookie.setMaxAge(0);
//                cookie.setPath(request.getContextPath());
//                response.addCookie(cookie);
//            }
//        }
//        response.sendRedirect("/");
//        return "redirect:/";
//    }
}
