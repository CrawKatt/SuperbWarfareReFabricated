package com.atsuishio.superbwarfare.annotation

/**
 * 标记一个 [net.minecraft.network.syncher.EntityDataAccessor] 字段，
 * 使其对应的 NBT 条目在超视距同步包中被移除，以节省带宽。
 *
 * 只有那些在 [addAdditionalSaveData] 中显式写入 NBT 的字段才需要使用此注解；
 * 未写入存档 NBT 的字段已隐式排除在超视距同步之外。
 *
 * @param nbtKey 在 [addAdditionalSaveData] 中使用的确切 NBT key 字符串
 *               （例如 "DogTagIcon", "LastAttacker"）
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ExcludeBvrSync(val nbtKey: String)
