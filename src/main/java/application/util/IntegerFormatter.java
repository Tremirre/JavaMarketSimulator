package application.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class IntegerFormatter extends TextFormatter<String> {

    private IntegerFormatter(UnaryOperator<Change> unaryOperator) {
        super(unaryOperator);
    }

    public static IntegerFormatter createFormatter() {
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (text.matches("[0-9]*")) {
                return c;
            } else {
                return null;
            }
        };
        return new IntegerFormatter(filter);
    }
}
