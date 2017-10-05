package com.ftps.model;

import sun.awt.shell.ShellFolder;

import java.io.File;
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
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, FTPSFileTransferDecorator.TransferType.DOWNLOAD));
    }

    @Override
    public void addFileDownload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, bufferSize, isBinaryTransfer, FTPSFileTransferDecorator.TransferType.DOWNLOAD));
    }

    @Override
    public void addFileUpload(File remoteFile, File localFile) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, FTPSFileTransferDecorator.TransferType.UPLOAD));
    }

    @Override
    public void addFileUpload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer) {
        fileTransferDecorators.add(new FTPSFileTransferDecorator(remoteFile, localFile, bufferSize, isBinaryTransfer, FTPSFileTransferDecorator.TransferType.UPLOAD));
    }

    @Override
    public void addFolderUpload(Folder remoteFolder, Folder localFolder) {
        doAddFolderUpload(remoteFolder, localFolder, 0, false, false);
    }

    @Override
    public void addFolderUpload(Folder remoteFolder, Folder localFolder, int bufferSize, boolean isBinaryTransfer) {
        doAddFolderUpload(remoteFolder, localFolder, bufferSize, isBinaryTransfer, false);
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


}

