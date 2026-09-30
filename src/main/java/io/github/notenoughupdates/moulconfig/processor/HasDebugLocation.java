package io.github.notenoughupdates.moulconfig.processor;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface HasDebugLocation {
    /**
     * @return a string identifying the position in code where this option was declared, intended for debugging purposes only
     */
    @Nullable
    String getDebugDeclarationLocation();
}
