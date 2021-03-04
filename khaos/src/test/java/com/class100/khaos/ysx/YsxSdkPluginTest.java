package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXMeetingStatus;
import com.class100.khaos.KhSdkAbility;

import org.junit.Assert;
import org.junit.Test;

public class YsxSdkPluginTest {
    @Test
    public void testConvertEnum() {
        YSXMeetingStatus ysxStatus = YSXMeetingStatus.MEETING_STATUS_DISCONNECTING;
        KhSdkAbility.KhMeetingStatus status = YsxSdkPlugin.convertEnum(ysxStatus, KhSdkAbility.KhMeetingStatus.class);
        Assert.assertEquals(status.value(), ysxStatus.value());
    }
}
