package com.pjh.jpadrill.common.config;

import java.util.Locale;

import org.hibernate.engine.jdbc.internal.FormatStyle;

import com.p6spy.engine.logging.Category;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpyFomatter implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,
            String sql, String url) {

        if (sql == null || sql.trim().isEmpty())
            return "";

        if (sql.contains("flyway_schema_history") || sql.contains("performance_schema")) {
            return "";
        }

        sql = formatSql(category, sql);

        return elapsed + "ms|" + category + "|" + sql;
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals(""))
            return sql;

        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}
