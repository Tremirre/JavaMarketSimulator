package application.panels.informative;

import application.util.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import simulation.market.StockMarketIndex;

public class StockIndexInfoPanelController extends InfoPanelController {

    private StockMarketIndex idx;
    @FXML
    private Label indexValueLabel;
    @FXML
    private ListView<String> companyListView;

    @Override
    public void refresh() {
        this.passID(this.idx.getName());
    }

    protected void updateFields() {
        this.companyListView.getItems().clear();
        this.nameLabel.setText(this.idx.getName());
        this.indexValueLabel.setText(new DecimalDisplayFormat(3).format(this.idx.calculateIndexValue()));
        for (var company : this.idx.getCompanies()) {
            this.companyListView.getItems().add(company.getCompanyName());
        }
    }

    @Override
    public void passID(String id) {
        this.idx = this.simulation.getEntitiesManager().getIndexByName(id);
        this.updateFields();
    }
}
