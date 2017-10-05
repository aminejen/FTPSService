package com.ftps.services;

import com.ftps.exceptions.FTPSFileTransferException;
import com.ftps.model.FTPSFileTransferBuilderImpl;
import com.ftps.model.FTPSFileTransferReport;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;

/**
 * Created by Amine Jendoubi on 04/10/2017.
 */
public interface FTPSManagerService {
    public FTPSClient getNewConnection() throws IOException;
    FTPSFileTransferReport transferFiles(FTPSFileTransferBuilderImpl fTPSFileTransferBuilderImpl) throws FTPSFileTransferException;
}
