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
public class ReturnBooksController {
	final static Logger logger = LoggerFactory.getLogger(ReturnBooksController.class);

	@Autowired
  private RentBooksService rentBooksService;
	@Autowired
	private BooksService booksService;

	/**
	 * 対象書籍を返却する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String returnBook(
			Locale locale,
			@RequestParam("bookId") Integer bookId,
			Model model) {
    logger.info("Welcome rent! The client locale is {}.", locale);

    if (rentBooksService.getRentBookInfo(bookId) != 0) {
      rentBooksService.deleteBook(bookId);
    } else {
      model.addAttribute("rentErrorMessage", "貸出されていません");
    }
		
    model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
    return "details";
	}

}
