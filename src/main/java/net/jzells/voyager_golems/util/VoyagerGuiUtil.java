package net.jzells.voyager_golems.util;

import net.jzells.voyager_golems.common.items.golems.GolemItem;
import net.jzells.voyager_golems.common.recipes.GolemProduceRecipe;
import net.jzells.voyager_golems.common.recipes.VoyagerRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class VoyagerGuiUtil {

    public static final int LEFT_CLICK = 0;

    public static boolean isMouseInBounds(int mx, int my, int targetX, int targetX_2, int targetY, int targetY_2) {
        return mx >= targetX && mx <= targetX_2
                && my >= targetY && my <= targetY_2;
    }

    public static void playClickSound() {
        Minecraft.getInstance().level.playLocalSound(
                Minecraft.getInstance().player.getX(),
                Minecraft.getInstance().player.getY(),
                Minecraft.getInstance().player.getZ(),
                SoundEvents.UI_BUTTON_CLICK.value(),
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
    }

    public static void playErrorSound() {
        Minecraft.getInstance().level.playLocalSound(
                Minecraft.getInstance().player.getX(),
                Minecraft.getInstance().player.getY(),
                Minecraft.getInstance().player.getZ(),
                SoundEvents.SHIELD_BLOCK,
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
    }

    public static void playLevelSound() {
        Minecraft.getInstance().level.playLocalSound(
                Minecraft.getInstance().player.getX(),
                Minecraft.getInstance().player.getY(),
                Minecraft.getInstance().player.getZ(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
    }

    public static void playPrestigeSound() {
        Minecraft.getInstance().level.playLocalSound(
                Minecraft.getInstance().player.getX(),
                Minecraft.getInstance().player.getY(),
                Minecraft.getInstance().player.getZ(),
                SoundEvents.ENDER_DRAGON_DEATH,
                SoundSource.MASTER,
                1.0F,
                1.0F,
                false
        );
    }

    public static void renderBottomToTopVertialBarPercentage(GuiGraphics guiGraphics, ResourceLocation texture_back, ResourceLocation texture_front, float percentage, int texture_width, int texture_height, int x, int y)
    {
        percentage = Mth.clamp(percentage, 0.0F, 1.0F);

        guiGraphics.blit(
                texture_back,
                x, y,
                0, 0,
                texture_width, texture_height,
                texture_width, texture_height
        );

        int filled_height = (int) (texture_height * percentage);

        if (filled_height > 0) {
            guiGraphics.blit(
                    texture_front,
                    x, y + texture_height - filled_height,
                    0, texture_height - filled_height,
                    texture_width, filled_height,
                    texture_width, texture_height
            );
        }
    }

    public static void renderLeftToRightHorizontalBarPercentage(
            GuiGraphics guiGraphics,
            ResourceLocation texture_back,
            ResourceLocation texture_front,
            float percentage,
            int texture_width,
            int texture_height,
            int x,
            int y
    ) {
        percentage = Mth.clamp(percentage, 0.0F, 1.0F);

        guiGraphics.blit(
                texture_back,
                x, y,
                0, 0,
                texture_width, texture_height,
                texture_width, texture_height
        );

        int filled_width = (int) (texture_width * percentage);

        if (filled_width > 0) {
            guiGraphics.blit(
                    texture_front,
                    x, y,
                    0, 0,
                    filled_width, texture_height,
                    texture_width, texture_height
            );
        }
    }

    public static List<GolemProduceRecipe> getGolemRecipes(GolemItem golem_item, Player player)
    {
        return player.level().getRecipeManager().getAllRecipesFor(VoyagerRecipes.GOLEM_PRODUCTION_TYPE).stream()
                .map(RecipeHolder::value)
                .filter(
                recipe -> recipe.getGolemItem().toString().equals(golem_item.toString())
        ).toList();
    }

    public static String colorPercentage(float percentage)
    {
        if(percentage >= 10f) return "§5" + String.format("%.2f", percentage);
        if(percentage >= 3f) return "§d" + String.format("%.2f", percentage);
        if(percentage >= 1.5f) return "§b" + String.format("%.2f", percentage);
        if(percentage >= 1.0f) return "§a" + String.format("%.2f", percentage);
        if(percentage >= .75f) return "§e" + String.format("%.2f", percentage);
        if(percentage >= .5f) return "§c" + String.format("%.2f", percentage);
        if(percentage >= .25f) return "§4" + String.format("%.2f", percentage);
        return "§f" + percentage;
    }

}
