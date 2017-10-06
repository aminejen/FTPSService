package com.ftps.model;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import sun.awt.shell.ShellFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amine Jendoubi on 05/10/2017.
 */
public class FTPSFileTransferBuilderImpl implements FTPSFileTransferBuilder {
    private List<FTPSFileTransferDecorator> fileTransferDecorators;

    public FTPSFileTransferBuilderImpl() {
        this.fileTransferDecorators = new ArrayList();
    }

    @Override
    public void addFileDownload(File remoteFile, File localFile) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, FTPSFileTransferDecorator.TransferType.DOWNLOAD, FTPSFileTransferDecorator.FileType.FILE));
    }

    @Override
    public void addFileDownload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, bufferSize, isBinaryTransfer, FTPSFileTransferDecorator.TransferType.DOWNLOAD, FTPSFileTransferDecorator.FileType.FILE));
    }

    @Override
    public void addFileUpload(File remoteFile, File localFile) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, FTPSFileTransferDecorator.TransferType.UPLOAD, FTPSFileTransferDecorator.FileType.FILE));
    }

    @Override
    public void addFileUpload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, bufferSize, isBinaryTransfer, FTPSFileTransferDecorator.TransferType.UPLOAD , FTPSFileTransferDecorator.FileType.FILE));
    }

    @Override
    public void addFolderUpload(Folder remoteFolder, Folder localFolder) {
        doAddFolderUpload(remoteFolder, localFolder, 0, false, true);
    }

    @Override
    public void addFolderUpload(Folder remoteFolder, Folder localFolder, int bufferSize, boolean isBinaryTransfer) {
        doAddFolderUpload(remoteFolder, localFolder, bufferSize, isBinaryTransfer, false);
    }

    @Override
    public void addFolderDownload(Folder remoteFolder, Folder localFolder) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFolder, localFolder, FTPSFileTransferDecorator.TransferType.UPLOAD , FTPSFileTransferDecorator.FileType.FOLDER));
    }

    @Override
    public List<FTPSFileTransferDecorator> getFileTransferDecorators() {
        return fileTransferDecorators;
    }

    private void doAddFolderUpload(Folder remoteFolder, Folder localFolder, int bufferSize, boolean isBinaryTransfer, boolean neglectParams) {
        List<File> allFolderFiles = getAllFilesFromFolder(localFolder);
        for (File file : allFolderFiles) {
            String relativePath = file.getPath().split(localFolder.getName())[1];
            File remoteFileToCreate = new File(remoteFolder, relativePath);
            if (neglectParams) {
                this.addFileUpload(remoteFileToCreate, file);
            } else {
                this.addFileUpload(remoteFileToCreate, file, bufferSize, isBinaryTransfer);
            }
        }
    }


    private List<File> getAllFilesFromFolder(File file) {
        List<File> fileList = new ArrayList();
        File[] innerFiles = file.listFiles();
        for (File innerFile : innerFiles) {
            if (!innerFile.isDirectory()) {
                fileList.add(innerFile);
            } else {
                fileList.addAll(getAllFilesFromFolder(innerFile));
            }
        }
        return fileList;
    }

    @Override
    public void extractDownloadFolderFilesFromServerInTheSameBuilder(FTPSFileTransferBuilder fTPSFileTransferBuilder,FTPSClient ftps) throws IOException {
        List<FTPSFileTransferDecorator> extractedFoldersInBuilder = new ArrayList();
        FTPSFileTransferBuilder filesToExtractInBuilder = new FTPSFileTransferBuilderImpl();
        for (FTPSFileTransferDecorator fTPSFileTransferDecorator : fTPSFileTransferBuilder.getFileTransferDecorators()) {
            if (fTPSFileTransferDecorator.getFileType().equals(FTPSFileTransferDecorator.FileType.FOLDER)) {
                extractedFoldersInBuilder.add(fTPSFileTransferDecorator);
                FTPFile file = new FTPFile();
                file.setLink(fTPSFileTransferDecorator.getRemoteFile().getPath());
                List<File> remoteFileList = this.getFTPFilesFromFolder(file,ftps);
                for (File remoteFile : remoteFileList){
                    String relativePath = remoteFile.getPath().split(fTPSFileTransferDecorator.getRemoteFile().getName())[1];
                    File localFileToCreate = new File(fTPSFileTransferDecorator.getLocalFile(), relativePath);
                    filesToExtractInBuilder.addFileDownload(remoteFile,localFileToCreate);
                }
            }
        }
        fTPSFileTransferBuilder.getFileTransferDecorators().removeAll(extractedFoldersInBuilder);
        fTPSFileTransferBuilder.getFileTransferDecorators().addAll(filesToExtractInBuilder.getFileTransferDecorators());
    }

    private List<File> getFTPFilesFromFolder(FTPFile file, FTPSClient ftps) throws IOException {
        List<File> fileList = new ArrayList();
        FTPFile[] innerFiles = ftps.listFiles(file.getLink());
        for (FTPFile innerFile : innerFiles) {
            innerFile.setLink(file.getLink() + File.separator + innerFile.getName());
            if (!innerFile.isDirectory()) {
                fileList.add(new File(innerFile.getLink()));
            } else {
                fileList.addAll(getFTPFilesFromFolder(innerFile, ftps));
            }
        }
        return fileList;
    }



}

