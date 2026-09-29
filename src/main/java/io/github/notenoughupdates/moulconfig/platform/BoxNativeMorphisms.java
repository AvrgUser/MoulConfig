package io.github.notenoughupdates.moulconfig.platform;

import net.minecraft.network.chat.Component;
import io.github.notenoughupdates.moulconfig.internal.TypeUtils;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.xml.ParametricTypeMorphism;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.lang.reflect.Type;
import java.util.Optional;

public interface BoxNativeMorphisms {
    class ComponentMorphism implements ParametricTypeMorphism {
        @Override
        public Optional<Type> codomain(Type domain) {
            var typ = TypeUtils.resolveRawType(domain);
            if (typ == Component.class) {
                return Optional.of(Component.class);
            }
            if (typ == MutableComponent.class) {
                return Optional.of(Component.Mutable.class);
            }
            return Optional.empty();
        }

        @Override
        public GetSetter<?> apply(Type domain, GetSetter<?> value) {
            // Act purely on Components here.. The underlying MoulConfigText forgets about mutability anyway, and this is all checked in codomain
            var valueC = (GetSetter<Component>) value;
            return new GetSetter<Component>() {
                @Override
                public Component get() {
                    return MoulConfigText.wrap(valueC.get());
                }

                @Override
                public void set(Component newValue) {
                    valueC.set(MoulConfigText.unwrap(newValue));
                }
            };
        }
    }
}
