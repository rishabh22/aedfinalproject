package edu.neu.javachip.aedfinalproject.model.workqueue;

import edu.neu.javachip.aedfinalproject.model.userAccount.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkQueue {
    private List<WorkRequest> workRequestList = new ArrayList<>();

    public List<WorkRequest> getAllWorkRequests() {
        return workRequestList;
    }

    public List<WorkRequest> getPendingWorkRequests() {
        return workRequestList.stream()
                .filter(workRequest -> workRequest.getStatus().getValue().equals(WorkRequest.WorkRequestStatus.PENDING.getValue()))
                .collect(Collectors.toList());
    }

    public List<WorkRequest> getRequestsByStatus(WorkRequest.WorkRequestStatus status) {
        return workRequestList.stream()
                .filter(workRequest -> workRequest.getStatus().getValue().equals(status.getValue()))
                .collect(Collectors.toList());
    }

    public List<WorkRequest> getSentRequests(UserAccount userAccount) {
        return workRequestList.stream()
                .filter(workRequest -> workRequest.getSender().equals(userAccount))
                .collect(Collectors.toList());
    }

    public void addWorkRequest(WorkRequest workRequest) {
        workRequest.setId(workRequestList.stream().mapToInt(WorkRequest::getId).max().orElse(0) + 1);
        workRequestList.add(workRequest);
    }

    public void cancelWorkRequest(WorkRequest workRequest) {
        workRequest.setStatus(WorkRequest.WorkRequestStatus.CANCELLED);
    }



}
