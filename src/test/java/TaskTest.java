import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.junit.Rule;

public class TaskTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(Task.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Task firstTask = new Task("Mow the lawn");
    Task secondTask = new Task("Mow the lawn");
    assertTrue(firstTask.equals(secondTask));
  }

  // @Test
  // public void equals_returnsTrueIfDueDatesAretheSame() {
  //   Task firstTask = new Task("Mow the lawn");
  //   Task secondTask = new Task("Mow the lawn");
  //   assertEquals(firstTask.getDueDate(), secondTask.getDueDate());
  // }

  @Test
  public void save_savesObjectIntoDatabase() {
    Task myTask = new Task("Mow the Lawn");
    myTask.save();
    assertTrue(Task.all().get(0).equals(myTask));
  }

  @Test
  public void save_assignsIDToObject() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(myTask.getId(), savedTask.getId());
  }

  @Test
  public void save_assignsCorrectIDToObject() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task otherTask = new Task("Mow the dog");
    otherTask.save();
    Task savedTask = Task.all().get(0);
    Task otherSavedTask = Task.all().get(1);
    assertEquals(myTask.getId(), savedTask.getId());
    assertEquals(otherTask.getId(), otherSavedTask.getId());

  }

  @Test
  public void find_findsTaskInDatabase_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertTrue(myTask.equals(savedTask));
  }

  // @Test
  // public void save_savesCategoryIdIntDB_true() {
  //   Category myCategory = new Category("Household chores");
  //   myCategory.save();
  //   Task myTask = new Task("Mow the lawn");
  //   myTask.save();
  //   Task savedTask = Task.find(myTask.getId());
  //   assertEquals(savedTask.getCategoryId(), myCategory.getId());
  // }

  @Test
  public void update_changesTaskDescription_false() {
    Task myTask = new Task("Feed the chickens");
    myTask.save();
    myTask.update("Feed the cats");
    Task updatedTask = Task.find(myTask.getId());
    assertFalse(updatedTask.equals(myTask));
  }

  @Test
  public void delete_deletesAllTasksAndListsAssoicationes() {
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myTask.addCategory(myCategory);
    myTask.delete();
    assertEquals(myCategory.getTasks().size(), 0);
  }
}
