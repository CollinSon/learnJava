/**
 * @author 许瑞锐
 * @date 2020/9/4 11:04
 * @description {java类描述}
 */
public class Resource {

    private int id;

    public Resource(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
