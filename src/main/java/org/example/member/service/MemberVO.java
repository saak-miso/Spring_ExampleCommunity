package org.example.member.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.example.member.status.MemberStatus;

@ApiModel(description="사용자 정보 Value Object")
public class MemberVO {

    @ApiModelProperty(value="시퀀스 UUID", example="", required=false)
    private String seq;

    @ApiModelProperty(value="사용자 ID", example="memberId", required=false)
    private String memberId;

    @ApiModelProperty(value="사용자 비밀번호", example="1q2w3e", required=false)
    private String memberPw;
    
    @ApiModelProperty(value="사용자 이름", example="사악미소", required=false)
    private String memberName;

    @ApiModelProperty(value="사용자 휴대폰 번호", example="010-0000-0000", required=false)
    private String memberPhone;

    @ApiModelProperty(value="사용자 Email 주소", example="wicked@saakmiso.com", required=false)
    private String memberEmail;

    @ApiModelProperty(value="기타사항", example="기타사항", required=false)
    private String memberOtherMatters;

    @ApiModelProperty(value="사용자 상태", example="ACTIVE", required=false)
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