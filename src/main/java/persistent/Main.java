package persistent;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.ObjectSelect;
import org.example.cayenne.persistent.Artist;
import org.example.cayenne.persistent.Gallery;
import org.example.cayenne.persistent.Painting;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .addConfig("cayenne-project.xml")
                .build();
        ObjectContext context = cayenneRuntime.newContext();

        Artist picasso = context.newObject(Artist.class);
        picasso.setName("Pablo Picasso");
        picasso.setDateOfBirthString("18811025");

        Gallery metropolitan = context.newObject(Gallery.class);
        metropolitan.setName("Metropolitan Museum of Art");

        Painting girl = context.newObject(Painting.class);
        girl.setName("Girl Reading at a Table");

        Painting stein = context.newObject(Painting.class);
        stein.setName("Gertrude Stein");

        picasso.addToPaintings(girl);
        picasso.addToPaintings(stein);

        girl.setGallery(metropolitan);
        stein.setGallery(metropolitan);

        context.commitChanges();

        List<Painting> paintings1 = ObjectSelect.query(Painting.class).select(context);

        List<Painting> paintings2 = ObjectSelect.query(Painting.class)
                .where(Painting.NAME.likeIgnoreCase("gi%")).select(context);

        List<Painting> paintings3 = ObjectSelect.query(Painting.class)
                .where(Painting.ARTIST.dot(Artist.DATE_OF_BIRTH).lt(LocalDate.of(1900,1,1)))
                .select(context);

        Artist picasso2 = ObjectSelect.query(Artist.class)
                .where(Artist.NAME.eq("Pablo Picasso")).selectOne(context);

        if (picasso2 != null) {
            context.deleteObject(picasso);
            context.commitChanges();
        }
    }
}
