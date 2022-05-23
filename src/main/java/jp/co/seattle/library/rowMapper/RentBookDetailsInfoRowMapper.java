package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.RentBookDetailsInfo;

@Configuration
public class RentBookDetailsInfoRowMapper implements RowMapper<RentBookDetailsInfo> {

    @Override
    public RentBookDetailsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        RentBookDetailsInfo rentBookDetailsInfo = new RentBookDetailsInfo();
        rentBookDetailsInfo.setBookId(rs.getInt("id"));
        rentBookDetailsInfo.setTitle(rs.getString("title"));
        rentBookDetailsInfo.setCheckoutDate(rs.getString("checkout_date"));
        rentBookDetailsInfo.setReturnDate(rs.getString("return_date"));
        return rentBookDetailsInfo;
    }
}