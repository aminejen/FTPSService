package com.ftps.exceptions;

/**
 * Created by Amine Jendoubi on 03/10/2017.
 */
public class FTPSFileTransferException extends Exception {

    public static String EMPTY_MESSAGE = "The file is empty";

    public FTPSFileTransferException(Throwable cause) {
        super(cause);
    }

    public FTPSFileTransferException(String message) {
        super(message);
    }
}


