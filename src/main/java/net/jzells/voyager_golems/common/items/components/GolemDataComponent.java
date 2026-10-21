package net.jzells.voyager_golems.common.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor()
public class GolemDataComponent {
    private int level;
    private float size;
    private float potency;
    private long currentExp;
    private long expToLevel;
    private String recipe;
    private int potencyUpgrade;
    private int sizeUpgrade;
    private int prestigeUpgrade;
    private int prestigeMultiplier;

    public GolemDataComponent() {
        this.level = 1;
        this.size = 1.0f;
        this.potency = 0.8f;
        this.currentExp = 0;
        this.expToLevel = 10;
        this.recipe = "none";
        this.potencyUpgrade = 0;
        this.sizeUpgrade = 0;
        this.prestigeUpgrade = 0;
        this.prestigeMultiplier = 1;
    }

    public static final Codec<GolemDataComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("level").forGetter(GolemDataComponent::getLevel),
                    Codec.FLOAT.fieldOf("size").forGetter(GolemDataComponent::getSize),
                    Codec.FLOAT.fieldOf("potency").forGetter(GolemDataComponent::getPotency),
                    Codec.LONG.fieldOf("currentExp").forGetter(GolemDataComponent::getCurrentExp),
                    Codec.LONG.fieldOf("expToLevel").forGetter(GolemDataComponent::getExpToLevel),
                    Codec.STRING.fieldOf("recipe").forGetter(GolemDataComponent::getRecipe),
                    Codec.INT.fieldOf("potencyUpgrade").forGetter(GolemDataComponent::getPotencyUpgrade),
                    Codec.INT.fieldOf("sizeUpgrade").forGetter(GolemDataComponent::getSizeUpgrade),
                    Codec.INT.fieldOf("prestigeUpgrade").forGetter(GolemDataComponent::getPrestigeUpgrade),
                    Codec.INT.fieldOf("prestigeMultiplier").forGetter(GolemDataComponent::getPrestigeMultiplier))
                    .apply(instance, GolemDataComponent::new)
                    );
    public static final StreamCodec<ByteBuf, GolemDataComponent> STREAM_CODEC = new StreamCodec<ByteBuf, GolemDataComponent>() {
        @Override
        public GolemDataComponent decode(ByteBuf buffer) {
            int level = ByteBufCodecs.INT.decode(buffer);
            float size = ByteBufCodecs.FLOAT.decode(buffer);
            float potency = ByteBufCodecs.FLOAT.decode(buffer);
            long currentXP = ByteBufCodecs.VAR_LONG.decode(buffer);
            long expToLevel = ByteBufCodecs.VAR_LONG.decode(buffer);
            String recipe = ByteBufCodecs.STRING_UTF8.decode(buffer);
            int potencyUpgrade = ByteBufCodecs.INT.decode(buffer);
            int sizeUpgrade = ByteBufCodecs.INT.decode(buffer);
            int prestigeUpgrade = ByteBufCodecs.INT.decode(buffer);
            int prestigeMultiplier = ByteBufCodecs.INT.decode(buffer);
            return new GolemDataComponent(level, size, potency, currentXP, expToLevel, recipe, potencyUpgrade, sizeUpgrade, prestigeUpgrade, prestigeMultiplier);
        }

        @Override
        public void encode(ByteBuf buffer, GolemDataComponent value) {
            ByteBufCodecs.INT.encode(buffer, value.getLevel());
            ByteBufCodecs.FLOAT.encode(buffer, value.getSize());
            ByteBufCodecs.FLOAT.encode(buffer, value.getPotency());
            ByteBufCodecs.VAR_LONG.encode(buffer, value.getCurrentExp());
            ByteBufCodecs.VAR_LONG.encode(buffer, value.getExpToLevel());
            ByteBufCodecs.STRING_UTF8.encode(buffer, value.getRecipe());
            ByteBufCodecs.INT.encode(buffer, value.getPotencyUpgrade());
            ByteBufCodecs.INT.encode(buffer, value.getSizeUpgrade());
            ByteBufCodecs.INT.encode(buffer, value.getPrestigeUpgrade());
            ByteBufCodecs.INT.encode(buffer, value.getPrestigeMultiplier());
        }
    };

    @Override
    public int hashCode() {
        return Objects.hash(this.level,this.size,this.potency,
                this.currentExp,this.expToLevel,this.recipe,
                this.potencyUpgrade,this.sizeUpgrade,this.prestigeUpgrade,this.prestigeMultiplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        else return obj instanceof GolemDataComponent gd
                && this.level == gd.level
                && this.size == gd.size
                && this.potency == gd.potency
                && this.currentExp == gd.currentExp
                && this.expToLevel == gd.expToLevel
                && this.recipe.equals(gd.recipe)
                && this.potencyUpgrade == gd.potencyUpgrade
                && this.sizeUpgrade == gd.sizeUpgrade
                && this.prestigeUpgrade == gd.prestigeUpgrade
                && this.prestigeMultiplier == gd.prestigeMultiplier;
    }

    public static void init() {
    };
}
