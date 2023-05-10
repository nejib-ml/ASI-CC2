package fr.univtours.polytech.locationapp.business;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.univtours.polytech.locationapp.dao.AddressDao;
import fr.univtours.polytech.locationapp.dao.LocationDao;
import fr.univtours.polytech.locationapp.dao.WeatherDao;
import fr.univtours.polytech.locationapp.model.LocationBean;
import fr.univtours.polytech.locationapp.model.address.Feature;
import fr.univtours.polytech.locationapp.model.weather.Main;

@Stateless
public class LocationBusinessImpl implements LocationBusinessLocal {

	@Inject
	private LocationDao locationDao;

	@Inject
	private WeatherDao weatherDao;

	@Inject
	private AddressDao addressDao;

	@Override
	public void addLocation(LocationBean bean) {
		locationDao.createLocation(bean);
	}

	@Override
	public List<LocationBean> getLocations() {
		List<LocationBean> locations = locationDao.getLocations();
		
		for (LocationBean location : locations) {
			List<Feature> addresses = addressDao.getAddresses(location.getCity() + " " + location.getZipCode());
			//List<Feature> addresses = addressDao.getAddresses("jhgjhgjhgjhgjh" + " " + "khgjgfjjhgjhg");
			
			if(addresses.size() != 0){
				double lon = addresses.get(0).getGeometry().getCoordinates().get(0);
				double lat = addresses.get(0).getGeometry().getCoordinates().get(1);
				
				double temp = weatherDao.getWeather(lon, lat).getTemp() - 273.5;
				String formattedTemp = String.format("%.2f", temp);
				
				location.setTemperature(String.valueOf(formattedTemp));
			}
		}
		
		return locations;
	}

	@Override
	public LocationBean getLocation(Integer id) {
		return locationDao.getLocation(id);
	}

	@Override
	public List<LocationBean> filterAndSortLocations(List<LocationBean> locations, String city, String sortOrder) {
		Stream<LocationBean> locationStream = locations.stream();

		if (city != null && !city.isEmpty()) {
			locationStream = locationStream.filter(location -> city.equalsIgnoreCase(location.getCity()));
		}

		if (sortOrder != null) {
			if (sortOrder.equalsIgnoreCase("asc")) {
				locationStream = locationStream.sorted(Comparator.comparing(LocationBean::getCity));
			} else if (sortOrder.equalsIgnoreCase("desc")) {
				locationStream = locationStream.sorted(Comparator.comparing(LocationBean::getCity).reversed());
			}
		}

		return locationStream.collect(Collectors.toList());
	}

	@Override
	public void updateLocation(LocationBean locationBean) {
		locationDao.updateLocation(locationBean);
	}

	@Override
	public void deleteLocation(Integer id) {
		LocationBean locationBean = getLocation(id);
		locationDao.deleteLocation(locationBean);
	}

}
