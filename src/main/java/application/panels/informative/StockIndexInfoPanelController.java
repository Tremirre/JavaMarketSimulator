package application.panels.informative;

import application.driver.MainController;
import application.util.DecimalDisplayFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import simulation.market.StockMarketIndex;

import java.util.ArrayList;

public class StockIndexInfoPanelController extends InfoPanelController {

    private StockMarketIndex idx;
    @FXML
    private Label indexValueLabel;
    @FXML
    private ListView<String> companyListView;
    private ArrayList<String> stockIDs = new ArrayList<>();


    @Override
    public void refresh() {
        this.passID(this.idx.getName());
    }

    protected void updateFields() {
        this.stockIDs.clear();
        this.companyListView.getItems().clear();
        this.nameLabel.setText(this.idx.getName());
        this.indexValueLabel.setText(new DecimalDisplayFormat(3).format(this.idx.calculateIndexValue()));
        for (var company : this.idx.getCompanies()) {
            this.companyListView.getItems().add(company.getCompanyName());
            this.stockIDs.add(String.valueOf(company.getAssociatedAsset()));
        }
    }

    @Override
    public void passID(String id) {
        this.idx = this.simulation.getEntitiesManager().getIndexByName(id);
        this.updateFields();
    }

    public void passMainControllerReference(MainController controller) {
        super.passMainControllerReference(controller);
        this.companyListView.setOnMouseClicked( me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                int index = this.companyListView.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    var id = this.stockIDs.get(index);
                    mainController.onListViewClicked("company_info_panel.fxml", "Company Info Panel", id);
                }

            }
        });
    }
}
