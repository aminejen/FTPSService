package com.ftps.test.unit_tests;

import com.ftps.services.DefaultFTPSManagerService;
import com.ftps.test.configurations.ApplicationTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Amine Jendoubi on 26/09/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class)

public class DefaultFTPSManagerServiceTest {
    @Autowired
    private DefaultFTPSManagerService fTPSManagerService;


    @Test
    public void fTPSManagerServiceTest() throws Exception{
        fTPSManagerService.uploadFile("/jen55.txt","C:\\FTPclient\\jen55.txt");
    }


}
