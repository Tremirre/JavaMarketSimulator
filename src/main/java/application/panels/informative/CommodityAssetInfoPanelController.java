package application.panels.informative;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import simulation.asset.CommodityData;

public class CommodityAssetInfoPanelController extends AssetInfoPanelController {
    protected CommodityData assetData;
    @FXML
    protected Label tradingUnitLabel;
    @FXML
    protected Label tradingCurrencyLabel;

    public void passID(String assetUID) {
        this.assetData = (CommodityData) this.simulation.getAssetManager().getAssetData(assetUID);
        this.updateFields(this.assetData);
    }

    @Override
    public void refresh() {
        this.passID(this.assetData.getUniqueIdentifyingName());
    }

    protected void updateFields(CommodityData assetData) {
        super.updateFields(assetData);
        this.tradingCurrencyLabel.setText(this.assetData.getTradingCurrency());
        this.tradingUnitLabel.setText(this.assetData.getTradingUnit());
    }

}