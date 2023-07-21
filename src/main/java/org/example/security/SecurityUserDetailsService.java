package org.example.security;

import org.example.member.service.MemberDAO;
import org.example.member.service.MemberVO;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUserDetailsService implements UserDetailsService {

    @Resource(name="memberDaoMyBatis")
    private MemberDAO memberDAO;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        MemberVO memberVO = memberDAO.findByUsername(memberId);
        if(memberVO == null) {
            throw new UsernameNotFoundException("User not found with username: " + memberId);
        }

        List<String> authorities = memberDAO.findAuthoritiesByUsername(memberId);

        List<GrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new User(memberVO.getMemberId(), memberVO.getMemberPw(), grantedAuthorities);
    }
}