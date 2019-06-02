package com.bmw.passbook.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;


public class HBaseUtil {
    private static final HBaseUtil INSTANCE = new HBaseUtil();
    private static Configuration configuration;
    private static Connection connection;

    static {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "master");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("log4j.logger.org.apache.hadoop.hbase", "WARN");
    }


    public static Admin getAdmin() {
        try {
            if(null ==connection){
               connection = getConnection();
            }
            Admin admin = connection.getAdmin();
            return admin;
        } catch (Exception e) {
            return null;
        }
    }

    public static Connection getConnection() {
        try {
            connection = ConnectionFactory.createConnection(configuration);
            return connection;
        } catch (Exception e) {
            return null;
        }
    }

    public static void createTable(String table, String... families) {
        TableName tableName = TableName.valueOf(table);
        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        for (String family : families) {
            tableDescriptor.addFamily(new HColumnDescriptor(family));
        }
        Admin admin = getAdmin();
        try {
            if (admin == null) {
                return;
            }
            if (admin.tableExists(tableName)) {
                return;
            }
            admin.createTable(tableDescriptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void add(String table, String rowkey,String family, HashMap<String, String> data) {
        Admin admin = getAdmin();
        if(admin==null){
            return;
        }
        TableName tableName = TableName.valueOf(table);
        try{
            Table table1 = admin.getConnection().getTable(TableName.valueOf(table));
            /*
            if(table1.exists(new Get(Bytes.toBytes(rowkey)))){
                return;
            }
            */
            HTableDescriptor tableDescriptor = table1.getTableDescriptor();
            HColumnDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
            Put put = new Put(Bytes.toBytes(rowkey));
            for(HColumnDescriptor columnFamily : columnFamilies){
                if(columnFamily.getNameAsString().equals(family)){
                    for(Entry<String,String> entry : data.entrySet()){
                        put.addColumn(Bytes.toBytes(family),Bytes.toBytes(entry.getKey()),Bytes.toBytes(entry.getValue()));
                    }
                    table1.put(put);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Result get(String tableName,String rowKey,String familyName,String column){
        try{
            Admin admin = getAdmin();
            if(admin==null){
                return Result.EMPTY_RESULT;
            }
            Table table = admin.getConnection().getTable(TableName.valueOf(tableName));
            Get get =new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(familyName),Bytes.toBytes(column));
            get.setMaxVersions(3);
            Result result=table.get(get);
            if(result.isEmpty()){
                return Result.EMPTY_RESULT;
            }
            return result;

        }catch (IOException e){
            e.printStackTrace();
            return Result.EMPTY_RESULT;
        }
    }

    public static ResultScanner scan(String tableName,String rowKey){
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(Bytes.toBytes(rowKey)));
        Admin admin = getAdmin();
        try {
            Table table = admin.getConnection().getTable(TableName.valueOf(tableName));
            ResultScanner results =  table.getScanner(scan);
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }












}

