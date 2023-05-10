package fr.univtours.polytech.locationapp.dao;

import fr.univtours.polytech.locationapp.model.weather.Main;

public interface WeatherDao {
	public Main getWeather(double lon, double lat);
}
