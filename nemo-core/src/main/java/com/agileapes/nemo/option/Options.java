package com.agileapes.nemo.option;

import com.agileapes.nemo.error.InvalidArgumentSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will model the input arguments. Note that they are all the arguments presented by the user,
 * and not necessary the correct options or values that the actions expect.
 *
 * To parse the input, you must use the internal {@link Builder} class.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/10, 19:38)
 */
public class Options {

    private final Map<String, String> options = new HashMap<String, String>();
    private final Map<Character, String> aliases = new HashMap<Character, String>();
    private final Map<Integer, String> indexes = new HashMap<Integer, String>();

    private Options() {}

    public Map<String, String> getOptions() {
        return Collections.unmodifiableMap(options);
    }

    public Map<Character, String> getAliases() {
        return Collections.unmodifiableMap(aliases);
    }

    public Map<Integer, String> getIndexes() {
        return Collections.unmodifiableMap(indexes);
    }

    public static class Builder {

        private static final Log log = LogFactory.getLog(Builder.class);

        public static final String OPTION_PREFIX = "--";
        public static final String DEFAULT_FLAG_VALUE = "true";
        public static final String ALIAS_PREFIX = "-";
        private final String[] arguments;

        public Builder(String... arguments) {
            this.arguments = arguments;
        }

        public Options build() throws InvalidArgumentSyntaxException {
            log.info("Parsing input arguments " + Arrays.toString(arguments));
            final Options options = new Options();
            int index = 0;
            for (int i = 0; i < arguments.length; i++) {
                String argument = arguments[i];
                String value;
                if (argument.matches("--?")) {
                    throw new InvalidArgumentSyntaxException(argument);
                }
                if (argument.startsWith(OPTION_PREFIX)) {
                    if (i == arguments.length - 1 || arguments[i + 1].startsWith("-")) {
                        value = DEFAULT_FLAG_VALUE;
                    } else {
                        value = arguments[++ i];
                    }
                    options.options.put(argument.substring(OPTION_PREFIX.length()), value);
                } else if (argument.startsWith(ALIAS_PREFIX)) {
                    if (argument.length() == 2) {
                        if (i == arguments.length - 1 || arguments[i + 1].startsWith("-")) {
                            value = DEFAULT_FLAG_VALUE;
                        } else {
                            value = arguments[++ i];
                        }
                        options.aliases.put(argument.charAt(1), value);
                    } else {
                        for (int j = 1; j < argument.length(); j ++) {
                            options.aliases.put(argument.charAt(j), DEFAULT_FLAG_VALUE);
                        }
                    }
                } else {
                    options.indexes.put(index ++, argument);
                }
            }
            return options;
        }

    }

}
