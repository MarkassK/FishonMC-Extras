package io.github.markassk.fishonmcextras.v1.overlay;


import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

public class RayTracing {
    public static final RayTracing INSTANCE = new RayTracing();
    private final MinecraftClient client = MinecraftClient.getInstance();
    @Nullable
    private HitResult target;

    private RayTracing() {}

    public void fire() {
        Entity viewEntity = client.getCameraEntity();
        PlayerEntity viewPlayer = viewEntity instanceof PlayerEntity ? (PlayerEntity) viewEntity : client.player;
        if (viewEntity == null || viewPlayer == null) {
            return;
        }

        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            Entity targetEntity = ((EntityHitResult) client.crosshairTarget).getEntity();
            if (canBeTarget(targetEntity, viewEntity)) {
                target = client.crosshairTarget;
                return;
            }
        }

        double blockReach = viewPlayer.getBlockInteractionRange();
        target = rayTrace(viewEntity, blockReach);
    }

    @Nullable
    public HitResult getTarget() {
        return target;
    }

    public HitResult rayTrace(Entity entity, double blockReach) {
//        float partialTicks = client.getRenderTickCounter().getTickDelta(true);
        float partialTicks = 0;
        Vec3d eyePosition = entity.getCameraPosVec(partialTicks);
        Vec3d lookVector = entity.getRotationVec(partialTicks);
        Vec3d traceEnd = eyePosition.add(lookVector.x * blockReach, lookVector.y * blockReach, lookVector.z * blockReach);

        RaycastContext.FluidHandling fluidView = RaycastContext.FluidHandling.NONE;
        RaycastContext context = new RaycastContext(eyePosition, traceEnd, RaycastContext.ShapeType.OUTLINE, fluidView, entity);
        return entity.getEntityWorld().raycast(context);
    }

    private boolean canBeTarget(Entity target, Entity viewEntity) {
        if (target.isRemoved()) {
            return false;
        }

        if (target.isSpectator()) {
            return false;
        }

        if (target == viewEntity.getVehicle()) {
            return false;
        }

        if (target instanceof ProjectileEntity && !target.getWorld().getTickManager().isFrozen()) {
            return false;
        }

        if (viewEntity instanceof PlayerEntity player) {
            if (target.isInvisibleTo(player)) {
                return false;
            }
            assert client.interactionManager != null;
            return !client.interactionManager.isBreakingBlock() || target.getType() != EntityType.ITEM;
        } else {
            return !target.isInvisible();
        }
    }
}
