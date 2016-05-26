package org.javacs;

import io.typefox.lsapi.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class AutocompleteTest extends Fixtures {
    private static final Logger LOG = Logger.getLogger("main");

    @Test
    public void staticMember() throws IOException {
        String file = "/org/javacs/example/AutocompleteStaticMember.java";

        // Static method
        Set<String> suggestions = insertText(file, 4, 33);

        assertThat(suggestions, hasItems("fieldStatic", "methodStatic", "class"));
        assertThat(suggestions, not(hasItems("field", "method", "getClass")));
    }

    @Test
    @Ignore
    public void staticReference() throws IOException {
        String file = "/org/javacs/example/AutocompleteStaticReference.java";

        // Static method
        Set<String> suggestions = insertText(file, 2, 37);

        assertThat(suggestions, hasItems("methodStatic"));
        assertThat(suggestions, not(hasItems( "method", "new")));
    }

    @Test
    public void member() throws IOException {
        String file = "/org/javacs/example/AutocompleteMember.java";

        // Static method
        Set<String> suggestions = insertText(file, 4, 13);

        assertThat(suggestions, not(hasItems("fieldStatic", "methodStatic", "class")));
        assertThat(suggestions, hasItems("field", "method", "getClass"));
    }

    @Test
    public void other() throws IOException {
        String file = "/org/javacs/example/AutocompleteOther.java";

        // Static method
        Set<String> suggestions = insertText(file, 4, 33);

        assertThat(suggestions, not(hasItems("fieldStatic", "methodStatic", "class")));
        assertThat(suggestions, hasItems("field", "method", "getClass"));
    }

    @Test
    @Ignore
    public void reference() throws IOException {
        String file = "/org/javacs/example/AutocompleteReference.java";

        // Static method
        Set<String> suggestions = insertText(file, 2, 14);

        assertThat(suggestions, not(hasItems("methodStatic")));
        assertThat(suggestions, hasItems("method", "getClass"));
    }

    @Test
    public void docstring() throws IOException {
        String file = "/org/javacs/example/AutocompleteDocstring.java";

        Set<String> docstrings = documentation(file, 7, 14);

        assertThat(docstrings, hasItems("A method", "A field"));

        docstrings = documentation(file, 11, 31);

        assertThat(docstrings, hasItems("A fieldStatic", "A methodStatic"));
    }

    private Set<String> insertText(String file, int row, int column) throws IOException {
        List<CompletionItemImpl> items = items(file, row, column);

        return items
                .stream()
                .map(CompletionItemImpl::getInsertText)
                .collect(Collectors.toSet());
    }

    private Set<String> documentation(String file, int row, int column) throws IOException {
        List<CompletionItemImpl> items = items(file, row, column);

        return items
                .stream()
                .flatMap(i -> {
                    if (i.getDocumentation() != null)
                        return Stream.of(i.getDocumentation().trim());
                    else
                        return Stream.empty();
                })
                .collect(Collectors.toSet());
    }

    private List<CompletionItemImpl> items(String file, int row, int column) {
        TextDocumentPositionParamsImpl position = new TextDocumentPositionParamsImpl();

        position.setPosition(new PositionImpl());
        position.getPosition().setLine(row);
        position.getPosition().setCharacter(column);
        position.setTextDocument(new TextDocumentIdentifierImpl());
        position.getTextDocument().setUri(uri(file).toString());

        JavaLanguageServer server = getJavaLanguageServer();

        return server.autocomplete(position);
    }

    private URI uri(String file) {
        try {
            return AutocompleteTest.class.getResource(file).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
