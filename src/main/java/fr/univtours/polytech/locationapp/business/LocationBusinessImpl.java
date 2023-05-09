package fr.univtours.polytech.locationapp.business;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.univtours.polytech.locationapp.dao.LocationDao;
import fr.univtours.polytech.locationapp.model.LocationBean;

@Stateless
public class LocationBusinessImpl implements LocationBusinessLocal {

	@Inject
	private LocationDao locationDao;

	@Override
	public void addLocation(LocationBean bean) {
		locationDao.createLocation(bean);
	}

	@Override
	public List<LocationBean> getLocations() {
		return locationDao.getLocations();
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
