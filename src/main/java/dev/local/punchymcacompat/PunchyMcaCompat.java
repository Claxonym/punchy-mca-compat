package dev.local.punchymcacompat;

import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Client-side bridge: asks Punchy to render its animated arms through
 * Minecraft's normal first-person arm renderer, so MCA's arm-render hook
 * has a chance to provide the MCA appearance.
 */
@Mod(value = PunchyMcaCompat.MOD_ID, dist = Dist.CLIENT)
public final class PunchyMcaCompat {
    public static final String MOD_ID = "punchy_mca_compat";
    private static final String PUNCHY_API = "punchy.api.PunchyApi";

    public PunchyMcaCompat() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(this::registerBridge);
    }

    private void registerBridge() {
        ModList mods = ModList.get();
        if (!mods.isLoaded("punchy") || !mods.isLoaded("mca")) {
            return;
        }

        try {
            Class<?> api = Class.forName(PUNCHY_API, true, getClass().getClassLoader());
            Method useMinecraftHands = api.getMethod(
                    "useMinecraftHands", String.class, BooleanSupplier.class);

            // Always use Minecraft's RenderArmEvent path while Punchy is active.
            // Punchy keeps its arm animation transforms and held-item rendering.
            useMinecraftHands.invoke(null, MOD_ID, (BooleanSupplier) () -> true);

            System.out.println("[Punchy-MCA-Compat] Registered Punchy/MCA first-person arm bridge.");
        } catch (ReflectiveOperationException | LinkageError exception) {
            System.err.println("[Punchy-MCA-Compat] Could not register the Punchy API bridge.");
            exception.printStackTrace();
        }
    }
}