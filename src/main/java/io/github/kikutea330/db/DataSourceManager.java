/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kikutea330.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * DataSourceManager
 * DataSourceの管理クラス（Singleton）
 * @author kikut
 */
public class DataSourceManager {
    private static final Logger LOG = Logger.getLogger(DataSourceManager.class.getName());
    /** ユニークなインスタンス */
    private volatile static DataSourceManager self;
    /** データソース */
    private DataSource ds;
    
    /**
     * 使用禁止
     */
    private DataSourceManager(){}
    
    /**
     * 非公開のコンストラクタ<br>
     * シングルトンとするため、インスタンス生成をクラス内部に限定します。
     * @param path - DataSourceを生成するためのプロパティファイルのパス
     * @throws IOException
     * @throws Exception 
     */
    private DataSourceManager(String path) throws IOException,Exception{
        File configFile = new File(path);
        Properties prop = new Properties();
        try(InputStream is = new FileInputStream(configFile)){
            prop.load(is);
            this.ds = BasicDataSourceFactory.createDataSource(prop);
        }catch(IOException ioe){
            LOG.log(Level.SEVERE,ioe.getMessage(),ioe);
            throw ioe;
        }catch(Exception e){
            LOG.log(Level.SEVERE,e.getMessage(),e);
            throw e;
        }
    }
    
    /**
     * DataSourceManagerの初期化メソッド
     * このクラスのインスタンスを利用するためには、必ず呼ばれている必要があります。
     * @param path - プロパティファイルのパス
     * @throws java.lang.Exception
     */
    public static void init(String path) throws Exception{
        if(self==null){
            synchronized (DataSourceManager.class){
                if(self == null){
                    try{
                        self = new DataSourceManager(path);
                    }catch(Exception e){
                        self = null;
                        throw e;
                    }
                }
            }
        }
    }
    
    /**
     * 自身のインスタンスを返すメソッド
     * 先にinit初期化されている必要があります
     * @return - DataSourceのインスタンス（初期化されていない場合null）
     */
    public static DataSource getDataSource(){
        return self.ds;
    }
    
    /**
     * DataSourceのコネクションプールからコネクションを取得するメソッド
     * @return 
     */
    public static Connection getConnection(){
        try {
            return self.ds.getConnection();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
