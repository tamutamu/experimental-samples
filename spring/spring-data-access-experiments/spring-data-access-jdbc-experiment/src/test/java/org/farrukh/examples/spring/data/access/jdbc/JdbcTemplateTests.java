package org.farrukh.examples.spring.data.access.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Test class for exercising JdbcTemplate methods.
 */
public class JdbcTemplateTests extends BaseTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Test
    public void testJdbcTemplate() {
        assertNotNull(jdbcTemplate);
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testQueryForObjectMethod() {
        String sql = "select count(*) from city";
        int countOfRecordsInCityTable = jdbcTemplate.queryForObject(sql, Integer.class);

        assertEquals(3, countOfRecordsInCityTable);
    }

    @Test
    public void testQueryForList() {
        String sql = "select * from city";
        List<Map<String, Object>> cities = jdbcTemplate.queryForList(sql);

        Assert.assertTrue(!cities.isEmpty());
    }
    
    @Test
    public void retrievingAutoGeneratedKeys() {
        String sql = "insert into city(name, countryCode, district, population) values(?, ?, ?, ?)";
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, "London");
            ps.setString(2, "En");
            ps.setString(3, "London");
            ps.setInt(4, 10000);
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        Assert.assertEquals(keyHolder.getKey(), 3);
    }

}
