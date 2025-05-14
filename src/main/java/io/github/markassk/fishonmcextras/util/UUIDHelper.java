package io.github.markassk.fishonmcextras.util;

import java.util.UUID;

public class UUIDHelper {
    public static UUID getUUID(int[] idArray) {
        if(idArray.length == 4) {
            long mostSigBits = ((long) idArray[0] << 32) | (idArray[1] & 0xFFFFFFFFL);
            long leastSigBits = ((long) idArray[2] << 32) | (idArray[3] & 0xFFFFFFFFL);
            return new UUID(mostSigBits, leastSigBits);
        }
        return UUID.randomUUID();
    }
}
