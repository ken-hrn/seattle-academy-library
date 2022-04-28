package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
public class BulkRegistController {
	final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

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

	@RequestMapping(value = "/bulk", method = RequestMethod.GET) //value＝actionで指定したパラメータ
	//RequestParamでname属性を取得
	public String bulk(Model model) {
		return "bulk";
	}

	/**
	 * 書籍情報を登録する(CSV一括登録)
	 * @param locale ロケール情報
	 * @param title 書籍名
	 * @param author 著者名
	 * @param publisher 出版社
	 * @param file サムネイルファイル
	* @param model モデル
	* @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/bulkRegist", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile, Model model) {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {

			String inputValue;
			int lineCount = 0;
			while ((inputValue = br.readLine()) != null) {
				String[] inputValues = inputValue.split(",");

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(inputValues[0]);
				bookInfo.setAuthor(inputValues[1]);
				bookInfo.setPublisher(inputValues[2]);
				bookInfo.setPublishDate(inputValues[3]);
				bookInfo.setIsbn(inputValues[4]);
				bookInfo.setThumbnailUrl("null");

				// 各バリデーションチェックのメソッド呼び出し
				Boolean checkRequired = booksService.checkRequired(bookInfo);
				Boolean checkDateResult = booksService.checkDateValidation(bookInfo.getPublishDate());
				Boolean checkIsbnResult = booksService.checkIsbnDigits(bookInfo.getIsbn());

				List<String> errorMessages = new ArrayList<String>();

				lineCount++;

				if (checkRequired == true || (!(checkIsbnResult == true) && bookInfo.getIsbn().matches("^[0-9]+$"))
						|| !(checkDateResult == true && bookInfo.getPublishDate().matches("^[0-9]+$"))) {
					errorMessages.add(lineCount + "行目でバリデーションエラーが発生しました");
				}
				// エラーメッセージあればrender
				if (CollectionUtils.isEmpty(errorMessages)) {
					// 書籍情報を新規登録する
					booksService.registCsvBook(bookInfo);
					return "redirect:home";
				} else {
					model.addAttribute("errorMessages", errorMessages);
					return "bulkRegist";
				}
			}
			return "bulkRegist";
		} catch (Exception e) {
			throw new RuntimeException("ファイルが読み込めません", e);
			return "bulkRegist";
		}
	}
}
