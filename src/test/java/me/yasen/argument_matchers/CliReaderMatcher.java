package me.yasen.argument_matchers;

import me.yasen.cli.Reader;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class CliReaderMatcher<T> implements ArgumentMatcher<Reader<T>> {
    private final String matchingName;

    public CliReaderMatcher(String matchingName) {
        this.matchingName = matchingName;
    }

    @Override
    public boolean matches(Reader<T> argument) {
        if (argument == null) {
            return false;
        }

        return argument.getName().equals(matchingName);
    }

    public static <T> Reader<T> whereName(String name) {
        return argThat(new CliReaderMatcher<>(name));
    }
}
