package org.example.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MemberController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;


    @RequestMapping(value="/main.do", method = RequestMethod.GET)
    public String firstWelcomePage() {
        return "member/memberMain";
    }

    @RequestMapping(value="/logIn.do", method = RequestMethod.GET)
    public String showLoginForm() {
        return "member/memberLogIn";
    }

    @ResponseBody
    @RequestMapping(value="/authenticationProcess.do", method = RequestMethod.POST)
    public String processLogin(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            // 사용자 인증을 위한 UsernamePasswordAuthenticationToken 객체 생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

            // AuthenticationManager를 사용하여 인증 수행
            Authentication authentication = authenticationManager.authenticate(token);

            // 인증 성공 후 SecurityContext에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/main.do";

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/login.do?logout";
        }
    }
}