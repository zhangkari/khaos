package com.class100.khaos.req;

public class KhReqGetMeetings extends KhReqMeeting {
    public long startTime;  // timestamp in milliseconds
    public long endTime;    // timestamp in milliseconds
    public int status;
    public int pageNo;
    public int pageSize;
}
