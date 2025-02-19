package jp.co.seattle.library.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 *
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);                      
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id, title, author, publisher, publish_date, thumbnail_url from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報れ
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT books.id, title, author, publisher, thumbnail_name, thumbnail_url, publish_date, isbn, introduction, checkout_date, return_date, " +
									"CASE " +      
										"WHEN rending_manages.checkout_date is NULL THEN '貸出可能'" + 
										"ELSE '貸出中'" + 
									"END AS status " + 								
									"FROM books LEFT OUTER JOIN rending_manages ON books.id = rending_manages.book_id WHERE books.id  = " + bookId;
		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
		return bookDetailsInfo;
	}


	public boolean isCheckedOut(Integer bookId) {
		String sql = "SELECT EXISTS(SELECT * FROM rending_manages WHERE book_id = " + bookId + "AND checkout_date IS NOT null) AS is_lend";

		boolean isCheckedOut = jdbcTemplate.queryForObject(sql, Boolean.class);
		return isCheckedOut;
	}

	/**
	 * 書籍を登録する
	 * @param bookInfo 書籍情報
	 * @return bookId
	 **/
	public int registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,thumbnail_name,thumbnail_url, publish_date, isbn, introduction, reg_date, upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','"
				+ bookInfo.getThumbnailUrl() + "','"
				+ bookInfo.getPublishDate() + "','"
				+ bookInfo.getIsbn() + "','"
				+ bookInfo.getIntroduction() + "',"
				+ "now(),"
				+ "now()) RETURNING id";

		int bookId = jdbcTemplate.queryForObject(sql, Integer.class);
		return bookId;
	}

	/**
	* 書籍を一括登録する
	* @param bookInfo 書籍情報
	**/
	public void bulkRegist(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher, thumbnail_url, publish_date, isbn, reg_date, upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailUrl() + "','"
				+ bookInfo.getPublishDate() + "','"
				+ bookInfo.getIsbn() + "',"
				+ "now(),"
				+ "now())";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を更新する
	 *
	 * @param bookInfo 書籍情報
	 * @param id 書籍id
	**/
	public void updateBook(BookDetailsInfo bookInfo, int id) {

		String sql = "update books set title = " + "'" + bookInfo.getTitle() + "', "
				+ "author = '" + bookInfo.getAuthor() + "', "
				+ "publisher = '" + bookInfo.getPublisher() + "', "
				+ "thumbnail_name = '" + bookInfo.getThumbnailName() + "', "
				+ "thumbnail_url = '" + bookInfo.getThumbnailUrl() + "', "
				+ "publish_date = '" + bookInfo.getPublishDate() + "', "
				+ "isbn = '" + bookInfo.getIsbn() + "', "
				+ "introduction = '" + bookInfo.getIntroduction() + "' where id = " + id;

		jdbcTemplate.update(sql);
	}

	/**
	* 書籍IDに紐づく書籍情報を削除する
	*
	* @param bookId 書籍ID
	*/
	// 本の削除
	public void deleteBook(int bookId) {

		String sql = "DELETE FROM books WHERE id = " + bookId;
		jdbcTemplate.update(sql);
	}

	/**
	* 書籍情報を受け取り、入力値があるか検査する
	*
	* @param bookInfo 書籍情報
	* @return 真偽値
	*/
	// 必須項目チェック
	public boolean checkRequired(BookDetailsInfo bookInfo) {
		if (bookInfo.getTitle().isEmpty() || bookInfo.getAuthor().isEmpty() || bookInfo.getPublisher().isEmpty()
				|| bookInfo.getPublishDate().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * isbnを受け取り桁数を検査する
	 *
	 * @param isbn isbnの情報
	 * @return 真偽値
	 */
	//ISBN桁数チェック
	public boolean checkIsbnDigits(String isbn) {
		Boolean result = isbn.length() == 13 || isbn.length() == 10 || isbn.length() == 0 ? true : false;
		return result;
	}

	/**
	 * publishDateを受け取り桁数と日付を検査する
	 *
	 * @param isbn isbnの情報
	 * @return 真偽値
	 */
	// publishDateバリデーションチェック
	public boolean checkDateValidation(String publishDate) {
		Boolean strLength = publishDate.length() == 8 ? true : false;
		Boolean format;

		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(false); // これで厳密にチェックしてくれるようになる
			String s1 = publishDate;
			String s2 = df.format(df.parse(s1)); // ←df.parseでParseExceptionがThrowされる
			format = true;
		} catch (ParseException p) {
			p.printStackTrace();
			format = false;
		}

		if (strLength == true && format == true) {
			return true;
		} else {
			return false;
		}
	}

	// 一括登録バリデーションチェック
	public boolean checkBulkValidation(BookDetailsInfo bookInfo) {
		if (checkRequired(bookInfo) == true
				|| (checkIsbnDigits(bookInfo.getIsbn()) == false && bookInfo.getIsbn().matches("^[0-9]+$")) ||
				!(checkDateValidation(bookInfo.getPublishDate()) == true
						&& bookInfo.getPublishDate().matches("^[0-9]+$"))) {
			return true;
		} else {
			return false;
		}
	}

	public List<BookInfo> getSearchBookList(String searchCriteria, String searchWord) {
		if (searchCriteria.equals("perfectMatching")) {
			List<BookInfo> getedSearchBooks = jdbcTemplate.query(
				"SELECT * FROM books WHERE title LIKE '" + searchWord + "'", new BookInfoRowMapper());
			return getedSearchBooks;
		} else {
			System.out.println("部分一致");
			List<BookInfo> getedSearchBooks = jdbcTemplate.query(
				"SELECT * FROM books WHERE title LIKE '%" + searchWord + "%'", new BookInfoRowMapper());
			return getedSearchBooks;
		}
	}
}
