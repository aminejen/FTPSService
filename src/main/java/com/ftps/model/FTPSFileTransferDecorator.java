package com.ftps.model;

import java.io.File;

/**
 * Created by Amine Jendoubi on 04/10/2017.
 */
public class FTPSFileTransferDecorator {
    public enum TransferType {
        DOWNLOAD, UPLOAD
    }

    public enum FileType{
        FOLDER,FILE
    }

    private static int DEFAULT_BUFFER_SIZE = 1000;
    private static boolean DEFAULT_STATE_OF_BINARY_TYPE = true;


    private File remoteFile;
    private File localFile;
    private int bufferSize;
    private boolean isBinaryTransfer;
    private TransferType transferType;
    private FileType fileType;

    public FTPSFileTransferDecorator() {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.isBinaryTransfer = DEFAULT_STATE_OF_BINARY_TYPE;
    }

    public FTPSFileTransferDecorator(File remoteFile, File localFile, TransferType transferType,FileType fileType) {
        this();
        this.remoteFile = remoteFile;
        this.localFile = localFile;
        this.transferType = transferType;
        this.fileType=fileType;
    }

    public FTPSFileTransferDecorator(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer, TransferType transferType ,FileType fileType) {
        this.remoteFile = remoteFile;
        this.localFile = localFile;
        this.bufferSize = bufferSize;
        this.isBinaryTransfer = isBinaryTransfer;
        this.transferType = transferType;
    }

    public File getRemoteFile() {
        return remoteFile;
    }

    public void setRemoteFile(File remoteFile) {
        this.remoteFile = remoteFile;
    }

    public File getLocalFile() {
        return localFile;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public boolean isBinaryTransfer() {
        return isBinaryTransfer;
    }

    public void setBinaryTransfer(boolean binaryTransfer) {
        isBinaryTransfer = binaryTransfer;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public boolean isValid() {
        return (this.remoteFile != null && this.localFile != null);
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "{\"FTPSFileTransferDecorator\":{"
                + "\"remoteFile\":\"" + remoteFile + "\""
                + ", \"localFile\":\"" + localFile + "\""
                + ", \"bufferSize\":\"" + bufferSize + "\""
                + ", \"isBinaryTransfer\":\"" + isBinaryTransfer + "\""
                + ", \"transferType\":\"" + transferType + "\""
                + "}}";
    }


}
