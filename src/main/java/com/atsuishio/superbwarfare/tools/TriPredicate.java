package com.atsuishio.superbwarfare.tools;

/**
 * A predicate that takes three arguments, mirroring
 * {@code net.minecraftforge.common.util.TriPredicate} from Forge.
 */
@FunctionalInterface
public interface TriPredicate<A, B, C> {
    boolean test(A a, B b, C c);
}
