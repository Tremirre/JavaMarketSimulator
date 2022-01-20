package application.panels.plot;

import application.util.format.DecimalDisplayFormat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AssetRecord extends HBox {
    private final Label priceLabel;
    private final CheckBox plotAssetCheckBox;
    private final DecimalDisplayFormat decimal;
    private final static int baseSpacing = 5;

    public AssetRecord(String assetID, double price, MultiAssetPanelController plotControllerReference) {
        super();
        GridPane gridpane = new GridPane();
        this.decimal = new DecimalDisplayFormat(6);
        this.setPadding(new Insets(baseSpacing, baseSpacing, baseSpacing, baseSpacing));
        this.setSpacing(baseSpacing*2);
        this.setAlignment(Pos.CENTER_LEFT);
        this.plotAssetCheckBox = new CheckBox();
        this.plotAssetCheckBox.setSelected(false);
        this.plotAssetCheckBox.setOnAction(e -> {
            if (this.plotAssetCheckBox.isSelected()) {
                plotControllerReference.plotAsset(assetID);
            } else {
                plotControllerReference.removeAssetFromPlot(assetID);
            }
        });
        Label assetIDLabel = new Label();
        assetIDLabel.setText(assetID);
        this.priceLabel = new Label();
        this.priceLabel.setText(this.decimal.format(price));
        gridpane.add(assetIDLabel, 0, 0);
        gridpane.add(this.priceLabel, 1, 0);
        gridpane.add(this.plotAssetCheckBox, 2, 0);
        gridpane.getColumnConstraints().add(new ColumnConstraints(100));
        gridpane.getColumnConstraints().add(new ColumnConstraints(70));
        this.getChildren().add(gridpane);
    }

    public void updatePrice(double newPrice) {
        this.priceLabel.setText(this.decimal.format(newPrice));
    }
}
