package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBooksService;

/**
 *  返却管理コントローラー
 */

@Controller //APIの入り口
public class SearchBookController {
	final static Logger logger = LoggerFactory.getLogger(SearchBookController.class);

	@Autowired
	private RentBooksService rentBooksService;
	@Autowired
	private BooksService booksService;

	/**
	 * 対象書籍を返却する
	 *
	 * @param locale ロケール情報
	 * @param searchWord 書籍ID
	 * @param model モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(
			Locale locale,
			@RequestParam("searchWord") String searchWord,
			@RequestParam("searchCriteria") String searchCriteria,
			Model model) {
		logger.info("Welcome search! The client locale is {}.", locale);
		model.addAttribute("bookList", booksService.getSearchBookList(searchCriteria, searchWord));
		return "home";
	}

}
