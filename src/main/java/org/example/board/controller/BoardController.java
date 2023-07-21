package org.example.board.controller;

import org.example.board.service.BoardService;
import org.example.board.service.BoardVO;
import org.example.utility.OutputPagination;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


@Controller
@RequestMapping("/board")
public class BoardController {
    @Resource(name="boardService")
    private BoardService boardService;

    // 현재 인증된 사용자 정보를 가져옵니다.


    @RequestMapping(value = "/boardDetail.do", method = RequestMethod.GET)
    public String boardDetail(HttpServletRequest request, Model model) throws Exception {

        BoardVO detailBoardVO = new BoardVO();

        if(request.getParameter("seq").isEmpty() == false) {

            detailBoardVO.setSeq(Integer.parseInt(request.getParameter("seq")));
            BoardVO responseBoardVO = boardService.selectBoard(detailBoardVO);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String memberId = authentication.getName();
            model.addAttribute("memberId", memberId);
            model.addAttribute("writeId", responseBoardVO.getMemberId());
            model.addAttribute("boardTitle", responseBoardVO.getBoardTitle());
            model.addAttribute("boardContent", responseBoardVO.getBoardContent());
            model.addAttribute("boardDate", responseBoardVO.getBoardDate());
        }
        return "board/boardDetail";
    }

    @RequestMapping(value = "/boardList.do")
    public String boardList(HttpServletRequest request, Model model) throws Exception {

        BoardVO boardVO = new BoardVO();

        int totalRow = boardService.selectCountBoard(boardVO);      // 해당 테이블의 전체 갯수
        int choicePage = 0;                                         // 선택 페이지
        int startRow = 0;                                           // MySQL LIMIT 시작점
        int limitRow = 10;                                          // MySQL LIMIT 종료점( 출력될 가로(row)의 개수를 지정 )

        if(request.getParameter("page") != null && request.getParameter("page").length() > 0) {
            choicePage = Integer.parseInt(request.getParameter("page"));
            startRow = (choicePage - 1) * limitRow;
        } else {
            choicePage = 1;
            startRow = 0;
        }

        OutputPagination outputPagination = new OutputPagination();

        model.addAttribute("boardList", boardService.selectListBoard(boardVO, startRow, limitRow));
        model.addAttribute("boardPagination", outputPagination.outputServletPagination(choicePage, limitRow, totalRow, "boardMovePage"));

        return "board/boardList";
    }
    @RequestMapping(value = "/boardWrite.do")
    public String boardWrite() throws Exception {
        return "board/boardWrite";
    }

    @RequestMapping(value = "/boardRevise.do", method = RequestMethod.GET)
    public String boardEdit(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 현재 인증된 사용자 정보를 가져옵니다.
        String memberId = authentication.getName(); // 여기서 getName()은 주로 사용자의 식별자 (ID)를 반환합니다.

        BoardVO editBoardVO = new BoardVO();
        editBoardVO.setSeq(Integer.parseInt(request.getParameter("seq")));
        editBoardVO.setMemberId(memberId);

        BoardVO responseBoardVO = boardService.selectBoard(editBoardVO);

        if(responseBoardVO == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('해당 포스팅을 수정할 권한을 가지고 있지 않습니다.');");
            out.println("window.location.href='./boardList.do';");
            out.println("</script>");
            out.flush();
        } else {
            model.addAttribute("seq", request.getParameter("seq"));
            model.addAttribute("memberId", responseBoardVO.getMemberId());
            model.addAttribute("boardTitle", responseBoardVO.getBoardTitle());
            model.addAttribute("boardContent", responseBoardVO.getBoardContent());
            model.addAttribute("boardDate", responseBoardVO.getBoardDate());
        }

        return "board/boardRevise";
    }

    @ResponseBody
    @RequestMapping(value = "/boardWriteInsert.do", method = RequestMethod.POST)
    public void boardWriteInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BoardVO boardVO = new BoardVO();

        // if(request.getParameter("memberId").isEmpty() == false) {
        //    boardVO.setMemberId(request.getParameter("memberId"));
        // }

        // 사용자 ID를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 여기서 getName()은 주로 사용자의 식별자 (ID)를 반환합니다.
        boardVO.setMemberId(memberId);

        if(request.getParameter("boardTitle").isEmpty() == false) {
            boardVO.setBoardTitle(request.getParameter("boardTitle"));
        }

        if(request.getParameter("boardDate").isEmpty() == false) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            boardVO.setBoardDate(formatter.parse(request.getParameter("boardDate")));
        }

        if(request.getParameter("boardContent").isEmpty() == false) {
            boardVO.setBoardContent(request.getParameter("boardContent"));
        }

        int resultNumber = boardService.insertBoard(boardVO);

        if(resultNumber > 0) {
            response.sendRedirect("./boardList.do");
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>alert('해당 글을 등록하는데 실패하였습니다.');</script>");
            out.flush();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/boardReviseUpdate.do", method = RequestMethod.POST)
    public void boardEditUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BoardVO boardVO = new BoardVO();

        if(request.getParameter("seq").isEmpty() == false) {
            boardVO.setSeq(Integer.parseInt(request.getParameter("seq")));
        }

        // if(request.getParameter("memberId").isEmpty() == false) {
        //    boardVO.setMemberId(request.getParameter("memberId"));
        // }

        // 사용자 ID를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName(); // 여기서 getName()은 주로 사용자의 식별자 (ID)를 반환합니다.
        boardVO.setMemberId(memberId);

        if(request.getParameter("boardTitle").isEmpty() == false) {
            boardVO.setBoardTitle(request.getParameter("boardTitle"));
        }

        if(request.getParameter("boardContent").isEmpty() == false) {
            boardVO.setBoardContent(request.getParameter("boardContent"));
        }

        if(request.getParameter("boardDate").isEmpty() == false) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            boardVO.setBoardDate(formatter.parse(request.getParameter("boardDate")));
        }

        int resultNumber = boardService.updateBoard(boardVO);

        if(resultNumber > 0) {
            response.sendRedirect("./boardDetail.do?seq=" + boardVO.getSeq());
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>alert('해당 글을 수정하는데 실패하였습니다.');</script>");
            out.flush();
        }
    }

    @RequestMapping(value = "/boardDelete.do", method = RequestMethod.GET)
    public void boardDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BoardVO boardVO = new BoardVO();

        if(request.getParameter("seq").isEmpty() == false) {
            boardVO.setSeq(Integer.parseInt(request.getParameter("seq")));
        }

        int resultNumber = boardService.deleteBoard(boardVO);

        if(resultNumber > 0) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('해당 글이 삭제되었습니다.');");
            out.println("window.location.href='./boardList.do';");
            out.println("</script>");
            out.flush();
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>alert('해당 글을 삭제하는데 실패하였습니다.');</script>");
            out.flush();
        }
    }
}