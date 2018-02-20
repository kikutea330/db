/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.kikutea330.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DataSourceManagerクラスのテストクラス
 * @author kikut
 */
public class DataSourceManagerTest {
    private String dbPropFilePath;
    @Before
    public void setUp() throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("test");
        String dbPropFileRelativePath = rb.getString("datasourcePropertiesFilePath");
        String currentDirAbsolutePath = System.getProperty("user.dir");
        this.dbPropFilePath = currentDirAbsolutePath + "/" + dbPropFileRelativePath;
        //Test DataSourceManager.init(String path);
        System.out.println("test DataSourceManager.Init("+this.dbPropFilePath+")");
        try{
            DataSourceManager.init(this.dbPropFilePath);
        }catch(Exception e){
            fail("fail init DataSourceManager. "+e.getMessage());
        }
    }

    /**
     * Test of init method, of class DataSourceManager.
     */
    @Test
    public void testInit() throws Exception {
        System.out.println("test DataSourceManager.Init("+this.dbPropFilePath+") @ second time.");
        try{
            DataSourceManager.init(this.dbPropFilePath);
        }catch(Exception e){
            fail("fail init DataSourceManager. "+e.getMessage());
        }
    }

    /**
     * Test of getDataSource method, of class DataSourceManager.
     */
    @Test
    public void testGetDataSource() {
        System.out.println("test DataSourceManager.getInstance()");
        DataSource result = DataSourceManager.getDataSource();
        if(result==null){
            fail("result is null");
        }
    }

    /**
     * Test of getConnection method, of class DataSourceManager.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        try(Connection result = DataSourceManager.getConnection()){
            if(result == null){
                fail("failed. connection is null.");
            }
            try(PreparedStatement ps = result.prepareStatement("SELECT 1 \n")){
                try(ResultSet rs = ps.executeQuery()){
                    if(!rs.next()){
                        fail("Result Set is empty.");
                    }
                }
            }
        }catch(SQLException e){
            fail("getConnection is failed. "+e.getMessage());
        }
    }
    
}
