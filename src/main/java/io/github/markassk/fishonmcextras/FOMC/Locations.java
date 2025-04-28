package io.github.markassk.fishonmcextras.FOMC;

//MAKE SURE : Location 1 < Location 2
public enum Locations {
    SPAWN(-53, -95, 294, 252),
    KENAI(298, -621, 661, -346),
    BIWA(1461, -785, 1640, -379),
    MURRAY(894, 598, 1102, 844),
    EVERGLADES(-1843, 184, -1563, 464),
    KEYWEST(-536, 2193, -214, 2556),
    TOLEDOBEND(-897, 1041, -383, 1495),
    GREATLAKES(-1096, 634, -840, 894),
    DANUBE(-1289, -258, -1130, -38),
    AMAZON(-1788, -298, -1509, 3),
    MEDITERRANEAN(-549, 395, -329, 670),
    CAPECOD(-197, -1296, 30, -1016),
    HAWAII(255, -1378, 526, -1122),
    CAIRNS(172, 2187, 448, 2380);

    public final int x1;
    public final int z1;
    public final int x2;
    public final int z2;

    Locations(int x1, int z1, int x2, int z2) {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }
}