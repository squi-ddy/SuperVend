package SuperVend.model;

public class Category implements Comparable<Category> {
    private String fullName;
    private String id;

    public Category(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Category o) {
        if (this.fullName.toUpperCase().compareTo(o.fullName.toUpperCase()) != 0) return this.fullName.toUpperCase().compareTo(o.fullName.toUpperCase());
        return this.id.compareTo(o.id);
    }
}
