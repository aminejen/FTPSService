package com.ftps.model;

/**
 * Created by Amine Jendoubi on 04/10/2017.
 */
public class FTPSFailedTransferReport {
    private FTPSFileTransferDecorator fTPSFileTransferDecorator;
    private Exception exception;

    public FTPSFailedTransferReport() {

    }

    public FTPSFailedTransferReport(FTPSFileTransferDecorator fTPSFileTransferDecorator, Exception exception) {
        this.fTPSFileTransferDecorator = fTPSFileTransferDecorator;
        this.exception = exception;
    }

    public FTPSFileTransferDecorator getfTPSFileTransferDecorator() {
        return fTPSFileTransferDecorator;
    }

    public void setfTPSFileTransferDecorator(FTPSFileTransferDecorator fTPSFileTransferDecorator) {
        this.fTPSFileTransferDecorator = fTPSFileTransferDecorator;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "{\"FTPSFailedTransferReport\":{"
                + "\"fTPSFileTransferDecorator\":" + fTPSFileTransferDecorator
                + ", \"exception\":\"" + exception +"\""
                + "}}";
    }
}
