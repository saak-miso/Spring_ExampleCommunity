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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "Rest API 샘플")
public class ApiController {

    @Resource(name="memberService")
    private MemberService memberService;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @ApiOperation(value="API 작업의 제목", notes="API 작업에 대한 설명", tags="sample", hidden=false)
    @ApiResponses(value = {
            @ApiResponse(code=200, message="Success")
            , @ApiResponse(code=400, message="Bad Request")
            , @ApiResponse(code=404, message="Not Found")
            , @ApiResponse(code=500, message="Internal Server Error")
    })
    @RequestMapping(value="/swaggerSample/id/{userId}/name/{userName}", method=RequestMethod.POST)
    public String swaggerSample(
          @ApiParam(name="사용자 아이디", value="saakmiso", required=true) @PathVariable("userId") String userId
        , @ApiParam(name="사용자 이름", value="사악미소", required=false) @PathVariable("userId") String userName
    ) {
        String returnMessage = String.format("사용자 ID : %s, 사용자 이름 : %s", userId, userName);
        return returnMessage;
    }

    @ResponseBody
    @RequestMapping(value="/authenticationProcess.do", method=RequestMethod.POST)
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