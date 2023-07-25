package org.example.api.controller;

import org.example.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Resource(name="memberService")
    private MemberService memberService;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;


    @ResponseBody
    @RequestMapping(value="/authenticationProcess.do", method = RequestMethod.POST)
    public void authenticate(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String memberId = request.getParameter("memberId");
        String memberPw = request.getParameter("memberPw");

        try {

            // 사용자 인증을 위한 UsernamePasswordAuthenticationToken 객체 생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberId, memberPw);

            // AuthenticationManager를 사용하여 인증 수행
            Authentication authentication = authenticationManager.authenticate(token);

            // 인증 성공 후 SecurityContext에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
        }
    }


    @RequestMapping(value="/jwtTokenValidation.do", method = RequestMethod.POST)
    public void jwtTokenValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> responseBody = new HashMap<>();

        // 토큰이 유효한 경우
        responseBody.put("status", "success");
        responseBody.put("message", "토큰이 유효");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
    }
}