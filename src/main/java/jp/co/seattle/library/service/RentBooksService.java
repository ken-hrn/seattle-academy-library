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
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(RentBooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
  public int getRentBookInfo(Integer bookId) {
    String sql = "SELECT COUNT(*) FROM rending_manages where book_id =" + bookId;
    int registId = jdbcTemplate.queryForObject(sql, Integer.class);
    return registId;
  }


	public void registBook(Integer bookId) {

		String sql = "INSERT INTO rending_manages (book_id, reg_date, upd_date) VALUES ('"
				+ bookId + "'," 
				+ "now(),"
        + "now())";

		jdbcTemplate.update(sql);
  }
  
}
