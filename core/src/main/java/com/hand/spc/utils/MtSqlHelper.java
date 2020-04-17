package com.hand.spc.utils;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.hand.hap.core.ILanguageProvider;
import com.hand.hap.core.ITlTableNameProvider;
import com.hand.hap.core.annotation.MultiLanguage;
import com.hand.hap.core.components.ApplicationContextHelper;
import com.hand.hap.core.impl.DefaultTlTableNameProvider;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.mybatis.common.Criteria;
import com.hand.hap.mybatis.entity.EntityColumn;
import com.hand.hap.mybatis.entity.EntityField;
import com.hand.hap.mybatis.entity.EntityTable;
import com.hand.hap.mybatis.mapperhelper.EntityHelper;
import com.hand.hap.mybatis.mapperhelper.SqlHelper;
import com.hand.hap.system.dto.BaseDTO;
import com.hand.hap.system.dto.DTOClassInfo;
import com.hand.hap.system.dto.Language;
import com.hand.spc.repository.dto.MultiLanguageVO;

public class MtSqlHelper {

    private static String dbType = null;

    static {
        InputStream in = null;
        try {
            in = MtSqlHelper.class.getClassLoader().getResourceAsStream("config.properties");
            Properties props = new Properties();
            props.load(in);
            dbType = props.getProperty("db.type");
        } catch (IOException e) {
            dbType = "";
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static List<String> getDeleteSql(BaseDTO dto) {
        final StringBuilder sql = new StringBuilder();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        EntityColumn pkColumn = null;

        sql.append("DELETE FROM ").append(entityTable.getName()).append(" ").append("WHERE").append(" ");

        for (EntityColumn column : columnList) {
            if (column.isId()) {
                pkColumn = column;
                break;
            }
        }

        if (pkColumn == null) {
            return Collections.emptyList();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Object obj = PropertyUtils.getProperty(dto, pkColumn.getProperty());
            if (obj != null) {
                if (obj instanceof String) {
                    sql.append(pkColumn.getColumn()).append("=").append("'").append(obj.toString()).append("'");
                } else if (obj instanceof Date) {
                    if (dbType.equals("oracle")) {
                        sql.append(pkColumn.getColumn()).append("=").append("to_date(").append("'")
                                        .append(format.format(obj)).append("','yyyy-MM-dd hh24:mi:ss')");
                    } else {
                        sql.append(pkColumn.getColumn()).append("=").append("'").append(format.format(obj)).append("'");
                    }
                } else {
                    sql.append(pkColumn.getColumn()).append("=").append(obj);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        }

        sqlList.add(sql.toString());

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            ITlTableNameProvider tableNameProvider = DefaultTlTableNameProvider.getInstance();
            String tableName = tableNameProvider.getTlTableName(entityTable.getName());
            StringBuilder languageSql = new StringBuilder("DELETE FROM " + tableName + " WHERE ");
            StringBuilder keySql = new StringBuilder();

            for (EntityField field : DTOClassInfo.getIdFields(entityClass)) {
                try {
                    Object pkObj = PropertyUtils.getProperty(dto, field.getName());
                    if (pkObj != null) {
                        if (pkObj instanceof String) {
                            keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("'")
                                            .append(pkObj.toString()).append("'").append(" AND ");
                        } else if (pkObj instanceof Date) {
                            if (dbType.equals("oracle")) {
                                keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("to_date(")
                                                .append("'").append(format.format(pkObj))
                                                .append("','yyyy-MM-dd hh24:mi:ss')").append(" AND ");

                            } else {
                                keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("'")
                                                .append(format.format(pkObj)).append("'").append(" AND ");
                            }
                        } else {
                            keySql.append(DTOClassInfo.getColumnName(field)).append("=").append(pkObj).append(" AND ");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            if (keySql.length() != 0) {
                languageSql.append(keySql);
                languageSql.delete(languageSql.length() - " AND ".length(), languageSql.length() - 1);
                sqlList.add(languageSql.toString());
            }
        }
        return sqlList;
    }

    public static List<String> getUpdateSql(BaseDTO dto) {
        final StringBuilder sql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");

        for (EntityColumn column : columnList) {
            if (column.isId()) {
                pkColumns.add(column);
                continue;
            }
            if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
                if (dbType.equals("oracle")) {
                    sql.append(column.getColumn()).append("=").append("SYSDATE,");
                } else {
                    sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
                }
                continue;
            }
            if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
                continue;
            }

            if (column.isUpdatable()) {
                try {
                    Object value = PropertyUtils.getProperty(dto, column.getProperty());
                    if (value != null) {
                        if (value instanceof String) {
                            sql.append(column.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                            .append(",");
                        } else if (value instanceof Date) {
                            if (dbType.equals("oracle")) {
                                sql.append(column.getColumn()).append("=").append("to_date(").append("'")
                                                .append(format.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                                .append(",");
                            } else {
                                sql.append(column.getColumn()).append("=").append("'").append(format.format(value))
                                                .append("'").append(",");
                            }
                        } else {
                            sql.append(column.getColumn()).append("=").append(value).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                }
            }
        }

        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");

        if (CollectionUtils.isEmpty(pkColumns)) {
            return Collections.emptyList();
        }

        pkColumns.stream().forEach(t -> {
            try {
                Object value = PropertyUtils.getProperty(dto, t.getProperty());
                if (value == null) {
                    sql.append(t.getColumn()).append("=").append("''").append(" AND ");
                } else {
                    if (value instanceof String) {
                        sql.append(t.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(" AND ");
                    } else if (value instanceof Date) {
                        if (dbType.equals("oracle")) {
                            sql.append(t.getColumn()).append("=").append("to_date(").append("'")
                                            .append(format.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                            .append(" AND ");
                        } else {
                            sql.append(t.getColumn()).append("=").append("'").append(format.format(value)).append("'")
                                            .append(" AND ");
                        }
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, Collections.emptyList()));

        return sqlList;
    }

    public static List<String> getUpdateOptionsSql(BaseDTO dto, Criteria criteria) {
        List<String> updateFields = null;
        if (criteria == null || CollectionUtils.isEmpty(criteria.getUpdateFields())) {
            return Collections.emptyList();
        }

        updateFields = criteria.getUpdateFields().stream().distinct().collect(toList());

        final StringBuilder sql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EntityColumn> pkColumns = new ArrayList<EntityColumn>();
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        sql.append("UPDATE ").append(entityTable.getName()).append(" ").append("set").append(" ");

        for (EntityColumn column : columnList) {
            if (column.isId()) {
                pkColumns.add(column);
                continue;
            }
            if ("last_update_date".equalsIgnoreCase(column.getColumn())) {
                if (dbType.equals("oracle")) {
                    sql.append(column.getColumn()).append("=").append("SYSDATE,");
                } else {
                    sql.append(column.getColumn()).append("=").append("CURRENT_TIMESTAMP,");
                }
                continue;
            }
            if ("object_version_number".equalsIgnoreCase(column.getColumn())) {
                sql.append(column.getColumn()).append("=").append(column.getColumn()).append("+1,");
                continue;
            }

            if (column.isUpdatable() && updateFields.stream().anyMatch(t -> t.equalsIgnoreCase(column.getProperty()))) {
                try {
                    Object obj = PropertyUtils.getProperty(dto, column.getProperty());
                    if (obj == null) {
                        sql.append(column.getColumn()).append("=").append("NULL,");
                    } else {
                        if (obj instanceof String) {
                            sql.append(column.getColumn()).append("=").append("'").append(obj.toString()).append("'")
                                            .append(",");
                        } else if (obj instanceof Date) {
                            if (dbType.equals("oracle")) {
                                sql.append(column.getColumn()).append("=").append("to_date(").append("'")
                                                .append(format.format(obj)).append("','yyyy-MM-dd hh24:mi:ss')")
                                                .append(",");
                            } else {
                                sql.append(column.getColumn()).append("=").append("'").append(format.format(obj))
                                                .append("'").append(",");
                            }
                        } else {
                            sql.append(column.getColumn()).append("=").append(obj).append(",");
                        }
                    }

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                }
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");

        if (CollectionUtils.isEmpty(pkColumns)) {
            return Collections.emptyList();
        }

        pkColumns.stream().forEach(t -> {
            try {
                Object value = PropertyUtils.getProperty(dto, t.getProperty());
                if (value == null) {
                    sql.append(t.getColumn()).append("=").append("''").append(" AND ");
                } else {
                    if (value instanceof String) {
                        sql.append(t.getColumn()).append("=").append("'").append(value.toString()).append("'")
                                        .append(" AND ");
                    } else if (value instanceof Date) {
                        if (dbType.equals("oracle")) {
                            sql.append(t.getColumn()).append("=").append("to_date(").append("'")
                                            .append(format.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                            .append(" AND ");
                        } else {
                            sql.append(t.getColumn()).append("=").append("'").append(format.format(value)).append("'")
                                            .append(" AND ");
                        }
                    } else {
                        sql.append(t.getColumn()).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            }
        });

        sqlList.add(sql.substring(0, sql.length() - " AND ".length()));
        sqlList.addAll(getUpdateMultiLanguage(dto, updateFields));

        return sqlList;
    }

    private static List<String> getUpdateMultiLanguage(BaseDTO dto, List<String> updateFields) {
        Class<?> entityClass = dto.getClass();
        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        EntityField[] fields = DTOClassInfo.getMultiLanguageFields(entityClass);
        if (multiLanguageTable == null || ArrayUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }

        List<String> sqlList = new ArrayList<String>();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        ITlTableNameProvider tableNameProvider = DefaultTlTableNameProvider.getInstance();
        String tableName = tableNameProvider.getTlTableName(entityTable.getName());

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final StringBuilder firstSql = new StringBuilder("UPDATE " + tableName + " SET ");
        final StringBuilder setSql = new StringBuilder();
        final StringBuilder keySql = new StringBuilder();

        // 获取主键拼接信息
        for (EntityField field : DTOClassInfo.getIdFields(entityClass)) {
            try {
                Object value = PropertyUtils.getProperty(dto, field.getName());
                if (value != null) {
                    if (value instanceof String) {
                        keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("'")
                                        .append(value.toString()).append("'").append(" AND ");
                    } else if (value instanceof Date) {
                        if (dbType.equals("oracle")) {
                            keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("to_date(").append("'")
                                            .append(format.format(value)).append("','yyyy-MM-dd hh24:mi:ss')")
                                            .append(" AND ");
                        } else {
                            keySql.append(DTOClassInfo.getColumnName(field)).append("=").append("'")
                                            .append(format.format(value)).append("'").append(" AND ");
                        }
                    } else {
                        keySql.append(DTOClassInfo.getColumnName(field)).append("=").append(value).append(" AND ");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            }
        }

        if (keySql.length() == 0) {
            return Collections.emptyList();
        }

        // 没有特殊备注TLS信息时，默认只更新当前语言环境的数据
        if (null == dto.get__tls() || dto.get__tls().isEmpty()) {
            for (EntityField field : fields) {
                if (CollectionUtils.isNotEmpty(updateFields) && !updateFields.contains(field.getName())) {
                    continue;
                }

                try {
                    Object value = PropertyUtils.getProperty(dto, field.getName());
                    if (value != null) {
                        setSql.append(DTOClassInfo.getColumnName(field)).append("=").append("'").append(value)
                                        .append("'").append(",");
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                }
            }

            if (setSql.length() == 0) {
                return Collections.emptyList();
            }

            setSql.append("LAST_UPDATED_BY=").append(dto.getLastUpdatedBy()).append(",");
            if (dbType.equals("oracle")) {
                setSql.append("LAST_UPDATE_DATE=SYSDATE");
            } else {
                setSql.append("LAST_UPDATE_DATE=CURRENT_TIMESTAMP");
            }
            setSql.append(" WHERE ");
            setSql.append("LANG='").append(RequestHelper.getCurrentRequest(true).getLocale()).append("' AND ");
            // 拼接主键信息
            sqlList.add(String.valueOf(firstSql) + setSql + keySql.substring(0, keySql.length() - " AND ".length()));
        }
        // 特殊备注TLS信息时，根据TLS信息进行多语言环境的数据修改
        else {
            List<MultiLanguageVO> infos = new ArrayList<MultiLanguageVO>();
            for (Map.Entry<String, Map<String, String>> columns : dto.get__tls().entrySet()) {
                for (Map.Entry<String, String> tls : columns.getValue().entrySet()) {
                    // 获取需要修改的语言环境
                    infos.add(new MultiLanguageVO(DTOClassInfo.camelToUnderLine(columns.getKey()), tls.getKey(),
                                    tls.getValue()));
                }
            }
            List<String> langs = infos.stream().map(MultiLanguageVO::getLang).distinct().collect(toList());
            // 拼接不同语言环境的数据
            for (String lang : langs) {
                // 获取不同语言环境的数据
                List<MultiLanguageVO> langInfo = infos.stream().filter(t -> lang.equals(t.getLang())).collect(toList());
                // 拼接修改的值
                setSql.delete(0, setSql.length());
                for (MultiLanguageVO ever : langInfo) {
                    setSql.append(ever.getColoumName()).append("=").append("'").append(ever.getValues()).append("'")
                                    .append(",");
                }
                setSql.append("LAST_UPDATED_BY=").append(dto.getLastUpdatedBy()).append(",");
                if (dbType.equals("oracle")) {
                    setSql.append("LAST_UPDATE_DATE=SYSDATE");
                } else {
                    setSql.append("LAST_UPDATE_DATE=CURRENT_TIMESTAMP");
                }
                setSql.append(" WHERE ");
                setSql.append("LANG='").append(lang).append("' AND ");
                // 拼接主键信息
                sqlList.add(String.valueOf(firstSql) + setSql
                                + keySql.substring(0, keySql.length() - " AND ".length()));
            }
        }
        return sqlList;
    }

    public static List<String> getInsertSql(BaseDTO dto) {
        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        // Table Name
        sql.append(SqlHelper.insertIntoTable(entityClass, entityTable.getName())).append("(");

        // Table Column&Value
        columnList.stream().forEach(t -> {
            if (t.isInsertable()) {
                try {
                    Object obj = PropertyUtils.getProperty(dto, t.getProperty());
                    if (obj != null) {
                        columnSql.append(t.getColumn()).append(",");
                        if (obj instanceof String) {
                            valueSql.append("'").append(obj.toString()).append("'").append(",");
                        } else if (obj instanceof Date) {
                            if (dbType.equals("oracle")) {
                                valueSql.append("to_date(").append("'").append(format.format(obj))
                                                .append("','yyyy-MM-dd hh24:mi:ss')").append(",");
                            } else {
                                valueSql.append("'").append(format.format(obj)).append("'").append(",");
                            }
                        } else {
                            valueSql.append(obj).append(",");
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                }
            }
        });

        columnSql.deleteCharAt(columnSql.length() - 1);
        valueSql.deleteCharAt(valueSql.length() - 1);
        sql.append(columnSql).append(") ").append("VALUES(").append(valueSql).append(")");
        sqlList.add(sql.toString());

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            ILanguageProvider languageProvider =
                            ApplicationContextHelper.getApplicationContext().getBean(ILanguageProvider.class);
            List<String> keys = new ArrayList<String>();
            List<Object> objs = new ArrayList<Object>();

            ITlTableNameProvider tableNameProvider = DefaultTlTableNameProvider.getInstance();
            String tableName = tableNameProvider.getTlTableName(entityTable.getName());

            StringBuilder languageSql = new StringBuilder("INSERT INTO " + tableName + "(");
            for (EntityField f : DTOClassInfo.getIdFields(entityClass)) {
                String columnName = DTOClassInfo.getColumnName(f);
                keys.add(columnName);
                try {
                    objs.add(PropertyUtils.getProperty(dto, f.getName()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            keys.add("LANG");
            // 占位符
            objs.add(null);

            EntityField[] mlFields = DTOClassInfo.getMultiLanguageFields(entityClass);
            for (EntityField f : mlFields) {
                keys.add(DTOClassInfo.getColumnName(f));
                Map<String, String> tls = dto.get__tls().get(f.getName());
                if (tls == null) {
                    /**
                     * if multi language value not exists in __tls, then use value on current field
                     */
                    try {
                        objs.add(PropertyUtils.getProperty(dto, f.getName()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                // 占位符
                objs.add(null);
            }

            keys.add("CREATED_BY");
            keys.add("CREATION_DATE");
            keys.add("LAST_UPDATED_BY");
            keys.add("LAST_UPDATE_DATE");

            languageSql.append(StringUtils.join(keys, ","));
            languageSql.append(") VALUES (");

            List<Language> languages = languageProvider.getSupportedLanguages();
            for (Language language : languages) {
                objs.set(objs.size() - mlFields.length - 1, language.getLangCode());
                for (int i = 0; i < mlFields.length; i++) {
                    int idx = objs.size() - mlFields.length + i;
                    Map<String, String> tls = dto.get__tls().get(mlFields[i].getName());
                    if (tls != null) {
                        objs.set(idx, tls.get(language.getLangCode()));
                    }
                    // 当tls为null时,不设置值(使用field的值,旧模式)
                }

                StringBuilder tmpSql = new StringBuilder(languageSql);
                for (Object obj : objs) {
                    if (obj == null) {
                        tmpSql.append("''").append(",");
                    } else {
                        tmpSql.append("'").append(obj).append("'").append(",");
                    }

                }

                tmpSql.append("" + dto.getCreatedBy()).append(",");
                if (dbType.equals("oracle")) {
                    tmpSql.append("SYSDATE").append(",");
                } else {
                    tmpSql.append("CURRENT_TIMESTAMP").append(",");
                }
                tmpSql.append("" + dto.getCreatedBy()).append(",");
                if (dbType.equals("oracle")) {
                    tmpSql.append("SYSDATE");
                } else {
                    tmpSql.append("CURRENT_TIMESTAMP");
                }
                tmpSql.append(")");
                sqlList.add(tmpSql.toString());
            }
        }
        return sqlList;
    }

    public static String getDbType(){
        return dbType;
    }
}
