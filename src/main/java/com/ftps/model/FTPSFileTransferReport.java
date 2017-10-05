package com.ftps.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Amine Jendoubi on 04/10/2017.
 */
public class FTPSFileTransferReport {

    private Date transferLaunchDate;
    private long spentTimeInMs;
    private List<FTPSFailedTransferReport> failedTransfers;

    public FTPSFileTransferReport() {
        this.failedTransfers = new ArrayList();
        this.transferLaunchDate = new Date();
    }

    public Date getTransferLaunchDate() {
        return transferLaunchDate;
    }

    public void setTransferLaunchDate(Date transferLaunchDate) {
        this.transferLaunchDate = transferLaunchDate;
    }

    public long getSpentTime() {
        return spentTimeInMs;
    }

    public void setSpentTime(long spentTime) {
        this.spentTimeInMs = spentTime;
    }

    public void endTransfer() {
        this.spentTimeInMs = new Date().getTime() - transferLaunchDate.getTime();
    }

    public boolean isTransferDoneWithoutErrors() {
        return this.failedTransfers.isEmpty();
    }

    public List<FTPSFailedTransferReport> getFailedTransfers() {
        return failedTransfers;
    }

    @Override
    public String toString() {
        return "{\"FTPSFileTransferReport\":{"
                + "\"transferLaunchDate\":" + transferLaunchDate
                + ", \"spentTimeInMs\":\"" + spentTimeInMs + "ms\""
                + ", \"failedTransfers\":" + failedTransfers
                + "}}";
    }
}
