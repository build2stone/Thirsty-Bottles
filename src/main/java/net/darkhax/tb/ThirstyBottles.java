package net.darkhax.tb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "thirstybottles", name = "Thirsty Bottles", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class ThirstyBottles {

    private static final Logger LOG = LogManager.getLogger("Thirsty Bottles");
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onItemUsed(RightClickBlock event) {

		if (event.getWorld().isRemote)
			return;
		
        if (event.getItemStack() != null && event.getItemStack().getItem() instanceof ItemGlassBottle) {

            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();

            Vec3d look = player.getLookVec();
            float f = 5.0F;
            Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            Vec3d end = new Vec3d(player.posX + look.x * f, player.posY + player.getEyeHeight() + look.y * f, player.posZ + look.z * f);
            RayTraceResult raytraceresult = world.rayTraceBlocks(start, end, true, false, true);

            if (raytraceresult != null) {

                BlockPos pos = raytraceresult.getBlockPos();
                IBlockState state = event.getWorld().getBlockState(pos);

                if (state.getMaterial() == Material.WATER && (state.getBlock() instanceof IFluidBlock || state.getBlock() instanceof BlockLiquid) && Blocks.WATER.canCollideCheck(state, true)) {
                    event.getWorld().setBlockToAir(pos);
                }
            }
        }
    
    @EventHandler
    public void onFingerprintViolation (FMLFingerprintViolationEvent event) {

        LOG.error("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
