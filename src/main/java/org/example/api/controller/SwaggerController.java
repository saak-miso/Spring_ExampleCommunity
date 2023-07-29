package org.example.api.controller;

import io.swagger.annotations.*;
import org.example.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/swagger")
@Api(tags = "02. Swagger RestAPI 서비스 샘플")
public class SwaggerController {

    private final Map<Long, String> users = new HashMap<>();

    @ApiOperation("Get all users")
    @GetMapping("/")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> userList = new ArrayList<>(users.values());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @ApiOperation("Get user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<String> getUser(@PathVariable long userId) {
        String user = users.get(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value="POST 테스트", notes="Swagger API POST 방식 사용 테스트", hidden=false)
    @RequestMapping(value="/swaggerPost.do", method = RequestMethod.POST)
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> responseBody = new HashMap<>();


        String memberId = request.getParameter("memberId");
        String memberPw = request.getParameter("memberPw");

        // 토큰이 유효한 경우
        responseBody.put("status", "success");
        responseBody.put("message", "토큰이 유효");
        responseBody.put("memberId", memberId);
        responseBody.put("memberPw", memberPw);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
    }

    @ApiOperation("Update an existing user")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable long userId, @RequestBody String username) {
        if (users.containsKey(userId)) {
            users.put(userId, username);
            return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Delete user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }
}