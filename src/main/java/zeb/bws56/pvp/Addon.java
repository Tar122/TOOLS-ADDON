package zeb.bws56.pvp;

import zeb.bws56.pvp.commands.*;
import zeb.bws56.pvp.modules.*;
import zeb.bws56.pvp.util.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;


public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Tools");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Tools Addon");

        // Modules
        Modules.get().add(new AutoTotem());
        Modules.get().add(new CrystalOptimizer());
        Modules.get().add(new AnchorExploder());
        Modules.get().add(new Test2());




    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "zeb.bws56.pvp";
    }


}
