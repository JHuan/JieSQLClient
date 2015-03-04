package com.huan.JieSQL.model;

/**
 * Created by barryjiang on 2015/3/3.
 */
public class SQLTemplate {

    public final static String DB_NAME = "SQLTemplate";
    public final static String DB_LAST_INDEX = "last_index";    //record the index of last time used template
    public final static String DB_TEMPLATE_NAME = "template_name";

    public final static String DEAFAULT_TEMPLATE = "select * from #";
    public final static char   TEMPLATE_KEY = '#';
    public final static String DEAFAULT_SLOT_TEXT = "       ";
}
