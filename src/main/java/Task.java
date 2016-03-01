import java.time.LocalDateTime;
import java.util.ArrayList;
import org.sql2o.*;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Task {
  private int id;
  private String description;
  // private java.sql.Timestamp dueDate;

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  // public Timestamp getDueDate() {
  //   return dueDate;
  // }

  public Task(String description) {
    // time = time.replace("T", " ");
    // time += ":00";
    // dueDate = Timestamp.valueOf(time);
    this.description = description;
  }

  @Override
  public boolean equals(Object otherTask) {
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
              this.getId() == newTask.getId();
              // this.getDueDate().equals(newTask.getDueDate()) &&
    }
  }

  public static List<Task> all() {
    String sql = "SELECT id, description FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks (description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
      return task;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET description = :description WHERE id = :id";
      con.createQuery(sql)
        .addParameter("description", description)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM tasks WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();

      String joinDeleteQuery = "DELETE FROM categories_tasks WHERE task_id=:taskId";
      con.createQuery(joinDeleteQuery)
      .addParameter("taskId", this.getId())
      .executeUpdate();
    }
  }

  public void addCategory(Category category){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("task_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Category> getCategories() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT category_id FROM categories_tasks WHERE task_id = :task_id";
      List<Integer> categoryIds = con.createQuery(sql)
        .addParameter("task_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Category> categories = new ArrayList<Category>();

      for (Integer categoryId : categoryIds) {
          String taskQuery = "SELECT * FROM categories WHERE id = :categoryId";
          Category category = con.createQuery(taskQuery)
            .addParameter("categoryId", categoryId)
            .executeAndFetchFirst(Category.class);
          categories.add(category);
      }
      return categories;
    }
  }
  // public static List<Task> getPriorityTasks() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM tasks ORDER BY duedate LIMIT 5";
  //     return con.createQuery(sql).executeAndFetch(Task.class);
  //   }
  // }
  //
  // public String getCategoryName() {
  //   Category foundCategory = Category.find(categoryId);
  //   return foundCategory.getName();
  // }
}
