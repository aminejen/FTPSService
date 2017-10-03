package com.ftps.services;

import com.ftps.customExceptions.FTPSFileTransferException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created by Amine Jendoubi on 03/10/2017.
 */
@Service
public class DefaultFTPSManagerService {

    private enum TransferType {
        DOWNLOAD, UPLOAD
    }

    private static int DEFAULT_BUFFER_SIZE = 1000;
    private static boolean DEFAULT_STATE_OF_BINARY_TYPE = true;

    private String host;
    private String userName;
    private String password;
    private String protocol;

    @Autowired
    public DefaultFTPSManagerService(Environment env) {
        this.host = env.getRequiredProperty("ftps.server.host");
        this.userName = env.getRequiredProperty("ftps.server.username");
        this.password = env.getRequiredProperty("ftps.server.password");
        this.protocol = env.getRequiredProperty("ftps.server.protocol");

    }

    protected FTPSClient getNewConnection() throws IOException {
        FTPSClient ftps = new FTPSClient(this.protocol);
        try {
            ftps.connect(this.host);
            int reply = ftps.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                if (ftps.login(userName, password)) {
                    // Set data channel protection to private
                    ftps.execPROT("P");
                } else {
                    ftps.logout();
                }
            }
        } catch (IOException e) {
            if (ftps.isConnected()) {
                try {
                    ftps.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            throw e;
        }
        return ftps;
    }

    protected void transferFile(String remotePath, String localPath, TransferType transfertType) throws FTPSFileTransferException {
        this.transferFile(remotePath, localPath, DEFAULT_STATE_OF_BINARY_TYPE, DEFAULT_BUFFER_SIZE, transfertType);
    }

    protected void transferFile(String remotePath, String localPath, boolean isBinaryTransferOn, int bufferSize, TransferType transfertType) throws FTPSFileTransferException {
        FTPSClient ftps = null;
        try {
            ftps = this.getNewConnection();
            if (isBinaryTransferOn) {
                ftps.setFileType(FTP.BINARY_FILE_TYPE);
            }
            ftps.setBufferSize(bufferSize);
            ftps.enterLocalPassiveMode();
            if (transfertType.equals(TransferType.UPLOAD)) {
                InputStream input;
                input = new FileInputStream(localPath);
                ftps.storeFile(remotePath, input);
                input.close();
            } else {
                OutputStream output;
                output = new FileOutputStream(localPath);
                ftps.retrieveFile(remotePath, output);
                output.close();
            }
            ftps.logout();
        } catch (FTPConnectionClosedException exception) {
            throw new FTPSFileTransferException(exception);
        } catch (IOException exception) {
            throw new FTPSFileTransferException(exception);
        } finally {
            if (ftps != null && ftps.isConnected()) {
                try {
                    ftps.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
    }

    public void downloadFile(String remotePath, String localPath) throws FTPSFileTransferException {
        transferFile(remotePath, localPath, TransferType.DOWNLOAD);
    }

    public void downloadFile(String remotePath, String localPath, boolean isBinaryTransferOn, int bufferSize, TransferType transfertType) throws FTPSFileTransferException {
        transferFile(remotePath, localPath, isBinaryTransferOn, bufferSize, TransferType.DOWNLOAD);
    }


    public void uploadFile(String remotePath, String localPath) throws FTPSFileTransferException {
        transferFile(remotePath, localPath, TransferType.UPLOAD);
    }

    public void uploadFile(String remotePath, String localPath, boolean isBinaryTransferOn, int bufferSize, TransferType transfertType) throws FTPSFileTransferException {
        transferFile(remotePath, localPath, isBinaryTransferOn, bufferSize, TransferType.UPLOAD);
    }

}
