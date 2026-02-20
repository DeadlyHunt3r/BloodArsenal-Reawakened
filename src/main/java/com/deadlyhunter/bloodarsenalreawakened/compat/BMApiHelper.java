package com.deadlyhunter.bloodarsenalreawakened.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Blood Magic LP Helper ohne Compile-Time-API-Imports (Reflection).
 * Unterstützt mehrere Methodennamen/Signaturen:
 *  - NetworkHelper#getSoulNetwork(Player)
 *  - SoulNetwork#getCurrentEssence()
 *  - SoulNetwork#syphon(Player,int) | syphonAndDamage(Player,int) | syphon(int)
 */
public final class BMApiHelper {
    private static final String BM_MODID = "bloodmagic";

    private static boolean triedInit = false;
    private static boolean available = false;

    private static Class<?> networkHelperCls;
    private static Class<?> soulNetworkCls;

    private static Method mGetSoulNetwork;     // static getSoulNetwork(Player)
    private static Method mGetCurrentEssence;  // getCurrentEssence()
    private static Method mSyphonPlayerInt;    // syphon..(Player,int)
    private static Method mSyphonInt;          // syphon..(int)

    private BMApiHelper() {}

    /** Nur: ist das BloodMagic-Mod geladen? */
    public static boolean isPresent() {
        return ModList.get().isLoaded(BM_MODID);
    }

    private static boolean init() {
        if (triedInit) return available;
        triedInit = true;

        if (!isPresent()) {
            available = false;
            return false;
        }
        try {
            // Häufige FQNs für 1.20.x:
            String[] helperCandidates = {
                "wayoftime.bloodmagic.api.helper.NetworkHelper",
                "wayoftime.bloodmagic.common.util.helper.NetworkHelper",
                "wayoftime.bloodmagic.common.helper.NetworkHelper"
            };
            String[] networkCandidates = {
                "wayoftime.bloodmagic.api.compat.SoulNetwork",
                "wayoftime.bloodmagic.common.soul.SoulNetwork"
            };

            networkHelperCls = tryLoadAny(helperCandidates);
            soulNetworkCls   = tryLoadAny(networkCandidates);

            if (networkHelperCls == null || soulNetworkCls == null) {
                available = false;
                return false;
            }

            mGetSoulNetwork = findMethod(networkHelperCls, "getSoulNetwork", Player.class);
            if (mGetSoulNetwork == null) { available = false; return false; }

            // getCurrentEssence() (oder ein 0-arg Number-Getter mit "essence"/"lp"/"current" im Namen)
            try {
                mGetCurrentEssence = soulNetworkCls.getMethod("getCurrentEssence");
            } catch (NoSuchMethodException e) {
                mGetCurrentEssence = Arrays.stream(soulNetworkCls.getMethods())
                        .filter(m -> m.getParameterCount() == 0 &&
                                (m.getReturnType() == int.class || Number.class.isAssignableFrom(m.getReturnType())) &&
                                nameHasAny(m.getName(), "essence", "lp", "current"))
                        .findFirst().orElse(null);
            }

            // syphon(Player,int) | syphonAndDamage(Player,int)
            mSyphonPlayerInt = findMethod(soulNetworkCls, "syphon", Player.class, int.class);
            if (mSyphonPlayerInt == null)
                mSyphonPlayerInt = findMethod(soulNetworkCls, "syphonAndDamage", Player.class, int.class);

            // syphon(int)
            mSyphonInt = findMethod(soulNetworkCls, "syphon", int.class);

            available = (mGetSoulNetwork != null && mGetCurrentEssence != null && (mSyphonPlayerInt != null || mSyphonInt != null));
        } catch (Throwable t) {
            available = false;
        }
        return available;
    }

    private static boolean nameHasAny(String name, String... keys) {
        String n = name.toLowerCase();
        for (String k : keys) if (n.contains(k)) return true;
        return false;
    }

    private static Class<?> tryLoadAny(String[] fqns) {
        for (String f : fqns) {
            try { return Class.forName(f); } catch (ClassNotFoundException ignored) {}
        }
        return null;
    }

    private static Method findMethod(Class<?> cls, String name, Class<?>... params) {
        try { return cls.getMethod(name, params); } catch (NoSuchMethodException e) { return null; }
    }

    private static Object getNetwork(Player player) throws Exception {
        if (!init()) return null;
        return mGetSoulNetwork.invoke(null, player);
    }

    /** Hat der Spieler mindestens 'cost' LP? */
    public static boolean hasEnoughLP(Player player, int cost) {
        if (!isPresent() || !init()) return false;
        try {
            Object net = getNetwork(player);
            if (net == null || mGetCurrentEssence == null) return false;
            Object cur = mGetCurrentEssence.invoke(net);
            int current = (cur instanceof Number) ? ((Number) cur).intValue() : 0;
            return current >= cost;
        } catch (Throwable t) {
            return false;
        }
    }

    /** Zieht LP; true = erfolgreich abgezogen. */
    public static boolean syphonLP(Player player, int cost) {
        if (!isPresent() || !init()) return false;
        try {
            Object net = getNetwork(player);
            if (net == null) return false;

            // Vorher lesen
            int before = 0;
            try {
                Object cur = mGetCurrentEssence.invoke(net);
                before = (cur instanceof Number) ? ((Number) cur).intValue() : 0;
            } catch (Throwable ignored) {}

            Object ret = null;
            if (mSyphonPlayerInt != null) {
                ret = mSyphonPlayerInt.invoke(net, player, cost);
            } else if (mSyphonInt != null) {
                ret = mSyphonInt.invoke(net, cost);
            } else {
                return false;
            }

            if (ret instanceof Boolean) return (Boolean) ret;
            if (ret instanceof Number)  return ((Number) ret).intValue() > 0;

            // void/unbekannt → Differenz prüfen
            int after = before;
            try {
                Object cur2 = mGetCurrentEssence.invoke(net);
                after = (cur2 instanceof Number) ? ((Number) cur2).intValue() : before;
            } catch (Throwable ignored) {}
            return after < before;
        } catch (Throwable t) {
            return false;
        }
    }
}
