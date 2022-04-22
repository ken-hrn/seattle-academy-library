package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jdk.nashorn.internal.ir.ReturnNode;
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
				"select * from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * FROM books where id ="
				+ bookId;

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,thumbnail_name,thumbnail_url, publish_date, isbn, introduction, reg_date, upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','"
				+ bookInfo.getThumbnailUrl() + "','"
				+ bookInfo.getPublishDate() + "','"
				+ bookInfo.getIsbn() + "','"
				+ bookInfo.getIntroduction() + "',"
				+ "now(),"
				+ "now())";

		jdbcTemplate.update(sql);
	}

    // 本の削除
	public void deleteBook(int bookId) {

		String sql = "DELETE FROM books WHERE id = " + bookId;
		System.out.println(sql);

		jdbcTemplate.update(sql);
	}

	//ISBN桁数チェック 
	public boolean checkIsbnDigits(String isbn) {
		System.out.println(isbn);
		Boolean result = isbn.length() == 13 || isbn.length() == 10 ? true : false;
		return result;		
	}

	// publishDateバリデーションチェック
	public boolean checkDateValidation(String publishDate) {
		Boolean result = publishDate.length() == 8 ? true : false;
		return result;		
	}
    
  // booksテーブルの最後のidを取得
  public BookDetailsInfo getLastRecord() {
      String sql = "select * from books where id = (select max(id) from books)";
      
      BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
      // Integer bookDetailsInfo = jdbcTemplate.queryForObject(sql, Integer.class);
      return bookDetailsInfo;

  }
}
