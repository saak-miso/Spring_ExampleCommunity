package org.example.api.controller;
import io.swagger.annotations.*;
import org.example.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/access")
@Api(tags = "01. 사용자 접속 RestAPI 서비스")
public class AccessController {

    @Resource(name="memberService")
    private MemberService memberService;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;


    @RequestMapping(value="/authenticationProcess.do", method=RequestMethod.POST)
    @ApiOperation(value="회원 인증 처리", notes="회원 인증을 수행합니다.")
    @ApiImplicitParams({
        @ApiImplicitParam(name="memberId", value="회원 아이디", required=true, dataTypeClass=String.class, paramType="query"),
        @ApiImplicitParam(name="memberPw", value="회원 비밀번호", required=true, dataTypeClass=String.class, paramType="query")
    })
    public void authenticate(@RequestParam String memberId, @RequestParam String memberPw, @ApiIgnore RedirectAttributes redirectAttributes) {

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
    public void jwtTokenValidation(HttpServletResponse response) throws Exception {

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