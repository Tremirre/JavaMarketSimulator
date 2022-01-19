package application.util;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
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
                }).decorates(field);
    }

    public void addNotEmptyCheck(String key, ListView<?> view) {
        this.createCheck().dependsOn(key, view.itemsProperty())
                .withMethod(c -> {
                    if (view.getItems().size() == 0) {
                        c.error("This list cannot be empty!");
                    }
                }).decorates(view);
    }

    public void addNotEmptyCheck(String key, DatePicker picker) {
        this.createCheck().dependsOn(key, picker.editorProperty())
                .withMethod(c -> {
                    if (picker.getValue() == null) {
                        c.error("This field cannot be empty!");
                    }
                }).decorates(picker);
    }

    public void addPositiveCheck(String key, TextField field) {
        this.createCheck().dependsOn(key, field.textProperty())
                .withMethod(c -> {
                    String text = c.get(key);
                    if (text != null && text.length() > 0 && !text.equals(".")) {
                        var num = Double.parseDouble(text);
                        if (num <= 0) {
                            c.error("This number has to be positive (larger than 0)!");
                        }
                    }
                }).decorates(field);
    }
}
