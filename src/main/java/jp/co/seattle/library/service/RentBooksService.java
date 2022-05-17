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
 * 貸出管理サービス
 *
 * rending_managesテーブルに関する処理を実装する
 */
@Service
public class RentBooksService {
  final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
  
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 貸出管理の本の情報を取得する
	 * @param bookId
	 * @return 書籍id
	 */
  public int getRentBookInfo(Integer bookId) {
    String sql = "SELECT COUNT(*) FROM rending_manages where book_id =" + bookId;
    int registId = jdbcTemplate.queryForObject(sql, Integer.class);
    return registId;
  }


	/**
	 * 貸出管理テーブルに本のidを登録する
	 * @param bookId
	 * @return 書籍id
	 */
	public void registBook(Integer bookId) {

		String sql = "INSERT INTO rending_manages (book_id, reg_date, upd_date) VALUES ('"
				+ bookId + "'," 
				+ "now(),"
        + "now())";

		jdbcTemplate.update(sql);
  }

  /**
	 * 貸出管理テーブルの本の情報を削除する
	 * @param bookId
	 * @return 書籍id
	 */
	public void deleteBook(Integer bookId) {

    String sql = "DELETE from rending_manages WHERE book_id = " + bookId;

		jdbcTemplate.update(sql);
	}
	
	/**
	 * 貸出管理テーブルの本の情報を削除する
	 * @param bookId
	 * @return 書籍id
	 */
	public int getRentStatus(Integer bookId) {
		String sql = "SELECT COUNT(rending_manages.book_id) FROM books LEFT OUTER JOIN rending_manages ON books.id = rending_manages.book_id WHERE rending_manages.book_id  = " + bookId;

		int existBookId = jdbcTemplate.queryForObject(sql, Integer.class);
    return existBookId;
	}
  
}
