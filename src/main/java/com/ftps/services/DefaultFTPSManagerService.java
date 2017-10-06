package com.ftps.services;

import com.ftps.exceptions.FTPSFileTransferException;
import com.ftps.model.FTPSFileTransferDecorator;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;

/**
 * Created by Amine Jendoubi on 06/10/2017.
 */
public interface DefaultFTPSManagerService extends  FTPSManagerService{
    void downloadFile(FTPSFileTransferDecorator fTPSFileTransferDecorator, FTPSClient ftps) throws FTPConnectionClosedException, IOException, FTPSFileTransferException;
    void uploadFile(FTPSFileTransferDecorator fTPSFileTransferDecorator, FTPSClient ftps) throws FTPConnectionClosedException, IOException, FTPSFileTransferException;
}
