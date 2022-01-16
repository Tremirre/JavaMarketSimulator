package application.panels.informative;

import application.util.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import simulation.holders.Investor;

import java.util.HashMap;

public class InvestorInfoPanelController extends InfoPanelController{
    private Investor investor;

    @FXML
    private Label fundsLabel;
    @FXML
    private Label strategyLabel;
    @FXML
    private ListView<String> assetListView;

    @Override
    public void refresh() {
        this.passID(String.valueOf(investor.getID()));
    }

    protected void updateFields() {
        this.assetListView.getItems().clear();
        this.nameLabel.setText(this.investor.getFirstName() + ' ' + this.investor.getLastName());
        var decimal = new DecimalDisplayFormat(2);
        this.fundsLabel.setText(decimal.format(this.investor.getInvestmentBudget()));
        this.strategyLabel.setText(this.investor.getStrategy().getClass().getName().split("\\.")[3]);
        var assets = new HashMap<>(this.investor.getOwnedAssets());
        for (var entry : assets.entrySet()) {
            this.assetListView.getItems().add(
                    entry.getKey() + " - " + decimal.format(entry.getValue())
            );
        }
    }

    @Override
    public void passID(String id) {
        int entityID = Integer.parseInt(id.replaceAll(".*\\(|\\).*", ""));
        this.investor = (Investor) this.simulation.getEntitiesManager().getHolderByID(entityID);
        this.updateFields();
    }
}
