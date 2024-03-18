package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
	private int itemsInAPage;
	private int totalPage;
	private int pageSize;
	private int from;
	private int end;
	
	public Page(int articlesCount, int page) {
		// 페이지네이션 한 페이지에 보여줄 게시글 수
		this.itemsInAPage = 10;
		// 페이지네이션 전체 버튼 수
		this.totalPage = (int) Math.ceil((double) articlesCount / itemsInAPage);
		// 페이지네이션 한 페이지 버튼 수
		this.pageSize = 10;
		// 페이지네이션 한 페이지 버튼 첫번째와 마지막 수
		this.from = ((page - 1) / pageSize) * pageSize + 1;
		this.end = from + pageSize - 1;
		if (end > totalPage) {
			end = totalPage;
		}
	}
	

}

