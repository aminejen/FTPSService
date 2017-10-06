package com.ftps.aspects;

import com.ftps.model.FTPSFileTransferDecorator;
import org.apache.log4j.Logger;

/**
 * Created by Amine Jendoubi on 06/10/2017.
 */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FTPSLoggingAspect {
    Logger logger = Logger.getLogger("FTPS File Transfer");
    boolean isLogEnabled;

    @Autowired
    public FTPSLoggingAspect(Environment env) {
        this.isLogEnabled = Boolean.valueOf(env.getRequiredProperty("ftps.log.enabled"));


    }

    @Pointcut("execution (* com.ftps.services.DefaultFTPSManagerServiceImpl.downloadFile(..)) || execution (* com.ftps.services.DefaultFTPSManagerServiceImpl.uploadFile(..)))")
    public void selectDownloadMethod() {
    }

    @Before("selectDownloadMethod()")
    public void logFileTransfert(JoinPoint joinPoint){
        if (!isLogEnabled){
            return;
        }
        FTPSFileTransferDecorator fTPSFileTransferDecorator = (FTPSFileTransferDecorator)joinPoint.getArgs()[0];
        StringBuilder log = new StringBuilder(fTPSFileTransferDecorator.getTransferType().toString());
        log.append(" ");
        if (fTPSFileTransferDecorator.getTransferType().equals(FTPSFileTransferDecorator.TransferType.DOWNLOAD)) {
            log.append(fTPSFileTransferDecorator.getRemoteFile());
            log.append(" --->  ");
            log.append(fTPSFileTransferDecorator.getLocalFile());
        }else{
            log.append(fTPSFileTransferDecorator.getLocalFile());
            log.append(" --->  ");
            log.append(fTPSFileTransferDecorator.getRemoteFile());
        }
        logger.info(log);
    }
}
