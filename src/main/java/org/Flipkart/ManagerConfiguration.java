package org.Flipkart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import io.dropwizard.db.DataSourceFactory;


public class ManagerConfiguration extends Configuration {

    public ManagerConfiguration() {
    }
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();


    public DataSourceFactory getDataSourceFactory() {
        if(Objects.isNull(database)){
            database = new DataSourceFactory();
        }
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(
            @Valid @NotNull DataSourceFactory database) {
        this.database = database;
    }

}


