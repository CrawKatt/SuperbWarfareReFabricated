package com.atsuishio.superbwarfare.mixins;

import com.sighs.apricityui.network.fabricutil.FabricAnnotationScanner;
import com.sighs.apricityui.util.spi.IAnnotationScanner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.ServiceLoader;

@Mixin(targets = "com.sighs.apricityui.util.AnnotationScanUtil", remap = false)
public abstract class AnnotationScanUtilMixin {

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/ServiceLoader;findFirst()Ljava/util/Optional;",
                    remap = false
            ),
            remap = false
    )
    private static Optional<IAnnotationScanner> superbwarfare$useAccessibleScanner(
            ServiceLoader<IAnnotationScanner> ignored
    ) {
        try {
            Constructor<FabricAnnotationScanner> constructor =
                    FabricAnnotationScanner.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return Optional.of(constructor.newInstance());
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
}
