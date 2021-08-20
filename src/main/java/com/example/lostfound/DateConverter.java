package com.example.lostfound;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author Zero
 * @Date 2021/8/19 22:47
 * @Since 1.8
 * @Description
 **/
@Component
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println(source);
            return sdf.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}