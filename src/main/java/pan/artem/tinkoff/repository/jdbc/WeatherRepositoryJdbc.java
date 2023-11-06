package pan.artem.tinkoff.repository.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pan.artem.tinkoff.dto.WeatherFullDto;
import pan.artem.tinkoff.exception.DataSourceException;
import pan.artem.tinkoff.exception.ResourceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Primary
public class WeatherRepositoryJdbc {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HikariDataSource dataSource;

    public WeatherRepositoryJdbc(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public record WeatherId(long id, WeatherFullDto weatherDto) {
    }

    private WeatherId mapResultSet(ResultSet rs) throws SQLException {
        int temperature = rs.getInt("temperature");
        LocalDateTime dateTime = rs.getObject("date_time", LocalDateTime.class);
        String weatherType = rs.getString("description");
        return new WeatherId(
            rs.getLong("weather.id"),
            new WeatherFullDto(
                temperature,
                dateTime,
                weatherType
            )
        );
    }

    private Optional<WeatherId> getWeather(Connection connection, String city, LocalDate date)
            throws SQLException {
        String getWeatherSql =
                "SELECT * FROM city INNER JOIN weather ON city.id = city_id " +
                        "INNER JOIN weather_type ON weather_type.id = weather_type_id " +
                        "WHERE city.name = ? and datediff(day, weather.date_time, ?) = 0 " +
                        "LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(getWeatherSql)) {
            statement.setString(1, city);
            statement.setObject(2, date);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            var weather = mapResultSet(rs);
            logger.debug("Weather from result set: {}", weather);
            return Optional.of(weather);
        }
    }

    public Optional<WeatherId> getWeather(String city, LocalDate date) {
        try (Connection connection = dataSource.getConnection()) {
            return getWeather(connection, city, date);
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    private int getCityId(Connection connection, String city) throws SQLException {
        String getCityIdSql = "SELECT id FROM city WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(getCityIdSql)) {
            statement.setString(1, city);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                throw new ResourceNotFoundException("No city named " + city + " found");
            }
            return rs.getInt(1);
        }
    }

    private long getWeatherTypeId(Connection connection, String description) throws SQLException {
        String getWeatherTypeSql = "SELECT id FROM weather_type WHERE description = ?";
        try (PreparedStatement statement = connection.prepareStatement(getWeatherTypeSql)) {
            statement.setString(1, description);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getLong(1);
        }
    }

    private long getWeatherTypeIdOrCreate(Connection connection, String description)
            throws SQLException {
        long weatherTypeId = getWeatherTypeId(connection, description);
        if (weatherTypeId == -1) {
            String addWeatherTypeSql = "INSERT INTO weather_type (description) VALUES ?";
            try (PreparedStatement statement = connection.prepareStatement(addWeatherTypeSql)) {
                statement.setString(1, description);
                statement.execute();
            }
            return getWeatherTypeId(connection, description);
        } else {
            return weatherTypeId;
        }
    }

    public void addWeather(String city, WeatherFullDto weatherDto) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            String addWeatherSql =
                    "INSERT INTO weather (temperature, date_time, weather_type_id, city_id) " +
                            "VALUES(?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(addWeatherSql)) {
                statement.setInt(1, weatherDto.temperature());
                statement.setObject(2, weatherDto.dateTime());
                statement.setLong(3, getWeatherTypeIdOrCreate(
                        connection, weatherDto.weatherType()
                ));
                statement.setLong(4, getCityId(
                        connection, city
                ));
                statement.execute();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public boolean updateWeather(String city, LocalDate date, WeatherFullDto weatherDto) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            var optional = getWeather(connection, city, date);
            if (optional.isEmpty()) {
                addWeather(city, weatherDto);
                return false;
            }
            long weatherId = optional.get().id;

            String updateWeatherSql =
                    "UPDATE weather SET temperature = ?, date_time = ?, " +
                            "weather_type_id = ?, city_id = ? " +
                            "WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateWeatherSql)) {
                statement.setInt(1, weatherDto.temperature());
                statement.setObject(2, weatherDto.dateTime());
                statement.setLong(3, getWeatherTypeIdOrCreate(
                        connection, weatherDto.weatherType()
                ));
                statement.setLong(4, getCityId(
                        connection, city
                ));
                statement.setLong(5, weatherId);
                statement.execute();
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
        return true;
    }

    public void deleteWeathers(String city) {
        String deleteWeathersSql = "DELETE FROM weather WHERE city_id = ?";
        try (Connection connection = dataSource.getConnection()) {
            long cityId = getCityId(connection, city);
            try (PreparedStatement statement = connection.prepareStatement(deleteWeathersSql)) {
                statement.setLong(1, cityId);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new DataSourceException(e);
        } catch (ResourceNotFoundException ignored) {
        }
    }
}
