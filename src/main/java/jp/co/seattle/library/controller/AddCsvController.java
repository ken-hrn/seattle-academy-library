package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
public class AddCsvController {
	final static Logger logger = LoggerFactory.getLogger(AddCsvController.class);

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

	@RequestMapping(value = "/addCsv", method = RequestMethod.GET) //value＝actionで指定したパラメータ
	//RequestParamでname属性を取得
	public String addCsv(Model model) {
		return "addCsv";
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
	@RequestMapping(value = "/insertCsv", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {

			String inputValue;
			while ((inputValue = br.readLine()) != null) {
				String[] inputValues = inputValue.split(",");

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(inputValues[0]);
				bookInfo.setAuthor(inputValues[1]);
				bookInfo.setPublisher(inputValues[2]);
				bookInfo.setPublishDate(inputValues[3]);
				bookInfo.setIsbn(inputValues[4]);
				bookInfo.setThumbnailUrl("null");
				
				System.out.println(Arrays.toString(inputValues));
				booksService.registCsvBook(bookInfo);
				
			}
		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}
		return "redirect:home";
	}

}
