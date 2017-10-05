package com.ftps.test.unit_tests;

import com.ftps.model.FTPSFileTransferBuilderImpl;
import com.ftps.model.FTPSFileTransferReport;
import com.ftps.model.Folder;
import com.ftps.services.FTPSManagerService;
import com.ftps.test.configurations.ApplicationTestConfig;
import org.apache.commons.net.ftp.FTPSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    private FTPSManagerService fTPSManagerService;



    @Test
    public void testUploadAndDownload() throws Exception {
        createRemoteTestFile();
        createLocalTestFile();
        FTPSFileTransferBuilderImpl fTPSFileTransferBuilderImpl = new FTPSFileTransferBuilderImpl();
        fTPSFileTransferBuilderImpl.addFolderUpload(new Folder(localFTPCLientFolder));
        FTPSFileTransferReport report = fTPSManagerService.transferFiles(fTPSFileTransferBuilderImpl);
        System.out.println(report);
//        clearRemoteFolder();
//        clearLocalFolder();
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
























//   ---------------------------


    @Test
    public void testFTPS () throws IOException{
        FTPSClient ftps = this.fTPSManagerService.getNewConnection();
        ftps.makeDirectory("/lom/fqsd/fdqs2");
        ftps.disconnect();

    }

    @Test
    public void printFolderTest () throws IOException{
        File file = new File ("C:\\folder\\f3\\f5\\f6\\f7\\jen.txt");
        Path path = Paths.get(file.getPath());
        Files.createDirectories(path.getParent());
    }


    @Test
    public void printFolderTest1 () throws IOException{
        File file = new File ("C:\\folder\\f3\\f5\\f6\\f7\\jen.txt");
        Path path = Paths.get(file.getPath());
        Files.createDirectories(path.getParent());
    }

    @Test
    public void printFolderTest3 () throws IOException{
        System.out.println(new File("C:\\folder").getName());
        System.out.println(printFolder(new File("C:\\folder")));
    }

    public List<File> printFolder (File file){
        List<File> fileList = new ArrayList();
        File[] innerFiles = file.listFiles();
        for (File innerFile : innerFiles){
            if (!innerFile.isDirectory()){
                fileList.add(innerFile);
            }else{
                fileList.addAll(printFolder(innerFile));
            }
        }
        return fileList;
    }


}
