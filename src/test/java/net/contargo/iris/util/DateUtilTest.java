package net.contargo.iris.util;

import org.junit.Test;

import java.time.LocalDate;

import java.util.Date;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class DateUtilTest {

    @Test
    public void asLocalDate() {

        LocalDate localDate = DateUtil.asLocalDate(new Date());

        assertThat(localDate, is(LocalDate.now()));
    }


    @Test
    public void asLocalDateFromSql() {

        LocalDate localDate = DateUtil.asLocalDate(new java.sql.Date(new Date().getTime()));

        assertThat(localDate, is(LocalDate.now()));
    }
}
