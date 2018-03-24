package org.lightning.quark.core.utils;

import com.google.common.primitives.Longs;

/**
 * Created by cook on 2018/3/13
 */
public class CdcUtils {

    private static final byte[] prefix = new byte[]{0x0, 0x2};

    /**
     * 0x0002 E27B00000DF00004
     *
     * 共10字节, 去除前缀
     * @param lsn
     * @return
     */
    public static long bytesToLong(byte[] lsn) {
        return Longs.fromBytes(lsn[2], lsn[3], lsn[4], lsn[5], lsn[6], lsn[7], lsn[8], lsn[9]);
    }

}
