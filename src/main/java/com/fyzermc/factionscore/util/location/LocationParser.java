package com.fyzermc.factionscore.util.location;

import java.util.function.Function;

public interface LocationParser<T> extends Function<SerializedLocation, T> {

}