package application.util;

import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class CustomValidator extends Validator {
    public CustomValidator() {
        super();
    }

    public void addPercentageCheck(String key, TextField field) {
        this.createCheck().dependsOn(key, field.textProperty())
                .withMethod(c -> {
                    String text = c.get(key);
                    if (text != null && text.length() > 0 && !text.equals(".")) {
                        double value = Double.parseDouble(text);
                        if (value > 1 || value < 0)
                            c.error("Percentage can only be between 0 and 1!");
                    }
                }).decorates(field).immediate();
    }

    public void addNotEmptyCheck(String key, TextField field) {
        this.createCheck().dependsOn(key, field.textProperty())
                .withMethod(c -> {
                    String text = c.get(key);
                    if (text == null || text.length() == 0) {
                        c.error("This field cannot be empty!");
                    }
                }).decorates(field).immediate();
    }
}
