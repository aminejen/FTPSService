package com.ftps.model;

import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Amine Jendoubi on 05/10/2017.
 */
public interface FTPSFileTransferBuilder {

    void addFileDownload(File remoteFile, File localFile);

    void addFileUpload(File remoteFile, File localFile);

    void addFolderUpload(Folder remoteFolder, Folder localFolder);

    void addFolderDownload(Folder remoteFolder, Folder localFolder);

    void addFileDownload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer);

    void addFileUpload(File remoteFile, File localFile, int bufferSize, boolean isBinaryTransfer);

    void addFolderUpload(Folder remoteFolder, Folder localFolder, int bufferSize, boolean isBinaryTransfer);

    List<FTPSFileTransferDecorator> getFileTransferDecorators();

    void extractDownloadFolderFilesFromServerInTheSameBuilder(FTPSFileTransferBuilder fTPSFileTransferBuilder,FTPSClient ftps) throws IOException;

}
