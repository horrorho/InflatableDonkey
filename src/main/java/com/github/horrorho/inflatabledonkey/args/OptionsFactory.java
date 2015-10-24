/* 
 * The MIT License
 *
 * Copyright 2015 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.inflatabledonkey.args;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.jcip.annotations.Immutable;
import org.apache.commons.cli.Option;

/**
 * Options factory.
 *
 * @author Ahseya
 */
@Immutable
public final class OptionsFactory implements Supplier<Map<Option, Property>> {

    public static OptionsFactory create() {
        return instance;
    }

    private static final OptionsFactory instance = new OptionsFactory();

    OptionsFactory() {
    }

    @Override
    public Map<Option, Property> get() {
        LinkedHashMap<Option, Property> options = new LinkedHashMap<>();

        options.put(Option.builder("d").longOpt("device")
                .desc("Device, default: 0 = first device.")
                .argName("int")
                .numberOfArgs(1)
                .hasArgs()
                .build(),
                Property.SELECT_DEVICE_INDEX);

        options.put(Option.builder("s").longOpt("snapshot")
                .desc("Snapshot, default: 0 = first snapshot.")
                .argName("int")
                .numberOfArgs(1)
                .hasArgs()
                .build(),
                Property.SELECT_SNAPSHOT_INDEX);

        options.put(Option.builder("m").longOpt("manifest")
                .desc("Manifest, default: 0 = first manifest.")
                .argName("int")
                .numberOfArgs(1)
                .hasArgs()
                .build(),
                Property.SELECT_MANIFEST_INDEX);
        
        options.put(Option.builder().longOpt("protoc")
                .desc("Protoc binary path, default: 'protoc', leave blank to disable")
                .argName("path")
                .numberOfArgs(1)
                .optionalArg(true) 
                .build(),
                Property.PROTOC_PATH);

        options.put(
                new Option(null, "help", false, "Display this help and exit."),
                Property.ARGS_HELP);

        options.put(
                new Option(null, "token", false, "Display the dsPrsID:mmeAuthToken and exit."),
                Property.ARGS_TOKEN);
        return options;
    }
}
