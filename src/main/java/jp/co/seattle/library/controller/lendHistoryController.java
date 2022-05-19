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
 * 貸出履歴コントローラー
 */
@Controller //APIの入り口
public class lendHistoryController {
	final static Logger logger = LoggerFactory.getLogger(lendHistoryController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentBooksService rentBooksService;

    /**
   * Homeボタンからホーム画面に戻るページ
   * @param model
   * @return
   */
    @RequestMapping(value = "/lendingHistroy", method = RequestMethod.GET)
    public String lendHistroy(Model model) {
        model.addAttribute("rentBookList", rentBooksService.getRentBookList());
        return "lendingHistroy";
    }

}
