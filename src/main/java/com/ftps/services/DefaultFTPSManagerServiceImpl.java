package com.ftps.services;

import com.ftps.exceptions.FTPSFileTransferException;
import com.ftps.model.*;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Amine Jendoubi on 03/10/2017.
 */
@Service("defaultFTPSManagerService")

public class DefaultFTPSManagerServiceImpl implements DefaultFTPSManagerService {


    private static String SECURITY_PROT_PRIVATE_TYPE = "P";

    private static Logger LOGGER = Logger.getLogger(DefaultFTPSManagerServiceImpl.class.getName());
    private String host;
    private String userName;
    private String password;
    private String protocol;
    @Autowired
    @Qualifier("defaultFTPSManagerService")
    private DefaultFTPSManagerService defaultFTPSManagerService;



    @Autowired
    public DefaultFTPSManagerServiceImpl(Environment env) {
        this.host = env.getRequiredProperty("ftps.server.host");
        this.userName = env.getRequiredProperty("ftps.server.username");
        this.password = env.getRequiredProperty("ftps.server.password");
        this.protocol = env.getRequiredProperty("ftps.server.protocol");

    }


    public FTPSClient getNewConnection() throws IOException {
        FTPSClient ftps = new FTPSClient(this.protocol);
        try {
            ftps.connect(this.host);
            int reply = ftps.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                if (ftps.login(userName, password)) {
                    ftps.execPROT(SECURITY_PROT_PRIVATE_TYPE);
                } else {
                    ftps.logout();
                }
            }
        } catch (IOException e) {
            if (ftps.isConnected()) {
                try {
                    ftps.disconnect();
                } catch (IOException f) {
                    LOGGER.severe(f.getMessage());
                }
            }
            throw e;
        }
        return ftps;
    }


    boolean isFilePresentOnRemote(FTPSClient ftps, String filePath) throws IOException {
        InputStream inputStream = ftps.retrieveFileStream(filePath);
        int returnCode = ftps.getReplyCode();
        if (inputStream == null || returnCode == 550) {
            inputStream.close();
            return false;
        }
        return true;
    }


    public FTPSFileTransferReport transferFiles(FTPSFileTransferBuilder fTPSFileTransferBuilder) throws FTPSFileTransferException {
        FTPSClient ftps = null;
        FTPSFileTransferReport report = new FTPSFileTransferReport();
        try {
            ftps = this.getNewConnection();
            fTPSFileTransferBuilder.extractDownloadFolderFilesFromServerInTheSameBuilder(fTPSFileTransferBuilder,ftps);
            ftps.enterLocalPassiveMode();
            List<FTPSFileTransferDecorator> fTPSFileTransferDecorators = fTPSFileTransferBuilder.getFileTransferDecorators();
            for (FTPSFileTransferDecorator fTPSFileTransferDecorator : fTPSFileTransferDecorators) {
                try {
                    if (fTPSFileTransferDecorator.isBinaryTransfer()) {
                        ftps.setFileType(FTP.BINARY_FILE_TYPE);
                    }
                    ftps.setBufferSize(fTPSFileTransferDecorator.getBufferSize());
                    if (fTPSFileTransferDecorator.getTransferType().equals(FTPSFileTransferDecorator.TransferType.UPLOAD)) {
                        uploadFile(fTPSFileTransferDecorator, ftps);
                    } else {

                        defaultFTPSManagerService.downloadFile(fTPSFileTransferDecorator, ftps);
                    }
                } catch (Exception ex) {
                    report.getFailedTransfers().add(new FTPSFailedTransferReport(fTPSFileTransferDecorator, ex));
                }
            }
            ftps.logout();
            report.endTransfer();
            return report;
        } catch (FTPConnectionClosedException exception) {
            throw new FTPSFileTransferException(exception);
        } catch (IOException exception) {
            throw new FTPSFileTransferException(exception);
        } finally {
            if (ftps != null && ftps.isConnected()) {
                try {
                    ftps.disconnect();
                } catch (IOException f) {
                    LOGGER.severe(f.getMessage());
                }
            }
        }
    }

    public void uploadFile(FTPSFileTransferDecorator fTPSFileTransferDecorator, FTPSClient ftps) throws FTPConnectionClosedException, IOException, FTPSFileTransferException {
        if (fTPSFileTransferDecorator.getLocalFile().length() > 0) {
            InputStream input;
            input = new FileInputStream(fTPSFileTransferDecorator.getLocalFile().getPath());
            ftps.makeDirectory(fTPSFileTransferDecorator.getRemoteFile().getParent());
            ftps.storeFile(fTPSFileTransferDecorator.getRemoteFile().getPath(), input);
            input.close();
        } else {
            throw new FTPSFileTransferException(FTPSFileTransferException.EMPTY_MESSAGE);
        }
    }

    public void downloadFile(FTPSFileTransferDecorator fTPSFileTransferDecorator, FTPSClient ftps) throws FTPConnectionClosedException, IOException, FTPSFileTransferException {
        OutputStream output;
        fTPSFileTransferDecorator.getLocalFile().getParentFile().mkdirs();
        output = new FileOutputStream(fTPSFileTransferDecorator.getLocalFile().getPath());
        ftps.retrieveFile(fTPSFileTransferDecorator.getRemoteFile().getPath(), output);
        output.close();
        if (fTPSFileTransferDecorator.getLocalFile().length() == 0) {
            fTPSFileTransferDecorator.getLocalFile().delete();
            throw new FTPSFileTransferException(FTPSFileTransferException.EMPTY_MESSAGE);
        }
    }



}
