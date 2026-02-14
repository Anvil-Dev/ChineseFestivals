package dev.anvilcraft.festivals.mixin;

import net.minecraft.client.resources.model.ModelBakery;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelBakery.class)
abstract class ModelBakeryMixin {
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;"), remap = false)
//    private static ImmutableMap<ResourceLocation, StateDefinition<Block, BlockState>> modify(Object k1, Object v1, Object k2, Object v2) {
//        List<Map.Entry<ResourceLocation, StateDefinition<Block, BlockState>>> entries = new ArrayList<>();
//        entries.add((Map.Entry) Map.entry(k1, v1));
//        entries.add((Map.Entry) Map.entry(k2, v2));
//        for (BlockModelData model : IFeature.BLOCK_MODELS) {
//            StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(Blocks.AIR);
//            model.properties.forEach(it -> builder.add(it.genProperty()));
//            StateDefinition<Block, BlockState> definition = builder.create(Block::defaultBlockState, BlockState::new);
//            entries.add(Map.entry(model.resourceLocation, definition));
//        }
//        return ImmutableMap.ofEntries(entries.toArray(new Map.Entry[0]));
//    }
//
//    @WrapOperation(
//        method = "<clinit>",
//        at = @At(
//            value = "INVOKE",
//            target = ""
//        )
//    )
//    private void modify(){}
}
