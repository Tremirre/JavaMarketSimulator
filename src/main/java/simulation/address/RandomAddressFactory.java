package simulation.address;

import simulation.util.RandomService;
import simulation.util.Resourced;

public class RandomAddressFactory implements AddressFactory, Resourced {
    public Address createAddress() {
        var rand = RandomService.getInstance();
        var country = (String) rand.sampleElement(resourceHolder.getCountries());
        var city = (String) rand.sampleElement(resourceHolder.getCitiesForCountry(country));
        var postalCode = rand.yieldPostCode();
        var streetName = (String) rand.sampleElement(resourceHolder.getStreets());
        var buildingNumber = rand.yieldRandomInteger(1000) + 1;
        return new Address(country, city, postalCode, streetName, buildingNumber);
    }

}
