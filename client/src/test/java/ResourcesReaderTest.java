import ru.dfhub.eirc.util.ResourcesReader;

public class ResourcesReaderTest {
    public static void main(String[] args) {
        ResourcesReader rs = new ResourcesReader("config.json");
        System.out.println(rs.readString());
    }
}
