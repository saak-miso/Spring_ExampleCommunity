package org.example.member.service;

import org.example.member.status.MemberStatus;

public class MemberVO {

    private String seq;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private String memberOtherMatters;
    private MemberStatus memberStatus;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPw() {
        return memberPw;
    }

    public void setMemberPw(String memberPw) {
        this.memberPw = memberPw;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberOtherMatters() {
        return memberOtherMatters;
    }

    public void setMemberOtherMatters(String memberOtherMatters) {
        this.memberOtherMatters = memberOtherMatters;
    }

    public MemberStatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }
}