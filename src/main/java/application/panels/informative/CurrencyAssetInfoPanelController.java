package application.panels.informative;

import application.util.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import simulation.asset.CurrencyData;

public class CurrencyAssetInfoPanelController extends AssetInfoPanelController {

    private CurrencyData assetData;
    @FXML
    protected Label stabilityLabel;
    @FXML
    protected ListView<String> countryListView;

    @Override
    public void passID(String assetUID) {
        this.assetData = (CurrencyData) this.simulation.getAssetManager().getAssetData(assetUID);
        this.updateFields(this.assetData);
    }

    protected void updateFields(CurrencyData assetData) {
        super.updateFields(assetData);
        this.stabilityLabel.setText(new DecimalDisplayFormat(3).format((this.assetData.getQualityMeasure())));
        for (var country : assetData.getCountriesOfUse())
            this.countryListView.getItems().add(country);
    }

    @Override
    public void refresh() {
        this.passID(this.assetData.getUniqueIdentifyingName());
    }
}
