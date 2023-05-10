package fr.univtours.polytech.locationapp.dao;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import fr.univtours.polytech.locationapp.model.weather.Main;
import fr.univtours.polytech.locationapp.model.weather.WsWeatherResult;

@Stateless
public class WeatherDaoImpl implements WeatherDao {
	private static String URL = "https://api.openweathermap.org";
	private static String appid = "20ecf3c863316c68e28b54cfc8e9ac39";	
	
	@Override
	public Main getWeather(double lon, double lat) {
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(URL);

		target = target.path("data");
		target = target.path("2.5");
		target = target.path("weather");

		target = target.queryParam("appid", appid);

		target = target.queryParam("lat", lat);
		target = target.queryParam("lon", lon);

		WsWeatherResult wsResult = target.request(MediaType.APPLICATION_JSON).get(WsWeatherResult.class);
		return wsResult.getMain();
	}
}
