package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;
import com.example.demo.util.Ut;
import com.example.demo.vo.Article;
import com.example.demo.vo.Board;
import com.example.demo.vo.Page;
import com.example.demo.vo.ResultData;
import com.example.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsrArticleController {

	@Autowired
	private Rq rq;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private MemberService memberService;


	// 액션 메소드
	@RequestMapping("/usr/article/detail")
	public String getArticleAction(Integer id, Model model) { // null 체크하려고 Integer로 바꿨다.

		// 그냥 article/detail 치고 들어오는 경우 체크
		if (id == null) {
			model.addAttribute("checkUrlId", "게시글 번호를 입력하세요");
			return "usr/article/detail";
		}

		// 게시글 db에서 가져오기 + 로그인 중인 아이디 권한체크까지 다 끝내고 가져온다.
		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);

		model.addAttribute("article", article);

		return "usr/article/detail";
	}

	@RequestMapping("/usr/article/doIncreaseHitCountRd")
	@ResponseBody
	public ResultData doIncreaseHitCountRd(int id) {

		// 조회수 증가
		ResultData increaseHitCountRd = articleService.increaseHitCount(id);

		// 가져올 게시글 없는 경우 체크
		if (increaseHitCountRd.isFail()) {
			return increaseHitCountRd;
		}

		ResultData rd = ResultData.newData(increaseHitCountRd, "hitCount", articleService.getArticleHitCount(id));

		rd.setData2("id", id);

		return rd;
	}

	@RequestMapping("/usr/article/list")
	public String showList(Model model, @RequestParam(defaultValue = "0") int boardId,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "title,body") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {

		// 게시글 전체 개수 구하기
		int articlesCount = articleService.getArticlesCount(boardId, searchKeywordTypeCode, searchKeyword);

		Page pagination = new Page(articlesCount, page);

		// 전체 게시글 가져오기
		if (boardId == 0) {
			// 게시판 번호로 게시글 가져오기 및 페이지네이션
			List<Article> articles = articleService.getForPrintArticles(boardId, pagination.getItemsInAPage(), page,
					searchKeywordTypeCode, searchKeyword);

			model.addAttribute("articlesCount", articlesCount);
			model.addAttribute("page", page);
			model.addAttribute("pagination", pagination);
//			model.addAttribute("board", board);
			model.addAttribute("boardId", boardId);
			model.addAttribute("articles", articles);

			return "usr/article/list";
		}

		// 게시판 이름표용 데이터
		Board board = boardService.getBoardById(boardId);

		// 게시판 번호가 없는 경우
		if (board == null) {
			return rq.historyBackOnView("없는 게시판이야");
		}

		// 게시판 번호로 게시글 가져오기 및 페이지네이션
		List<Article> articles = articleService.getForPrintArticles(boardId, pagination.getItemsInAPage(), page,
				searchKeywordTypeCode, searchKeyword);

		model.addAttribute("articlesCount", articlesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagination", pagination);
		model.addAttribute("board", board);
		model.addAttribute("boardId", boardId);
		model.addAttribute("articles", articles);

		return "usr/article/list";
	}

	@RequestMapping("/usr/article/modify")
	public String showModify(Integer id, Model model) {

		// 로그인 정보 가져오기
//		Rq rq = (Rq) req.getAttribute("rq");

		// 게시글 존재여부 체크
		if (id == null) {
			return rq.historyBackOnView("없는 게시글이야");
		}

		Article article = articleService.getForPrintArticle(rq.getLoginedMemberId(), id);

		model.addAttribute("article", article);

		return "usr/article/modify";
	}

	// 로그인 체크 -> 유무 체크 -> 권한 체크 -> 수정
	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public String doModify(int id, String title, String body) {
		// 로그인 상태 체크 - 인터셉터에서

		Article article = articleService.getArticle(id); // 해당 게시글 가져오기

		// 로그인 정보 가져오기
//		Rq rq = (Rq) req.getAttribute("rq");

		// 로그인 중인 아이디 권한체크(서비스에 요청)
		ResultData loginedMemberCanModifyRd = articleService.userCanModify(rq.getLoginedMemberId(), article);

		// 글 수정 작업
		if (loginedMemberCanModifyRd.isSuccess()) {
			articleService.modifyArticle(id, title, body);
		}
		return Ut.jsReplace(loginedMemberCanModifyRd.getResultCode(), loginedMemberCanModifyRd.getMsg(),
				"../article/detail?id=" + id);
	}

	// 로그인 체크 -> 유무 체크 -> 권한 체크 -> 삭제
	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public String doDelete(int id) {
		// 로그인 상태 체크 - 인터셉터에서

		Article article = articleService.getArticle(id);

		// 게시글 존재여부 체크
		if (article == null) {
			return Ut.jsHistoryBack("F-1", Ut.f("%d번 글은 존재하지 않습니다", id));
		}

		// 로그인 정보 가져오기
//		Rq rq = (Rq) req.getAttribute("rq");

		// 로그인 중인 아이디 권한체크(서비스에 요청)
		ResultData loginedMemberCanDeleteRd = articleService.userCanDelete(rq.getLoginedMemberId(), article);

		// 글 삭제 작업
		if (loginedMemberCanDeleteRd.isSuccess()) {
			articleService.deleteArticle(id);
		}
		return Ut.jsReplace(loginedMemberCanDeleteRd.getResultCode(), loginedMemberCanDeleteRd.getMsg(),
				"../article/list");
	}

	@RequestMapping("/usr/article/write")
	public String showJoin(Model model) {

		int currentId = articleService.getCurrentArticleId();

		model.addAttribute("currentId", currentId);
		return "usr/article/write";
	}

	@RequestMapping("/usr/article/doWrite")
	@ResponseBody
	public String doWrite(HttpServletRequest req, int boardId, String title, String body) {
		// 로그인 상태 체크 - 인터셉터에서

		// 제목 내용 빈 칸 확인
		if (Ut.isNullOrEmpty(title)) {
			return Ut.jsHistoryBack("F-1", "제목을 입력해주세요");
		}
		if (Ut.isNullOrEmpty(body)) {
			return Ut.jsHistoryBack("F-2", "내용을 입력해주세요");
		}

		// 로그인 정보 가져오기
//		Rq rq = (Rq) req.getAttribute("rq");

		// 게시글 작성 작업
		ResultData<Integer> writeArticleRd = articleService.writeArticle(title, body, rq.getLoginedMemberId(), boardId);

		// 작성된 게시글 번호 가져오기
		int id = (int) writeArticleRd.getData1();


		return Ut.jsReplace(writeArticleRd.getResultCode(), writeArticleRd.getMsg(), "../article/detail?id=" + id);
	}

}
