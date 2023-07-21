<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>회원 로그인</title>
</head>
<style type="text/css">
    table, thead, tbody, tfoot { border:1px solid #000000;border-collapse:collapse; }
    tfoot { text-align:right; }
    th, td { border:1px solid #000000;padding:10px; }
    tbody > tr > td { cursor:pointer;cursor:hand; }
    tbody > tr > td:first-child { text-align:center; }
    button { cursor:pointer;cursor:hand; }
</style>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {

        document.addEventListener("keydown", function(event) {
            if(event.keyCode === 13) {
                accessMemberRequest();
            }
        });

        document.getElementById("memberSingUp").addEventListener("click", function() {
            window.location.href = "/singUp.do";
        });

        document.getElementById("memberLogIn").addEventListener("click", function() {
            accessMemberRequest();
        });
    });

    function accessMemberRequest() {

        if(document.getElementsByName("memberId")[0].value.replace(/\s/gi, "") == "") {
            alert("ID가 입력되지 않았습니다.\nID를 입력해 주세요.");
            document.getElementsByName("memberId")[0].focus();
            return false;
        }

        if(document.getElementsByName("memberPw")[0].value.replace(/\s/gi, "") == "") {
            alert("비밀번호가 입력되지 않았습니다.\n비밀번호를 입력해 주세요.");
            document.getElementsByName("memberPw")[0].focus();
            return false;
        }

        document.getElementById("formMemberAccess").method = "POST";
        document.getElementById("formMemberAccess").action = "/authenticationProcess.do";
        document.getElementById("formMemberAccess").submit();
    }
</script>
<body>
<h1>회원 로그인</h1>
<c:if test="${param.error != null}">
    <p style="color:#FF0000;">Login failed.</p>
</c:if>
<form id="formMemberAccess">
    <table>
        <tbody>
            <tr>
                <th>사용자 ID</th>
                <td><input type="text" name="memberId" required/></td>
            </tr>
            <tr>
                <th>비밀번호</th>
                <td><input type="password" name="memberPw" required/></td>
            </tr>
        </tbody>
        <tfoot style="border:0px">
            <tr>
                <td colspan="2">
                    <button type="button" id="memberSingUp">회원가입</button>
                    &nbsp;|&nbsp;
                    <button type="button" id="memberLogIn">로그인</button>
                </td>
            </tr>
        </tfoot>
    </table>
</form>
</body>
</html>