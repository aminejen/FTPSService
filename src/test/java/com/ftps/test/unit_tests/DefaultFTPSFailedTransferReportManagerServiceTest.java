package com.ftps.test.unit_tests;

import com.ftps.model.FTPSFileTransferBuilderImpl;
import com.ftps.model.FTPSFileTransferReport;
import com.ftps.model.Folder;
import com.ftps.services.DefaultFTPSManagerService;
import com.ftps.services.DefaultFTPSManagerServiceImpl;
import com.ftps.test.configurations.ApplicationTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * Created by Amine Jendoubi on 26/09/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTestConfig.class)

public class DefaultFTPSFailedTransferReportManagerServiceTest {

    public static String localFTPCLientFolder = "C:\\FTPclient";
    public static String remoteFTPRelativeFolder = "/destination";
    public static String remoteFTPAbsoluteFolderForTest = "C:\\FTP";
    public static String localFileName = "local.txt";
    public static String remoteFileName = "remote.txt";
    public static String fileContent = "Hello World";

    @Autowired
    @Qualifier("defaultFTPSManagerService")
    private DefaultFTPSManagerService defaultFTPSManagerService;



    @Test
    public void testUploadAndDownload() throws Exception {
        FTPSFileTransferBuilderImpl fTPSFileTransferBuilderImpl = new FTPSFileTransferBuilderImpl();
        fTPSFileTransferBuilderImpl.addFolderDownload(new Folder(remoteFTPRelativeFolder),new Folder(localFTPCLientFolder));
        FTPSFileTransferReport report = defaultFTPSManagerService.transferFiles(fTPSFileTransferBuilderImpl);
        System.out.println(report);
    }


    public void createRemoteTestFile() throws IOException {
        createTestFile(remoteFTPAbsoluteFolderForTest,remoteFileName);
    }


    public void createLocalTestFile() throws IOException {
        createTestFile(localFTPCLientFolder,localFileName);
    }


    public void clearRemoteFolder() throws IOException {
        clearFolder(remoteFTPAbsoluteFolderForTest);
    }


    public void clearLocalFolder() throws IOException {
        clearFolder(localFTPCLientFolder);
    }


    public void clearFolder( String folderPath) throws IOException {
        File remoteFolder = new File(folderPath);
        File[] remoteFiles = remoteFolder.listFiles();
        for (File file : remoteFiles){
            file.delete();
        }
    }

    public void createTestFile(String folderPath,String fileName) throws IOException{
        File file = new File(folderPath, fileName);
        FileWriter fw = new FileWriter(file);
        fw.write(fileContent);
        fw.close();
    }

}
