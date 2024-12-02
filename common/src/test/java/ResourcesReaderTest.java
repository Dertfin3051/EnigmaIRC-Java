import ru.dfhub.eirc.common.ResourcesReader;

public class ResourcesReaderTest {
    public static void main(String[] args) {
        ResourcesReader rs = new ResourcesReader("config.json");
        System.out.println(rs.readString());
    }
}
