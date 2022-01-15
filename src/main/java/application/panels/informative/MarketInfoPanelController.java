package application.panels.informative;

import application.panels.Refreshable;
import application.util.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import simulation.market.Market;
import simulation.market.StockMarket;

public class MarketInfoPanelController extends InfoPanelController implements Refreshable {
    private Market market;

    @FXML
    private Label buyFeeLabel;
    @FXML
    private Label sellFeeLabel;
    @FXML
    private Label buyOfferLabel;
    @FXML
    private Label sellOfferLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label buildingNumberLabel;
    @FXML
    private Label currencyLabel;
    @FXML
    private Label listTitleLabel;
    @FXML
    private ListView<String> assetListView;

    @Override
    public void refresh() {
        this.passID(market.getName());
    }

    public void updateFields() {
        this.assetListView.getItems().clear();
        this.nameLabel.setText(this.market.getName());
        var decimal = new DecimalDisplayFormat(3);
        this.buyFeeLabel.setText(decimal.format(this.market.getBuyFee()));
        this.sellFeeLabel.setText(decimal.format(this.market.getSellFee()));
        this.buyOfferLabel.setText(String.valueOf(this.market.getBuyOfferCount()));
        this.sellOfferLabel.setText(String.valueOf(this.market.getSellOfferCount()));
        var address = market.getAddress();
        this.countryLabel.setText(address.getCountry());
        this.cityLabel.setText(address.getCity());
        this.postalCodeLabel.setText(address.getPostalCode());
        this.streetLabel.setText(address.getStreetName());
        this.buildingNumberLabel.setText(String.valueOf(address.getBuildingNumber()));
        if (this.market instanceof StockMarket) {
            this.currencyLabel.setText(this.market.getAssetTradingCurrency(null));
            this.listTitleLabel.setText("Indexes on Market:");
            for (var idx : ((StockMarket) this.market).getStockMarketIndexes())
                this.assetListView.getItems().add(idx.getName());
        } else {
            this.currencyLabel.setText("-");
            this.listTitleLabel.setText("Assets on Market:");
            for (var asset : this.market.getAvailableAssetTypes())
                this.assetListView.getItems().add(asset);
        }
    }

    @Override
    public void passID(String marketName) {
        this.market = this.simulation.getMarketByName(marketName);
        this.updateFields();
    }
}
