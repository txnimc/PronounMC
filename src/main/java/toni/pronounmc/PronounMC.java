package toni.pronounmc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.api.ModInitializer;
    #if after_21_1
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    #endif

    #if current_20_1
    import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
    #endif
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
#endif


#if NEO
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
#endif


#if FORGELIKE
@Mod("pronounmc")
#endif
public class PronounMC #if FABRIC implements ModInitializer, ClientModInitializer #endif
{
    public static final String MODNAME = "PronounMC";
    public static final String MODID = "pronounmc";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    public PronounMC(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if FORGE
        var context = FMLJavaModLoadingContext.get();
        var modEventBus = context.getModEventBus();
        #endif

        #if FORGELIKE
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        #endif
//
//        AllConfigs.register((type, spec) -> {
//            #if FORGE
//            ModLoadingContext.get().registerConfig(type, spec);
//            #elif NEO
//            modContainer.registerConfig(type, spec);
//            #elif FABRIC
//                #if AFTER_21_1
//                NeoForgeConfigRegistry.INSTANCE.register(PronounMC.MODID, type, spec);
//                #else
//                ForgeConfigRegistry.INSTANCE.register(PronounMC.MODID, type, spec);
//                #endif
//            #endif
//        });
    }


    #if FABRIC @Override #endif
    public void onInitialize() {

    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {

    }


    public static Component getTextWithColoredPronoun(Component name, String pronouns) {
        if (pronouns == null || pronouns.isEmpty() || pronouns.equals("⚠"))
            return name;

        var pronoun = Component
                .literal(pronouns)
                .withStyle(getFormatting(pronouns));

        return Component.empty()
                .append(pronoun)
                .append(" ")
                .append(name);
    }

    public static ChatFormatting getFormatting(String pronouns) {
        if (pronouns.startsWith("she"))
            return ChatFormatting.LIGHT_PURPLE;

        if (pronouns.startsWith("he"))
            return ChatFormatting.GREEN;

        if (pronouns.startsWith("they"))
            return ChatFormatting.AQUA;

        return ChatFormatting.DARK_GRAY;
    }

    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
