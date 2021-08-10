package org.example.cayenne.persistent;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.time.LocalDate;

public class MainTest {

    @ClassRule
    public static SybContainer sybContainer = new SybContainer("jaschweder/sybase");

    private ServerRuntime cayenneRuntime;

    @Before
    public void initRuntime(){
        cayenneRuntime = ServerRuntime.builder()
                .url(sybContainer.getJdbcUrl())
                .user(sybContainer.getUsername())
                .password(sybContainer.getPassword())
                .jdbcDriver(sybContainer.getDriverClassName())
                .addConfig("cayenne-project.xml")
                .build();
    }


    @Test
    public void whenSelectQueryExecuted_thenResulstsReturned() {
        ObjectContext context = cayenneRuntime.newContext();
        Artist artist = context.newObject(Artist.class);
        artist.setName("name");
        artist.setDateOfBirth(LocalDate.now());
        context.commitChanges();
    }
}
