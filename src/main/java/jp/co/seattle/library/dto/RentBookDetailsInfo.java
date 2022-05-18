package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 貸出書籍基本情報格納DTO
 */
@Configuration
@Data
public class RentBookDetailsInfo {

    private int bookId;

    private String title;

    private String checkoutDate;

    private String returnDate;

    public RentBookDetailsInfo() {

    }

}