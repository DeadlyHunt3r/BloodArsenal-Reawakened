package com.deadlyhunter.bloodarsenalreawakened.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BloodMagicCompat {
    private static final String BM_MODID = "bloodmagic";

    private static Class<?> clsNetworkHelper;
    private static Method mGetSoulNetwork; 
    private static Method mGetCurrentEssence;
    private static Method mSyphonWithPlayer;  
    private static Method mAdd; 

    private static boolean resolved = false;
    private static boolean available = false;

    private BloodMagicCompat() {}

    public static boolean isPresent() {
        return ModList.get().isLoaded(BM_MODID);
    }

    private static void resolve() {
        if (resolved) return;
        resolved = true;

        if (!isPresent()) {
            available = false;
            return;
        }

        try {

            clsNetworkHelper = Class.forName("wayoftime.bloodmagic.api.impl.NetworkHelper");

            for (Method m : clsNetworkHelper.getDeclaredMethods()) {
                if (m.getName().equals("getSoulNetwork")
                        && m.getParameterCount() == 1
                        && Player.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.setAccessible(true);
                    mGetSoulNetwork = m;
                    break;
                }
            }

            available = (mGetSoulNetwork != null);
        } catch (ClassNotFoundException e) {
            available = false;
        }
    }

    public static int getCurrentLP(Player player) {
        resolve();
        if (!available) return -1;

        try {
            Object network = mGetSoulNetwork.invoke(null, player);
            if (network == null) return -1;


            if (mGetCurrentEssence == null) {
                mGetCurrentEssence = findNoArgMethod(network.getClass(),
                        "getCurrentEssence", "getEssence", "getCurrent", "getSoul");
            }
            if (mGetCurrentEssence != null) {
                Object val = mGetCurrentEssence.invoke(network);
                if (val instanceof Number n) return n.intValue();
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {}

        return -1;
        }

    public static boolean consumeLP(Player player, int cost) {
        resolve();
        if (!available) return false;

        try {
            Object network = mGetSoulNetwork.invoke(null, player);
            if (network == null) return false;

            if (mSyphonWithPlayer == null) {
                mSyphonWithPlayer = findMethodWithParams(network.getClass(), "syphon",
                        new Class<?>[]{Player.class, int.class});
            }
            if (mSyphonWithPlayer != null) {
                mSyphonWithPlayer.invoke(network, player, cost);
                return true;
            }

            if (mAdd == null) {
                mAdd = findMethodWithParams(network.getClass(), "add",
                        new Class<?>[]{int.class});
            }
            if (mAdd != null) {
                mAdd.invoke(network, -cost);
                return true;
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {}

        return false;
    }

    private static Method findNoArgMethod(Class<?> cls, String... possibleNames) {
        for (String name : possibleNames) {
            try {
                Method m = cls.getMethod(name);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }

    private static Method findMethodWithParams(Class<?> cls, String name, Class<?>[] params) {
        try {
            Method m = cls.getMethod(name, params);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException ignored) {}
        return null;
    }
}
