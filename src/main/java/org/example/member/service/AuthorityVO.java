package org.example.member.service;

import org.example.member.status.MemberAuthority;

public class AuthorityVO {

    private int seq;
    private String memberId;
    private MemberAuthority memberAuthority;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MemberAuthority getMemberAuthority() {
        return memberAuthority;
    }

    public void setMemberAuthority(MemberAuthority memberAuthority) {
        this.memberAuthority = memberAuthority;
    }
}
