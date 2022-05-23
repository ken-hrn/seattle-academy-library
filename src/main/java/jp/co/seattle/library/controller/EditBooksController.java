package jp.co.seattle.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class EditBooksController {
	final static Logger logger = LoggerFactory.getLogger(EditBooksController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	/**
	 * 書籍情報を登録する
	 * @param locale ロケール情報
	 * @param bookId bookId
	* @param model モデル
	* @return 遷移先画面
	 */

	@RequestMapping(value = "/editBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8") //value＝actionで指定したパラメータ
	//RequestParamでname属性を取得
	public String editBook(Locale locale,
			@RequestParam("bookId") Integer bookId,
			Model model) {
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "editBook";
	}

	/**
	 * 書籍情報を登録する
	 * @param locale ロケール情報
	 * @param title 書籍名
	 * @param author 著者名
	 * @param publisher 出版社
	 * @param file サムネイルファイル
	* @param model モデル
	* @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String editBook(Locale locale,
			@RequestParam("bookId") Integer bookId,
			@RequestParam("title") String title,
			@RequestParam("author") String author,
			@RequestParam("publisher") String publisher,
			@RequestParam("publishDate") String publishDate,
			@RequestParam("thumbnail") MultipartFile file,
			@RequestParam("isbn") String isbn,
			@RequestParam("introduction") String introduction,
			Model model) {
		logger.info("Welcome editBooks.java! The client locale is {}.", locale);
		// パラメータで受け取った書籍情 報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publishDate);
		bookInfo.setIsbn(isbn);
		bookInfo.setIntroduction(introduction);

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);
			} catch (Exception e) {

				// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookDetailsInfo", bookInfo);
				return "editBook";
			}
		}

		// 各バリデーションチェックのメソッド呼び出し
		Boolean checkRequired = booksService.checkRequired(bookInfo);
		Boolean checkIsbnResult = booksService.checkIsbnDigits(isbn);
		Boolean checkDateResult = booksService.checkDateValidation(publishDate);
		List<String> errorMessages = new ArrayList<String>();

		// 必須項目チェック
		if (checkRequired == true) {
			errorMessages.add("未入力の必須項目があります");
		}

		// ISBN桁数チェック
		if (!(checkIsbnResult == true && isbn.matches("^[0-9]+$"))) {
			errorMessages.add("ISBNの桁数または半角数字が正しくありません");
		}

		// publishDateチェック
		if (!(checkDateResult == true && publishDate.matches("^[0-9]+$"))) {
			errorMessages.add("出版日は半角数字のYYYYMMDD形式で入力してください");
		}

		// エラーメッセージあればrender
		if (CollectionUtils.isEmpty(errorMessages)) {
			// 書籍情報を新規登録する
			booksService.updateBook(bookInfo, bookId);

			model.addAttribute("resultMessage", "登録完了");

			// TODO 登録した書籍の詳細情報を表示するように実装
			//  詳細画面に遷移する
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			return "details";
		} else {
			model.addAttribute("errorMessages", errorMessages);
			model.addAttribute("bookDetailsInfo", bookInfo);
			return "editBook";
		}
	}

}
